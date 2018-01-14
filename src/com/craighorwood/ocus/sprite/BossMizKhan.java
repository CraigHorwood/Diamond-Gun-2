package com.craighorwood.ocus.sprite;
import java.awt.Graphics;
import java.util.ArrayList;
import com.craighorwood.ocus.*;
public class BossMizKhan extends Boss
{
	private int time = 480;
	private boolean mouthOpen = false;
	private double z = 1;
	private int attackPhase = 0, attackTime = 300;
	private int stepTime = 150, pushTime = 0;
	private java.util.List<MizEnergy> bullets = new ArrayList<MizEnergy>();
	public BossMizKhan()
	{
		super("MIZ KHAN");
	}
	public void tick()
	{
		if (!activated)
		{
			if (!level.player.enabled)
			{
				if (--time == 240)
				{
					Sound.playSound("mizkhanroar");
					level.explosionTime = 240;
					mouthOpen = true;
				}
				else if (time < 240) z += 1 / 120.0;
				if (time == 60)
				{
					Sound.playSound("bossdoor");
					level.setBlock(9, 14, (byte) 1);
				}
				else if (time == 0)
				{
					z = 3;
					level.player.enabled = true;
					mouthOpen = false;
					activated = true;
				}
			}
			else if (level.player.onGround)
			{
				Music.playMusic(8);
				level.player.enabled = false;
			}
		}
		else
		{
			super.tick();
			if (!level.player.removed)
			{
				if (pushTime > 0) pushTime--;
				if (stepTime > 0 && attackPhase != 2)
				{
					if (--stepTime == 0)
					{
						Sound.playSound("step");
						if ((z -= 0.1) <= 1)
						{
							Sound.playSound("mizkhanroar");
							Sound.playSound("rumbledeath");
							level.player.enabled = false;
							level.flashTime = 1;
							mouthOpen = true;
						}
						else
						{
							stepTime = (int) (health * 150);
							if (stepTime < 30) stepTime = 30;
						}
					}
				}
				else if (level.flashTime == 240) level.player.die();
				else if (stepTime < 0)
				{
					mouthOpen = Math.random() < 0.9;
					if (--stepTime == -300)
					{
						level.boss = null;
						Constants.BOSSES_KILLED = 7;
						Sound.playSound("bossdoor");
						level.setBlock(9, 14, (byte) 2);
						remove();
					}
					return;
				}
				if (z > 1 && attackTime > 0)
				{
					attackTime--;
					switch (attackPhase)
					{
					case 0:
						if (attackTime <= 120 && attackTime % 10 == 0)
						{
							Sound.playSound("mizkhanshot");
							shoot(0, (Math.random() - 0.5) * 0.01, (Math.random() - 0.5) * 0.01, false);
						}
						break;
					case 1:
						if (attackTime <= 120 && attackTime % 20 == 0)
						{
							Sound.playSound("mizkhanshot");
							shoot(0, 0, 0, true);
						}
						break;
					case 2:
						if (attackTime > 0 && attackTime <= 240 && attackTime % 60 == 0)
						{
							if (attackTime == 240) Sound.playSound("beam");
							int xx = 16;
							if (level.player.x > 240) xx = 256;
							else if (level.player.x > 160) xx = 180;
							else if (level.player.x > 64) xx = 92;
							level.addSprite(new DiamondBeam(xx));
						}
						break;
					case 3:
						if (attackTime <= 200 && attackTime % 20 == 0)
						{
							if (attackTime == 200) Sound.playSound("thunder");
							level.addSprite(new Lightning(random.nextInt(256) + 32, -64, 0, 3));
						}
						break;
					}
					if (attackTime == 0)
					{
						if (health < 0.5)
						{
							int lastPhase = attackPhase;
							while (attackPhase == lastPhase)
							{
								attackPhase = random.nextInt(4);
							}
						}
						else
						{
							attackPhase++;
							if (health < 0.7) attackPhase &= 3;
							else if (health < 0.8) attackPhase %= 3;
							else if (health < 0.9) attackPhase &= 1;
							else attackPhase = 0;
						}
						mouthOpen = false;
						attackTime = 300;
					}
				}
			}
			for (int i = 0; i < bullets.size(); i++)
			{
				MizEnergy me = bullets.get(i);
				if (!me.removed) me.tick();
				else bullets.remove(i--);
			}
		}
	}
	public void renderBackground(Graphics g)
	{
		boolean shouldRender = time < 300 && invincibleTime == 0 && (stepTime >= 0 || random.nextInt(240) > -stepTime);
		if (time >= 300 && time < 360 && Math.random() < 0.5) shouldRender = true;
		if (shouldRender)
		{
			double w = 213 / z;
			double h = 200 / z;
			int xp = (int) (160 - w / 2.0);
			int yp = (int) (140 - h / 2.0) - (stepTime < 15 ? stepTime >> 1 : 0);
			g.drawImage(Images.sht_boss6[mouthOpen ? 1 : 0][0], xp, yp, (int) w, (int) h, null);
		}
		renderBullets(g, false);
	}
	public boolean damage(Sprite damager)
	{
		if (invincibleTime == 0 && damager == null)
		{
			if ((health -= 0.00625) <= 0) die();
			else
			{
				Sound.playSound("bosshurt");
				invincibleTime = 10;
			}
			return true;
		}
		return false;
	}
	public void die()
	{
		if (stepTime >= 0)
		{
			Music.stopMusic();
			Sound.playSound("mizkhandeath");
			bullets.clear();
			stepTime = -1;
		}
	}
	public void renderBullets(Graphics g, boolean closer)
	{
		for (int i = 0; i < bullets.size(); i++)
		{
			MizEnergy me = bullets.get(i);
			if (!((me.z <= 1) ^ closer)) bullets.get(i).renderBackground(g);
		}
	}
	private void shoot(double x, double xa, double ya, boolean homing)
	{
		mouthOpen = true;
		MizEnergy me = new MizEnergy(x, 0.5, z, xa, ya, homing);
		me.init(level);
		bullets.add(me);
	}
	public void push()
	{
		if (stepTime >= 0 && z < 3 && pushTime == 0)
		{
			Sound.playSound("bang");
			if ((z += 0.1) > 3) z = 3;
			pushTime = 10;
		}
	}
}