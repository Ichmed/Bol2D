package com.ichmed.bol2d.gui;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.vector.Vector2f;

import com.ichmed.bol2d.Game;
import com.ichmed.bol2d.util.input.InputManager;

public class Menu extends GuiContainer
{
	public boolean isVisible;
	
	@Override
	public boolean keyboardCallback(long window, int key, int scancode, int action, int mods)
	{
		if(key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE) this.disable();
		return true;
	}

	public void enable()
	{
		Game g = Game.getCurrentGame();
		if(!g.gui.contains(this))g.gui.add(this);
		this.isVisible = true;
		Game.pause(this);
		InputManager.setInputRceiver(this);
	}

	public void disable()
	{
		this.isVisible = false;
		Game.unpause(this);
		InputManager.yield(this);
	}

	@Override
	public Vector2f getPosition()
	{
		return new Vector2f(-1, -1);
	}

	@Override
	public Vector2f getSize()
	{
		return new Vector2f(2, 2);
	}

	@Override
	public boolean isVisible()
	{
		return isVisible;
	}

	@Override
	protected void renderBackground()
	{
	}

	@Override
	public void receivePriority()
	{
		this.isVisible = true;
	}

	@Override
	public void update()
	{
	}
}
