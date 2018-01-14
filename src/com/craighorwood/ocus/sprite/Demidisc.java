package com.craighorwood.ocus.sprite;
import java.awt.Graphics;
import com.craighorwood.ocus.Images;
public class Demidisc extends Enemy
{
	private int xo, yo;
	private double time = 0;
	private boolean spin;
	public Demidisc(int x, int y)
	{
		this.x = x;
		this.y = y;
		xo = (int) x;
		yo = (int) y;
		spin = ((x >> 4) & 1) == 0;
		power = 0.5;
	}
	public void tick()
	{
		super.tick();
		time += 0.04;
		x = xo + (spin ? Math.cos(time) : Math.sin(time)) * 48 - 8;
		y = yo + (spin ? Math.sin(time) : Math.cos(time)) * 48 - 8;
	}
	public void render(Graphics g)
	{
		g.drawImage(Images.sht_enemies[0][3], (int) x, (int) y, null);
	}
}