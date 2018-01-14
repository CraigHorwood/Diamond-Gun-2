package com.craighorwood.ocus.sprite;
import java.awt.Graphics;
import com.craighorwood.ocus.*;
public class EnergyBall extends Sprite
{
	private boolean behaviour;
	public EnergyBall(boolean behaviour, double x, double y, double xa, double ya)
	{
		this.x = x;
		this.y = y;
		this.xa = xa;
		this.ya = ya;
		this.behaviour = behaviour;
		power = 1;
		w = 10;
		h = 10;
	}
	private int animTime = 0;
	public void tick()
	{
		animTime++;
		java.util.List<Sprite> hits = level.getHits(x, y, w, h);
		for (int i = 0; i < hits.size(); i++)
		{
			if (hits.get(i) instanceof Player)
			{
				level.player.damage(this);
				break;
			}
		}
		if (behaviour)
		{
			y += 2;
			if (y < level.player.y + 24 && y + 12 > level.player.y)
			{
				Sound.playSound("laser");
				behaviour = false;
			}
		}
		else
		{
			x += xa;
			y += ya;
		}
	}
	public void render(Graphics g)
	{
		g.drawImage(Images.sht_energy[(animTime >> 2) & 1][0], (int) x, (int) y, null);
	}
}