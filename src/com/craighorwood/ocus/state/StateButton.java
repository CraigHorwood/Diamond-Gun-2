package com.craighorwood.ocus.state;
import java.awt.Graphics;
import com.craighorwood.ocus.Images;
public class StateButton
{
	private State parent;
	public int x, y;
	public String msg;
	public int frame = 0;
	public StateButton(State parent, int x, int y, String msg)
	{
		this.parent = parent;
		this.x = x;
		this.y = y;
		this.msg = msg;
	}
	public void render(Graphics g)
	{
		g.drawImage(Images.sht_button[0][frame], x, y, null);
		parent.drawString(msg, g, x - (msg.length() << 2) + 64, y + 4);
	}
}