package com.ichmed.bol2d.gui;

import static org.lwjgl.glfw.GLFW.*;

import java.util.*;

import org.lwjgl.util.vector.Vector2f;

import com.ichmed.bol2d.Game;
import com.ichmed.bol2d.entity.Entity;
import com.ichmed.bol2d.render.*;
import com.ichmed.bol2d.util.input.InputManager;

public class Console implements IGuiElement
{
	private final static Console INSTANCE = new Console();

	static String screen = "";
	static List<String> consoleHistory = new ArrayList<String>();
	public static String consoleIn = "";
	int historyCounter;

	private boolean promptFlicker = true;

	public static Console getInstance()
	{
		return INSTANCE;
	}

	@Override
	public void render()
	{
		if (!this.isVisible()) return;
		RenderUtil.setColor(RenderUtil.BLACK, 0.5f);
		RenderUtil.drawRect(-1, -1, 2, 2);
		RenderUtil.setColor(RenderUtil.WHITE, 1);
		int count = screen.length() - screen.replace("\n", "").length();
		TextUtil.drawText(screen, "default", -1, (count) * 0.05f - 1, 0.05f);
		String t = ">" + consoleIn;
		if (Game.getTicksTotal() % 30 == 0) promptFlicker = !promptFlicker;
		if (promptFlicker) t += "_";
		TextUtil.drawText(t, "default", -1, -1, .05f);
	}

	@Override
	public boolean keyboardCallback(long window, int key, int scancode, int action, int mods)
	{
		if (action == GLFW_RELEASE && key == GLFW_KEY_ESCAPE)
		{
			InputManager.yield(this);
			Game.unpause(INSTANCE);
		}
		if (action == GLFW_PRESS || action == GLFW_REPEAT)
		{

			if (key == GLFW_KEY_UP && historyCounter < consoleHistory.size() - 1) consoleIn = getConsoleHistoryForIndex(historyCounter + 1);
			else if (key == GLFW_KEY_DOWN && historyCounter > 0) consoleIn = getConsoleHistoryForIndex(historyCounter - 1);
			else
			{
				historyCounter = -1;
				if (key >= 32 && key < 96) consoleIn += (char) key;
				if (key == GLFW_KEY_BACKSPACE && consoleIn.length() > 0) consoleIn = consoleIn.substring(0, consoleIn.length() - 1);
				if (key == GLFW_KEY_ENTER)
				{
					log(consoleIn);
					interpretInput(consoleIn);
					consoleHistory.add(consoleIn);
					consoleIn = "";
				}
			}
		}

		return true;
	}

	private String getConsoleHistoryForIndex(int i)
	{
		historyCounter = i;
		return consoleHistory.get(consoleHistory.size() - 1 - historyCounter);
	}

	public void interpretInput(String consoleIn)
	{
		if (consoleIn.equals("EXIT")) Game.close();
		else if (consoleIn.equals("KILL ALL")) for (Entity e : Game.getGameWorld().getCurrentEntities())
			e.kill();
		else if (consoleIn.equals("PRINT ENTITIES")) for (Entity e : Game.getGameWorld().getCurrentEntities())
			log(e.getName());
		else if (consoleIn.startsWith("OVERRIDEKEY "))
		{
			try
			{
				String keyName = consoleIn.split(" ")[1];
				boolean isPressed = Boolean.valueOf(consoleIn.split(" ")[2]);
				char c = keyName.charAt(0);
				Game.overrideIsKeyDown((int) c, isPressed);
			} catch (Exception e)
			{
				e.printStackTrace();
				log("Use like this: OVERRRIDEKEY [KEY] [VALUE]");
			}
		} else if (consoleIn.split(" ").length > 1) try
		{
			String name = consoleIn.split(" ")[0];
			String arg = consoleIn.split(" ")[1];
			if (arg.equals("TRUE")) Game.setFlag(name, 1);
			else if (arg.equals("FALSE")) Game.setFlag(name, 0);
			else Game.setFlag(name, Float.valueOf(arg));
		} catch (Exception e)
		{
			log(e.toString());
			e.printStackTrace();
		}
	}

	public static void log(Object o)
	{
		log(o.toString());
	}
	
	public static void log(String s)
	{
		screen += (s + "\n");
		System.out.println(s);
	}

	@Override
	public boolean mouseCallback(long window, int button, int action, int mods)
	{
		return false;
	}

	public static void enable()
	{
		InputManager.setInputRceiver(INSTANCE);
		Game.pause(INSTANCE);
	}

	@Override
	public Vector2f getPosition()
	{
		return null;
	}

	@Override
	public Vector2f getSize()
	{
		return null;
	}

	@Override
	public boolean isVisible()
	{
		return InputManager.isPrimaryReceiver(this);
	}

	@Override
	public void receivePriority()
	{
	}

	@Override
	public void update()
	{
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
}
