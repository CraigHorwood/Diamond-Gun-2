package com.craighorwood.ocus.sprite;
import java.awt.Graphics;
import com.craighorwood.ocus.*;
public class BossVoltere extends Boss
{
	private boolean firstTick = true;
	private int dir = 1;
	private int walkTime = 0, castingTime = -39;
	private double walkSpeed = 2;
	private int seenPlayer = 0;
	private boolean turn = false;
	public BossVoltere()
	{
		super("VOLTERE");
		x = -32;
		y = 32;
		w = 30;
		h = 30;
	}
	private int time = 0;
	public void tick()
	{
		if (!activated)
		{
			if (firstTick)
			{
				for (int i = 0; i < 3; i++)
				{
					level.setBlock(0, i + 1, (byte) 0);
				}
				firstTick = false;
			}
			if ((level.player.x > 144 || level.player.y < 112) && time == 0)
			{
				level.player.enabled = false;
				time = 208;
			}
			else if (time > 0)
			{
				if (--time >= 120)
				{
					x += walkSpeed;
					walkTime = time - 120;
					if (Math.random() < 0.1) Sound.playSound("gunlord0");
				}
				else if (time == 60)
				{
					Sound.playSound("bossdoor");
					level.setBlock(1, 14, (byte) 1);
					for (int i = 0; i < 3; i++)
					{
						level.setBlock(0, i + 1, (byte) 1);
						level.setBlock(19, i + 11, (byte) 1);
					}
					level.setBlock(4, 4, (byte) 13);
					level.setBlock(8, 4, (byte) 13);
					level.setBlock(11, 4, (byte) 13);
					level.setBlock(15, 4, (byte) 13);
				}
				else if (time == 0)
				{
					Music.playMusic(5);
					activated = true;
					level.player.enabled = true;
					dir = 1;
				}
			}
		}
		else
		{
			super.tick();
			time++;
			java.util.List<Sprite> hits = level.getHits(x, y, w, h);
			for (int i = 0; i < hits.size(); i++)
			{
				if (hits.get(i) instanceof Player)
				{
					level.player.damage(this);
					break;
				}
			}
			if (seenPlayer == 0)
			{
				if (walkTime > 0)
				{
					x += walkSpeed * dir;
					if (--walkTime == 0)
					{
						castingTime = (int) (health * 70 + 30);
					}
				}
				if (castingTime > -40)
				{
					if (--castingTime == 0)
					{
						if (health >= 0.8 || Math.random() >= 0.5 * health)
						{
							Sound.playSound("electrocute");
							for (int i = -1; i < 2; i++)
							{
								level.addSprite(new Lightning(x + 10.5, y + 32, i * Math.PI / 16.0, 0));
							}
						}
						else
						{
							Sound.playSound("electrocast");
							for (int i = 0; i < 10; i++)
							{
								double xx = i * 30.8 + 6;
								if (xx < level.player.x - 56 || xx > level.player.x + 72) level.addSprite(new Lightning(i * 30.8 + 6, -256 - random.nextInt(64), 0, 0));
							}
						}
					}
					else if (castingTime == -40)
					{
						double dest = x;
						while (Math.abs(dest - x) < 5 || (turn && ((x < dest && dir == 1) || (x > dest && dir == -1))))
						{
							int r = random.nextInt(4);
							switch (r)
							{
							case 0:
								dest = 56;
								break;
							case 1:
								dest = 120;
								break;
							case 2:
								dest = 168;
								break;
							case 3:
								dest = 232;
								break;
							}
						}
						if (turn) turn = false;
						if ((x > dest && dir == 1) || (x < dest && dir == -1)) dir = -dir;
						double d = dest - x;
						if (dir == -1) d = x - dest;
						walkTime = (int) (d / walkSpeed);
					}
				}
				if (level.player.y <= 64 && dir == Math.signum(level.player.x - x))
				{
					Sound.playSound("gunlord1");
					seenPlayer = 40;
				}
			}
			else
			{
				if (time % 10 == 0 && dir == Math.signum(level.player.x - x))
				{
					Sound.playSound("shoot_enemy");
					Bullet bullet = new Bullet(x + (dir << 4) + 16, y + 16, dir * 6, 0, -1, this);
					bullet.power = 1;
					level.addSprite(bullet);
				}
				if (level.player.y >= 80 && level.player.onGround)
				{
					if (--seenPlayer == 0)
					{
						Sound.playSound("bossdoor");
						level.setBlock(4, 4, (byte) 13);
						level.setBlock(8, 4, (byte) 13);
						level.setBlock(11, 4, (byte) 13);
						level.setBlock(15, 4, (byte) 13);
						level.setBlock(4, 7, (byte) (random.nextInt(4) + 79));
						level.setBlock(8, 7, (byte) (random.nextInt(4) + 79));
						level.setBlock(11, 7, (byte) (random.nextInt(4) + 79));
						level.setBlock(15, 7, (byte) (random.nextInt(4) + 79));
						level.setBlock(3, 11, (byte) (random.nextInt(4) + 79));
						level.setBlock(7, 11, (byte) (random.nextInt(4) + 79));
						level.setBlock(12, 11, (byte) (random.nextInt(4) + 79));
						level.setBlock(16, 11, (byte) (random.nextInt(4) + 79));
					}
				}
				else if (level.player.y <= 64) seenPlayer = 40;
			}
		}
	}
	public void render(Graphics g)
	{
		if (invincibleTime < 9) g.drawImage(Images.sht_gunlords[((walkTime & 7) > 0 && ((walkTime + 1) & 7) > 0) ? 3 : 2][(dir - 1) / -2], (int) x, (int) y, null);
	}
	public boolean damage(Sprite damager)
	{
		if (invincibleTime == 0)
		{
			if (damager instanceof Bullet)
			{
				Sound.playSound("bosshurt");
				if ((health -= 0.015625) <= 0) die();
				else
				{
					if (seenPlayer == 0) turn = true;
					walkSpeed = 3 - health * 3 + 2;
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
		level.setBlock(1, 14, (byte) 0);
		for (int i = 0; i < 3; i++)
		{
			level.setBlock(19, i + 11, (byte) 0);
		}
		Constants.BOSSES_KILLED = 4;
		for (int xo = 0; xo < 2; xo++)
		{
			for (int yo = 0; yo < 2; yo++)
			{
				level.addSprite(new Explosion(x + (xo << 4), y + (yo << 4)));
			}
		}
		super.die();
	}
}