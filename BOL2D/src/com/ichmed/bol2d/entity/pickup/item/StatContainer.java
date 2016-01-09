package com.ichmed.bol2d.entity.pickup.item;

public class StatContainer
{
	public StatContainer(String name, float value)
	{
		super();
		this.name = name;
		this.value = value;
	}

	String name;
	float value;
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public float getValue()
	{
		return value;
	}
	public void setValue(float value)
	{
		this.value = value;
	}
	
	
}
