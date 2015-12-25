package com.ichmed.bol2d.entity;

import java.util.*;

import org.lwjgl.util.vector.Vector2f;

import com.ichmed.bol2d.entity.ai.behaviour.BehaviourRemoveOnCleanup;
import com.ichmed.bol2d.render.RenderContainerEntity;

public class EntityGenericProjectile extends Entity
{
	protected float damage = 5;

	public EntityGenericProjectile()
	{
		this.speed = 20;
		this.isSolid = false;
		this.addBehaviour(new BehaviourRemoveOnCleanup());
	}

	@Override
	public Vector2f getInitialSize()
	{
		return new Vector2f(40, 40);
	}
	
	protected List<RenderContainerEntity> getRenderContainers(int layer)
	{
		if (layer == 3)
		{
			ArrayList<RenderContainerEntity> l = new ArrayList<RenderContainerEntity>();
			l.add(new RenderContainerEntity(this, true, true, this.textureName, new Vector2f()));
			return l;
		}
		return new ArrayList<RenderContainerEntity>();
	}

	@Override
	public EntityType getType()
	{
		return EntityType.PROJECTILE;
	}
}
