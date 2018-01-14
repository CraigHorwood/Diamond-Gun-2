package com.craighorwood.ocus.sprite;
import java.awt.Graphics;
import com.craighorwood.ocus.Images;
public class BronzeCube extends Sprite
{
	private int dir;
	public BronzeCube(int x, int y, int dir)
	{
		this.x = x;
		this.y = y;
		this.dir = dir;
		xa = dir << 2;
	}
	public void tick()
	{
		x += xa;
		if (level.getBlock(xSlot + dir, ySlot) > 0)
		{
			if (level.boss instanceof BossKiinam)
			{
				java.util.List<Sprite> hits = level.getHits(x, y, w, h);
				if (hits.contains(level.boss)) level.boss.damage(this);
				level.setBlock(xSlot, ySlot, (byte) 0);
			}
			else level.setBlock(xSlot, ySlot, (byte) 23);
			removed = true;
		}
	}
	public void render(Graphics g)
	{
		g.drawImage(Images.sht_blocks[23][0], (int) x, (int) y, null);
	}
}