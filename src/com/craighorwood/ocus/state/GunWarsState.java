package com.craighorwood.ocus.state;
import java.awt.Graphics;
import java.util.ArrayList;
import com.craighorwood.ocus.*;
public class GunWarsState extends ExtraState
{
	private int gameState = 4;
	private int gunLevel = 0;
	private int countdown = 0;
	private int playerOneGun = 0, playerOnePos = 108, playerTwoGun = 0, playerTwoPos = 108, playerOneBullets = 32, playerTwoBullets = 32, playerOneInvincible = 0, playerTwoInvincible = 0;
	private String winner = "";
	private byte[] field = new byte[120];
	private java.util.List<int[]> bullets = new ArrayList<int[]>(), drops = new ArrayList<int[]>();
	private StateButton doneButton, endMatchButton, playAgainButton, quitButton;
	public GunWarsState(ExtrasState parent, int gunLevel)
	{
		super(parent);
		this.gunLevel = gunLevel;
		hideMouse = false;
		doneButton = new StateButton(this, 96, 200, "DONE");
		endMatchButton = new StateButton(this, 96, 252, "END MATCH");
		playAgainButton = new StateButton(this, 96, 180, "PLAY AGAIN");
		quitButton = new StateButton(this, 96, 200, "QUIT");
		buttonPressed(0);
	}
	public void tick(Input input)
	{
		if (countdown > 0)
		{
			if (--countdown == 0)
			{
				Sound.playSound("success");
				buttons.add(endMatchButton);
				gameState = 3;
			}
		}
		else if (gameState == 3)
		{
			if (playerOneInvincible > 0) playerOneInvincible--;
			if (playerTwoInvincible > 0) playerTwoInvincible--;
			if (input.isKeyDown(81) && playerOnePos > 0) playerOnePos--;
			else if (input.isKeyDown(65) && playerOnePos < 216) playerOnePos++;
			if (input.isKeyPressed(67) && playerOneBullets > 0)
			{
				Sound.playSound("shoot_player");
				int[] bullet = new int[3];
				bullet[0] = 36;
				bullet[1] = playerOnePos + 12;
				bullet[2] = 1;
				bullets.add(bullet);
				playerOneBullets--;
			}
			if (input.isKeyDown(80) && playerTwoPos > 0) playerTwoPos--;
			else if (input.isKeyDown(76) && playerTwoPos < 216) playerTwoPos++;
			if (input.isKeyPressed(78) && playerTwoBullets > 0)
			{
				Sound.playSound("shoot_player");
				int[] bullet = new int[3];
				bullet[0] = 276;
				bullet[1] = playerTwoPos + 12;
				bullet[2] = -1;
				bullets.add(bullet);
				playerTwoBullets--;
			}
			for (int i = 0; i < bullets.size(); i++)
			{
				int[] b = bullets.get(i);
				boolean remove = false;
				int xc = b[0] += b[2] * 6;
				if (xc < 0 || xc > 320) remove = true;
				else
				{
					if ((b[2] == -1 && xc < 48 && b[1] > playerOnePos && b[1] < playerOnePos + 24 && playerOneInvincible == 0) || (b[2] == 1 && xc > 272 && b[1] > playerTwoPos && b[1] < playerTwoPos + 24 && playerTwoInvincible == 0))
					{
						Sound.playSound("hurt");
						if (b[2] == -1)
						{
							if ((playerOneBullets -= 4) <= 0)
							{
								playerOneBullets = 0;
								gameOver(-1);
							}
							else playerOneInvincible = 60;
						}
						else
						{
							if ((playerTwoBullets -= 4) <= 0)
							{
								playerTwoBullets = 0;
								gameOver(1);
							}
							else playerTwoInvincible = 60;
						}
						remove = true;
					}
					else
					{
						int xSlot = (xc >> 4) - 6;
						if (xSlot >= 0 && xSlot < 8)
						{
							int j = xSlot + ((b[1] >> 4) << 3);
							if (field[j] > 0)
							{
								if ((b[2] == 1 && field[j] == playerOneGun + 64) || (b[2] == -1 && field[j] == playerTwoGun + 64))
								{
									Sound.playSound("explosion");
									field[j] = 0;
									if ((b[2] == 1 && Math.random() > playerOneBullets / 32.0) || (b[2] == -1 && Math.random() > playerTwoBullets / 32.0))
									{
										int[] drop = new int[3];
										drop[0] = (xSlot + 6) << 4;
										drop[1] = (b[1] >> 4) << 4;
										drop[2] = (Math.random() < 0.4 ? 2 : 1) * b[2];
										drops.add(drop);
									}
								}
								remove = true;
							}
						}
					}
				}
				if (remove)
				{
					if (b[2] == 1 && playerOneBullets == 0) gameOver(-1);
					else if (b[2] == -1 && playerTwoBullets == 0) gameOver(1);
					bullets.remove(i--);
				}
			}
			for (int i = 0; i < drops.size(); i++)
			{
				int[] d = drops.get(i);
				boolean remove = false;
				int xc = d[0] -= (d[2] << 1);
				if (xc < -16 || xc > 320) remove = true;
				else
				{
					if (d[2] < 0)
					{
						if (xc < 288 && xc + 16 > 272 && d[1] < playerTwoPos + 24 && d[1] + 16 > playerTwoPos)
						{
							Sound.playSound("getgem");
							playerTwoBullets -= d[2] << 3;
							if (playerTwoBullets > 32) playerTwoBullets = 32;
							remove = true;
						}
					}
					else
					{
						if (xc < 48 && xc + 16 > 32 && d[1] < playerOnePos + 24 && d[1] + 16 > playerOnePos)
						{
							Sound.playSound("getgem");
							playerOneBullets += d[2] << 3;
							if (playerOneBullets > 32) playerOneBullets = 32;
							remove = true;
						}
					}
				}
				if (remove) drops.remove(i--);
			}
		}
	}
	public void render(Graphics g)
	{
		fillBackground(g);
		switch (gameState)
		{
		case 0:
			fillCenteredDialog(g, 200, 200);
			drawString("PLAYER ONE", g, 120, 28);
			drawString("SELECT YOUR GUN", g, 100, 44);
			renderGuns(g, playerOneGun);
			break;
		case 1:
			fillCenteredDialog(g, 200, 200);
			drawString("PLAYER TWO", g, 120, 28);
			drawString("SELECT YOUR GUN", g, 100, 44);
			renderGuns(g, playerTwoGun);
			break;
		case 2:
			renderGame(g);
			g.drawImage(Images.bg_overlay, 0, 0, null);
			drawString((countdown / 60 + 1) + "", g, 152, 116);
			break;
		case 3:
			renderGame(g);
			break;
		case 4:
			renderGame(g);
			g.drawImage(Images.bg_overlay, 0, 0, null);
			fillCenteredDialog(g, 200, 200);
			drawString("The winner is...", g, 96, 28);
			drawString(winner, g, 120, 44);
			if (winner.startsWith("P"))
			{
				drawString("with the", g, 128, 76);
				String gunName = winner == "PLAYER ONE" ? Constants.GUN_NAMES[playerOneGun] : Constants.GUN_NAMES[playerTwoGun];
				gunName += " GUN";
				drawString(gunName, g, 160 - (gunName.length() << 2), 108);
			}
			break;
		}
		super.render(g);
		if (gameState < 2) fillHud(g);
	}
	protected void buttonPressed(int id)
	{
		switch (id)
		{
		case 0:
			if (gameState < 3)
			{
				if (++gameState == 2)
				{
					buttons.clear();
					for (int y = 0; y < 15; y++)
					{
						for (int x = 0; x < 8; x++)
						{
							byte b = 0;
							if (Math.random() > 0.6)
							{
								if (x < 4) b = (byte) (playerOneGun + 64);
								else b = (byte) (playerTwoGun + 64);
							}
							field[x + (y << 3)] = b;
						}
					}
					playerOnePos = 108;
					playerTwoPos = 108;
					playerOneBullets = 32;
					playerTwoBullets = 32;
					playerOneInvincible = 0;
					playerTwoInvincible = 0;
					countdown = 180;
					hideMouse = true;
					setState(this);
				}
			}
			else if (gameState == 3) gameOver(0);
			else
			{
				gameState = 0;
				bullets.clear();
				drops.clear();
				buttons.clear();
				buttons.add(doneButton);
			}
			break;
		case 1:
			setState(parent);
			break;
		}
	}
	public void mouseReleased(int x, int y)
	{
		super.mouseReleased(x, y);
		if (gameState < 2 && x > 88 && x < 248 && y > 76 && y < 172)
		{
			int selected = ((x - 88) >> 5) + ((y - 76) >> 5) * 5;
			if (selected < gunLevel)
			{
				Sound.playSound("turn");
				if (gameState == 0) playerOneGun = selected;
				else playerTwoGun = selected;
			}
		}
	}
	private void renderGame(Graphics g)
	{
		if ((playerOneInvincible & 3) == 0) g.drawImage(Images.sht_player[0][0], 32, playerOnePos, null);
		if ((playerTwoInvincible & 3) == 0) g.drawImage(Images.sht_player[2][0], 272, playerTwoPos, null);
		g.setColor(Constants.GUN_COLORS[playerOneGun + 1]);
		g.fillRect(40, playerOnePos + 15, 7, 3);
		g.setColor(Constants.GUN_COLORS[playerTwoGun + 1]);
		g.fillRect(273, playerTwoPos + 15, 7, 3);
		for (int y = 0; y < 15; y++)
		{
			for (int x = 0; x < 8; x++)
			{
				byte b = field[x + (y << 3)];
				if (b > 0) g.drawImage(Images.sht_blocks[b][0], 96 + (x << 4), y << 4, null);
			}
		}
		for (int i = 0; i < bullets.size(); i++)
		{
			int[] b = bullets.get(i);
			if (b[2] == 1) g.setColor(Constants.GUN_COLORS[playerOneGun + 1]);
			else g.setColor(Constants.GUN_COLORS[playerTwoGun + 1]);
			g.fillRect(b[0], b[1], 2, 2);
		}
		for (int i = 0; i < drops.size(); i++)
		{
			int[] d = drops.get(i);
			g.drawImage(Images.sht_blocks[16][0], d[0], d[1], null);
			int amount = Math.abs(d[2] << 3);
			drawString(amount + "", g, d[0] + (amount == 16 ? 0 : 4), d[1] + 4);
		}
		drawString(playerOneBullets + "", g, 8, 8);
		drawString(playerTwoBullets + "", g, playerTwoBullets > 9 ? 296 : 304, 8);
		fillHud(g);
		drawString("Q  Up", g, 8, 244);
		drawString("A  Down", g, 8, 254);
		drawString("C  Shoot", g, 8, 264);
		drawString("Up  P", g, 272, 244);
		drawString("Down  L", g, 256, 254);
		drawString("Shoot  N", g, 248, 264);
	}
	private void renderGuns(Graphics g, int selected)
	{
		g.setColor(Constants.GUN_COLORS[0]);
		for (int y = 0; y < 3; y++)
		{
			for (int x = 0; x < 5; x++)
			{
				int xx = (x << 5) + 88;
				int yy = (y << 5) + 76;
				int gunIndex = x + y * 5;
				if (gunIndex < gunLevel) g.drawImage(Images.sht_guns[gunIndex][0], xx, yy, null);
				else drawString("?", g, xx, yy);
				if (gunIndex == selected) g.drawRect(xx - 8, yy - 6, 32, 26);
			}
		}
	}
	private void gameOver(int winner)
	{
		Sound.playSound("death");
		if (winner == 1) this.winner = "PLAYER ONE";
		else if (winner == -1) this.winner = "PLAYER TWO";
		else this.winner = "NO CONTEST";
		buttons.clear();
		buttons.add(playAgainButton);
		buttons.add(quitButton);
		gameState = 4;
		hideMouse = false;
		setState(this);
	}
}