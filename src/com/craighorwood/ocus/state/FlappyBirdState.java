package com.craighorwood.ocus.state;
import java.awt.Graphics;
import java.io.*;
import java.net.*;
import java.util.Random;
import com.craighorwood.ocus.*;
public class FlappyBirdState extends ExtraState
{
	private static final Random random = new Random();
	private StateButton submitButton, playAgainButton, quitButton;
	private String name = "";
	private String[] highScores;
	private int gameState = 0, clickDelay = 0;
	private int time = 0, score = 0;
	private double playerPos = 72, playerA = 0;
	private int[] gaps = new int[4];
	private boolean connecting = false;
	public FlappyBirdState(ExtrasState parent)
	{
		super(parent);
		hideMouse = false;
		submitButton = new StateButton(this, 96, 108, "SUBMIT SCORE");
		playAgainButton = new StateButton(this, 96, 128, "PLAY AGAIN");
		quitButton = new StateButton(this, 96, 148, "QUIT");
		for (int i = 0; i < gaps.length; i++)
		{
			gaps[i] = -1;
		}
	}
	public void tick(Input input)
	{
		if (gameState == 1)
		{
			if (time++ % 144 == 0)
			{
				for (int i = 0; i < gaps.length; i++)
				{
					if (i < 3) gaps[i] = gaps[i + 1];
					else gaps[i] = random.nextInt(9) + 1;
				}
			}
			if (time > 288)
			{
				if ((time - 56) % 144 == 0)
				{
					Sound.playSound("scoreup");
					score++;
				}
				int nextGap = score * 144 + 432;
				if (time + 112 > nextGap && time + 96 < nextGap + 16 && (playerPos < gaps[1] << 4 || playerPos > (gaps[1] << 4) + 32))
				{
					die();
				}
			}
 			playerPos += playerA;
			if (playerPos > 200) die();
			else playerA += 0.1;
		}
		else if (gameState == 2 && clickDelay > 0)
		{
			if (--clickDelay == 0)
			{
				playAgainButton.y = 128;
				quitButton.y = 148;
				buttons.add(submitButton);
				buttons.add(playAgainButton);
				buttons.add(quitButton);
			}
		}
		else if (gameState == 3)
		{
			if ((input.lastPressed == 32 || (input.lastPressed > 64 && input.lastPressed < 91)) && name.length() < 16)
			{
				name += (char) input.lastPressed;
			}
			else if (input.lastPressed == 8 && name.length() > 0) name = name.substring(0, name.length() - 1);
			else if (input.lastPressed == 10 && name.length() > 0)
			{
				gameState = 4;
				connecting = true;
				new Thread()
				{
					public void run()
					{
						highScores = new String[16];
						try
						{
							URL url = new URL("http://www.craighorwood.com/game/diamond2/flappy.php");
							HttpURLConnection connection = (HttpURLConnection) url.openConnection();
							connection.setRequestMethod("POST");
							connection.setDoOutput(true);
							OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
							writer.write("a=" + score + "&v=" + name);
							writer.flush();
							connection.connect();
							BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
							for (int i = 0; i < highScores.length; i++)
							{
								String line = br.readLine();
								if (line != null)
								{
									if (line == "!--")
									{
										highScores = null;
										break;
									}
									else highScores[i] = line;
								}
								else if ((i & 1) == 0) highScores[i] = "NOBODY";
								else highScores[i] = "0";
							}
							br.close();
						}
						catch (Exception e)
						{
							e.printStackTrace();
							highScores = null;
						}
						finally
						{
							connecting = false;
						}
						playAgainButton.y = 184;
						quitButton.y = 204;
						buttons.add(playAgainButton);
						buttons.add(quitButton);
					}
				}.start();
			}
		}
	}
	public void render(Graphics g)
	{
		int offs = -(time - 1) % 144 - 16;
		g.translate(offs, 0);
		for (int y = 0; y < 15; y++)
		{
			for (int x = 0; x < 36; x++)
			{
				int xImg = 4;
				int yImg = 1;
				if (y == 14 || (x % 9 == 0 && (y < gaps[x / 9] || y >= gaps[x / 9] + 3) && gaps[x / 9] > 0))
				{
					xImg = 1;
					yImg = 0;
				}
				g.drawImage(Images.sht_blocks[xImg][yImg], x << 4, y << 4, null);
			}
		}
		g.translate(-offs, 0);
		g.drawImage(Images.sht_player[0][0], 80, (int) playerPos, null);
		drawString(score + "", g, 8, 8);
		if (gameState != 1)
		{
			g.drawImage(Images.bg_overlay, 0, 0, null);
			if (gameState == 0)
			{
				drawString("CONTROLS", g, 128, 90);
				drawString("Jump", g, 96, 110);
				drawString("Click", g, 184, 110);
				drawString("Click to start!", g, 100, 142);
			}
			else if (gameState == 2)
			{
				fillCenteredDialog(g, 160, 128);
				g.drawImage(Images.bg_youdead, 127, 64, null);
				super.render(g);
			}
			else if (gameState == 3)
			{
				fillCenteredDialog(g, 160, 64);
				drawString("ENTER YOUR NAME", g, 100, 96);
				drawString(name, g, 160 - (name.length() << 2), 116);
			}
			else
			{
				fillCenteredDialog(g, 208, 208);
				drawString("HIGH SCORES", g, 116, 24);
				if (connecting) drawString("Connecting...", g, 108, 64);
				else if (highScores != null)
				{
					for (int y = 0; y < 8; y++)
					{
						for (int x = 0; x < 2; x++)
						{
							drawString(highScores[x + (y << 1)], g, 64 + x * 160, 50 + (y << 4));
						}
					}
				}
				else drawString("A problem occurred.", g, 84, 64);
				super.render(g);
			}
		}
	}
	protected void buttonPressed(int id)
	{
		if (score > 0) buttons.clear();
		switch (id)
		{
		case 0:
			if (gameState == 2 && score > 0) gameState = 3;
			else if (gameState == 4)
			{
				gameState = 0;
				mouseReleased(0, 0);
			}
			break;
		case 1:
			if (gameState == 2)
			{
				buttons.clear();
				gameState = 0;
				mouseReleased(0, 0);
				break;
			}
		case 2:
			setState(parent);
			break;
		}
	}
	public void mouseReleased(int x, int y)
	{
		if (gameState == 0)
		{
			time = 0;
			score = 0;
			playerPos = 72;
			playerA = 0;
			for (int i = 0; i < gaps.length; i++)
			{
				gaps[i] = -1;
			}
			name = "";
			gameState = 1;
			hideMouse = true;
			setState(this);
		}
		else if (gameState == 1 && playerPos > 16)
		{
			Sound.playSound("swim");
			playerA = -2;
		}
		else super.mouseReleased(x, y);
	}
	private void die()
	{
		Sound.playSound("death");
		clickDelay = 30;
		gameState = 2;
		hideMouse = false;
		setState(this);
	}
}