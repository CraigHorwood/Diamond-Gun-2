package com.craighorwood.ocus.sprite;
import java.awt.Graphics;

import com.craighorwood.ocus.Images;
import com.craighorwood.ocus.Sound;
public class GunPhysics extends Sprite
{
	private int gunLevel;
	public boolean onConveyor = false, becomesReal = false;
	public GunPhysics(double x, double y, double xa, double ya, int gunLevel, boolean becomesReal)
	{
		this.x = x;
		this.y = y;
		this.xa = xa;
		this.ya = ya;
		this.gunLevel = gunLevel;
		this.becomesReal = becomesReal;
	}
	private int time = 0;
	public void tick()
	{
		time++;
		java.util.List<Sprite> hits = level.getHits(x, y, w, h);
		for (int i = 0; i < hits.size(); i++)
		{
			Sprite spr = hits.get(i);
			if (spr instanceof Player && time > 20)
			{
				Sound.playSound("getgun");
				remove();
			}
		}
		if (onGround)
		{
			if (becomesReal)
			{
				level.addSprite(new Collectible(x, y, (byte) 0, (byte) gunLevel));
				remove();
			}
			else if (onConveyor) x -= 2;
		}
		else
		{
			move(xa, ya);
			ya += 0.4;
		}
	}
	public void render(Graphics g)
	{
		g.drawImage(Images.sht_guns[gunLevel][0], (int) x, (int) y, null);
	}
}