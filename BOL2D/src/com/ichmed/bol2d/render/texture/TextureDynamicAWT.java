package com.ichmed.bol2d.render.texture;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class TextureDynamicAWT implements Texture
{
	int width, height;
	
	TextureSimple t;
	
	private final BufferedImage awtImage;

	public TextureDynamicAWT(int width, int height)
	{
		this.awtImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}
	
	public Graphics2D getGraphics()
	{
		return this.awtImage.createGraphics();
	}
	
	public void bind()
	{
		t = TextureSimple.makeTexture(awtImage);
		t.bind();
	}
}
