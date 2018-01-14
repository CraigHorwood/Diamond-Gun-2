package com.craighorwood.ocus.sprite;
import java.awt.Graphics;
import com.craighorwood.ocus.*;
public class Adversary extends Player
{
	private boolean activated = false;
	private int xTarget = 7, yTarget = 13;
	private boolean reachedTarget = false;
	private int attackTime = 0, deadTime = 0;
	public Adversary(int x, int y)
	{
		super(x, y, 1);
		health = 3;
		gunLevel = 63;
		equippedGun = 5;
		dir = -1;
	}
	public void tick()
	{
		boolean left = false, right = false, a = false, b = false;
		if (!activated)
		{
			if (level.player.onGround && attackTime == 0)
			{
				level.player.enabled = false;
				level.player.dir = 1;
				attackTime = 180;
			}
			else if (attackTime > 0)
			{
				if (--attackTime < 120)
				{
					if (attackTime % 10 == 0) b = true;
					if (attackTime == 0)
					{
						Music.playMusic(5);
						level.player.enabled = true;
						attackTime = 240;
						activated = true;
					}
				}
			}
		}
		else
		{
			if (deadTime > 0)
			{
				if (--deadTime == 0)
				{
					level.player.enabled = true;
					level.boss = new BossKhan(true);
					level.addSprite(level.boss);
					remove();
				}
			}
			else if (level.player.enabled)
			{
				xTarget = level.player.xSlot - (level.player.dir << 1);
				while (xTarget < 0 || xTarget >= 20)
				{
					xTarget += level.player.dir;
				}
				yTarget = level.player.ySlot;
				if (level.player.gunLevel == 32767)
				{
					Music.stopMusic();
					Sound.playSound("gunlord1");
					level.player.dir = 1;
					level.player.enabled = false;
					xTarget = 14;
					yTarget = 8;
				}
			}
			if (xTarget != xSlot)
			{
				if (reachedTarget) reachedTarget = false;
				int l = xSlot - 1 + ySlot * 20;
				int r = xSlot + 1 + ySlot * 20;
				int u = xSlot + ySlot * 20 - 20;
				for (int i = 0; i < level.spriteMap[l].size(); i++)
				{
					Sprite spr = level.spriteMap[l].get(i);
					if (spr instanceof Bullet)
					{
						if (((Bullet) spr).shooter == level.player)
						{
							a = true;
							break;
						}
					}
				}
				if (!a)
				{
					for (int i = 0; i < level.spriteMap[r].size(); i++)
					{
						Sprite spr = level.spriteMap[r].get(i);
						if (spr instanceof Bullet)
						{
							if (((Bullet) spr).shooter == level.player)
							{
								a = true;
								break;
							}
						}
					}
				}
				if (l % 20 > 0)
				{
					while (level.getBlock(l % 20, l / 20) == 1)
					{
						l -= 20;
					}
				}
				if (r % 20 < 19)
				{
					while (level.getBlock(r % 20, r / 20) == 1)
					{
						r -= 20;
					}
				}
				int lxd = l % 20 - xTarget;
				int lyd = l / 20 - yTarget;
				int rxd = r % 20 - xTarget;
				int ryd = r / 20 - yTarget;
				int uxd = u % 20 - xTarget;
				int uyd = u / 20 - yTarget;
				int ld = lxd * lxd + lyd * lyd;
				int rd = rxd * rxd + ryd * ryd;
				int ud = uxd * uxd + uyd * uyd;
				int closest = min(ld, rd, ud);
				if (closest == ld)
				{
					left = true;
					if (l / 20 < ySlot) a = true;
				}
				else if (closest == rd)
				{
					right = true;
					if (r / 20 < ySlot) a = true;
				}
				else if (closest == ud) a = true;
			}
			else if (!reachedTarget)
			{
				dir = (int) Math.signum(level.player.x - x);
				if (level.player.gunLevel == 32767)
				{
					if (yTarget == ySlot)
					{
						x = 224;
						Sound.playSound("transform");
						deadTime = 120;
						reachedTarget = true;
					}
				}
				else reachedTarget = true;
			}
			if (deadTime == 0)
			{
				if (--attackTime < 120 && attackTime % 10 == 0 && Math.random() < (1 - health / 3.0 + 0.3))
				{
					b = true;
				}
				if (attackTime == 0) attackTime = 240;
			}
		}
		super.tick(left, right, false, false, a, b, false);
	}
	public void render(Graphics g)
	{
		if (!activated && attackTime > 0 && attackTime < 30 && Math.random() < 0.3) g.drawImage(Images.sht_gunlords[4][1], (int) x, (int) (y - 8), null);
		else
		{
			boolean shouldRender = Math.random() >= 0.3;
			if (deadTime == 0 || shouldRender) super.render(g);
			else if (!shouldRender) g.drawImage(Images.sht_gunlords[4][1], (int) x, (int) (y - 8), null);
		}
	}
	public boolean damage(Sprite damager)
	{
		if (health == 0 || !activated) return false;
		boolean damaged = super.damage(damager);
		if (damaged)
		{
			level.addSprite(new GunPhysics(x, y, -3, -3, (int) (5 - health * 2), true));
			if (health == 0) gunLevel = 0;
		}
		return damaged;
	}
	public void die()
	{
	}
	private int min(int a, int b, int c)
	{
		int n = a;
		if (b < n) n = b;
		if (c < n) n = c;
		return n;
	}
}