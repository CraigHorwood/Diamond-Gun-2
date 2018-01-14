package com.craighorwood.ocus.sprite;
import java.awt.Graphics;
import com.craighorwood.ocus.*;
public class BossErebus extends Boss
{
	private int time = 0;
	private int attackPhase = 0;
	private int teleportTime = 61, attackTime = 0, deadTime = 0;
	public BossErebus()
	{
		super("EREBUS");
		x = 126;
		y = 80;
		w = 66;
		h = 42;
		power = 0.5;
	}
	public void tick()
	{
		if (!activated)
		{
			if (level.player.x > 208 && time == 0)
			{
				Sound.playSound("bossdoor");
				level.dark = 0;
				level.player.enabled = false;
				level.setBlock(6, 14, (byte) 1);
				for (int i = 0; i < 7; i++)
				{
					level.setBlock(18, i, (byte) (i == 0 ? 1 : 0));
				}
				time = 210;
			}
			else if (time > 0)
			{
				if (--time == 180) Sound.playSound("laugh");
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
			if (deadTime > 0)
			{
				level.dark = 0;
				if (Math.random() > deadTime / 100.0) level.dark = -1;
				if (--deadTime == 0)
				{
					level.dark = -1;
					level.setBlock(6, 14, (byte) 2);
					for (int i = 0; i < 7; i++)
					{
						level.setBlock(18, i, (byte) 2);
					}
					remove();
				}
				return;
			}
			super.tick();
			if (attackTime > 0)
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
				boolean onLeft = x == 32;
				switch (attackPhase)
				{
				case 0:
					if (attackTime % 20 == 0)
					{
						Sound.playSound("shoot_enemy");
						double r = onLeft ? (Math.PI / 4.0 - attackTime * Math.PI / 2.0 / 150.0) : (Math.PI * 0.75 + attackTime * Math.PI / 2.0 / 150.0);
						double cos = Math.cos(r);
						double sin = Math.sin(r);
						level.addSprite(new Bullet(x + 32 + cos * 32, y + 32 + sin * 32, cos * 5, sin * 5, -1, this));
					}
					break;
				case 1:
					y = 56 + Math.sin(attackTime * 0.1) * 56;
					if (attackTime % 20 == 0)
					{
						Sound.playSound("shoot_enemy");
						level.addSprite(new Bullet(onLeft ? x + 32 : x, y + 32, onLeft ? 6 : -6, 0, -1, this));
					}
					break;
				case 2:
					if (attackTime % 50 == 0)
					{
						Sound.playSound("shoot_enemy");
						int count = health < 0.4 ? 16 : 8;
						for (int i = 0; i < count; i++)
						{
							double r = Math.PI * 2 / count * i;
							double cos = Math.cos(r);
							double sin = Math.sin(r);
							level.addSprite(new Bullet(x + 32 + cos * 32, y + 32 + sin * 32, cos * 5, sin * 5, -1, this));
						}
					}
					break;
				}
				if (--attackTime == 0)
				{
					if (Math.random() > health) Sound.playSound("laugh");
					teleportTime = (int) (180 * health);
					if (teleportTime <= 60) teleportTime = 61;
				}
			}
			else if (teleportTime > 0)
			{
				if (--teleportTime == 60)
				{
					switch (random.nextInt(3))
					{
					case 0:
						x = 32;
						if (Math.random() < health) attackPhase = 0;
						else attackPhase = 1;
						break;
					case 1:
						x = 126;
						attackPhase = 2;
						break;
					case 2:
						x = 220;
						if (Math.random() < health) attackPhase = 0;
						else attackPhase = 1;
						break;
					}
					y = 80;
				}
				else if (teleportTime == 0) attackTime = 150;
			}
		}
	}
	public void render(Graphics g)
	{
		if (!activated)
		{
			if (time > 30 && time < 120 && Math.random() < 0.2) g.drawImage(Images.bg_boss4, (int) x, (int) y, null);
		}
		else if (teleportTime > 0 && teleportTime < 60 && Math.random() < 0.2) g.drawImage(Images.bg_boss4, (int) x, (int) y, null);
	}
	public boolean damage(Sprite damager)
	{
		if (invincibleTime == 0 && teleportTime < 60 && deadTime == 0)
		{
			Sound.playSound("bosshurt");
			if ((health -= 0.0125) <= 0) die();
			else invincibleTime = 10;
			return true;
		}
		return false;
	}
	public void die()
	{
		Music.stopMusic();
		level.dark = -1;
		level.boss = null;
		Constants.BOSSES_KILLED = 5;
		super.die();
		removed = false;
		teleportTime = 0;
		deadTime = 100;
	}
}