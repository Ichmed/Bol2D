package com.ichmed.bol2d.entity.pickup.item;

import org.lwjgl.util.vector.Vector2f;

import com.ichmed.bol2d.render.*;

public class RenderContainerItemStack implements IRenderContainer
{
	public RenderContainerItemStack(Item item, Vector2f size)
	{
		super();
		this.item = item;
		this.size = size;
	}

	Item item;
	private Vector2f size;
	
	@Override
	public void render()
	{
		RenderUtil.drawLibraryTextureRect(0, 0, size.x, size.y, item.getTexture());
	}

}
