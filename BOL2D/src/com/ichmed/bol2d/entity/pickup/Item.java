package com.ichmed.bol2d.entity.pickup;

import java.util.*;

import com.ichmed.bol2d.entity.Entity;

public abstract class Item
{
	private List<StatContainer> stats = new ArrayList<StatContainer>();

	public abstract void init();

	public void addStat(String name, float value)
	{
		System.out.println(name + " " + value);
		this.stats.add(new StatContainer(name, value));
	}

	public void pickup(Entity e)
	{
		for (StatContainer c : stats)
		{
			e.modStat(c.name, c.value);
			System.out.println(c.name + " " + c.value);
		}
	}

	public void drop(Entity e)
	{
		for (StatContainer c : stats)
			e.modStat(c.name, -c.value);
	}

	private static class StatContainer
	{
		public StatContainer(String name, float value)
		{
			super();
			this.name = name;
			this.value = value;
		}

		String name;
		float value;
	}
}
