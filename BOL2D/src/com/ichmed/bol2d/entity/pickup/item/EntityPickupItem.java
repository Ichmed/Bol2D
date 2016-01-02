package com.ichmed.bol2d.entity.pickup.item;

import org.lwjgl.util.vector.Vector2f;

import com.ichmed.bol2d.entity.*;
import com.ichmed.bol2d.entity.pickup.EntityPickup;

public class EntityPickupItem extends EntityPickup
{
	public EntityPickupItem(Item item)
	{
		this.item = item;
		this.item.init();
		this.textureName = item.getTexture();
	}

	Item item;

	@Override
	public boolean pickUp(Entity e)
	{
		return ((IInventory)e).pickUpItem(new ItemStack(this.item, 1));
	}

	@Override
	public boolean canPickUp(Entity e)
	{
		return e.getType() == EntityType.PLAYER;
	}

	@Override
	public Vector2f getInitialSize()
	{
		return new Vector2f(64, 64);
	}
}
