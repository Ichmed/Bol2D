package com.ichmed.bol2d.util.procedural;

import java.util.*;

public class RandomArrayPool<T> implements RandomPool<T>
{
	private int size;
	private int amount;
	
	private List<Container<T>> content = new ArrayList<Container<T>>();

	private final Random randomGen;
	
	public RandomArrayPool()
	{
		this.randomGen = new Random();
	}
	
	public RandomArrayPool(long seed)
	{
		this.randomGen = new Random(seed);
	}

	public void add(T t, int weight)
	{
		amount++;
		for(int i = 0; i < weight; i++)
			content.add(new Container<T>(weight, t));
		size += weight;
		content.sort(new Comparator<Container<T>>()
		{
			@Override
			public int compare(Container<T> o1, Container<T> o2)
			{
				return o1.weight == o2.weight ? 0 : o1.weight > o2.weight ? -1 : 1;
			}
		});
	}

	public T get(int value)
	{
		return content.get(value).entry;
	}

	public T getRandom()
	{
		return get(randomGen.nextInt(size));
	}
	
	public int size()
	{
		return size;
	}

	public int amount()
	{
		return amount;
	}
	
	private static class Container<T>
	{
		public Container(int weight, T entry)
		{
			super();
			this.weight = weight;
			this.entry = entry;
		}
		public int weight;
		T entry;
	}

}
