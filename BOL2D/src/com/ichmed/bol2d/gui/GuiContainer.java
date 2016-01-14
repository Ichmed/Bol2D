package com.ichmed.bol2d.gui;

import java.util.*;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.vector.Vector2f;

import com.ichmed.bol2d.Game;
import com.ichmed.bol2d.util.MathUtil;

public abstract class GuiContainer extends DefaultGuiElement
{
	public List<IGuiElement> content = new ArrayList<IGuiElement>();

	public void add(IGuiElement element)
	{
		element.setParent(this);
		this.content.add(element);
	}

	@Override
	public void render()
	{
		if (!this.isVisible()) return;
		renderBackground();
		for (IGuiElement e : this.content)
			e.render();
	}

	protected abstract void renderBackground();

	@Override
	public boolean keyboardCallback(long window, int key, int scancode, int action, int mods)
	{
		boolean b = false;
		for (IGuiElement e : this.content)
		{
			if (e.keyboardCallback(window, key, scancode, action, mods)) b = true;
		}
		return b;
	}

	@Override
	public boolean mouseCallback(long window, int button, int action, int mods)
	{
		boolean b = false;
		for (IGuiElement e : this.content)
		{
			if (e instanceof IClickable)
			{
				if (MathUtil.isPointInArea(Game.getCursorPosition(), e.getPosition(), e.getSize()))
				{
					if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) ((IClickable) e).leftClick(action);
					if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) ((IClickable) e).leftClick(action);
					b = true;
				}
			}
			if (e.mouseCallback(window, button, action, mods)) b = true;
		}
		return b;
	}

	@Override
	public void update()
	{
		for(IGuiElement g : this.content)
			g.update();
		super.update();
	}
	
	

}
