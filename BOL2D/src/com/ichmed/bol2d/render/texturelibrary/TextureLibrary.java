package com.ichmed.bol2d.render.texturelibrary;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

import javax.imageio.ImageIO;

import com.ichmed.bol2d.gui.Console;
import com.ichmed.bol2d.render.TextUtil;

public abstract class TextureLibrary
{
	static boolean log;
	protected String libPath;

	protected static Map<String, TextureLibrary> libraries = new HashMap<String, TextureLibrary>();

	protected String name;

	public static TextureLibrary getTextureLibrary(String name)
	{
		return libraries.get(name);
	}
	
	public abstract void drawLibraryTextureRect(float x, float y, float width, float height, String name);

	protected static List<TextureData> getTextureData(String path) throws IOException
	{
		List<TextureData> l = new ArrayList<TextureData>();
		File raw = new File(path + "raw");
		l.add(createDefaultTexture());
		for (File f : raw.listFiles())
		{
			l.addAll(getTextureDataFromFile(f));
		}

		return l;
	}

	public TextureLibrary(String name, String path, boolean showStiching) throws IOException
	{
		this.name = name;
		log = showStiching;
		libPath = path;
	}

	protected abstract void populate(List<TextureData> data) throws IOException;

	private static TextureData createDefaultTexture()
	{
		BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = img.createGraphics();
		g2d.setColor(Color.WHITE);
		g2d.drawRect(0, 0, 16, 16);

		return new TextureData(img, "default");
	}

	private static List<TextureData> getTextureDataFromFile(File f) throws IOException
	{
		List<TextureData> l = new ArrayList<TextureData>();
		if (f.getName().endsWith(".fontdata"))
		{
			Scanner sc = new Scanner(f);
			String s;
			while (sc.hasNextLine())
			{
				s = sc.nextLine();
				if (log) Console.log(("Generating " + s));
				int size;
				String nameAWT;
				String nameTexture;
				if (s.startsWith("font"))
				{
					nameAWT = s.split(" ")[1];
					nameTexture = s.split(" ")[2];
					size = Integer.valueOf(s.split(" ")[3]);
					l.addAll(generateFont(nameAWT, nameTexture, size));
				}
			}
			sc.close();
		} else
		{
			l.add(new TextureData(ImageIO.read(f), f.getName().split("\\.")[0]));
		}

		return l;
	}

	private static List<TextureData> generateFont(String nameAWT, String nameTexture, int fontSize)
	{
		ArrayList<TextureData> l = new ArrayList<TextureLibrary.TextureData>();
		for (char c = ' '; c < ' ' + 96; c++)
		{
			BufferedImage bfdImg = new BufferedImage(fontSize, fontSize, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = bfdImg.createGraphics();
			g2d.setFont(new Font(nameAWT.replace("_", " "), Font.BOLD, fontSize * 2 / 3));
			g2d.drawChars(new char[] { c }, 0, 1, 0, fontSize / 2);
			l.add(new TextureData(bfdImg, "letter_" + nameTexture + "_" + TextUtil.getNameForChar(c)));
		}
		return l;
	}

	public static void cleanUpAll()
	{
		for (TextureLibrary t : libraries.values())
		{
			Console.log("Saving texture library \"" + t.name + "\"");
			t.cleanUp();
		}
	}

	public abstract void cleanUp();

	public static String getNameFromRawFile(File f)
	{
		return f.getName().split("\\.")[0];
	}

	public abstract boolean doesTextureExist(String texture);

	static class TextureData
	{
		BufferedImage awtImg;
		String name;

		public TextureData(BufferedImage awtImg, String name)
		{
			super();
			this.awtImg = awtImg;
			this.name = name;
		}
	}

	public int getNumberOfTextureCycles(String texture)
	{
		int ret = 0;
		for (; this.doesTextureExist(texture + "_" + (ret + 1)); ret++)
			;
		return ret == 0 ? 1 : ret;

	}
}
