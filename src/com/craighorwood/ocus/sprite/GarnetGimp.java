package com.craighorwood.ocus.sprite;
import java.awt.Graphics;
import com.craighorwood.ocus.Images;
public class GarnetGimp extends Gimp
{
	public GarnetGimp(int x, int y)
	{
		super(x, y);
	}
	public void render(Graphics g)
	{
		g.drawImage(Images.sht_enemies[onGround ? 1 : 2][3], (int) x, (int) y, null);
	}
	protected void bounce()
	{
		super.bounce();
		level.addSprite(new Fire(x, ySlot << 4));
	}
}