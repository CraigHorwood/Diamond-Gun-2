package com.craighorwood.ocus.sprite;
import java.awt.Graphics;
import java.util.Random;
import com.craighorwood.ocus.level.Level;
public class Sprite
{
	protected static final Random random = new Random(444);
	protected Level level;
	public double x, y, xa, ya, power = 0;
	public int w = 14, h = 14;
	public int xSlot, ySlot;
	public boolean removed = false;
	public boolean destroyer = false;
	protected boolean onGround = false;
	public void init(Level level)
	{
		this.level = level;
	}
	public void tick()
	{
	}
	public void render(Graphics g)
	{
	}
	protected void move(double xa, double ya)
	{
		onGround = false;
		if (level.isFree(this, x + xa, y, w, h, xa, 0))
		{
			x += xa;
		}
		else
		{
			hitWall(xa, 0);
			if (this instanceof Bullet) return;
			if (xa < 0)
			{
				double xx = x / 16.0;
				xa = -(xx - ((int) xx)) * 16;
			}
			else
			{
				double xx = (x + w) / 16.0;
				xa = 16 - (xx - ((int) xx)) * 16;
			}
			if (level.isFree(this, x + xa, y, w, h, xa, 0))
			{
				x += xa;
			}
		}
		if (level.isFree(this, x, y + ya, w, h, 0, ya))
		{
			y += ya;
		}
		else
		{
			if (ya > 0) onGround = true;
			if (this instanceof Player)
			{
				if (((Player) this).playerId == 3 && ya < 0) onGround = true;
			}
			else if (this instanceof Bullet)
			{
				hitWall(0, ya);
				return;
			}
			if (ya < 0)
			{
				double yy = y / 16.0;
				ya = -(yy - ((int) yy)) * 16;
			}
			else
			{
				double yy = (y + h) / 16.0;
				ya = 16 - (yy - ((int) yy)) * 16;
			}
			if (level.isFree(this, x, y + ya, w, h, 0, ya))
			{
				y += ya;
			}
			double yao = this.ya;
			this.ya = 0;
			hitWall(0, yao);
		}
	}
	protected void hitWall(double xa, double ya)
	{
	}
	public void hitSpikes()
	{
	}
	public void remove()
	{
		removed = true;
	}
	public void die()
	{
		if (removed) return;
		Explosion ex = new Explosion(x, y);
		ex.destroyer = false;
		level.addSprite(ex);
		removed = true;
	}
	public boolean damage(Sprite damager)
	{
		return false;
	}
}