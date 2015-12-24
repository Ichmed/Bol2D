package com.ichmed.bol2d.entity.pickup;

import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import com.ichmed.bol2d.Game;
import com.ichmed.bol2d.entity.*;

public abstract class EntityPickup extends Entity
{

	@Override
	public Vector2f getInitialSize()
	{
		return new Vector2f(20, 20);
	}

	@Override
	public void onUpdate()
	{
		if (this.actionCooldown <= 0)
		{
			List<Entity> l = Game.getGameWorld().getOverlappingEntities(this);
			for (Entity e : l)
				if (canPickUp(e))
				{
					pickUp(e);
					this.kill();
				}
			this.actionCooldown = 3;
		}

		super.onUpdate();
	}

	public abstract void pickUp(Entity e);

	public boolean canPickUp(Entity e)
	{
		if (e.getType() == EntityType.PARTICLE || e.getType() == EntityType.PICKUP) return false;
		return true;

	}
}
