package com.ichmed.bol2d.gui;

import org.lwjgl.util.vector.Vector2f;

public class DefaultGuiElement implements IGuiElement
{
	IGuiElement parent;
	private Vector2f position;
	private Vector2f size;

	public void setSize(Vector2f size)
	{
		this.size = size;
	}

	public void setPosition(Vector2f position)
	{
		this.position = position;
	}

	public void setParent(IGuiElement parent)
	{
		this.parent = parent;
	}

	@Override
	public void render()
	{
	}

	@Override
	public boolean keyboardCallback(long window, int key, int scancode, int action, int mods)
	{
		return false;
	}

	@Override
	public boolean mouseCallback(long window, int button, int action, int mods)
	{
		return false;
	}

	@Override
	public void receivePriority()
	{
	}

	@Override
	public Vector2f getPosition()
	{
		if(this.getParent() == null) return this.position;
		return Vector2f.add(this.position, this.getParent().getPosition(), null);
	}

	@Override
	public Vector2f getSize()
	{
		return size;
	}

	@Override
	public boolean isVisible()
	{
		return false;
	}

	@Override
	public IGuiElement getParent()
	{
		return null;
	}

	@Override
	public void update()
	{
	}

}
