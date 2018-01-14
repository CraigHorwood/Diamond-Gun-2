package com.craighorwood.ocus.sprite;
import java.awt.Graphics;
import com.craighorwood.ocus.Images;
public class Lightning extends Sprite
{
	private double r;
	private int isDiamond;
	public Lightning(double x, double y, double r, int isDiamond)
	{
		this.x = x;
		this.y = y;
		this.r = r;
		this.isDiamond = isDiamond;
		w = 9;
		h = 30;
		power = 0.5;
	}
	private int animTime = 0;
	public void tick()
	{
		animTime++;
		x -= Math.sin(r) * 4;
		y += Math.cos(r) * 4;
		java.util.List<Sprite> hits = level.getHits(x, y, w, h);
		for (int i = 0; i < hits.size(); i++)
		{
			Sprite spr = hits.get(i);
			if (spr instanceof Player)
			{
				level.player.damage(this);
				break;
			}
		}
	}
	public void render(Graphics g)
	{
		java.awt.Graphics2D g2d = (java.awt.Graphics2D) g;
		g2d.rotate(r, x + 5.5, y);
		g2d.drawImage(Images.sht_lightning[(animTime >> 1) % 3 + isDiamond][0], (int) x, (int) y, null);
		g2d.rotate(-r, x + 5.5, y);
	}
	public void remove()
	{
		if (y > 240) super.remove();
	}
}