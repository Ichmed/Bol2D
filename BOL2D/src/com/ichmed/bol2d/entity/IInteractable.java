package com.ichmed.bol2d.entity;

public interface IInteractable
{
	public boolean isActive();
	
	public float getRange();
	
	public int[] getAcceptedKeys();
	public void interact(int key);
}
