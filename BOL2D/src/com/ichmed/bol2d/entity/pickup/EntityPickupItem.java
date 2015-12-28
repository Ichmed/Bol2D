package com.ichmed.bol2d.entity.pickup;

import com.ichmed.bol2d.entity.*;

public class EntityPickupItem extends EntityPickup
{
	public EntityPickupItem(Item item)
	{
		this.item = item;
		this.item.init();
	}

	Item item;

	@Override
	public void pickUp(Entity e)
	{
		item.pickup(e);
	}

	@Override
	public boolean canPickUp(Entity e)
	{
		return e.getType() == EntityType.PLAYER;
	}

}
