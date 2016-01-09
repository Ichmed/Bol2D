package com.ichmed.bol2d.entity.pickup.item;

import java.util.List;

public interface IInventory
{
	public boolean pickUpItem(ItemStack stack);
	public List<ItemStack> getContent();
}
