package com.ichmed.bol2d;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.nio.DoubleBuffer;
import java.util.*;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.util.vector.Vector2f;

import com.ichmed.bol2d.entity.Entity;
import com.ichmed.bol2d.gui.*;
import com.ichmed.bol2d.render.*;
import com.ichmed.bol2d.render.texturelibrary.*;
import com.ichmed.bol2d.util.Database;
import com.ichmed.bol2d.util.input.*;
import com.ichmed.bol2d.world.World;

public abstract class Game
{
	private static Game currentGame;
	private static Vector2f cursorPos;

	private static Database flags = new Database();

	private static int fps;

	private static HashMap<Integer, Boolean> isButtonDown = new HashMap<Integer, Boolean>();
	private static HashMap<Integer, Boolean> isKeyDown = new HashMap<Integer, Boolean>();
	private static HashMap<Integer, Boolean> isKeyDownOverride = new HashMap<Integer, Boolean>();
	static long lastSecond = System.currentTimeMillis();
	private static List<Object> pausedList = new ArrayList<Object>();

	static final int TICKS_PER_SECOND = 60;
	private static int ticksThisSecond;

	private static HashMap<Integer, Boolean> wasButtonDown = new HashMap<Integer, Boolean>();

	private static HashMap<Integer, Boolean> wasKeyDown = new HashMap<Integer, Boolean>();

	// The window handle
	private static long window;

	public static void close()
	{
		glfwSetWindowShouldClose(window, GLFW_TRUE);
	}

	public static Game getCurrentGame()
	{
		return currentGame;
	}

	public static Vector2f getCursorPosition()
	{
		return new Vector2f(cursorPos);
	}

	public static float getFlag(String flag)
	{
		return getFlag(flag, 0);
	}

	public static float getFlag(String flag, float initialValue)
	{
		return flags.getFloat(flag, initialValue);
	}

	public static int getFps()
	{
		return fps;
	}

	public static World getGameWorld()
	{
		return currentGame.getCurrentGameWorld();
	}

	public static int getTicksThisSecond()
	{
		return ticksThisSecond;
	}

	public static int getTicksTotal()
	{
		return currentGame.ticksTotal;
	}

	public static boolean isButtonDown(int key)
	{
		Boolean b = isButtonDown.get(key);
		return b == null ? false : b;
	}

	public static boolean isKeyDown(int key)
	{
		Boolean b = isKeyDown.get(key);
		Boolean c = isKeyDownOverride.get(key);
		if (c == null || c == false) return b == null ? false : b;
		return true;
	}

	public static boolean isPaused()
	{
		return !pausedList.isEmpty();
	}

	public static void overrideIsKeyDown(Integer key, Boolean value)
	{
		isKeyDownOverride.put(key, value);
	}

	public static void pause(Object o)
	{
		pausedList.add(o);
	}

	public static void setFlag(String flag, float value)
	{
		flags.setFloat(flag, value);
	}

	public static void startNewGame(Game game, World world)
	{
		Game.currentGame = game;
		game.pauseScreen = game.getPauseScreen();
		Game.currentGame.gameWorld = world;
		game.run();
	}

	public static void unpause(Object o)
	{
		pausedList.remove(o);
	}

	public static void updateKeyMaps()
	{
		wasKeyDown = new HashMap<Integer, Boolean>(isKeyDown);
	}

	public static boolean wasKeyPressed(int key)
	{
		Boolean was = wasKeyDown.get(key);
		Boolean is = isKeyDown(key);

		if (was == null || is == null) return false;
		return was == false && is == true;
	}

	public static boolean wasKeyReleased(int key)
	{
		Boolean was = wasKeyDown.get(key);
		Boolean is = isKeyDown(key);

		if (was == null || is == null) return false;
		return was == true && is == false;
	}

	// We need to strongly reference callback instances.
	private GLFWErrorCallback errorCallback;

	private World gameWorld;

	@SuppressWarnings("unchecked")
	public List<IGuiElement>[] gui = new List[10];

	int HEIGHT = 1000;

	boolean isPaused = false;

	private GLFWKeyCallback keyCallback;

	long lastMillis = System.currentTimeMillis();

	private GLFWMouseButtonCallback mouseCallback;

	public Menu pauseScreen;

	private int ticksTotal;

	int WIDTH = 1000;

	protected World getCurrentGameWorld()
	{
		return gameWorld;
	}

	public abstract IInputReceiver getDefaultInputReceiver();

	public abstract int getMaxTexLibSize();

	public abstract int getParticleMax();

	public abstract float getParticleRate();

	public abstract Menu getPauseScreen();

	public abstract float getRotationUpdateInterval();

	private void init()
	{
		glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if (glfwInit() != GLFW_TRUE) throw new IllegalStateException("Unable to initialize GLFW");

		// Configure our window
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

		// Create the window
		window = glfwCreateWindow(WIDTH, HEIGHT, "Rogue Galaxy", NULL, NULL);
		if (window == NULL) throw new RuntimeException("Failed to create the GLFW window");

		// Setup a key callback. It will be called every time a key is pressed,
		// repeated or released.
		glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback()
		{
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods)
			{
				if (action == GLFW_PRESS || action == GLFW_REPEAT) isKeyDown.put(key, true);
				if (action == GLFW_RELEASE) isKeyDown.put(key, false);

				InputManager.keyboardCallback(window, key, scancode, action, mods);
			}
		});

		glfwSetMouseButtonCallback(window, mouseCallback = new GLFWMouseButtonCallback()
		{

			@Override
			public void invoke(long window, int button, int action, int mods)
			{
				if (action == GLFW_PRESS || action == GLFW_REPEAT) isButtonDown.put(button, true);
				if (action == GLFW_RELEASE) isButtonDown.put(button, false);
				InputManager.mouseCallback(window, button, action, mods);
			}
		});

		// Get the resolution of the primary monitor
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		// Center our window
		glfwSetWindowPos(window, (vidmode.width() - WIDTH) / 2, (vidmode.height() - HEIGHT) / 2);

		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
		// Enable v-sync
		glfwSwapInterval(1);

		// Make the window visible
		glfwShowWindow(window);
	}

	public abstract void initGameData();

	protected void loop()
	{
		DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
		DoubleBuffer y = BufferUtils.createDoubleBuffer(1);
		glfwGetCursorPos(window, x, y);
		cursorPos = new Vector2f((float) (x.get() - WIDTH / 2) / (WIDTH / 2), -(float) (y.get() - HEIGHT / 2) / (HEIGHT / 2));

		if (ticksThisSecond < TICKS_PER_SECOND)
		{
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			RenderUtil.pushMatrix();
			gameWorld.update();
			RenderUtil.popMatrix();
			Game.updateKeyMaps();
			for (int i = 0; i < gui.length; i++)
				for (IGuiElement e : gui[i])
				{
					e.update();
					if (e.isVisible()) e.render();
				}
			glfwSwapBuffers(window);
			glfwPollEvents();
			ticksThisSecond++;
			ticksTotal++;
			long m = System.currentTimeMillis();
			long n = lastMillis + (1000 / 60);
			long l = n - m;
			if (l > 0) try
			{
				Thread.sleep(l);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			lastMillis = System.currentTimeMillis();
		} else
		{
			if (lastSecond + 1000L < System.currentTimeMillis())
			{
				for (Entity e : gameWorld.getCurrentEntities())
					e.performCleanup();

			} else
			{
				try
				{
					Thread.sleep(lastSecond + 1000L - System.currentTimeMillis());
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
		if (System.currentTimeMillis() >= lastSecond + 1000)
		{
			lastSecond = System.currentTimeMillis();
			fps = ticksThisSecond;
			ticksThisSecond = 0;
		}

	}

	public void run()
	{
		try
		{
			init();
			runGame();

			// Release window and window callbacks
			glfwDestroyWindow(window);
			keyCallback.release();
		} finally
		{
			// Terminate GLFW and release the GLFWErrorCallback
			glfwTerminate();
			errorCallback.release();
		}
	}

	private void runGame()
	{
		GL.createCapabilities();
		glClearColor(.004f, 0.0f, 0.1f, 0.0f);
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		try
		{
			TextureLibrarySheet.createLibrary("default", "resc/texture/default/", shouldShowTextureStiching());
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		for (int i = 0; i < gui.length; i++)
			gui[i] = new ArrayList<IGuiElement>();

		currentGame.initGameData();
		InputManager.init();
		gui[9].add(Console.getInstance());
		gui[8].add(this.pauseScreen);
		gameWorld.init();

		while (glfwWindowShouldClose(window) == GLFW_FALSE)
		{
			loop();
		}
		TextureLibrary.cleanUpAll();
	}

	public abstract boolean shouldShowTextureStiching();

	public static void addGui(IGuiElement e, int i)
	{
		getCurrentGame().gui[i].add(e);
	}

	public static long getSeed()
	{
		return 1337;
	}
}
