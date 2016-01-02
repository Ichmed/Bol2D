package com.ichmed.bol2d.entity.pickup.item;

import com.ichmed.bol2d.entity.Entity;

public class ItemStack
{
	public Item item;
	public ItemStack(Item item, int amount)
	{
		super();
		this.item = item;
		this.amount = amount;
	}

	public int amount;
	
	public boolean add(ItemStack stack)
	{
		if(this.item.equals(stack.item))
		{
			this.amount += stack.amount;
			return true;
		}
		return false;
	}

	public void pickUp(Entity entity)
	{
		for(int i = 0; i < this.amount; i++)
			item.pickup(entity);
	}
}
