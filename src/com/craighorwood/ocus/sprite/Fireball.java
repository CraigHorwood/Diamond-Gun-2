package com.craighorwood.ocus.sprite;
import java.awt.Graphics;
import com.craighorwood.ocus.Images;
public class Fireball extends Sprite
{
	public Fireball(double x, double y, double xa, double ya)
	{
		this.x = x;
		this.y = y;
		this.xa = xa;
		this.ya = ya;
		power = 0.2;
		w = 4;
		h = 4;
	}
	public void tick()
	{
		x += xa;
		y += ya;
		java.util.List<Sprite> hits = level.getHits(x, y, w, h);
		for (int i = 0; i < hits.size(); i++)
		{
			Sprite spr = hits.get(i);
			if (spr instanceof Player)
			{
				removed = spr.damage(this);
				break;
			}
		}
	}
	public void render(Graphics g)
	{
		g.drawImage(Images.bg_fireball, (int) x, (int) y, null);
	}
	public boolean damage(Sprite damager)
	{
		die();
		return true;
	}
}