package com.craighorwood.ocus.sprite;
import java.awt.Graphics;
import com.craighorwood.ocus.Images;
public class Lowlock extends Enemy
{
	private boolean horizontal;
	public Lowlock(int x, int y, boolean horizontal)
	{
		this.x = x;
		this.y = y;
		this.horizontal = horizontal;
		power = 0.5;
		if (horizontal) xa = Math.random() < 0.5 ? -1 : 1;
		else ya = Math.random() < 0.5 ? -1 : 1;
		dropsHealth = false;
	}
	public void tick()
	{
		java.util.List<Sprite> hits = level.getHits(x, y, w, h);
		for (int i = 0; i < hits.size(); i++)
		{
			if (hits.get(i) instanceof Player)
			{
				level.player.damage(this);
				break;
			}
		}
		move(xa, ya);
		if (x < 0 || x > 304 || y < 0 || y > 224) hitWall(horizontal ? xa : 0, horizontal ? 0 : ya);
	}
	public void render(Graphics g)
	{
		g.drawImage(horizontal ? Images.sht_enemies[0][5] : Images.sht_enemies[2][4], (int) x, (int) y, null);
	}
	public boolean damage(Sprite damager)
	{
		die();
		return true;
	}
	protected void hitWall(double xa, double ya)
	{
		if (horizontal) this.xa = -xa;
		else this.ya = -ya;
	}
}