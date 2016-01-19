package com.ichmed.bol2d.util;

import java.util.Map;

import com.ichmed.bol2d.entity.pickup.item.ItemData;
import com.ichmed.bol2d.util.procedural.RandomPool;

public interface IGameWithItems
{
	public Map<String, RandomPool<String>> getItemPools();
	public Map<String, ItemData> getAllItems();
}
