package com.craighorwood.ocus.sprite;
import java.awt.Graphics;
import com.craighorwood.ocus.*;
public class Osmosis extends Enemy
{
	private double rot;
	private int rotTime = 0, followTime = 0, followCooldown = 0;
	public Osmosis(int x, int y)
	{
		this.x = x;
		this.y = y;
		power = 0.5;
	}
	private int time = 0;
	public void tick()
	{
		time++;
		double xd = level.player.x - x;
		double yd = level.player.y - y;
		if (followCooldown > 0) followCooldown--;
		else if (xd * xd + yd * yd < 6400)
		{
			if (followTime == 0)
			{
				Sound.playSound("swash");
				followTime = 120;
			}
			else if (++followTime > 120) followTime = 120;
		}
		else if (followTime > 0) followTime--;
		if (followTime == 0)
		{
			if (rotTime == 0)
			{
				double ran = Math.random();
				if (ran < 0.06) rotTime = (int) (120 * ran / 0.06 * (Math.random() < 0.5 ? -1 : 1));
			}
			else
			{
				if (rotTime > 0) rotTime--;
				else if (rotTime < 0) rotTime++;
				rot += 3 * Math.signum(rotTime) * Math.PI / 180.0;
			}
		}
		else rot = Math.atan2(yd, xd);
		double speed = followTime == 0 ? 0.3 : 0.8;
		xa = Math.cos(rot) * speed;
		ya = Math.sin(rot) * speed;
		move(xa, ya);
		super.tick();
	}
	public void render(Graphics g)
	{
		int sh = followTime == 0 ? 4 : 2;
		g.drawImage(Images.sht_enemies[((time >> sh) & 1) << (((time >> sh) & 2) >> 1)][2], (int) x, (int) y, null);
	}
	protected void hitWall(double xa, double ya)
	{
		Sound.playSound("bubbles");
		rot += Math.PI;
		if (followTime > 0)
		{
			followTime = 0;
			followCooldown = 60;
		}
	}
	public boolean damage(Sprite damager)
	{
		die();
		return true;
	}
}