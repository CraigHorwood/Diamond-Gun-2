package com.craighorwood.ocus.state;
import java.awt.Graphics;
import com.craighorwood.ocus.*;
public class PauseState extends State
{
	private GameState parent;
	private boolean quitting = false;
	public boolean justSet = true;
	private StateButton setControlsButton, quitButton, yesButton, noButton;
 	public PauseState(GameState parent)
	{
		this.parent = parent;
		setControlsButton = new StateButton(this, 96, 80, "SET CONTROLS");
		quitButton = new StateButton(this, 96, 160, "QUIT TO TITLE");
		yesButton = new StateButton(this, 96, 110, "YES");
		noButton = new StateButton(this, 96, 130, "NO");
		setupGUI();
	}
 	private void setupGUI()
 	{
 		buttons.clear();
 		if (quitting)
 		{
 			buttons.add(yesButton);
 			buttons.add(noButton);
 		}
 		else
 		{
 			buttons.add(setControlsButton);
 			buttons.add(quitButton);
 		}
 	}
	public void tick(Input input)
	{
		if (input.isKeyPressed(Constants.K_PAUSE))
		{
			Music.resumeMusic();
			Sound.resumeAllSounds();
			setState(parent);
		}
	}
	public void render(Graphics g)
	{
		if (justSet)
		{
			parent.render(g);
			g.drawImage(Images.bg_overlay, 0, 0, null);
			justSet = false;
		}
		fillCenteredDialog(g, 144, 128);
		String msg = quitting ? "ARE YOU SURE?" : "GAME PAUSED";
		drawString(msg, g, 160 - (msg.length() << 2), 64);
		super.render(g);
	}
	protected void buttonPressed(int id)
	{
		if (id == 0)
		{
			if (quitting)
			{
				Sound.stopAllSounds();
				Music.stopMusic();
				setState(new TitleState());
			}
			else setState(new SetControlsState(this));
		}
		else
		{
			quitting = !quitting;
			setupGUI();
		}
	}
}