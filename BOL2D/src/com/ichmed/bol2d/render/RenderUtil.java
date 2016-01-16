package com.ichmed.bol2d.render;

import static org.lwjgl.opengl.GL11.*;

import java.util.*;

import org.lwjgl.util.vector.*;

import com.ichmed.bol2d.entity.Entity;
import com.ichmed.bol2d.render.texturelibrary.TextureLibrary;
import com.ichmed.bol2d.util.MathUtil;

public class RenderUtil
{
	private static Map<String, Vector3f> colors = new HashMap<String, Vector3f>();
	
	public static final Vector3f RED = new Vector3f(1, 0, 0);
	public static final Vector3f GREEN = new Vector3f(0, 1, 0);
	public static final Vector3f BLUE = new Vector3f(0, 0, 1);
	public static final Vector3f YELLOW = new Vector3f(1, 1, 0);
	public static final Vector3f CYAN = new Vector3f(0, 1, 1);
	public static final Vector3f MAGENTA = new Vector3f(1, 0, 1);
	
	public static final Vector3f WHITE = new Vector3f(1, 1, 1);
	public static final Vector3f BLACK = new Vector3f(0, 0, 0);
	
	static
	{
		colors.put("RED", RED);
		colors.put("GREEN", GREEN);
		colors.put("BLUE", BLUE);
		colors.put("YELLOW", YELLOW);
		colors.put("CYAN", CYAN);
		colors.put("MAGENTA", MAGENTA);
		
		colors.put("WHITE", WHITE);
		colors.put("BLACK", BLACK);
	}

	public static void drawLibraryTextureRect(float x, float y, float width, float height, String name)
	{
		String libName = "default";
		if(name.split("\\$").length > 1)
		{
			libName = name.split("\\$")[0];
			name = name.split("\\$")[1];
		}
		glEnable(GL_TEXTURE_2D);
		TextureLibrary t = TextureLibrary.getTextureLibrary(libName);
		t.drawLibraryTextureRect(x, y, width, height, name);
	}

	public static void drawTexturedRect(Vector4f coords, Vector4f textureCoords)
	{
		glEnable(GL_TEXTURE_2D);
		glBegin(GL_QUADS);
		{
			glTexCoord2f(textureCoords.x, textureCoords.y);
			glVertex2d(coords.x, coords.w);
			glTexCoord2f(textureCoords.z, textureCoords.y);
			glVertex2d(coords.z, coords.w);
			glTexCoord2f(textureCoords.z, textureCoords.w);
			glVertex2d(coords.z, coords.y);
			glTexCoord2f(textureCoords.x, textureCoords.w);
			glVertex2d(coords.x, coords.y);
		}
		glEnd();
	}

	public static void drawRect(double x, double y, double width, double height)
	{
		glDisable(GL_TEXTURE_2D);
		glBegin(GL_QUADS);
		{
			glTexCoord2f(0, 0);
			glVertex2d(x, y);
			glTexCoord2f(1, 0);
			glVertex2d(x + width, y);
			glTexCoord2f(1, 1);
			glVertex2d(x + width, y + height);
			glTexCoord2f(0, 1);
			glVertex2d(x, y + height);
		}
		glEnd();
	}

	private static Entity lastEntityCentered;

	public static void centerViewOnEntity(Entity entity)
	{
		Vector2f v = lastEntityCentered == null ? new Vector2f() : lastEntityCentered.getCenter();
		glTranslatef((v.x - entity.getCenter().x) / 1000, (v.y - entity.getCenter().y) / 1000, 0);
		lastEntityCentered = entity;
	}

	public static void resetLastEntityCentered()
	{
		lastEntityCentered = null;
	}
	
	public static void setColor(String color)
	{
		setColor(colors.get(color));
	}

	public static void setColor(Vector3f color)
	{
		setColor(color, 1);
	}

	public static void setColor(Vector3f color, float alpha)
	{
		glColor4f(color.x, color.y, color.z, alpha);
	}

	public static void pushMatrix()
	{
		glPushMatrix();
	}

	public static void popMatrix()
	{
		glPopMatrix();
	}

	public static void translate(Vector2f v)
	{
		glTranslatef(v.x, v.y, 0);
	}

	public static void rotateToDirection(Vector2f direction)
	{
		double d = MathUtil.getRotationForDirection(direction);
		glRotatef((float) d, 0, 0, 1);
	}

	public static void rotateByDegrees(float degrees)
	{
		glRotatef(degrees, 0, 0, 1);
	}
}
