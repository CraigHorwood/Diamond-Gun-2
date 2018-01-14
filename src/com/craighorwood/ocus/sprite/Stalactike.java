package com.craighorwood.ocus.sprite;
import java.awt.Graphics;
import com.craighorwood.ocus.Images;
public class Stalactike extends Enemy
{
	private int castingTime = 0;
	public Stalactike(int x, int y)
	{
		this.x = x;
		this.y = y;
		power = 0.5;
		dropsHealth = false;
	}
	public void tick()
	{
		if (ya == 0 && castingTime == 0)
		{
			for (int y = ySlot; y < ySlot + 8; y++)
			{
				if (y >= 15 || level.getBlock(xSlot, y) > 0) break;
				java.util.List<Sprite> sprites = level.spriteMap[xSlot + y * 20];
				for (int i = 0; i < sprites.size(); i++)
				{
					if (sprites.get(i) instanceof Player)
					{
						castingTime++;
						break;
					}
				}
			}
		}
		else if (castingTime > 0 && castingTime < 60)
		{
			if (++castingTime == 60) ya = 1;
		}
		else
		{
			move(0, ya);
			ya += 0.5;
		}
		super.tick();
	}
	public void render(Graphics g)
	{
		g.drawImage(Images.sht_enemies[castingTime > 0 ? ((60 - castingTime) / castingTime) & 1 : 0][1], (int) x, (int) y, null);
	}
	protected void hitWall(double xa, double ya)
	{
		if (xa == 0) die();
	}
}