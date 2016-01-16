package com.ichmed.bol2d.render.texturelibrary;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

import javax.imageio.ImageIO;

import org.lwjgl.util.vector.Vector4f;

import com.ichmed.bol2d.Game;
import com.ichmed.bol2d.gui.Console;
import com.ichmed.bol2d.render.*;

public class TextureLibrarySheet extends TextureLibrary
{
	private HashMap<String, Vector4f> textureCoordinates;
	private Texture textureGL;
	private BufferedImage textureAWT;
	private int size;

	public static void createLibrary(String name, String path, boolean showStiching) throws IOException
	{
		if (!path.endsWith("/")) path += "/";
		TextureLibrary lib = new TextureLibrarySheet(name, path, showStiching);
		lib.populate(getTextureData(path));
		TextureLibrary.libraries.put(name, lib);	
	}
	
	public TextureLibrarySheet(String name, String path, boolean showStiching) throws IOException
	{
		super(name, path, showStiching);
		textureCoordinates = new HashMap<String, Vector4f>();

		File lib = new File(path + "library.png");
		File raw = new File(path + "raw");
		
		if (needsRefreshing(lib, raw))
		{
			size = Game.getCurrentGame().getMaxTexLibSize();
			textureAWT = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);

		} else
		{
			loadLibrary(path);
		}
	}


	private boolean needsRefreshing(File lib, File raw)
	{
		for (File f : raw.listFiles())
			if (f.lastModified() > lib.lastModified()) return true;
		return false;
	}

	@Override
	protected void populate(List<TextureData> data) throws IOException
	{
		Graphics2D g2d = textureAWT.createGraphics();
		g2d.setColor(new Color(255, 0, 255));
		g2d.fillRect(0, 0, size, size);
		if (placeTexturesRecursiv(g2d, data) || placeTexturesRecursivSkipFirst(g2d, data))
		;
		textureGL = Texture.makeTexture(textureAWT);	
	}
	
	public void cleanUp()
	{
		try
		{
			ImageIO.write(textureAWT, "png", new File(libPath + this.name + ".png"));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private void loadLibrary(String path)
	{
	}

	private boolean placeTexturesRecursivSkipFirst(Graphics2D g2d, List<TextureData> raw) throws IOException
	{
		if (raw.size() == 0) return true;
		if (raw.size() < 2) return false;
		if (tryToPlaceImage(g2d, raw.get(1)))
		{
			ArrayList<TextureData> l = new ArrayList<TextureData>(raw);
			l.remove(1);
			if (placeTexturesRecursiv(g2d, l) || placeTexturesRecursivSkipFirst(g2d, l)) return true;
			Vector4f v = getCoordinates(raw.get(1).name);
			removeImage(v, g2d);
		}
		return false;
	}

	private boolean placeTexturesRecursiv(Graphics2D g2d, List<TextureData> raw) throws IOException
	{
		if (raw.size() == 0) return true;
		if (tryToPlaceImage(g2d, raw.get(0)))
		{
			ArrayList<TextureData> l = new ArrayList<TextureData>(raw);
			l.remove(0);
			if (placeTexturesRecursiv(g2d, l) || placeTexturesRecursivSkipFirst(g2d, l)) return true;
			Vector4f v = getCoordinates(raw.get(0).name);
			removeImage(v, g2d);
		}
		return false;
	}
	
	public boolean doesTextureExist(String texture)
	{
		return this.textureCoordinates.get(texture) != null;
	}
	
	private static void removeImage(Vector4f v, Graphics2D g2d)
	{
		g2d.drawRect((int) v.x, (int) v.y, (int) v.z, (int) v.w);
	}

	public boolean tryToPlaceImage(Graphics2D g2d, TextureData data)
	{
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
				if (spotIsEmpty(j, i, data.awtImg.getWidth(), data.awtImg.getHeight()))
				{
					int x = j;
					int y = i;
					if (log) Console.log("Placed " + data.name);
					for (int k = 0; k < data.awtImg.getWidth(); k++)
						for (int l = 0; l < data.awtImg.getHeight(); l++)
						{
							textureAWT.setRGB(k + x, l + y, 0);
						}
					g2d.drawImage(data.awtImg, x, y, null);
					textureCoordinates.put(data.name, new Vector4f(x, y, data.awtImg.getWidth(), data.awtImg.getHeight()));
					return true;
				}
		return false;
	}

	private boolean spotIsEmpty(int x, int y, int width, int height)
	{
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
			{
				if (!isPixelTransparent(x + i, y + j)) return false;
			}
		return true;
	}

	public boolean isPixelTransparent(int x, int y)
	{
		if (x >= size || y >= size) return false;
		Color c = new Color(textureAWT.getRGB(x, y));
		return c.getRed() == 255 && c.getBlue() == 255;
	}

	public Vector4f getCoordinates(String textureName)
	{
		Vector4f v = textureCoordinates.get(textureName);
		if (v == null)
		{
			if (!textureName.contains("_")) return getCoordinates("default");
			return getCoordinates(textureName.substring(0, textureName.lastIndexOf("_")));
		}
		v = new Vector4f(v);
		v.scale(1f / size);
		return v;
	}

	@Override
	public void drawLibraryTextureRect(float x, float y, float width, float height, String name)
	{
		Vector4f v = getCoordinates(name);
		
		float x1 = 1 - v.x;
		float y1 = 1 - v.y;
		float x2 = 1 - (v.x + v.z);
		float y2 = 1 - (v.y + v.w);
		
		this.textureGL.bind();
//		RenderUtil.drawRect(x, y, width, height);
//		RenderUtil.drawTexturedRect(new Vector4f(x, y, width, height), new Vector4f(0, 0, 1, 1));
		RenderUtil.drawTexturedRect(new Vector4f(x, y, x + width, y + height), new Vector4f(x1, y1, x2, y2));
	}
}
