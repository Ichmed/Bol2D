package com.ichmed.bol2d.gui;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.vector.Vector2f;

import com.ichmed.bol2d.Game;
import com.ichmed.bol2d.util.input.InputManager;

public class Menu extends GuiContainer
{
	public boolean isVisible;
	
	public Menu()
	{
		this.setPosition(new Vector2f(-1, -1));
	}
	
	@Override
	public boolean keyboardCallback(long window, int key, int scancode, int action, int mods)
	{
		if(key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE) this.disable();
		return true;
	}

	public void enable()
	{
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
	public void receivePriority()
	{
		this.isVisible = true;
	}
	@Override
	public IGuiElement getParent()
	{
		return null;
	}

	@Override
	public void setParent(IGuiElement parent)
	{
	}

	@Override
	protected void renderBackground()
	{
	}
}
