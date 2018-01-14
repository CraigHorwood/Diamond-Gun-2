package com.craighorwood.ocus.sprite;
import java.awt.Graphics;
import com.craighorwood.ocus.Images;
public class Coptop extends Enemy
{
	private double xo, yo;
	private boolean flying = false, casting = true;
	private int swoopCooldown = 0;
	public Coptop(int x, int y)
	{
		this.x = x;
		this.y = y;
		xo = x;
		yo = y;
		power = 0.5;
	}
	private int time = 0;
	public void tick()
	{
		time++;
		if (casting)
		{
			x = xo + Math.sin(time * 0.1) * 8;
			y = yo + Math.sin(time * 0.2) * 4;
			if (swoopCooldown > 0) swoopCooldown--;
			else if (level.player.onGround || level.player.ya >= 8)
			{
				double yd = level.player.y - yo;
				if (yd > 0 && yd < 96)
				{
					double xd = level.player.x - xo;
					if (xd * xd + yd * yd < 10816)
					{
						double r = Math.atan2(yd, xd);
						xa = Math.cos(r) * yd / 16;
						ya = Math.sin(r) * yd / 16;
						casting = false;
					}
				}
			}
		}
		else
		{
			move(xa, 0);
			if (x < 0 || x >= 304)
			{
				x -= xa;
				xa = -xa;
			}
			move(0, ya);
			ya -= 0.3;
			if (ya < 0 && y <= yo)
			{
				xo = x;
				swoopCooldown = 30;
				casting = true;
			}
		}
		super.tick();
	}
	public void render(Graphics g)
	{
		g.drawImage(Images.sht_enemies[0][flying ? 1 : 0], (int) x, (int) y, null);
		g.drawImage(Images.sht_enemies[((time >> 3) & 1) + 2][1], (int) x, (int) (y) - (flying ? 16 : 13), null);
	}
	public boolean damage(Sprite damager)
	{
		die();
		return true;
	}
}