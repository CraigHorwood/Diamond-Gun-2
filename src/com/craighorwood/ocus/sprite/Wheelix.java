package com.craighorwood.ocus.sprite;
import java.awt.Graphics;
import com.craighorwood.ocus.*;
public class Wheelix extends Enemy
{
	private int slowTime = 0;
	public Wheelix(int x, int y)
	{
		this.x = x;
		this.y = y;
		power = 0.5;
		xa = 1;
	}
	public void tick()
	{
		if (slowTime > 60) slowTime--;
		else slowTime = 0;
		move(xa, ya);
		if (x < 0 || x >= 304 || (level.getBlock(xSlot, ySlot + 1) == 0 && slowTime == 0))
		{
			x -= xa * 2;
			hitWall(xa, 0);
		}
		int dir = (int) Math.signum(xa);
		if (slowTime < 100)
		{
			for (int x = xSlot; x >= 0 && x < 20; x += dir)
			{
				if (level.getBlock(x, ySlot) > 0) break;
				java.util.List<Sprite> sprites = level.spriteMap[x + ySlot * 20];
				for (int i = 0; i < sprites.size(); i++)
				{
					if (sprites.get(i) instanceof Player && onGround)
					{
						Sound.playSound("rev");
						xa = 4 * dir;
						ya = -4;
						slowTime = 240;
						break;
					}
				}
			}
		}
		if (slowTime > 0) xa = slowTime / 60.0 * dir;
		ya += 0.5;
		super.tick();
	}
	public void render(Graphics g)
	{
		java.awt.Graphics2D g2d = (java.awt.Graphics2D) g;
		double r = x * 0.1;
		g2d.rotate(r, x + 8, y + 8);
		g2d.drawImage(Images.sht_enemies[0][4], (int) x, (int) y, null);
		g2d.rotate(-r, x + 8, y + 8);
	}
	protected void hitWall(double xa, double ya)
	{
		if (xa != 0)
		{
			this.xa = -this.xa;
		}
	}
	public boolean damage(Sprite damager)
	{
		if (Math.signum(xa) + level.player.dir == 0) die();
		else Sound.playSound("dink");
		return true;
	}
}