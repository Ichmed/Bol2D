package com.ichmed.bol2d.render;

import java.util.*;

public class TextUtil
{
	public static void drawText(String text, String font, float startX, float startY, float size)
	{
		drawText(text, font, startX, startY, size, TextOrientation.LEFT_BOUND);
	}

	public static void drawText(String text, String font, float startX, float startY, float size, TextOrientation orient)
	{
		List<String> l = seperateStringIntoLines(text);
		float posX = 0, posY = 0;
		for (String s : l)
		{
			float length = getStringLength(removeVariables(s));
			if (orient == TextOrientation.CENTERED) posX = -length / 2f;
			if (orient == TextOrientation.RIGHT_BOUND) posX = -length;
			for (int i = 0; i < s.length(); i++)
			{
				char c = s.toCharArray()[i];

				String name;
				if (c == '$')
				{
					if (s.length() < i + 2) name = "letter_" + font + "_" + getNameForChar('+');
					else if (s.toCharArray()[i + 1] == '$')
					{
						name = "letter_" + font + "_" + getNameForChar('$');
						i++;
					} else if (s.toCharArray()[i + 1] == '[')
					{
						name = s.substring(i + 2, s.indexOf(']', i + 1));
						i += name.length() + 2;
					} else name = "letter_" + font + "_" + getNameForChar('+');

				} else name = "letter_" + font + "_" + getNameForChar(c);
				RenderUtil.drawLibraryTextureRect(startX + posX * size, startY + posY, size, size, name);
				if (c != '$') posX += getWidthForChar(c);
			}
			posY -= size;
			posX = 0;
		}
	}

	public static String removeVariables(String s)
	{
		String ret = "";
		for (int i = 0; i < s.length(); i++)
		{
			char c = s.toCharArray()[i];

			String name;
			if (c == '$')
			{
				if (s.length() < i + 2) ret += '$';
				else if (s.toCharArray()[i + 1] == '$')
				{
					ret += '$';
					i++;
				} else if (s.toCharArray()[i + 1] == '[')
				{
					name = s.substring(i + 2, s.indexOf(']', i + 1));
					ret += "%";
					i += name.length() + 2;
				} else ret += '$';

			} else ret += c;
		}
		return ret;

	}

	public static float getStringLength(String s)
	{
		float f = 0;
		for (char c : s.toCharArray())
			f += getWidthForChar(c);
		return f;
	}

	public static List<String> seperateStringIntoLines(String s)
	{
		List<String> l = new ArrayList<String>();
		String t = "";
		for (int i = 0; i < s.length(); i++)
		{
			if (s.charAt(i) == '\n')
			{
				l.add(t);
				t = "";
			} else t += s.charAt(i);
		}
		if (!t.equals(""))
		{
			l.add(t);
		}
		return l;
	}

	public static enum TextOrientation
	{
		LEFT_BOUND, CENTERED, RIGHT_BOUND;
	}

	public static float getWidthForChar(char c)
	{
		switch (c)
		{
		case 'B':
			return 0.5f;
		case 'c':
			return 0.4f;
		case 'f':
			return 0.25f;
		case 'h':
			return 0.4f;
		case 'I':
			return 0.25f;
		case 'i':
			return 0.25f;
		case 'j':
			return 0.25f;
		case 'L':
			return 0.4f;
		case 'l':
			return 0.25f;
		case 'M':
			return 0.65f;
		case 'm':
			return 0.65f;
		case 'n':
			return 0.45f;
		case 'o':
			return 0.45f;
		case 'u':
			return 0.4f;
		case 'W':
			return 0.8f;
		case ' ':
			return 0.3f;
		default:
			return 0.55f;
		}
	}

	public static String getNameForChar(char c)
	{
		switch (c)
		{
		case '?':
			return "questionmark";
		case '!':
			return "exclamationmark";
		case ':':
			return "colon";
		case ';':
			return "semicolon";
		case '\\':
			return "backslash";
		case '/':
			return "forwardslash";
		case '^':
			return "circumflex";
		case '_':
			return "underscore";
		case '.':
			return "point";
		case ',':
			return "comma";
		case '-':
			return "dash";
		case '+':
			return "plus";
		case '#':
			return "pound";
		case '*':
			return "asterisk";
		case '\"':
			return "doublequotes";
		case '\'':
			return "quote";
		case '$':
			return "dollar";
		case '%':
			return "percent";
		case '&':
			return "ampersand";
		case '(':
			return "bracket_A_open";
		case ')':
			return "bracket_A_closed";
		case '{':
			return "bracket_B_open";
		case '}':
			return "bracket_B_closed";
		case '[':
			return "bracket_C_open";
		case ']':
			return "bracket_C_closed";
		case '<':
			return "bracket_D_open";
		case '>':
			return "bracket_D_closed";
		case '|':
			return "pipe";
		case '=':
			return "equals";
		case ' ':
			return "space";
		case '~':
			return "tilde";
		case '@':
			return "at";
		default:
			return "" + c;
		}
	}

}
