package com.craighorwood.ocus;
import java.awt.event.*;
public class Input implements KeyListener
{
	private boolean[] buttons = new boolean[256];
	private boolean[] oldButtons = new boolean[256];
	public int lastPressed = 0;
	public void tick()
	{
		for (int i = 0; i < buttons.length; i++)
		{
			oldButtons[i] = buttons[i];
		}
		lastPressed = 0;
	}
	public void releaseAll()
	{
		for (int i = 0; i < buttons.length; i++)
		{
			buttons[i] = false;
		}
	}
	public boolean isKeyDown(int code)
	{
		return buttons[code];
	}
	public boolean isKeyPressed(int code)
	{
		return buttons[code] && !oldButtons[code];
	}
	public void keyPressed(KeyEvent ke)
	{
		int code = ke.getKeyCode();
		if (code >= 0 && code < buttons.length)
		{
			lastPressed = code;
			buttons[code] = true;
		}
	}
	public void keyReleased(KeyEvent ke)
	{
		int code = ke.getKeyCode();
		if (code >= 0 && code < buttons.length)
		{
			buttons[code] = false;
		}
	}
	public void keyTyped(KeyEvent ke)
	{
	}
}