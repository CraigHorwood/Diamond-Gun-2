package com.craighorwood.ocus.sprite;
import java.awt.Graphics;
import com.craighorwood.ocus.Images;
public class GoldCube extends Sprite
{
	public GoldCube(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	public void tick()
	{
		if (ya < 16) ya++;
		y += ya;
		if (level.getBlock(xSlot, ySlot + 1) > 0)
		{
			level.setBlock(xSlot, ySlot, (byte) 17);
			remove();
		}
	}
	public void render(Graphics g)
	{
		g.drawImage(Images.sht_blocks[17][0], (int) x, (int) y, null);
	}
}