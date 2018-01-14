package com.craighorwood.ocus.sprite;
import java.awt.Graphics;

import com.craighorwood.ocus.*;
public class BossKhan extends Boss
{
	public int time = 180, deadTime = 0, growTime = 0;
	private int attackTime = 0, waitTime = 0, stompTime = 0;
	private int attackPhase = 0;
	private boolean passageOn = false, passageLater = false;
	public BossKhan(boolean playSound)
	{
		super("KHAN");
		x = 224;
		y = 80;
		w = 62;
		h = 62;
		power = 0.5;
		if (playSound) Sound.playSound("roar");
	}
	public void tick()
	{
		if (!activated)
		{
			level.player.enabled = false;
			if (--time > 120)
			{
				level.explosionTime += 2;
			}
			else if (time == 0)
			{
				Music.playMusic(6);
				Sound.playSound("bossdoor");
				level.setBlock(13, 13, (byte) 0);
				level.player.enabled = true;
				activated = true;
				waitTime = 100;
			}
		}
		else
		{
			if (deadTime > 0)
			{
				if (++deadTime == 120)
				{
					Sound.playSound("bossdoor");
					for (int i = 0; i < 11; i++)
					{
						level.setBlock(1, i, (byte) 2);
					}
				}
				if (growTime > 60)
				{
					if (growTime > 120) growTime += 32;
					else growTime++;
				}
				return;
			}
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
			if (attackTime > 0)
			{
				switch (attackPhase)
				{
				case 0:
					if (attackTime % 120 == 0)
					{
						int skip = random.nextInt(5) + 1;
						for (int i = 0; i < 7; i++)
						{
							if (i != skip) level.addSprite(new BlueFlame((i << 5) + 16));
						}
					}
					else if (attackTime % 60 == 0)
					{
						Sound.playSound("froosh");
						if (health < 0.3) level.addSprite(new EnergyBall(true, x - 12, y + 26, -6, 0));
					}
					break;
				case 1:
					if (attackTime % ((int) (health * 20) + 20) == 0)
					{
						Sound.playSound("laser");
						double r = Math.atan2(level.player.y - y - 26, level.player.x - x + 6);
						level.addSprite(new EnergyBall(false, x - 6, y + 26, Math.cos(r) * 6, Math.sin(r) * 6));
					}
					break;
				}
				if (--attackTime == 0)
				{
					waitTime = (int) (health * 70 + 30);
					if (attackPhase == 2) togglePassage();
					if (++attackPhase == 3) attackPhase = 0;
				}
			}
			else if (waitTime > 0)
			{
				if (--waitTime == 0)
				{
					if (attackPhase == 0) attackTime = 360;
					else if (attackPhase == 1) attackTime = 240;
					else passageLater = true;
				}
			}
			if (stompTime > 0) stompTime--;
			if (passageLater && level.player.onGround)
			{
				attackTime = (int) (health * 60 + 240);
				stompTime = 6;
				togglePassage();
			}
		}
	}
	public void render(Graphics g)
	{
		int w = 64;
		int h = 64;
		if (growTime > 60)
		{
			w += growTime - 60;
			h += growTime - 60;
		}
		if (time > 160) w = h = (int) (32 + (180 - time) / 20.0 * 32);
		else if (stompTime > 0) h = 64 - (stompTime << 1);
		if (invincibleTime < 9 ^ (deadTime > 0 && Math.random() < 0.5)) g.drawImage(Images.sht_gunlords[!activated || (attackPhase == 1 && waitTime == 0) ? 5 : 4][deadTime > 0 && growTime == 0 ? 0 : 1], (int) (x - growTime / 5), (int) (y + 64 - h), w, h, null);
	}
	public boolean damage(Sprite damager)
	{
		if (deadTime == 0 && damager instanceof Bullet && invincibleTime == 0)
		{
			if (((Bullet) damager).gunLevel == -1)
			{
				if ((health -= 0.01) <= 0) die();
				else
				{
					Sound.playSound("bosshurt");
					invincibleTime = 10;
				}
				return true;
			}
		}
		return false;
	}
	public void die()
	{
		Music.stopMusic();
		level.boss = null;
		if (passageOn) togglePassage();
		Constants.BOSSES_KILLED = 6;
		super.die();
		removed = false;
		waitTime = 0;
		deadTime = 1;
	}
	private void togglePassage()
	{
		Sound.playSound("bossdoor");
		byte to = (byte) (passageOn ? 0 : 1);
		level.setBlock(9, 3, to);
		level.setBlock(9, 4, to);
		for (int i = 0; i < 3; i++)
		{
			level.setBlock(i + 7, 5, to);
		}
		level.setBlock(4, 7, to);
		level.setBlock(4, 8, to);
		for (int i = 0; i < 4; i++)
		{
			level.setBlock(5, i + 8, to);
		}
		for (int i = 0; i < 6; i++)
		{
			level.setBlock(i + 6, 11, to);
		}
		passageOn = !passageOn;
		passageLater = false;
	}
}