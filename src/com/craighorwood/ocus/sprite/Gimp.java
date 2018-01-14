package com.craighorwood.ocus.sprite;
import java.awt.Graphics;
import com.craighorwood.ocus.Images;
public class Gimp extends Enemy
{
	private int bounceTime = 60, bounceDir = 1;
	public Gimp(int x, int y)
	{
		this.x = x;
		this.y = y;
		power = 0.5;
	}
	public void tick()
	{
		xa = 0;
		if (onGround)
		{
			if (bounceTime-- == 0) bounce();
		}
		else xa += (bounceDir << 1);
		move (xa, ya);
		if (ya < 6) ya += 0.5;
		super.tick();
	}
	public void render(Graphics g)
	{
		g.drawImage(Images.sht_enemies[onGround ? 0 : 1][0], (int) x, (int) y, null);
	}
	protected void bounce()
	{
		ya -= 6;
		bounceTime = 60;
		bounceDir = Math.random() < 0.5 ? -1 : 1;
		double xc = x + (bounceDir << 6);
		if (bounceDir == 1 && xc > 288) bounceDir = -1;
		else if (bounceDir == -1 && xc < 16) bounceDir = 1;
	}
	public boolean damage(Sprite damager)
	{
		die();
		return true;
	}
}