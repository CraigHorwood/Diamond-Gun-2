package com.craighorwood.ocus.sprite;
import java.awt.Graphics;
import com.craighorwood.ocus.Images;
public class BigExplosion extends Sprite
{
	private int frame = 0;
	public BigExplosion(int xo, int yo)
	{
		x = xo - 40;
		y = yo - 40;
	}
	public void tick()
	{
		w += (frame << 4);
		h += (frame << 4);
		if (++frame >= 5) removed = true;
	}
	public void render(Graphics g)
	{
		g.drawImage(Images.sht_explosion_big[frame][0], (int) x, (int) y, null);
	}
}