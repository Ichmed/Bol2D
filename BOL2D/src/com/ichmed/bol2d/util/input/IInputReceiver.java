package com.ichmed.bol2d.util.input;

public interface IInputReceiver
{
	public boolean keyboardCallback(long window, int key, int scancode, int action, int mods);
	public boolean mouseCallback(long window, int button, int action, int mods);
	public void receivePriority();
}
