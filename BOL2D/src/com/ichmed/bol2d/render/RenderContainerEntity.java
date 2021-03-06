package com.ichmed.bol2d.render;

import org.lwjgl.util.vector.*;

import com.ichmed.bol2d.entity.Entity;

public class RenderContainerEntity implements IRenderContainer
{
	public Entity owner;
	boolean useEntityRotation;
	boolean useEntityColor;
	private String textureName = "";
	private Vector2f translate;
	private float rotation;
	private Vector3f color = RenderUtil.WHITE;
		
	public RenderContainerEntity(Entity owner, boolean useEntityRotation, boolean useEntityColor, String textureName, Vector2f translate)
	{
		super();
		this.owner = owner;
		this.useEntityRotation = useEntityRotation;
		this.useEntityColor = useEntityColor;
		this.textureName = textureName;
		this.translate = translate;
	}

	public void render()
	{
		float rot = rotation;
		if (this.useEntityRotation) rot += owner.rotation;
		if(useEntityColor)color = owner.color;
		RenderUtil.pushMatrix();
		RenderUtil.setColor(color);
		RenderUtil.translate(translate);
		Vector2f v = new Vector2f(owner.getCenter());
		RenderUtil.translate((Vector2f) v.scale(0.001f));
		RenderUtil.rotateByDegrees((float) rot);
		RenderUtil.translate(new Vector2f(-owner.getSize().getX() / 2000f, -owner.getSize().getY() / 2000f));
		RenderUtil.drawLibraryTextureRect(0f, 0f, owner.getSize().getX() / 1000f, owner.getSize().getY() / 1000f, this.textureName);
		RenderUtil.popMatrix();
	}

	public String getTextureName()
	{
		return textureName;
	}

	public void setTextureName(String textureName)
	{
		this.textureName = textureName;
	}
}
