package com.craighorwood.ocus.sprite;
import java.awt.Graphics;
import com.craighorwood.ocus.*;
public class Rubble extends Sprite
{
	private int type, boss;
	public Rubble(int x, int y, int type, int boss)
	{
		this.x = x;
		this.y = y;
		this.type = type;
		this.boss = boss;
		if (boss == 0)
		{
			xa = Math.random() * 2;
			ya = -Math.random() * 4;
		}
		else if (boss == 1)
		{
			xa = Math.random() * 4 - 2;
			ya = Math.random() * 4;
		}
		else if (boss == 2)
		{
			xa = Math.random() - 0.5;
		}
	}
	public void tick()
	{
		x += xa;
		y += ya;
		ya += 0.1;
	}
	public void render(Graphics g)
	{
		if (boss == 0) g.drawImage(Images.sht_boss0[0][type], (int) x, (int) y, null);
		else if (boss == 1) g.drawImage(Images.sht_boss1[1][0], (int) x, (int) y, null);
		else if (boss == 2)
		{
			if (type == 0) g.drawImage(Images.bg_boss2, (int) x, (int) y, null);
			else g.drawImage(Images.sht_enemies[1][4], (int) x, (int) y, null);
		}
	}
}