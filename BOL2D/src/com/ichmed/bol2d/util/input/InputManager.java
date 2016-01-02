package com.ichmed.bol2d.util.input;

import java.util.*;

import com.ichmed.bol2d.Game;

public class InputManager
{
	private static List<IInputReceiver> receivers = new ArrayList<IInputReceiver>();
	
	private static boolean isInitialized = false;
	
	public static void init()
	{
		if(isInitialized) return;
		isInitialized = true;
		receivers.add(new DefaultInputReceiver());
		receivers.add(Game.getCurrentGame().getDefaultInputReceiver());
		System.out.println("Initiaized InputManager");
	}
	
	public static void setInputRceiver(IInputReceiver receiver)
	{
		receivers.add(receiver);
	}

	public static void yield(IInputReceiver receiver)
	{
		receivers.remove(receiver);
		receivers.get(receivers.size() - 1).receivePriority();
	}

	public static void keyboardCallback(long window, int key, int scancode, int action, int mods)
	{
		for (int i = receivers.size() - 1; !receivers.get(i).keyboardCallback(window, key, scancode, action, mods); i--)
			;
	}

	public static void mouseCallback(long window, int button, int action, int mods)
	{
		for (int i = receivers.size() - 1; !receivers.get(i).mouseCallback(window, button, action, mods); i--)
			;
	}

	public static boolean isPrimaryReceiver(IInputReceiver receiver)
	{
		return receivers.get(receivers.size() - 1) == receiver;
	}
}
