package com.craighorwood.ocus.state;
import java.awt.*;
import com.craighorwood.ocus.*;
import com.craighorwood.ocus.level.Save;
public class FallingState extends State
{
	private int time = 0;
	private GameState state = new GameState(new Save(7, 0, 152, -12, 0, 0, 0, 0, 0, 0, (byte) 0));
	public FallingState()
	{
		hideMouse = true;
	}
	public void tick(Input input)
	{
		time++;
		if (time == 112) setState(state);
	}
	public void render(Graphics g)
	{
		fillBackground(g);
		for (int y = 0; y < 16; y++)
		{
			for (int x = 0; x < 20; x++)
			{
				int left = time < 56 ? 9 : 8;
				int right = time < 56 ? 10 : 11;
				if (x < left || x > right) g.drawImage(Images.sht_blocks[1][0], x << 4, y << 4, null);
			}
		}
		g.drawImage(Images.sht_player[0][0], 152, (time << 4) % 240, null);
		fillHud(g);
	}
}