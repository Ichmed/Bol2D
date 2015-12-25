package com.ichmed.bol2d.entity.player;

import com.ichmed.bol2d.entity.*;

public abstract class EntityPlayer extends Entity
{
	public boolean enableInput;

	@Override
	public EntityType getType()
	{
		return EntityType.PLAYER;
	}

}
