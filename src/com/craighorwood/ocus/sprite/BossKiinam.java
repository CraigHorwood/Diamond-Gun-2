package com.craighorwood.ocus.sprite;
import java.awt.Graphics;

import com.craighorwood.ocus.*;
public class BossKiinam extends Boss
{
	private int time = 0;
	private double wiggleCenter = 80, wiggleSpeed = 0.02, wiggleSize = 64;
	private int jawOffset = 0;
	private int attackPhase = 0, attackTime = 0, nextPhaseTime = 1, nextCubeTime, nextCubePosition;
	private int noShootTime = 0;
	public BossKiinam(int x, int y)
	{
		super("K'IINAM");
		this.x = x;
		this.y = y;
		w = 18;
		h = 78;
		power = 0.5;
	}
	private boolean nextPositionOpen = true;
	public void tick()
	{
		if (!activated)
		{
			if (level.player.x < 96 && time == 0)
			{
				Sound.playSound("bossdoor");
				level.setBlock(4, 14, (byte) 1);
				for (int i = 0; i < 3; i++)
				{
					level.setBlock(19, 11 + i, (byte) 1);
				}
				level.player.enabled = false;
				time = 180;
			}
			else if (time > 0)
			{
				if (--time == 90) Sound.playSound("chant0");
				else if (time == 0)
				{
					Music.playMusic(5);
					activated = true;
					level.player.enabled = true;
					attackTime = random.nextInt(60) + 30;
					nextCubeTime = random.nextInt(240) + 180;
					nextCubePosition = (random.nextInt(2) * 80 + 64) >> 4;
				}
			}
		}
		else
		{
			super.tick();
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
			if (health < 0.54 && --nextPhaseTime == 0)
			{
				attackTime = 1;
				nextPhaseTime = random.nextInt(240) + 240;
				if (health >= 0.3) attackPhase = ~attackPhase & 1;
				else attackPhase = (attackPhase & 1) + 1;
			}
			if (noShootTime == 0 && attackPhase < 2 && time % 100 == 0)
			{
				if (attackPhase == 0) Sound.playSound("chant0");
				else Sound.playSound("chant1");
			}
			if (attackTime > 0 && noShootTime == 0)
			{
				attackTime--;
				switch (attackPhase)
				{
				case 0:
					if (attackTime < 16 && (attackTime & 7) == 0)
					{
						int offset = health < 0.85 ? (Math.random() < 0.5 ? 24 : 64) : 64;
						level.addSprite(new Fireball(x + 16, y + offset, 3, 0));
						jawOffset = 8;
						if (attackTime == 0) attackTime = random.nextInt(60) + 30;
					}
					break;
				case 1:
					if (attackTime == 0)
					{
						double r = -Math.atan2(level.player.y - y - 52, level.player.x - x - 8);
						level.addSprite(new Fireball(x + 16, y + 64, Math.cos(r) * 3.0, Math.sin(r) * -3.0));
						jawOffset = 8;
						attackTime = random.nextInt(15) + 20;
					}
					break;
				case 2:
					if (attackTime == 0)
					{
						Sound.playSound("chant2");
						for (int i = -4; i < 5; i++)
						{
							double r = i / 32.0 * Math.PI * 2;
							level.addSprite(new Fireball(x + 16, y + 64, Math.cos(r) * 3.0, Math.sin(r) * 3.0));
						}
						jawOffset = 8;
						attackTime = random.nextInt(100) + 100;
					}
					break;
				}
			}
			else noShootTime--;
			y = wiggleCenter + Math.sin(time * wiggleSpeed) * wiggleSize;
			if (jawOffset > 0) jawOffset--;
			if (--nextCubeTime < 24)
			{
				if ((nextPositionOpen = level.getBlock(17, nextCubePosition) == 0))
				{
					if (level.player.x > 256) level.player.x = 256;
				}
				if (nextCubeTime == 0)
				{
					nextCubeTime = random.nextInt(240) + 180;
					if (nextPositionOpen) level.setBlock(17, nextCubePosition, (byte) 23);
					nextCubePosition = (random.nextInt(2) * 80 + 64) >> 4;
				}
			}
		}
	}
	public void render(Graphics g)
	{
		if (invincibleTime < 9)
		{
			for (int i = 0; i < 5; i++)
			{
				g.drawImage(Images.sht_boss0[activated ? (noShootTime > 0 ? 2 : 1) : 0][i], (int) x, (int) (y) + (i << 4) + (i == 4 ? jawOffset : 0), null);
			}
		}
		if (nextCubeTime > 0 && nextCubeTime < 24)
		{
			if (nextPositionOpen) g.drawImage(Images.sht_blocks[23][0], 272 + (nextCubeTime << 1), nextCubePosition << 4, null);
		}
	}
	public boolean damage(Sprite damager)
	{
		if (invincibleTime == 0 || damager instanceof BronzeCube)
		{
			double damage = 1 / 120.0;
			if (damager instanceof BronzeCube)
			{
				Sound.playSound("stun");
				damage *= 4;
				noShootTime = (int) (((health * 4) / 4) * 240);
			}
			else Sound.playSound("bosshurt");
			double oldHealth = health;
			if ((health -= damage) <= 0) die();
			else
			{
				if ((oldHealth >= 0.8 && health <= 0.8) || (oldHealth >= 0.6 && health <= 0.6) || (oldHealth >= 0.4 && health <= 0.4) || (oldHealth >= 0.2 && health <= 0.2))
				{
					level.addSprite(new HealthDrop((int) (x + 8), (int) (y + 64)));
				}
				if (oldHealth >= 0.3 && health <= 0.3) attackPhase = 2;
			}
			invincibleTime = 10;
			return true;
		}
		return false;
	}
	public void die()
	{
		Music.stopMusic();
		for (int i = 0; i < 5; i++)
		{
			level.addSprite(new Rubble((int) x, (int) (y) + (i << 4), i, 0));
		}
		level.boss = null;
		level.setBlock(4, 14, (byte) 0);
		for (int i = 0; i < 3; i++)
		{
			level.setBlock(19, 11 + i, (byte) 0);
		}
		Constants.BOSSES_KILLED = 1;
		super.die();
	}
}