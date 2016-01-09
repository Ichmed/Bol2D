package com.ichmed.bol2d.util;

import java.util.*;

public class Database
{
	private Map<String, Object> content = new HashMap<String, Object>();
	
	public Database(Database d)
	{
		this(d.content);
	}
	
	public Database(Map<String, Object> content)
	{
		this.content = new HashMap<String, Object>(content);
	}

	public Database()
	{
	}

	public void setFloat(String name, float value)
	{
		content.put(name, value);
	}
	
	public void modFloat(String name, float mod)
	{
		modFloat(name, mod, 0);
	}
	
	public void modFloat(String name, float mod, float initialValue)
	{
		float f = getFloatNoMod(name, initialValue);
		f += mod;
		setFloat(name, f);
	}
	
	public float getFloat(String name)
	{
		return getFloat(name, 1);
	}

	public float getFloatNoMod(String name, float initialValue)
	{
		Float f = (Float) content.get(name);
		if (f == null)
		{
			content.put(name, initialValue);
			f = initialValue;
		}
		return f;
	}

	public float getFloat(String name, float initialValue)
	{
		Float f = (Float) content.get(name);
		if (f == null)
		{
			content.put(name, initialValue);
			f = initialValue;
		}
		float mul = getFloatNoMod(name + "_MUL", 1);
		float add = getFloatNoMod(name + "_ADD", 0);
		return f*  mul + add;
	}
	
	public String getString(String name)
	{
		return getString(name, "");
	}
	
	public String getString(String name, String initialValue)
	{
		String s = (String)content.get(name);
		if(s == null)
		{
			content.put(name,  initialValue);
			s = initialValue;
		}
		return s;
	}
}
