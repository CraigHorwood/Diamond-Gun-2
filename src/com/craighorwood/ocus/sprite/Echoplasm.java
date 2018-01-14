package com.craighorwood.ocus.sprite;
import java.awt.Graphics;
import com.craighorwood.ocus.*;
public class Echoplasm extends Enemy
{
	private boolean[] slots;
	private int faceTime = 1, teleportTime = 0;
	public Echoplasm(boolean[] slots)
	{
		this.slots = slots;
	}
	public void tick()
	{
		if (level.dark > 0) return;
		if (faceTime > 0)
		{
			if (--faceTime >= 30 && faceTime <= 150)
			{
				if ((level.player.xa != 0 || level.player.ya != 1) && slots[level.player.xSlot + level.player.ySlot * 20]) attack();
			}
			else if (faceTime == 0) teleportTime = 120;
		}
		else if (teleportTime > 0)
		{
			if (--teleportTime == 0)
			{
				Sound.playSound("chatter" + random.nextInt(2));
				int i = 0;
				do
				{
					i = random.nextInt(300);
				}
				while (!slots[i]);
				x = (i % 20) << 4;
				y = (i / 20) << 4;
				faceTime = 180;
			}
		}
		else if (!level.player.enabled && !level.player.removed)
		{
			super.tick();
			double r = Math.atan2(level.player.y - y, level.player.x - x);
			x += Math.cos(r) * 4;
			y += Math.sin(r) * 4;
		}
	}
	public void render(Graphics g)
	{
		if (teleportTime == 0 && level.dark == 0)
		{
			boolean shouldRender = true;
			if (faceTime < 30) shouldRender = Math.random() < faceTime / 100.0;
			else if (faceTime > 150) shouldRender = Math.random() < 1.8 - faceTime / 100.0;
			if (shouldRender || !level.player.enabled) g.drawImage(Images.sht_enemies[1][5], (int) x, (int) y, null);
		}
	}
	public boolean damage(Sprite damager)
	{
		if (teleportTime > 0) return false;
		Sound.playSound("dink");
		return true;
	}
	private void attack()
	{
		Sound.playSound("scream");
		level.player.enabled = false;
		faceTime = 0;
		teleportTime = 0;
	}
}