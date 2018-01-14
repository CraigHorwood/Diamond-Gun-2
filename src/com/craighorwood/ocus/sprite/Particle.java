package com.craighorwood.ocus.sprite;
import java.awt.Graphics;
import com.craighorwood.ocus.Constants;
public class Particle extends Sprite
{
	private int col;
	public Particle(int col, int x, int y, double xa, double ya)
	{
		this.col = col;
		this.x = x;
		this.y = y;
		this.xa = xa;
		this.ya = ya;
	}
	public void tick()
	{
		x += xa;
		y += ya;
		ya += 0.4;
		if (ya > 0 && col < 12)
		{
			if (level.getBlock((int) (x / 16), (int) (y / 16)) > 0) removed = true;
		}
	}
	public void render(Graphics g)
	{
		g.setColor(Constants.GUN_COLORS[col]);
		g.fillRect((int) x, (int) y, 1 + (col == 5 ? 0 : random.nextInt(2)), 1 + (col ==5 ? 0 : random.nextInt(2)));
	}
}