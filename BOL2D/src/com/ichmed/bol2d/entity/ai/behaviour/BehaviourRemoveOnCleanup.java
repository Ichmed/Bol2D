package com.ichmed.bol2d.entity.ai.behaviour;

import com.ichmed.bol2d.Game;
import com.ichmed.bol2d.entity.Entity;

public class BehaviourRemoveOnCleanup extends BehaviourCleanup
{
	@Override
	public boolean perform(Entity entity, Entity target)
	{
		Game.getGameWorld().removeEntity(entity);
		return true;
	}

}
