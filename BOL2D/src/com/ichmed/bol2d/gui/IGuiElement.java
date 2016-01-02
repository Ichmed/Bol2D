package com.ichmed.bol2d.gui;

import org.lwjgl.util.vector.Vector2f;

import com.ichmed.bol2d.render.IRenderContainer;
import com.ichmed.bol2d.util.input.IInputReceiver;

public interface IGuiElement extends IRenderContainer, IInputReceiver
{
	public Vector2f getPosition();
	public Vector2f getSize();
	public boolean isVisible();
}
