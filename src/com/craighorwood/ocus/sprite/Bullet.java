package com.craighorwood.ocus.sprite;
import java.awt.Graphics;
import com.craighorwood.ocus.Constants;
public class Bullet extends Sprite
{
	public int gunLevel = 0;
	public Sprite shooter;
	public Bullet(double x, double y, double xa, double ya, int gunLevel, Sprite shooter)
	{
		this.x = x;
		this.y = y;
		this.xa = xa;
		this.ya = ya;
		this.gunLevel = gunLevel;
		this.shooter = shooter;
		destroyer = true;
		w = h = 2;
		power = 0.5;
	}
	public void tick()
	{
		move(xa, ya);
		java.util.List<Sprite> hits = level.getHits(x, y, w, h);
		for (int i = 0; i < hits.size(); i++)
		{
			Sprite spr = hits.get(i);
			if (spr == shooter) continue;
			if (spr.damage(this)) removed = true;
		}
	}
	public void render(Graphics g)
	{
		g.setColor(Constants.GUN_COLORS[gunLevel + 1]);
		g.fillRect((int) x, (int) y, w, h);
	}
	protected void hitWall(double xa, double ya)
	{
		remove();
	}
}