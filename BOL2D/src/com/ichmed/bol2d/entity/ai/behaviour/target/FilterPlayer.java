package com.ichmed.bol2d.entity.ai.behaviour.target;

import com.ichmed.bol2d.entity.*;
import com.ichmed.bol2d.util.Filter;

public class FilterPlayer extends Filter<Entity>
{

	@Override
	public boolean doesAccept(Entity e)
	{
		return e.getType() == EntityType.PLAYER;
	}
	
}
