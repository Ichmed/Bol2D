package com.ichmed.bol2d.render.animation;

import com.ichmed.bol2d.entity.Entity;

public class AnimationSimple extends Animation
{
	public AnimationSimple(Entity e, String name)
	{
		super(e, "default", e.textureName + "_" + name);
	}
}
