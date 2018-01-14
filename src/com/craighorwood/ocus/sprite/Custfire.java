package com.craighorwood.ocus.sprite;
import java.awt.Graphics;
import com.craighorwood.ocus.*;
public class Custfire extends Enemy
{
	private int dir, shootTime = 110;
	public Custfire(int x, int y, int dir)
	{
		this.x = x;
		this.y = y;
		this.dir = dir;
	}
	public void tick()
	{
		if (--shootTime == 0)
		{
			Sound.playSound("shoot_enemy");
			Bullet bullet = new Bullet(x + ((dir + 1) << 3), y + 6, dir * 6, 0, -1, this);
			bullet.destroyer = false;
			level.addSprite(bullet);
			shootTime = 110;
		}
	}
	public void render(Graphics g)
	{
		g.drawImage(Images.sht_enemies[dir == 1 ? 2 : 3][0], (int) x, (int) y, null);
	}
}