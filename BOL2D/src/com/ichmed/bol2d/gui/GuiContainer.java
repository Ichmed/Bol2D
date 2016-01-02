package com.ichmed.bol2d.gui;

import java.util.*;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.vector.Vector2f;

import com.ichmed.bol2d.Game;
import com.ichmed.bol2d.render.RenderUtil;
import com.ichmed.bol2d.util.MathUtil;

public abstract class GuiContainer implements IGuiElement
{
	public List<IGuiElement> content = new ArrayList<IGuiElement>();

	public void add(IGuiElement element)
	{
		this.content.add(element);
	}

	@Override
	public void render()
	{
		if(!this.isVisible())return;
		RenderUtil.pushMatrix();
		RenderUtil.translate(this.getPosition());
		renderBackground();
		for (IGuiElement e : this.content)
			e.render();
		RenderUtil.popMatrix();
	}
	
	protected abstract void renderBackground();

	@Override
	public boolean keyboardCallback(long window, int key, int scancode, int action, int mods)
	{
		boolean b = false;
		for(IGuiElement e : this.content)
		{
			if(e.keyboardCallback(window, key, scancode, action, mods)) b = true;
		}
		return b;
	}

	@Override
	public boolean mouseCallback(long window, int button, int action, int mods)
	{
		boolean b = false;
			Vector2f mousePosition = Vector2f.sub(Game.getCursorPosition(), this.getPosition(), null);
		for(IGuiElement e : this.content)
		{
			if(e instanceof IClickable){
				if(MathUtil.isPointInArea(mousePosition, e.getPosition(), e.getSize()))
				{
					if(button == GLFW.GLFW_MOUSE_BUTTON_LEFT) ((IClickable)e).leftClick(action);
					if(button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) ((IClickable)e).leftClick(action);
					b = true;
				}
			}
			if(e.mouseCallback(window, button, action, mods)) b = true;
		}
		return b;
	}

}
