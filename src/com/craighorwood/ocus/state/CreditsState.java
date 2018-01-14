package com.craighorwood.ocus.state;
import java.awt.Graphics;
import com.craighorwood.ocus.*;
public class CreditsState extends State
{
	private int time = -240;
	public void tick(Input input)
	{
		if (++time == 7900) setState(new TitleState());
	}
	public void render(Graphics g)
	{
		if (time < -210)
		{
			int scale = (int) ((-time - 210) / 3.0) + 1;
			int w = 320 * scale;
			int h = 240 * scale;
			g.drawImage(Images.bg_title, 160 - (w >> 1), 169 - (h >> 1), w, h, null);
		}
		else
		{
			if (time == -210) Sound.playSound("thud");
			int offs = 0;
			if (time > 0) offs = time >> 2;
			g.drawImage(Images.bg_credits, 0, -offs, null);
		}
	}
}