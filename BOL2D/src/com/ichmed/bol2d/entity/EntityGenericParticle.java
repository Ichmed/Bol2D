package com.ichmed.bol2d.entity;

import org.lwjgl.util.vector.Vector2f;

import com.ichmed.bol2d.entity.ai.behaviour.BehaviourRemoveOnCleanup;
import com.ichmed.bol2d.render.animation.Animation;

public class EntityGenericParticle extends Entity
{

	public EntityGenericParticle()
	{
		this.lifespan = 20 + (int)(Math.random() * 50);
		this.isSolid = false;
		this.spawnDebrisOnDeath = false;
		this.rotateToMovement = false;
		this.addBehaviour(new BehaviourRemoveOnCleanup());
	}
	
	@Override
	public Vector2f getInitialSize()
	{
		return new Vector2f(5, 5);
	}

	@Override
	public void performCleanup()
	{
		this.kill();
	}

	@Override
	public EntityType getType()
	{
		return EntityType.PARTICLE;
	}
}
