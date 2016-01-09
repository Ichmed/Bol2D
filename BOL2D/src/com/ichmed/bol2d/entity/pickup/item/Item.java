package com.ichmed.bol2d.entity.pickup.item;

import java.util.*;

import com.ichmed.bol2d.entity.Entity;
import com.ichmed.bol2d.gui.Console;

public abstract class Item
{
	private List<StatContainer> stats = new ArrayList<StatContainer>();
	public String name = "please give me a name developer-senpai *-*";
	public String description;

	public abstract void init();

	public void addStat(String name, float value)
	{
		this.stats.add(new StatContainer(name, value));
	}

	public void pickup(Entity e)
	{
		Console.log(e.getName() + " picked up " + this.getName());
		for (StatContainer c : stats)
		{
			e.modStat(c.name, c.value);
			Console.log("    " + (c.value >= 0 ? "+" : "") + c.value + "  "  + c.name);
		}
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof Item))return false;
		return this.name.equals(((Item)obj).name);
	}

	private String getName()
	{
		return this.name;
	}

	public void drop(Entity e)
	{
		for (StatContainer c : stats)
			e.modStat(c.name, -c.value);
	}

	public boolean isUnique()
	{
		return false;
	}

	public abstract String getTexture();
}
