package com.ichmed.bol2d.entity.player;

import com.ichmed.bol2d.entity.*;
import com.ichmed.bol2d.util.input.IInputReceiver;

public abstract class EntityPlayer extends Entity implements IInputReceiver
{
	public boolean enableInput;

	@Override
	public EntityType getType()
	{
		return EntityType.PLAYER;
	}

	@Override
	public String getName()
	{
		return "The Player";
	}

	
}
