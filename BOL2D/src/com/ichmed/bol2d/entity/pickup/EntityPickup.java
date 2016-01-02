package com.ichmed.bol2d.entity.pickup;

import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import com.ichmed.bol2d.Game;
import com.ichmed.bol2d.entity.*;
import com.ichmed.bol2d.entity.damage.*;

public abstract class EntityPickup extends Entity
{
	
	@Override
	public Vector2f getInitialSize()
	{
		return new Vector2f(20, 20);
	}
	
	@Override
	protected HealthSystem getHealthSystem(Float health)
	{
		return new HealthSystemIndestructible();
	}

	@Override
	public void onUpdate()
	{
		List<Entity> l = Game.getGameWorld().getOverlappingEntities(this, EntityType.CHARCTERS_ONLY);
		for (Entity e : l)
			if (canPickUp(e))
			{
				pickUp(e);
				this.kill();
			}
		super.onUpdate();
	}

	public abstract boolean pickUp(Entity e);

	@Override
	public EntityType getType()
	{
		return EntityType.PICKUP;
	}

	public abstract boolean canPickUp(Entity e);
}
