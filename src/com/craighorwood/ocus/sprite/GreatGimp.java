package com.craighorwood.ocus.sprite;
import java.awt.Graphics;

import com.craighorwood.ocus.*;
public class GreatGimp extends Gimp
{
	private int health = 50;
	private int invincibleTime = 0;
	public GreatGimp(int x, int y)
	{
		super(x, y);
		power = 0.8;
		w = 94;
		h = 94;
	}
	public void tick()
	{
		if (invincibleTime > 0) invincibleTime--;
		super.tick();
	}
	public void render(Graphics g)
	{
		if (invincibleTime < 9) g.drawImage(Images.sht_enemies[onGround ? 0 : 1][0], (int) x, (int) y, w + 2, h + 2, null);
	}
	protected void bounce()
	{
		Sound.playSound("enemyjump");
		ya -= 3;
		super.bounce();
	}
	public boolean damage(Sprite damager)
	{
		if (invincibleTime == 0)
		{
			Sound.playSound("bosshurt");
			if (--health == 0) die();
			else invincibleTime = 10;
			return true;
		}
		return false;
	}
	public void die()
	{
		for (int i = 0; i < 10; i++)
		{
			level.addSprite(new HealthDrop((int) (x + i * w / 10.0), (int) (y) + (h >> 1) + 1));
		}
		for (int i = 0; i < 5; i++)
		{
			Explosion ex = new Explosion(x + Math.random() * 96, y + Math.random() * 96);
			ex.destroyer = false;
			level.addSprite(ex);
		}
		removed = true;
	}
}