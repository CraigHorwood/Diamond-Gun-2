package com.craighorwood.ocus.sprite;
import java.awt.Graphics;
import com.craighorwood.ocus.Images;
public class MizEnergy extends Sprite
{
	private int xp, yp;
	public double z;
	private boolean homing;
	public MizEnergy(double x, double y, double z, double xa, double ya, boolean homing)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.xa = xa;
		this.ya = ya;
		this.homing = homing;
		power = 1;
		w = 10;
		h = 10;
	}
	private int animTime = 0;
	public void tick()
	{
		animTime++;
		xp = (int) (x / z * 160 + 160);
		yp = (int) (y / z * 120 + 120);
		if (homing)
		{
			double r = Math.atan2(level.player.y - yp, level.player.x - xp);
			xa = Math.cos(r) * 0.01;
			ya = Math.sin(r) * 0.01;
		}
		w = (int) (12 / z);
		h = (int) (12 / z);
		x += xa;
		y += ya;
		z -= 1 / 60.0;
		if (z <= 1)
		{
			if (z > 0.9)
			{
				java.util.List<Sprite> hits = level.getHits(xp, yp, w, h);
				for (int i = 0; i < hits.size(); i++)
				{
					if (hits.get(i) instanceof Player)
					{
						level.player.damage(this);
						break;
					}
				}
			}
			if (xp < 0 || xp >= 320 || yp < 0 || yp >= 240 || z <= 0) remove();
		}
	}
	public void renderBackground(Graphics g)
	{
		g.drawImage(Images.sht_energy[(animTime >> 2) & 1][0], xp, yp, w, h, null);
	}
}