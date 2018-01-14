package com.craighorwood.ocus.sprite;
import com.craighorwood.ocus.Sound;
public class Boss extends Sprite
{
	public boolean activated = false;
	public double health = 1;
	public String name;
	public int invincibleTime = 0;
	public Boss(String name)
	{
		this.name = name;
	}
	public void tick()
	{
		if (invincibleTime > 0) invincibleTime--;
	}
	public void die()
	{
		Sound.playSound("explosion");
		Sound.playSound("bossdeath");
		removed = true;
	}
}