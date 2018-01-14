package com.craighorwood.ocus.sprite;
import java.awt.Graphics;
import com.craighorwood.ocus.*;
public class Mitosis extends Enemy
{
	private int phase, rushCooldown = 0;
	private double xaa;
	public Mitosis(int x, int y, int phase)
	{
		this.x = x;
		this.y = y;
		this.phase = phase;
		w = 14 - (phase << 2);
		h = 14 - (phase << 2);
		power = 0.5 / (phase + 1.0);
		xaa = Math.random() < 0.5 ? -0.1 : 0.1;
	}
	public void tick()
	{
		if (rushCooldown > 0) rushCooldown--;
		else
		{
			double xd = level.player.x - x;
			double yd = level.player.y + 12 - y;
			if (yd < 0)
			{
				if (xd * xd + yd * yd < 6400)
				{
					double r = Math.atan2(yd, xd);
					Sound.playSound("swash");
					xa += Math.cos(r) * 5;
					ya += Math.sin(r) * 5;
					rushCooldown = 40;
				}
			}
			if (rushCooldown == 0 && Math.random() < 0.01)
			{
				Sound.playSound("bubbles");
				if (Math.random() < 0.5)
				{
					if (xaa < 0) xaa = 0;
					xaa += 0.1;
				}
				else
				{
					if (xaa > 0) xaa = 0;
					xaa -= 0.1;
				}
			}
		}
		xa += xaa;
		move(xa, ya);
		if (x < 0 || x > 304) hitWall(1, 0);
		if (y > 224) hitWall(0, 1);
		ya += 0.04;
		xa *= 0.8;
		ya *= 0.96;
		super.tick();
	}
	public void render(Graphics g)
	{
		g.drawImage(Images.sht_enemies[3][phase + 2], (int) x, (int) y, null);
	}
	public boolean damage(Sprite damager)
	{
		if (++phase < 3)
		{
			Mitosis m0 = new Mitosis((int) x, (int) y, phase);
			Mitosis m1 = new Mitosis((int) x, (int) y, phase);
			double r = Math.random() * 2 * Math.PI;
			m0.xa = Math.cos(r) * 4;
			m0.ya = Math.sin(r) * 4;
			m1.xa = -m0.xa;
			m1.ya = -m0.ya;
			level.addSprite(m0);
			level.addSprite(m1);
		}
		die();
		return true;
	}
	protected void hitWall(double xa, double ya)
	{
		if (ya > 0)
		{
			Sound.playSound("bubbles");
			this.ya = -3;
		}
		if (xa != 0)
		{
			Sound.playSound("bubbles");
			this.xa *= -2;
			xaa = -xaa;
		}
	}
}