package com.craighorwood.ocus.state;
import java.awt.Graphics;
import com.craighorwood.ocus.*;
import com.craighorwood.ocus.level.Save;
public class SaveGameState extends State
{
	private GameState parent;
	private Save save;
	private int cooldown = 24;
	private boolean saveFailed = false;
	public SaveGameState(GameState parent, Save save)
	{
		this.parent = parent;
		this.save = save;
		Sound.playSound("save");
	}
	public void tick(Input input)
	{
		if (cooldown > 0)
		{
			cooldown--;
			return;
		}
		if (input.isKeyPressed(Constants.K_A))
		{
			if (!saveFailed)
			{
				parent.save = save;
				if (LoadSave.saveGame(save))
				{
					Sound.playSound("success");
					setState(parent);
				}
				else
				{
					Sound.playSound("error");
					saveFailed = true;
				}
			}
			else setState(parent);
		}
		else if (input.isKeyPressed(Constants.K_B))
		{
			setState(parent);
		}
	}
	public void render(Graphics g)
	{
		fillCenteredDialog(g, 192, 64);
		if (!saveFailed)
		{
			drawString("SAVE PEDESTAL ACTIVATED", g, 68, 96);
			drawString("Would you like to save", g, 72, 112);
			drawString("the game?", g, 124, 122);
			if (cooldown == 0)
			{
				drawString("YES. " + Constants.KEY_NAMES[Constants.K_A], g, 68, 140);
				String msg = "NO. " + Constants.KEY_NAMES[Constants.K_B];
				drawString(msg, g, 252 - (msg.length() << 3), 140);
			}
		}
		else
		{
			drawString("ERROR", g, 140, 96);
			drawString("Failed to save.", g, 104, 112);
			String msg = "Press " + Constants.KEY_NAMES[Constants.K_A];
			drawString(msg, g, 160 - (msg.length() << 2), 136);
		}
	}
}