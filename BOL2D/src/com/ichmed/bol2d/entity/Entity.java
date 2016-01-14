package com.ichmed.bol2d.entity;

import java.util.*;

import org.lwjgl.util.vector.*;

import com.ichmed.bol2d.Game;
import com.ichmed.bol2d.entity.ai.behaviour.*;
import com.ichmed.bol2d.entity.damage.*;
import com.ichmed.bol2d.render.RenderContainerEntity;
import com.ichmed.bol2d.render.animation.*;
import com.ichmed.bol2d.util.*;

public abstract class Entity implements IAnimated
{
	protected Vector2f size, position = new Vector2f(0, 0);
	public Vector2f velocity = new Vector2f(0, 0);

	public HealthSystem healthSystem;
	public float damage = 10;
	public float lifespan = -1;
	public float speed = 3.0f;
	public Entity owner;
	public Entity parent = null;
	public Vector3f color = new Vector3f(1, 1, 1);
	public EntityType enemy = EntityType.PLAYER;
	public String textureName = "player1";

	public int actionCooldown;

	protected double desiredRotation = 0;
	protected Vector2f lastRotDir;
	public double rotation = 0;
	public double rotationSpeed = 10;
	protected boolean rotateToMovement = true;

	public boolean spawnDebrisOnDeath = false;
	public boolean isInmoveable = false;

	private Database stats = new Database();

	public Entity target = null;
	private int ticksExisted;

	private boolean isDead = false;

	public boolean isSolid = true;

	@SuppressWarnings("unchecked")
	public List<Behaviour>[] behaviours = new ArrayList[10];
	
	public Animation animation;

	protected String debrisTexture = "debris1";
	protected float debrisSize = 0.2f;
	protected float debrisFactor = 0.2f;
	
	protected RenderContainerEntity renderContainer = new RenderContainerEntity(this, true, false, textureName, new Vector2f());

	public Entity()
	{
		for (int i = 0; i < behaviours.length; i++)
			behaviours[i] = new ArrayList<Behaviour>();
		this.size = this.getInitialSize();
	}

	public void init()
	{
		initStats();
		animation = new AnimationSimple(this, "idle");
		this.healthSystem = getHealthSystem(stats.getFloat("MAX_HEALTH"));
	}

	protected HealthSystem getHealthSystem(Float health)
	{
		return new HealthSystemDefault(health);
	}

	public boolean accelerate(Vector2f v)
	{
		if (this.isInmoveable) return false;
		this.velocity.x += v.x;
		this.velocity.y += v.y;
		return true;
	}

	public void addBehaviour(Behaviour behaviour)
	{
		addBehaviour(behaviour, 0);
	}

	public void addBehaviour(Behaviour behaviour, int priority)
	{
		this.behaviours[priority].add(behaviour);
	}

	public void addBehaviour(Collection<Behaviour> behaviourSet)
	{
		addBehaviour(behaviourSet, 0);
	}

	public void addBehaviour(Collection<Behaviour> behaviourSet, int priority)
	{
		this.behaviours[priority].addAll(behaviourSet);
	}

	public Entity clone()
	{
		try
		{
			Class<? extends Entity> c = this.getClass();
			Entity e = (Entity) c.newInstance();
			e.setPosition(this.getPosition());
			e.setSize(this.size);
			e.velocity = new Vector2f(this.velocity);
			for (int i = 0; i < behaviours.length; i++)
				e.behaviours[i] = new ArrayList<Behaviour>(this.behaviours[i]);
			e.owner = this.owner;
			e.parent = this.parent;
			e.enemy = this.enemy;
			e.isInmoveable = this.isInmoveable;
			e.isSolid = this.isDead;
			e.color = new Vector3f(this.color);
			e.spawnDebrisOnDeath = this.spawnDebrisOnDeath;
			e.textureName = this.textureName;
			e.stats = new Database(this.stats);
			return e;
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public float getStat(String stat)
	{
		return getStat(stat, 0);
	}

	public float getStat(String stat, float initValue)
	{
		return stats.getFloat(stat, initValue);
	}

	public void modStat(String stat, float mod)
	{
		modStat(stat, mod, 0);
	}

	public void modStat(String stat, float mod, float initValue)
	{
		stats.modFloat(stat, mod, initValue);
	}

	public void initStats()
	{
		setStat("MAX_HEALTH", 20f);
	}

	public void setStat(String stat, float value)
	{
		stats.setFloat(stat, value);
	}

	public void render(int i)
	{
		if(i != this.getLayer()) return;
		animation.update(this);
		this.renderContainer.render();
	}
	
	public int getLayer()
	{
		return 4;
	}

	@Override
	public void setTexture(String s)
	{
		this.renderContainer.setTextureName(s);
	}

	@Override
	public void setAnimation(Animation animation)
	{
		this.animation = animation;
	}

	@Override
	public void render()
	{
	}

	public Vector2f getCenter()
	{
		return new Vector2f(this.position.x + this.size.x / 2, this.position.y + this.size.y / 2);
	}

	public abstract Vector2f getInitialSize();

	public Vector2f getPosition()
	{
		return position;
	}

	public Vector2f getSize()
	{
		return size;
	}

	public int getTicksExisted()
	{
		return ticksExisted;
	}

	public abstract EntityType getType();

	public Vector2f getVelocity()
	{
		return new Vector2f(this.velocity);
	}

	public float hurt(float damage)
	{
		return hurt(damage, DamageType.TRUE);
	}

	public float hurt(float damage, DamageType type)
	{
		return this.healthSystem.hurt(damage, type);
	}

	public boolean isDead()
	{
		return isDead;
	}

	public void kill()
	{
		this.setDead(true);
		this.onDeath();
		Game.getGameWorld().removeEntity(this);
	}

	public void move(Vector2f v)
	{
		this.position.x += v.x;
		this.position.y += v.y;
	}

	public void onDeath()
	{
		for (int i = 0; i < behaviours.length; i++)
			for (Behaviour b : this.behaviours[i])
				if (b.type == BehaviourType.ON_DEATH) b.perform(this, null);
		if (this.spawnDebrisOnDeath) spawnDebris();
	}

	public int getDebrisAmount()
	{
		return (int) ((this.size.x * this.size.y / 25) * Game.getCurrentGame().getParticleRate() * this.debrisFactor);
	}

	protected void spawnDebris()
	{
		int n = getDebrisAmount();
		for (int i = 0; i < n; i++)
		{
			Vector2f pos = new Vector2f(this.position.x + (float) Math.random() * this.size.x, this.position.y + (float) Math.random() * this.size.y);
			Entity p = modDebris(new EntityGenericParticle());
			p.setCenter(pos);
			p.size = (Vector2f) (new Vector2f(this.size)).scale(this.debrisSize);
			p.color = new Vector3f(this.color);
			p.textureName = this.debrisTexture;
			p.accelerate(new Vector2f(((float) Math.random() - 0.5f) * 5, ((float) Math.random() - 0.5f) * 5));
			Game.getGameWorld().spawn(p);
		}
	}

	public void onSpawn()
	{

	}

	protected Entity modDebris(Entity debris)
	{
		return debris;
	}

	public void onUpdate()
	{
		this.ticksExisted++;
		this.actionCooldown--;
		this.healthSystem.update();
		if (this.healthSystem.shouldDie()) this.kill();
		if (this.parent == null) Vector2f.add(this.position, this.velocity, this.position);
		else Vector2f.add(this.position, this.parent.velocity, this.position);
		this.lifespan--;
		if (this.lifespan == 0) this.kill();
		// if (this.desiredRotation >= 0)
		// {
		// this.rotation += Math.min(this.rotationSpeed, this.desiredRotation -
		// this.rotation);
		// this.rotation %= 360;
		// }
		if(this instanceof IInteractable)
		{
			IInteractable interactable = (IInteractable)this;
			if(MathUtil.positionsInRange(this.getCenter(), Game.getGameWorld().player.getCenter(), interactable.getRange()))
				for(int key : interactable.getAcceptedKeys())
					if(Game.isKeyDown(key)) interactable.interact(key);
		}
		if (rotateToMovement && !MathUtil.areVectorsEqual(this.lastRotDir, this.velocity, Game.getCurrentGame().getRotationUpdateInterval()))
		{
			this.lastRotDir = new Vector2f(this.velocity);
			this.desiredRotation = MathUtil.getRotationForDirection(this.velocity) % 360;
		}
		if (this.desiredRotation >= 0) this.rotation = this.desiredRotation;
		List<Entity> l = Game.getGameWorld().getOverlappingEntities(this, true, EntityType.NO_PARTICLES);
		for (int i = 0; i < behaviours.length; i++)
			for (Behaviour b : this.behaviours[i])
			{
				if (b.type == BehaviourType.ON_UPDATE) b.perform(this, this.target);
				if (b.type == BehaviourType.ON_IMPACT && l.size() > 0) b.perform(this, l.get(0));
			}
	}

	public void performCleanup()
	{
		for (int i = 0; i < behaviours.length; i++)
			for (Behaviour b : this.behaviours[i])
				if (b.type == BehaviourType.ON_CLEANUP) b.perform(this, null);
	}

	public void setCenter(Vector2f center)
	{
		this.position.x = center.x - this.size.x / 2;
		this.position.y = center.y - this.size.y / 2;
	}

	public void setDead(boolean isDead)
	{
		this.isDead = isDead;
	}

	public void setPosition(Vector2f position)
	{
		this.position = new Vector2f(position);
	}

	public void setSize(Vector2f size)
	{
		this.size = new Vector2f(size);
	}

	public String getName()
	{
		return this.toString();
	}
}
