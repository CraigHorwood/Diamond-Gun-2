package com.craighorwood.ocus.sprite;
import java.awt.Graphics;
import com.craighorwood.ocus.Images;
public class Fire extends Sprite
{
	private boolean firstTick = true;
	private int life = 180;
	public Fire(double x, double y)
	{
		this.x = x;
		this.y = y;
		power = 0.5;
	}
	public void tick()
	{
		if (firstTick)
		{
			firstTick = false;
			if (level.getBlock((int) (x / 16), (int) (y / 16)) > 0)
			{
				remove();
				return;
			}
		}
		java.util.List<Sprite> hits = level.getHits(x, y, w, h);
		for (int i = 0; i < hits.size(); i++)
		{
			if (hits.get(i) instanceof Player)
			{
				level.player.damage(this);
				break;
			}
		}
		if (--life == 0) removed = true;
	}
	public void render(Graphics g)
	{
		if (life > 60 || (life & 1) == 0) g.drawImage(Images.sht_fire[(life >> 2) & 1][0], (int) x, (int) y, null);
	}
}