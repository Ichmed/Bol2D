package com.ichmed.bol2d.util;

import java.util.*;

public class Database
{
	private Map<String, Object> content = new HashMap<String, Object>();

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
		f = f * getFloatNoMod(name + "_MUL", 1) + getFloatNoMod(name + "_ADD", 0);
		return f;
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
