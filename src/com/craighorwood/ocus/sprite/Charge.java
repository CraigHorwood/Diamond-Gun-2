package com.craighorwood.ocus.sprite;
import java.awt.Graphics;
import com.craighorwood.ocus.*;
public class Charge extends Sprite
{
	private int dir, flashTime = 0;
	public Charge(int x, int y, int dir)
	{
		this.x = x;
		this.y = y;
		this.dir = dir;
		w = 6;
		h = 6;
	}
	private int animTime = 0;
	public void tick()
	{
		animTime++;
		if (flashTime > 0)
		{
			if (--flashTime == 0) remove();
			return;
		}
		if (dir == 0) y -= 2;
		else if (dir == 1) x += 2;
		else if (dir == 2) y += 2;
		else if (dir == 3) x -= 2;
		if ((x - 8) % 16 == 0 && (y - 8) % 16 == 0)
		{
			byte b = level.getBlock(xSlot, ySlot);
			switch (b)
			{
			case 13:
				Sound.playSound("electricfail");
				destroyChargeBlock(xSlot, ySlot);
				remove();
				break;
			case 32:
			case 33:
				break;
			case 34:
				if (dir == 2) dir = 1;
				else if (dir == 3) dir = 0;
				break;
			case 35:
				if (dir == 2) dir = 3;
				else if (dir == 1) dir = 0;
				break;
			case 36:
				if (dir == 0) dir = 1;
				else if (dir == 3) dir = 2;
				break;
			case 37:
				if (dir == 0) dir = 3;
				else if (dir == 1) dir = 2;
				break;
			case 79:
			case 80:
			case 81:
			case 82:
				dir = b - 79;
				break;
			default:
				Sound.playSound("electricfail");
				flashTime = 30;
				break;
			}
		}
	}
	public void render(Graphics g)
	{
		if (((flashTime >> 1) & 1) == 0) g.drawImage(Images.sht_font[19 + ((animTime >> 1) & 1)][2], (int) (x - 4), (int) (y - 4), null);
	}
	private void destroyChargeBlock(int x, int y)
	{
		for (int xo = -1; xo < 2; xo++)
		{
			for (int yo = -1; yo < 2; yo++)
			{
				if (xo != 0 && yo != 0) continue;
				int xx = x + xo;
				int yy = y + yo;
				if (xx >= 0 && yy >= 0 && xx < 20 && yy < 15)
				{
					if (level.getBlock(xx, yy) == 13)
					{
						level.setBlock(xx, yy, (byte) 0);
						int count = random.nextInt(3) + 2;
						for (int i = 0; i < count; i++)
						{
							level.addSprite(new Particle(13, (xx << 4) + 8, (yy << 4) + 8, Math.random() - 0.5, -Math.random() * 4));
						}
						destroyChargeBlock(xx, yy);
					}
				}
			}
		}
	}
}