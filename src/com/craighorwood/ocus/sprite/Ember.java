package com.craighorwood.ocus.sprite;
import java.awt.Graphics;

import com.craighorwood.ocus.Images;
import com.craighorwood.ocus.Sound;
public class Ember extends Sprite
{
	public Ember(double x, double y, double xa, double ya)
	{
		this.x = x;
		this.y = y;
		this.xa = xa;
		this.ya = ya;
		power = 0.2;
		w = 4;
		h = 4;
	}
	public void tick()
	{
		move(xa, ya);
		if (level.getBlock(xSlot, ySlot) == 8)
		{
			remove();
			return;
		}
		ya += 0.1;
	}
	public void render(Graphics g)
	{
		g.drawImage(Images.bg_fireball, (int) x, (int) y, null);
	}
	protected void hitWall(double xa, double ya)
	{
		if (ya != 0)
		{
			Sound.playSound("lavasplash");
			level.addSprite(new Fire(x, ySlot << 4));
			remove();
		}
		else if (xa != 0) remove();
	}
}