package com.craighorwood.ocus.sprite;
import java.awt.Graphics;

import com.craighorwood.ocus.*;
public class BossCytosis extends Boss
{
	private static class Daughter extends Sprite
	{
		private BossCytosis boss;
		private Daughter(BossCytosis boss, int x, int y)
		{
			this.boss = boss;
			this.x = x;
			this.y = y;
			power = 0.2;
		}
		private int time = 0;
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
			if (xa == 0 && ya == 0)
			{
				time++;
				x = boss.x + 24 + Math.sin(time * 0.06) * 42;
				y = boss.y + 24 + Math.cos(time * 0.06) * 42;
			}
			else
			{
				x += xa;
				y += ya;
			}
		}
		public void render(Graphics g)
		{
			g.drawImage(Images.sht_enemies[1][4], (int) x, (int) y, null);
		}
		public boolean damage(Sprite damager)
		{
			Sound.playSound("dink");
			return true;
		}
	}
	private boolean firstTick = true;
	private Daughter[] daughters = new Daughter[8];
	private int daughtersLeft = 0;
	private int refillTime = 0, attackTime = 0, daughterTime = 0, moveTime = 0;
	private int xLast, yLast, xNext, yNext, xd, yd, duration;
	public BossCytosis(int x, int y)
	{
		super("CYTOSIS");
		this.x = x;
		this.y = y;
		w = 62;
		h = 62;
		power = 0.5;
	}
	private int time = 0;
	private double cos = 1, sin = 0;
	public void tick()
	{
		if (!activated)
		{
			if (firstTick)
			{
				level.setBlock(6, 14, (byte) 1);
				firstTick = false;
			}
			if (level.player.x < 176 && time == 0)
			{
				Sound.playSound("waterburst");
				for (int i = 0; i < 2; i++)
				{
					level.setBlock(19, 12 + i, (byte) 1);
				}
				level.player.enabled = false;
				time = 180;
				y = 16;
			}
			else if (time > 0)
			{
				double t = 180 - time;
				if (t < 120)
				{
					if (t == 76) refillTime = 92;
					double tt = t / 120.0 - 1;
					y = 72 * (tt * tt * (2.70158 * tt + 1.70158) + 1) + 16;
				}
				time--;
				if (time == 0)
				{
					Music.playMusic(5);
					activated = true;
					level.player.enabled = true;
					attackTime = 160 + random.nextInt(300);
					daughterTime = 130 + random.nextInt(40) - 20;
					xLast = (int) (x + 32);
					yLast = (int) (y + 32);
					xNext = 160;
					yNext = 120;
					xd = xNext - xLast;
					yd = yNext - yLast;
					duration = (int) (150 * Math.sqrt(xd * xd + yd * yd) / 112.0);
				}
			}
		}
		else
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
			time++;
			moveTime++;
			double r = Math.atan2(level.player.y - y - 32, level.player.x - x - 32);
			cos = Math.cos(r);
			sin = Math.sin(r);
			if (refillTime == 0)
			{
				if (attackTime > 0)
				{
					if (--attackTime <= 100)
					{
						int freq = (int) (health * 20);
						if (freq < 2) freq = 2;
						if (attackTime % freq == 0)
						{
							Sound.playSound("laser");
							level.addSprite(new Fireball(x + 32 + cos * 32, y + 32 + sin * 32, cos * 3, sin * 3));
							if (attackTime == 0) attackTime = 160 + random.nextInt(300);
						}
					}
				}
				if (daughterTime > 0)
				{
					if (--daughterTime == 0)
					{
						fireDaughter();
						daughterTime = 140 + random.nextInt(40) - 20;
					}
				}
			}
			x = tween(moveTime, xLast, xd, duration) - 32;
			y = tween(moveTime, yLast, yd, duration) - 32;
			if (moveTime == duration)
			{
				xLast = (int) (x + 32);
				yLast = (int) (y + 32);
				do
				{
					xNext = 48 + random.nextInt(3) * 112;
					yNext = 48 + random.nextInt(3) * 72;
				}
				while (xNext == xLast && yNext == yLast);
				xd = xNext - xLast;
				yd = yNext - yLast;
				duration = (int) (150 * Math.sqrt(xd * xd + yd * yd) / 112.0);
				moveTime = 0;
			}
			super.tick();
		}
		if (refillTime > 0)
		{
			if (--refillTime <= 91 && refillTime % 13 == 0)
			{
				Sound.playSound("swash");
				Daughter d = new Daughter(this, (int) x, (int) y);
				addDaughter(d);
				level.addSprite(d);
				daughtersLeft++;
			}
		}
	}
	public void render(Graphics g)
	{
		if ((time > 0 || activated) && invincibleTime < 9)
		{
			double scale = 1;
			if (!activated) scale = (180 - time) / 60.0;
			if (scale > 1) scale = 1;
			int s0 = (int) (64 * scale);
			int s1 = (int) (16 * scale);
			g.drawImage(Images.bg_boss2, (int) (x + 32 - (s0 >> 1)), (int) (y + 32 - (s0 >> 1)), s0, s0, null);
			g.drawImage(Images.sht_enemies[1][4], (int) (x + 32 - (s1 >> 1)), (int) (y + 32 - (s1 >> 1)), s1, s1, null);
		}
	}
	private void addDaughter(Daughter d)
	{
		for (int i = 0; i < daughters.length; i++)
		{
			if (daughters[i] == null)
			{
				daughters[i] = d;
				break;
			}
			else if (daughters[i].xa != 0 || daughters[i].ya != 0)
			{
				daughters[i] = d;
				break;
			}
		}
	}
	private void fireDaughter()
	{
		Sound.playSound("swash_big");
		int i;
		do
		{
			i = random.nextInt() & 7;
		}
		while (daughters[i].xa != 0 || daughters[i].ya != 0);
		daughters[i].xa = cos * 3;
		daughters[i].ya = sin * 3;
		if (--daughtersLeft == 0)
		{
			refillTime = 212 + random.nextInt(70) - 35;
		}
	}
	public boolean damage(Sprite damager)
	{
		if (!activated || invincibleTime > 0) return false;
		Sound.playSound("bosshurt");
		invincibleTime = 10;
		if ((health -= 0.01) <= 0) die();
		else if (((int) (health * 100) % 20) == 0) level.addSprite(new HealthDrop((int) (x + 32), (int) (y + 32)));
		return true;
	}
	public void die()
	{
		Music.stopMusic();
		level.addSprite(new Rubble((int) x, (int) y, 0, 2));
		level.addSprite(new Rubble((int) (x + 24), (int) (y + 24), 1, 2));
		for (int i = 0; i < daughters.length; i++)
		{
			if (!daughters[i].removed && daughters[i].xa == 0 && daughters[i].ya == 0)
			{
				level.addSprite(new Rubble((int) daughters[i].x, (int) daughters[i].y, 1, 2));
				daughters[i].remove();
			}
		}
		level.boss = null;
		level.setBlock(6, 14, (byte) 4);
		for (int i = 0; i < 2; i++)
		{
			level.setBlock(19, 12 + i, (byte) 4);
		}
		Constants.BOSSES_KILLED = 3;
		super.die();
	}
	private double tween(double t, double b, double c, int d)
	{
		if ((t /= d / 2.0) < 1.0) return c / 2.0 * t * t + b;
		return -c / 2.0 * ((--t) * (t - 2) - 1) + b;
	}
}