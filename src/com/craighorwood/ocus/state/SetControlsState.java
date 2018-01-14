package com.craighorwood.ocus.state;
import java.awt.Graphics;
import com.craighorwood.ocus.*;
public class SetControlsState extends State
{
	private State parent;
	private int[] oldButtons = { Constants.K_LEFT, Constants.K_RIGHT, Constants.K_UP, Constants.K_DOWN, Constants.K_A, Constants.K_B, Constants.K_GUN, Constants.K_PAUSE };
	private int buttonSetting = -1;
	private StateButton buttonBack, buttonReset, buttonChange, buttonCancel;
	public SetControlsState(State parent)
	{
		this.parent = parent;
		buttonBack = new StateButton(this, 4, 220, "BACK");
		buttonReset = new StateButton(this, 188, 220, "RESET CONTROLS");
		buttonChange = new StateButton(this, 96, 180, "CHANGE CONTROLS");
		buttonCancel = new StateButton(this, 4, 220, "CANCEL");
		setupGUI();
	}
	private void setupGUI()
	{
		buttons.clear();
		if (buttonSetting == -1)
		{
			buttons.add(buttonBack);
			buttons.add(buttonReset);
			buttons.add(buttonChange);
		}
		else buttons.add(buttonCancel);
	}
	private void setOldButtons()
	{
		oldButtons[0] = Constants.K_LEFT;
		oldButtons[1] = Constants.K_RIGHT;
		oldButtons[2] = Constants.K_UP;
		oldButtons[3] = Constants.K_DOWN;
		oldButtons[4] = Constants.K_A;
		oldButtons[5] = Constants.K_B;
		oldButtons[6] = Constants.K_GUN;
		oldButtons[7] = Constants.K_PAUSE;
	}
	public void tick(Input input)
	{
		if (buttonSetting >= 0 && input.lastPressed > 0 && input.lastPressed < 256)
		{
			if (Constants.KEY_NAMES[input.lastPressed].isEmpty())
			{
				Sound.playSound("error");
				return;
			}
			switch (buttonSetting)
			{
			case 0:
				Constants.K_LEFT = input.lastPressed;
				break;
			case 1:
				Constants.K_RIGHT = input.lastPressed;
				break;
			case 2:
				Constants.K_UP = input.lastPressed;
				break;
			case 3:
				Constants.K_DOWN = input.lastPressed;
				break;
			case 4:
				Constants.K_A = input.lastPressed;
				break;
			case 5:
				Constants.K_B = input.lastPressed;
				break;
			case 6:
				Constants.K_GUN = input.lastPressed;
				break;
			case 7:
				Constants.K_PAUSE = input.lastPressed;
				break;
			}
			if (++buttonSetting == 8)
			{
				setOldButtons();
				LoadSave.saveControls(oldButtons);
				buttonSetting = -1;
				setupGUI();
			}
		}
	}
	public void render(Graphics g)
	{
		fillBackground(g);
		super.render(g);
		if (buttonSetting == -1)
		{
			for (int x = 0; x < 2; x++)
			{
				for (int y = 0; y < 8; y++)
				{
					if (x == 0) drawString(Constants.CONTROL_NAMES[y], g, 42 + (x << 7), 32 + (y << 4));
					else
					{
						int kn = Constants.K_LEFT;
						if (y == 1) kn = Constants.K_RIGHT;
						else if (y == 2) kn = Constants.K_UP;
						else if (y == 3) kn = Constants.K_DOWN;
						else if (y == 4) kn = Constants.K_A;
						else if (y == 5) kn = Constants.K_B;
						else if (y == 6) kn = Constants.K_GUN;
						else if (y == 7) kn = Constants.K_PAUSE;
						drawString(Constants.KEY_NAMES[kn], g, 42 + (x << 7), 32 + (y << 4));
					}
				}
			}
		}
		else
		{
			String msg = "Press new '" + Constants.CONTROL_NAMES[buttonSetting] + "' key.";
			drawString(msg, g, 160 - (msg.length() << 2), 120);
		}
		fillHud(g);
	}
	public void buttonPressed(int id)
	{
		if (id == 0)
		{
			if (buttonSetting == -1)
			{
				if (parent instanceof PauseState) ((PauseState) parent).justSet = true;
				setState(parent);
			}
			else
			{
				Constants.K_LEFT = oldButtons[0];
				Constants.K_RIGHT = oldButtons[1];
				Constants.K_UP = oldButtons[2];
				Constants.K_DOWN = oldButtons[3];
				Constants.K_A = oldButtons[4];
				Constants.K_B = oldButtons[5];
				Constants.K_GUN = oldButtons[6];
				Constants.K_PAUSE = oldButtons[7];
				buttonSetting = -1;
				setupGUI();
			}
		}
		else if (id == 1)
		{
			Constants.K_LEFT = Constants.DEFAULT_LEFT;
			Constants.K_RIGHT = Constants.DEFAULT_RIGHT;
			Constants.K_UP = Constants.DEFAULT_UP;
			Constants.K_DOWN = Constants.DEFAULT_DOWN;
			Constants.K_A = Constants.DEFAULT_A;
			Constants.K_B = Constants.DEFAULT_B;
			Constants.K_GUN = Constants.DEFAULT_GUN;
			Constants.K_PAUSE = Constants.DEFAULT_PAUSE;
			setOldButtons();
			LoadSave.saveControls(oldButtons);
		}
		else if (id == 2)
		{
			buttonSetting++;
			setupGUI();
		}
	}
}