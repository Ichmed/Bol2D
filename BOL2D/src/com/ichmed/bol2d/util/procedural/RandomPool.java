package com.ichmed.bol2d.util.procedural;

public interface RandomPool<T>
{
	public void add(T t, int weight);

	public T get(int value);

	public T getRandom();

	public int size();

	public int amount();

}
