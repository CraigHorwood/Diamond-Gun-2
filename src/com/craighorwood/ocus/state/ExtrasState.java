package com.craighorwood.ocus.state;
import java.awt.Graphics;

import com.craighorwood.ocus.*;
import com.craighorwood.ocus.level.Save;
public class ExtrasState extends State
{
	private TitleState parent;
	private int percentage, guns, gems, armor, bosses, items;
	private int dialog = 0;
	private Save randomSave;
	private StateButton startButton, continueButton;
	public ExtrasState(TitleState parent, int percentage, int guns, int gems, int armor, int bosses, int items)
	{
		this.parent = parent;
		this.percentage = percentage;
		this.guns = guns;
		this.gems = gems;
		this.armor = armor;
		this.bosses = bosses;
		this.items = items;
		buttons.add(new StateButton(this, 4, 220, "BACK"));
		buttons.add(new StateButton(this, 188, 220, "MY PROGRESS"));
		if (percentage >= 25) buttons.add(new StateButton(this, 186, 32, "FLAPPY BIRD"));
		if (percentage >= 50) buttons.add(new StateButton(this, 186, 64, "GUN WARS"));
		if (percentage >= 75) buttons.add(new StateButton(this, 186, 96, "SCRAPBOOK"));
		if (percentage >= 100) buttons.add(new StateButton(this, 186, 128, "RANDOM MODE"));
		startButton = new StateButton(this, 96, 160, "START");
		continueButton = new StateButton(this, 96, 180, "CONTINUE");
	}
	public void tick(Input input)
	{
	}
	public void render(Graphics g)
	{
		fillBackground(g);
		super.render(g);
		drawString("25% complete", g, 6, 36);
		drawString("50% complete", g, 6, 68);
		drawString("75% complete", g, 6, 100);
		drawString("100% complete", g, 6, 132);
		if (percentage == 100) drawString("You have completed the game 100%!", g, 28, 200);
		if (dialog > 0)
		{
			fillCenteredDialog(g, 192, 160);
			if (dialog == 1)
			{
				drawString("MY PROGRESS", g, 116, 46);
				drawString("GUNS", g, 70, 72);
				String msg = guns + "/15";
				drawString(msg, g, 256 - (msg.length() << 3) - 6, 72);
				drawString("GEMS", g, 70, 92);
				msg = gems + "/10";
				drawString(msg, g, 256 - (msg.length() << 3) - 6, 92);
				drawString("ARMOUR", g, 70, 112);
				msg = armor + "/26";
				drawString(msg, g, 256 - (msg.length() << 3) - 6, 112);
				drawString("BOSSES DEFEATED", g, 70, 132);
				msg = bosses + "/7";
				drawString(msg, g, 226, 132);
				drawString("SPECIAL GEMS", g, 70, 152);
				msg = items + "/2";
				drawString(msg, g, 226, 152);
				msg = percentage + "% complete";
				drawString(msg, g, 160 - (msg.length() << 2), 186);
			}
			else
			{
				if (dialog == 2)
				{
					drawString("FLAPPY BIRD", g, 116, 46);
					drawString("It's Flappy Bird! You", g, 70, 72);
					drawString("all know Flappy Bird,", g, 70, 82);
					drawString("right? Well, this is", g, 70, 92);
					drawString("the Diamond Gun version", g, 70, 102);
					drawString("of Flappy Bird. It's", g, 70, 112);
					drawString("exactly the same. Score", g, 70, 122);
					drawString("real big now!!", g, 70, 132);
				}
				else if (dialog == 3)
				{
					drawString("GUN WARS", g, 128, 46);
					drawString("Which is your favourite", g, 70, 72);
					drawString("gun? Wield it proudly", g, 70, 82);
					drawString("in this two player", g, 70, 92);
					drawString("death match! 32 bullets", g, 70, 102);
					drawString("for each of you! Use", g, 70, 112);
					drawString("them wisely!", g, 70, 122);
				}
				else if (dialog == 4)
				{
					drawString("SCRAPBOOK", g, 124, 46);
					drawString("Check out concept art,", g, 70, 72);
					drawString("game design documents,", g, 70, 82);
					drawString("and other such things", g, 70, 92);
					drawString("I scribbled in my", g, 70, 102);
					drawString("school diary while", g, 70, 112);
					drawString("working on the game!", g, 70, 122);
				}
				else if (dialog == 5)
				{
					drawString("RANDOM MODE", g, 116, 46);
					drawString("Play through the game", g, 70, 72);
					drawString("again, but with a new", g, 70, 82);
					drawString("and sinister twist!", g, 70, 92);
					drawString("There's no telling what", g, 70, 102);
					drawString("enemies you'll find in", g, 70, 112);
					drawString("each room! Good luck!", g, 70, 122);
				}
				startButton.render(g);
				if (dialog == 5 && randomSave != null) continueButton.render(g);
			}
		}
		fillHud(g);
	}
	public void buttonPressed(int id)
	{
		switch (id)
		{
		case 0:
			setState(parent);
			break;
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
			openDialog(id);
			break;
		case 6:
			switch (dialog)
			{
			case 2:
				setState(new FlappyBirdState(this));
				break;
			case 3:
				setState(new GunWarsState(this, guns + 6));
				break;
			case 4:
				setState(new ScrapbookState(this));
				break;
			case 5:
				setState(new CutsceneState(null, 0));
				break;
			}
			dialog = 0;
			break;
		case 7:
			setState(new GameState(randomSave));
			break;
		}
	}
	public void mousePressed(int x, int y)
	{
		super.mousePressed(x, y);
		if (dialog > 1)
		{
			if (startButton.frame == 1) startButton.frame = 0;
			if (randomSave != null)
			{
				if (continueButton.frame == 1) continueButton.frame = 0;
			}
		}
	}
	public void mouseReleased(int x, int y)
	{
		super.mouseReleased(x, y);
		if (dialog > 1)
		{
			if (x > startButton.x && x < startButton.x + 128 && y > startButton.y && y < startButton.y + 16)
			{
				startButton.frame = 1;
				buttonPressed(6);
			}
			if (randomSave != null)
			{
				if (x > continueButton.x && x < continueButton.x + 128 && y > continueButton.y && y < continueButton.y + 16)
				{
					continueButton.frame = 1;
					buttonPressed(7);
				}
			}
		}
	}
	public void mouseMoved(int x, int y)
	{
		super.mouseMoved(x, y);
		if (dialog > 1)
		{
			if (x > startButton.x && x < startButton.x + 128 && y > startButton.y && y < startButton.y + 16) startButton.frame = 1;
			else startButton.frame = 0;
			if (randomSave != null)
			{
				if (x > continueButton.x && x < continueButton.x + 128 && y > continueButton.y && y < continueButton.y + 16) continueButton.frame = 1;
				else continueButton.frame = 0;
			}
		}
	}
	private void openDialog(int dialog)
	{
		if (this.dialog == 0)
		{
			Sound.playSound("save");
			if (dialog > 1)
			{
				if (dialog == 5)
				{
					Constants.SAVE_FILENAME = "/.rsave";
					randomSave = LoadSave.loadGame();
				}
			}
			this.dialog = dialog;
		}
		else
		{
			Constants.SAVE_FILENAME = "/.save";
			this.dialog = 0;
		}
	}
}