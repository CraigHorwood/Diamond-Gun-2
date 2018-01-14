package com.craighorwood.ocus.state;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import com.craighorwood.ocus.*;
public class State
{
	private OcusMain main;
	public List<StateButton> buttons = new ArrayList<StateButton>();
	public boolean hideMouse = false;
	public void init(OcusMain main)
	{
		this.main = main;
	}
	public void tick(Input input)
	{
	}
	public void render(Graphics g)
	{
		for (int i = 0; i < buttons.size(); i++)
		{
			buttons.get(i).render(g);
		}
	}
	public void setState(State newState)
	{
		main.setState(newState);
	}
	protected void buttonPressed(int id)
	{
	}
	protected void drawString(String msg, Graphics g, int x, int y)
	{
		for (int i = 0; i < msg.length(); i++)
		{
			int ch = Constants.CHARACTERS.indexOf(msg.charAt(i));
			if (ch >= 0)
			{
				g.drawImage(Images.sht_font[ch % 26][ch / 26], x + (i << 3), y, null);
			}
		}
	}
	protected void fillBackground(Graphics g)
	{
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 320, 240);
	}
	protected void fillHud(Graphics g)
	{
		g.setColor(Color.BLACK);
		g.fillRect(0, 240, 320, 40);
	}
	protected void fillCenteredDialog(Graphics g, int w, int h)
	{
		int x = 160 - (w >> 1);
		int y = 120 - (h >> 1);
		g.setColor(Color.BLACK);
		g.fillRect(x, y, w, h);
		g.setColor(Color.WHITE);
		g.drawRect(x, y, w, h);
	}
	public void mousePressed(int x, int y)
	{
		for (int i = 0; i < buttons.size(); i++)
		{
			StateButton b = buttons.get(i);
			if (b.frame == 1) b.frame = 0;
		}
	}
	public void mouseReleased(int x, int y)
	{
		for (int i = 0; i < buttons.size(); i++)
		{
			StateButton b = buttons.get(i);
			if (x > b.x && x < b.x + 128 && y > b.y && y < b.y + 16)
			{
				b.frame = 1;
				buttonPressed(i);
				break;
			}
		}
	}
	public void mouseMoved(int x, int y)
	{
		for (int i = 0; i < buttons.size(); i++)
		{
			StateButton b = buttons.get(i);
			if (x > b.x && x < b.x + 128 && y > b.y && y < b.y + 16) b.frame = 1;
			else b.frame = 0;
		}
	}
}