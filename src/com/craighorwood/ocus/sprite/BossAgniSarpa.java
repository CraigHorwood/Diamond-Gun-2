package com.craighorwood.ocus.sprite;
import java.awt.Graphics;
import com.craighorwood.ocus.*;
public class BossAgniSarpa extends Boss
{
	private static class Segment extends Sprite
	{
		public double health = 1;
		public double xOld, yOld;
		private boolean inLava = false;
		private BossAgniSarpa boss;
		private Segment parent;
		private int invincibleTime = 0;
		private Segment(BossAgniSarpa boss, Segment parent, int x, int y)
		{
			this.boss = boss;
			this.parent = parent;
			this.x = x;
			this.y = y;
			w = 30;
			h = 30;
			power = 0.2;
		}
		public void tick()
		{
			if (invincibleTime > 0) invincibleTime--;
			if (parent != null)
			{
				x += (parent.x - xOld) / 8.0;
				y += (parent.y - yOld) / 8.0;
				xOld = x;
				yOld = y;
			}
			else
			{
				x += xa;
				y += ya;
				ya += 0.1;
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
			byte standing = level.getBlock(xSlot, ySlot);
			if ((standing == 8 && !inLava) || (standing == 0 && inLava))
			{
				inLava = !inLava;
				for (int i = 0; i < 8; i++)
				{
					level.addSprite(new Particle(12, (int) (x + 16), (int) (y + 32), Math.random() * 4 - 2, Math.random() * -8));
				}
			}
		}
		public void render(Graphics g)
		{
			g.drawImage(Images.sht_boss1[health == 0 ? 1 : invincibleTime == 0 ? 0 : 2][0], (int) x, (int) y, null);
		}
		public boolean damage(Sprite damager)
		{
			if (damager instanceof Bullet && health > 0 && invincibleTime == 0)
			{
				if ((health -= 0.05) <= 0) health = 0;
				else
				{
					Sound.playSound("bosshurt");
					if (Math.random() > health)
					{
						double x = this.x + 16;
						double y = this.y + 16;
						double r = Math.atan2(y - level.player.y - 12 - level.player.ya, x - level.player.x - 8 - level.player.xa);
						level.addSprite(new Ember(x, y, Math.cos(r) * -2, Math.sin(r) * 2));
					}
					invincibleTime = 2;
				}
				boss.updateHealth();
				return true;
			}
			return false;
		}
		public void remove()
		{
		}
	}
	private Segment[] segments = new Segment[8];
	private double landed = 0;
	private int leapTime = 0;
	public BossAgniSarpa()
	{
		super("AGNI SARPA");
		for (int i = 0; i < segments.length; i++)
		{
			segments[i] = new Segment(this, i == 0 ? null : segments[i - 1], 96, 208);
		}
	}
	private int time = 0;
	public void tick()
	{
		if (!activated)
		{
			if (level.player.playerId == 0 && level.player.x < 176 && time == 0)
			{
				level.player.enabled = false;
				time = 258;
				Sound.playSound("rumble");
			}
			else if (time > 0)
			{
				if (--time > 129) level.explosionTime += 2;
				else if (time == 129)
				{
					Sound.playSound("roar");
					leap(96, 1);
				}
				else if (time == 0)
				{
					Music.playMusic(5);
					activated = true;
					level.player.enabled = true;
				}
			}
		}
		else
		{
			super.tick();
			if (leapTime == 0)
			{
				if (segments[0].ya > 0 && segments[7].y > 240)
				{
					landed = segments[7].x - 32 * segments[0].xa;
					for (int i = 0; i < segments.length; i++)
					{
						segments[i].removed = true;
					}
					leapTime = 160;
				}
			}
			else
			{
				if (--leapTime == 0) leap(landed, (level.player.x + 8 - landed) / 121);
			}
		}
	}
	public void updateHealth()
	{
		invincibleTime = 10;
		health = 0;
		for (int i = 0; i < segments.length; i++)
		{
			health += segments[i].health;
		}
		if (health == 0) die();
		else health /= 8.0;
	}
	private void leap(double x, double xa)
	{
		for (int i = 7; i >= 0; i--)
		{
			segments[i].removed = false;
			segments[i].x = x;
			segments[i].y = 208;
			segments[i].xOld = segments[i].x;
			segments[i].yOld = segments[i].y;
			level.addSprite(segments[i]);
		}
		segments[0].xa = xa;
		segments[0].ya = -6;
	}
	public void die()
	{
		Music.stopMusic();
		for (int i = 0; i < segments.length; i++)
		{
			level.addSprite(new Rubble((int) segments[i].x, (int) segments[i].y, i, 1));
			segments[i].removed = true;
		}
		level.boss = null;
		for (int x = 1; x < 20; x++)
		{
			level.setBlock(x, 13, (byte) 0);
		}
		Constants.BOSSES_KILLED = 2;
		super.die();
	}
}