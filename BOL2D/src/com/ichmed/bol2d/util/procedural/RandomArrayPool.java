package com.ichmed.bol2d.util.procedural;

import java.util.*;

public class RandomArrayPool<T> implements RandomPool<T>
{
	private int size;
	private int amount;
	
	private List<T> content = new ArrayList<T>();

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
			content.add(t);
		size += weight;
	}

	public T get(int value)
	{
		return content.get(value);
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

}
