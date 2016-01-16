package com.ichmed.bol2d.render.animation;

import com.ichmed.bol2d.gui.Console;
import com.ichmed.bol2d.render.texturelibrary.TextureLibrary;

public class Animation
{
	protected boolean doesLoop = true;
	protected int framesPerTexture = 1;
	
	protected int phases;
	
	protected String texture = "default";
	protected String library;	
	protected IAnimated target;
	
	protected String lastTexture = "default";
	
	private int currentTicks = 0;
	private String currentTexture = "default";
	
	public Animation(IAnimated target, String library, String texture)
	{
		this(target, library, texture, 60);
	}
	
	public Animation(IAnimated target, String library, String texture, int length)
	{
		
		this.texture = texture;
		this.library = library;
		this.target = target;
		
		this.phases = TextureLibrary.getTextureLibrary(library).getNumberOfTextureCycles(texture);
		
		this.framesPerTexture = length / phases;
	}
	
	public void update(IAnimated target)
	{
		currentTicks++;
		this.currentTexture = creatCurrentTexture();
//		if(currentTexture.startsWith("loot"))
//		{
//			Console.log(phases);
//			Console.log(currentTexture);
//		}
		target.setTexture(this.currentTexture);
	}
	
	protected String creatCurrentTexture()
	{
		if(this.doesLoop || this.currentTicks < phases * framesPerTexture) 
			return texture + "_" + ((currentTicks / framesPerTexture) % phases + 1);
		else return lastTexture;
	}
}
