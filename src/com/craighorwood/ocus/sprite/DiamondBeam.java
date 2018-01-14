package com.craighorwood.ocus.sprite;
import java.awt.Graphics;
import com.craighorwood.ocus.*;
public class DiamondBeam extends Sprite
{
	public DiamondBeam(int x)
	{
		this.x = x;
		y = -480;
		w = 46;
		h = 238;
		power = 1;
	}
	private int animTime = 0;
	public void tick()
	{
		animTime++;
		if ((y += 4) > 240) removed = true;
		java.util.List<Sprite> hits = level.getHits(x, y, w, h);
		for (int i = 0; i < hits.size(); i++)
		{
			if (hits.get(i) instanceof Player)
			{
				level.player.damage(this);
				break;
			}
		}
	}
	public void render(Graphics g)
	{
		g.drawImage(Images.sht_beam[(animTime >> 1) % 3][0], (int) x, (int) y, null);
	}
	public void remove()
	{
	}
}