package com.craighorwood.ocus.sprite;
import java.awt.Graphics;
import com.craighorwood.ocus.*;
public class BlueFlame extends Sprite
{
	private int time = 0;
	public BlueFlame(int x)
	{
		this.x = x;
		y = 124;
		power = 1;
		w = 26;
		h = 98;
	}
	public void tick()
	{
		time++;
		if (time > 60)
		{
			java.util.List<Sprite> hits = level.getHits(x, y, w, h);
			for (int i = 0; i < hits.size(); i++)
			{
				if (hits.get(i) instanceof Player)
				{
					level.player.damage(this);
					break;
				}
			}
			if (time == 101) remove();
		}
	}
	public void render(Graphics g)
	{
		int frame = (time >> 2) & 1;
		if (time == 60 || time > 95) frame = 4;
		else if (time < 60) frame += 2;
		g.drawImage(Images.sht_blueflame[frame][0], (int) x, (int) y, null);
	}
}