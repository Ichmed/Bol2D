package com.ichmed.bol2d.util.input;

import org.lwjgl.glfw.GLFW;

import com.ichmed.bol2d.Game;

public class DefaultInputReceiver implements IInputReceiver
{

	@Override
	public boolean keyboardCallback(long window, int key, int scancode, int action, int mods)
	{
		if(key == GLFW.GLFW_KEY_ESCAPE) Game.close();
		return true;
	}

	@Override
	public boolean mouseCallback(long window, int button, int action, int mods)
	{
		return true;
	}

	@Override
	public void receivePriority()
	{		
	}

}
