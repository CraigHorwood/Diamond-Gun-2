package com.craighorwood.ocus.sprite;
import java.awt.Graphics;
import com.craighorwood.ocus.*;
public class Explosion extends Sprite
{
	private int xi, yi;
	private double xo, yo;
	public Explosion(double x, double y)
	{
		this.x = xo = x + 8;
		this.y = yo = y + 8;
		xi = (int) x;
		yi = (int) y;
		w = h = 0;
		destroyer = true;
		Sound.playSound("explosion");
	}
	private int time = 0;
	public void tick()
	{
		time++;
		w += 4;
		h += 4;
		xo += xa;
		yo += ya;
		x = xo - (w >> 1);
		y = yo - (h >> 1);
		if (w > 24) removed = true;
		move(xa, ya);
	}
	public void render(Graphics g)
	{
		if (time < 6) g.drawImage(Images.sht_explosion[time][0], xi, yi, null);
		g.setColor(Constants.GUN_COLORS[2]);
	}
}