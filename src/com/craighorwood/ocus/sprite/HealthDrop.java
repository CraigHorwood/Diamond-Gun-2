package com.craighorwood.ocus.sprite;
import java.awt.Graphics;
import com.craighorwood.ocus.*;
public class HealthDrop extends Sprite
{
	private double amount;
	public HealthDrop(int x, int y)
	{
		this.x = x;
		this.y = y;
		w = 6;
		h = 6;
		amount = 0;
		ya = -2;
	}
	public void tick()
	{
		if (amount == 0)
		{
			amount = 0.2;
			boolean chance = Math.random() >= level.player.health;
			if (level.player.health < 0.4 && chance) amount = 1;
			else if (level.player.health < 0.7 && chance) amount = 0.5;
			if (amount >= 0.5)
			{
				w = 10;
				if (amount == 1) h = 10;
			}
		}
		if (!onGround)
		{
			move(0, ya);
			ya++;
		}
		java.util.List<Sprite> hits = level.getHits(x, y, w, h);
		for (int i = 0; i < hits.size(); i++)
		{
			Sprite spr = hits.get(i);
			if (spr instanceof Player)
			{
				Sound.playSound("gethealth");
				Player player = (Player) spr;
				if ((player.health += amount) > 1)
				{
					player.health = 1;
					if (++player.gems > player.maxGems) player.gems = player.maxGems;
				}
				removed = true;
				break;
			}
		}
	}
	public void render(Graphics g)
	{
		int x0 = 0;
		int x1 = 1;
		int y1 = 1;
		if (amount == 0.5)
		{
			x0 = 2;
			x1 = 4;
		}
		else if (amount == 1)
		{
			x0 = 5;
			x1 = 7;
			y1 = 2;
		}
		for (int xx = x0; xx <= x1; xx++)
		{
			for (int yy = 0; yy <= y1; yy++)
			{
				g.drawImage(Images.sht_health[xx][yy], (int) (x) + ((xx - x0) << 2), (int) (y) + (yy << 2), null);
			}	
		}
	}
}