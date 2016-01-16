package com.ichmed.bol2d.entity.ai.behaviour.target;

import java.util.List;

import com.ichmed.bol2d.Game;
import com.ichmed.bol2d.entity.Entity;
import com.ichmed.bol2d.entity.ai.behaviour.BehaviourUpdate;
import com.ichmed.bol2d.util.Filter;

public class BehaviourAquireTarget extends BehaviourUpdate {

	Filter<Entity> filter;
	float range;
	
	TargetType targetType;
	
	public BehaviourAquireTarget(Filter<Entity> filter, TargetType targetType) {
		this(filter, targetType, 2000);
	}

	public BehaviourAquireTarget(Filter<Entity> filter, TargetType targetType, float range) {
		super();
		this.targetType = targetType;
		this.filter = filter;
		this.range = range;
	}

	@Override
	public boolean perform(Entity e, Entity f) {
		if(e == null) return false;

		if(e.targets.get(targetType) != null && !e.targets.get(targetType).isDead()) return false;
		List<Entity> l = Game.getGameWorld().sortListByDistance(e, Game.getGameWorld().getCurrentEntities(), range);
		for(Entity t : l)
			if(filter.doesAccept(t))
			{
				e.targets.put(targetType, t);
				return true;
			}
		return false;
	}

}
