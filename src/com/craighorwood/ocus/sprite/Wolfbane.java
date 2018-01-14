package com.craighorwood.ocus.sprite;
import java.awt.Graphics;
import java.util.Random;

import com.craighorwood.ocus.*;
public class Wolfbane extends Sprite
{
	private double health = 1;
	private int invincibleTime = 0;
	private Random random = new Random(1397511762);
	public Wolfbane()
	{
		x = -32;
		y = 192;
		w = 30;
		h = 30;
	}
	private int time = 0;
	public void tick()
	{
		if (invincibleTime > 0) invincibleTime--;
		if (++time < 180)
		{
			if (time < 60) x += 2;
			if (random.nextDouble() < 0.1) Sound.playSound("gunlord0");
		}
	}
	public void render(Graphics g)
	{
		if (invincibleTime < 9) g.drawImage(Images.sht_gunlords[time < 180 && ((time & 7) > 0 && ((time + 1) & 7) > 0) ? 1 : 0][0], (int) x, (int) y, null);
	}
	public boolean damage(Sprite damager)
	{
		if (invincibleTime == 0)
		{
			Sound.playSound("bosshurt");
			Sound.playSound("gunlord1");
			if ((health -= 0.1) < 1.E-15) die();
			else
			{
				x -= 2;
				invincibleTime = 10;
			}
			return true;
		}
		return false;
	}
	public void die()
	{
		for (int xo = 0; xo < 2; xo++)
		{
			for (int yo = 0; yo < 2; yo++)
			{
				level.addSprite(new Explosion(x + (xo << 4), y + (yo << 4)));
			}
		}
		removed = true;
	}
}