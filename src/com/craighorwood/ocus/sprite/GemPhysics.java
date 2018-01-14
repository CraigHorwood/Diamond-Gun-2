package com.craighorwood.ocus.sprite;
import java.awt.Graphics;
import com.craighorwood.ocus.*;
public class GemPhysics extends Sprite
{
	private int animTime = 0;
	private boolean gravity;
	public GemPhysics(int x, int y, boolean gravity)
	{
		this.x = x;
		this.y = y;
		this.gravity = gravity;
		if (gravity) ya = -10;
	}
	public void tick()
	{
		animTime++;
		java.util.List<Sprite> hits = level.getHits(x, y, w, h);
		for (int i = 0; i < hits.size(); i++)
		{
			Sprite spr = hits.get(i);
			if ((spr instanceof Player && !gravity) || spr instanceof BossKhan)
			{
				Sound.playSound("getgem");
				removed = true;
				break;
			}
		}
		if (gravity)
		{
			ya += 0.5;
			move(6, ya);
		}
	}
	public void render(Graphics g)
	{
		g.drawImage(Images.sht_guns[(animTime >> 3) & 3][1], (int) x, (int) y, null);
	}
}