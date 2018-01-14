package com.craighorwood.ocus.sprite;
public class Enemy extends Sprite
{
	protected boolean dropsHealth = true;
	public void tick()
	{
		java.util.List<Sprite> hits = level.getHits(x, y, w, h);
		for (int i = 0; i < hits.size(); i++)
		{
			Sprite spr = hits.get(i);
			if (spr instanceof Player) spr.damage(this);
		}
	}
	public boolean damage(Sprite damager)
	{
		return false;
	}
	public void die()
	{
		if (dropsHealth)
		{
			if (level.player.health < 0.3 || Math.random() > level.player.health) level.addSprite(new HealthDrop((int) (x) + (w >> 1) + 1, (int) (y) + (h >> 1) + 1));
		}
		super.die();
	}
}