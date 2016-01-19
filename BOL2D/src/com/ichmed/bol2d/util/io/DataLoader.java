package com.ichmed.bol2d.util.io;

import java.io.*;
import java.util.Scanner;

import com.ichmed.bol2d.Game;
import com.ichmed.bol2d.entity.pickup.item.ItemData;
import com.ichmed.bol2d.gui.Console;
import com.ichmed.bol2d.util.IGameWithItems;
import com.ichmed.bol2d.util.procedural.*;

public class DataLoader
{
	public static void loadAll(IGameWithItems g)
	{
		game = g;
		File folder = new File("resc/data/main");
		for (File f : folder.listFiles())
			loadItems(g, f);
		folder = new File("resc/data/mods");
		for (File f : folder.listFiles())
			loadItems(g, f);
	}

	private static Object currentObject;
	private static IGameWithItems game;

	private static void loadItems(IGameWithItems g, File f)
	{
		if (f.isDirectory()) for (File file : f.listFiles())
			loadItems(g, file);
		else if (!f.getName().endsWith(".rgd")) return;
		try
		{
			Scanner scan = new Scanner(f);
			while (scan.hasNextLine())
			{
				String orig = scan.nextLine();
				orig = orig.trim();
				String s = orig.toUpperCase();
				s = s.trim();

				if (!(s.startsWith("//") || s.equals("")))
				{
					Console.log(orig);
					if (s.startsWith("CREATE ITEM ")) createItem(orig);
					else if (s.startsWith("SELECT ITEM")) selectItem(orig);
					else if (s.startsWith("NAME ")) setReadableName(orig);
					else if (s.startsWith("STAT ")) setStat(orig);
					else if (s.startsWith("DESCRIPTION ")) setDescritpion(orig);
					else if (s.startsWith("TEXTURE ")) setTexture(orig);
					else if (s.startsWith("CREATE POOL")) createPool(orig);
					else if (s.startsWith("SELECT POOL")) selectPool(orig);
					else if (s.startsWith("ADD ITEM ")) addItem(orig);
					else Console.log("Could not interpret \"" + s + "\"");
				}

			}
			scan.close();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	private static void addItem(String s)
	{
		makeItemPool().add(s.split(" ")[2], Integer.valueOf(s.split(" ")[3]));
	}

	@SuppressWarnings("unchecked")
	private static RandomPool<String> makeItemPool()
	{
		if (currentObject instanceof RandomPool<?>)
		{
			RandomPool<String> r = (RandomPool<String>) currentObject;
			return r;
		}
		return null;
	}

	private static void selectPool(String s)
	{
		String poolName = s.split(" ")[2];
		currentObject = game.getItemPools().get(poolName);
		if (currentObject == null) Console.log("Could not find pool " + poolName);

	}

	private static void createPool(String s)
	{
		RandomPool<String> r = new RandomArrayPool<String>(Game.getSeed());
		currentObject = r;
		String poolName = s.split(" ")[2];
		game.getItemPools().put(poolName, r);
		Console.log("Creating item pool " + poolName);
	}

	private static void setTexture(String string)
	{
		makeItemData().setDescritpion(string.split(" ")[1]);
	}

	private static void setDescritpion(String substring)
	{
		makeItemData().setDescritpion(substring.substring(12));
	}

	private static void setReadableName(String substring)
	{
		makeItemData().setName(substring.substring(5));
	}

	private static void setStat(String s)
	{
		String statName = s.split(" ")[1];
		String type = s.split(" ")[2];
		String value = s.split(" ")[3];

		if (type.equals("MOD")) statName += "_MUL";
		else if (type.equals("FLAT")) statName += "_ADD";
		else if (!type.equals("BASE")) Console.log("Plese use \"Base\"");
		makeItemData().addStat(statName, Float.valueOf(value));
	}

	private static ItemData makeItemData()
	{
		if (currentObject instanceof ItemData) return (ItemData) currentObject;
		return null;
	}

	private static void createItem(String s)
	{
		String name = s.split(" ")[2];
		currentObject = new ItemData();
		game.getAllItems().put(name, (ItemData) currentObject);
		((ItemData) currentObject).setName(name);
	}

	private static void selectItem(String s)
	{
		String name = s.split(" ")[2];
		currentObject = game.getAllItems().get(name);
		if (currentObject == null) Console.log("Could not find item " + name);
	}
}
