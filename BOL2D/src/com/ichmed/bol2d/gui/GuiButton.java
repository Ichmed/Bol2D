package com.ichmed.bol2d.gui;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.util.vector.Vector2f;

import com.ichmed.bol2d.render.*;
import com.ichmed.bol2d.render.TextUtil.TextOrientation;

public class GuiButton implements IGuiElement , IClickable
{
	public String label = "[SET_LABEL]";
	public Vector2f position = new Vector2f();
	public Vector2f size = new Vector2f();
	public String font = "default";
	public boolean isVisible;
	
	public GuiButton(float posX, float posY, float width, float height)
	{
		this(posX, posY, width, height, "NO LABEL");
	}
	public GuiButton(float posX, float posY, float width, float height, String label)
	{
		this.position = new Vector2f(posX, posY);
		this.size = new Vector2f(width, height);
		this.label = label;
		this.setActionHandler(new ActionHandler()
		{			
			@Override
			public void leftClick()
			{
				setLabel("NO HANDLER");
			}
		});
	}
	
	private boolean clicked;
	
	private ActionHandler actionHandler;

	public String getLabel()
	{
		return label;
	}

	public void setLabel(String label)
	{
		this.label = label;
	}

	public String getFont()
	{
		return font;
	}

	public void setFont(String font)
	{
		this.font = font;
	}

	public ActionHandler getActionHandler()
	{
		return actionHandler;
	}

	public void setActionHandler(ActionHandler actionHandler)
	{
		this.actionHandler = actionHandler;
	}

	public void setPosition(Vector2f position)
	{
		this.position = position;
	}

	public void setSize(Vector2f size)
	{
		this.size = size;
	}

	public void setVisible(boolean isVisible)
	{
		this.isVisible = isVisible;
	}

	@Override
	public void render()
	{
		renderBackground();
		RenderUtil.setColor(RenderUtil.BLACK, 1);
		TextUtil.drawText(label, font, position.x + size.x / 2, position.y, size.y, TextOrientation.CENTERED);
	}
	
	protected void renderBackground()
	{
		RenderUtil.setColor(RenderUtil.WHITE, 1);
		RenderUtil.drawRect(position.x, position.y, size.x, size.y);		
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
	public Vector2f getPosition()
	{
		return position;
	}

	@Override
	public Vector2f getSize()
	{
		return size;
	}

	public static abstract class ActionHandler
	{
		public abstract void leftClick();
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
	public boolean rightClick(int action)
	{
		return false;
	}

	@Override
	public boolean leftClick(int action)
	{
		if(action == GLFW.GLFW_PRESS) clicked = true;
		if(action == GLFW.GLFW_RELEASE && clicked)
			this.actionHandler.leftClick();
		return true;
	}

	@Override
	public void reset()
	{
		this.clicked = false;
	}
	@Override
	public void update()
	{
	}

}
