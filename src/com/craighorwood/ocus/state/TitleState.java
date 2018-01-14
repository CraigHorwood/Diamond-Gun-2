package com.craighorwood.ocus.state;
import java.awt.Graphics;
import java.io.*;
import java.net.URL;
import com.craighorwood.ocus.*;
import com.craighorwood.ocus.level.Save;
public class TitleState extends State
{
	private Save save;
	private boolean updateAvailable = false, failedOpenUpdate = false;
	public TitleState()
	{
		Constants.SAVE_FILENAME = "/.save";
		save = LoadSave.loadGame();
		int[] controls = LoadSave.loadControls();
		if (controls != null)
		{
			Constants.K_LEFT = controls[0];
			Constants.K_RIGHT = controls[1];
			Constants.K_UP = controls[2];
			Constants.K_DOWN = controls[3];
			Constants.K_A = controls[4];
			Constants.K_B = controls[5];
			Constants.K_GUN = controls[6];
			Constants.K_PAUSE = controls[7];
		}
		buttons.add(new StateButton(this, 96, 144, "NEW GAME"));
		if (save != null) buttons.add(new StateButton(this, 96, 164, "CONTINUE GAME"));
		buttons.add(new StateButton(this, 96, save != null ? 184 : 164, "SET CONTROLS"));
		buttons.add(new StateButton(this, 96, save != null ? 204 : 184, "QUIT"));
		if (save != null && save.getPercentComplete() >= 0.25) buttons.add(new StateButton(this, 96, 244, "EXTRAS!"));
		new Thread()
		{
			public void run()
			{
				try
				{
					URL url = new URL("http://www.craighorwood.com/game/diamond2/version.php");
					BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
					if (Double.parseDouble(br.readLine()) > OcusMain.VERSION) updateAvailable = true;
					br.close();
				}
				catch (IOException ioe)
				{
					ioe.printStackTrace();
				}
			}
		}.start();
	}
	public void init(OcusMain main)
	{
		super.init(main);
		Constants.SAVE_FILENAME = "/.save";
	}
	public void buttonPressed(int id)
	{
		switch (id)
		{
		case 0:
			setState(new CutsceneState(null, 0));
			break;
		case 1:
			if (save != null) setState(new GameState(save));
			else setState(new SetControlsState(this));
			break;
		case 2:
			if (save != null) setState(new SetControlsState(this));
			else
			{
				Sound.close();
				System.exit(0);
			}
			break;
		case 3:
			Sound.close();
			System.exit(0);
			break;
		case 4:
			setState(new ExtrasState(this, (int) (save.getPercentComplete() * 100), Integer.bitCount(save.gunLevel), Integer.bitCount(save.gems), Integer.bitCount(save.armor), save.bosses, save.items));
			break;
		}
	}
	public void render(Graphics g)
	{
		fillBackground(g);
		g.drawImage(Images.bg_title, 0, 20, null);
		if (!failedOpenUpdate && updateAvailable) g.drawImage(Images.bg_update, 10, 8, null);
		else if (failedOpenUpdate)
		{
			drawString("Failed to open browser.", g, 68, 8);
			drawString("Go to craighorwood.com/game/diamond2", g, 16, 16);
		}
		fillHud(g);
		super.render(g);
	}
	public void mouseReleased(int x, int y)
	{
		super.mouseReleased(x, y);
		if (!failedOpenUpdate && x >= 10 && x < 310 && y >= 8 && y < 24) openUpdate();
	}
	private void openUpdate()
	{
		if (java.awt.Desktop.isDesktopSupported())
		{
			java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
			try
			{
				desktop.browse(new java.net.URI("http://www.craighorwood.com/game/diamond2"));
			}
			catch (Exception e)
			{
				Sound.playSound("error");
				failedOpenUpdate = true;
				e.printStackTrace();
			}
		}
		else
		{
			Runtime runtime = Runtime.getRuntime();
			try
			{
				runtime.exec("xdg-open http://www.craighorwood.com/game/diamond2");
			}
			catch (IOException ioe)
			{
				Sound.playSound("error");
				failedOpenUpdate = true;
				ioe.printStackTrace();
			}
		}
		System.exit(0);
	}
}