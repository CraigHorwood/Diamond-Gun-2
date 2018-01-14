package com.craighorwood.ocus.state;
import java.awt.Graphics;
import java.util.*;
import com.craighorwood.ocus.*;
import com.craighorwood.ocus.level.*;
import com.craighorwood.ocus.sprite.*;
public class CutsceneState extends State
{
	private static class Event
	{
		public int executeFrame;
		public short[][] codes;
		public Event(int executeFrame, short[][] codes)
		{
			this.executeFrame = executeFrame;
			this.codes = codes;
		}
	}
	private int frame = 0;
	private List<Player> players = new ArrayList<Player>();
	private List<Event> events = new ArrayList<Event>();
	private Event nextEvent;
	private byte[] keyFlags = new byte[4];
	private Save save;
	private int cutsceneId;
	private String introText;
	private Level level;
	private int xLevel, yLevel, xTransition, yTransition;
	private String endText = "";
	public CutsceneState(Save save, int cutsceneId)
	{
		this(save, cutsceneId, 0, 0);
	}
	public CutsceneState(Save save, int cutsceneId, int equippedGun, int lastArmor)
	{
		this.save = save;
		this.cutsceneId = cutsceneId;		
		hideMouse = true;
		introText = cutsceneId == 0 ? "Last time..." : "Meanwhile...";
		switch (cutsceneId)
		{
		case 0:
			xLevel = 0;
			yLevel = 0;
			xTransition = 1;
			yTransition = -1;
			addEvent(120, new short[][] { new short[] {4, 0} });
			addEvent(280, new short[][] { new short[] {0, 32, 40, 0, 63, 5, -1} });
			addEvent(340, new short[][] { new short[] {1, 0, 5} });
			addEvent(400, new short[][] { new short[] {1, 0, 2} });
			addEvent(424, new short[][] { new short[] {1, 0, 2} });
			addEvent(480, new short[][] { new short[] {1, 0, 2} });
			addEvent(550, new short[][] { new short[] {1, 0, 2}, new short[] {4, 1}, new short[] {13, 318, 96, -6, 0} });
			addEvent(580, new short[][] { new short[] {9, 0, 0, 0}, new short[] {2, 0, 10, -6, 0}, new short[] {2, 0, 16, -6, 1}, new short[] {2, 0, 23, -6, 2}, new short[] {2, 0, 30, -6, 3}, new short[] {2, 0, 36, -6, 4}, new short[] {2, 0, 43, -6, 5} });
			addEvent(720, new short[][] { new short[] {11, -1}, new short[] {0, 320, 72, 1, 0, 0, -1}, new short[] {8, 1}, new short[] {1, 1, 6} });
			addEvent(728, new short[][] { new short[] {1, 1, 6} });
			addEvent(820, new short[][] { new short[] {1, 1, 6} });
			addEvent(875, new short[][] { new short[] {1, 1, 6}, new short[] {1, 1, 5} });
			break;
		case 1:
			xLevel = 2;
			yLevel = 0;
			xTransition = 1;
			yTransition = 1;
			addEvent(380, new short[][] { new short[] {0, -16, 80, 1, 63, 5, -1}, new short[] {1, 0, 5} });
			addEvent(465, new short[][] { new short[] {1, 0, 5}, new short[] {1, 0, 3} });
			addEvent(600, new short[][] { new short[] {12, -1}, new short[] {4, 1}, new short[] {13, 0, 208, 6, 0}, new short[] {1, 0, 5} });
			addEvent(640, new short[][] { new short[] {9, 0, 1, 1}, new short[] {2, 0, -10, -6, 1}, new short[] {2, 0, -15, -6, 2}, new short[] {2, 0, -20, -6, 3}, new short[] {2, 0, -25, -6, 4}, new short[] {2, 0, -30, -6, 5} });
			addEvent(720, new short[][] { new short[] {0, -16, 192, 2, 0, 0, -1}, new short[] {1, 1, 5} });
			addEvent(780, new short[][] { new short[] {1, 1, 5} });
			addEvent(900, new short[][] { new short[] {1, 1, 5} });
			addEvent(960, new short[][] { new short[] {1, 1, 5}, new short[] {1, 1, 6} });
			break;
		case 2:
			xLevel = 3;
			yLevel = 0;
			yTransition = 1;
			addEvent(320, new short[][] { new short[] {0, 48, -32, 2, 63, 5, -1}, new short[] {1, 0, 5}, new short[] {1, 0, 3} });
			addEvent(390, new short[][] { new short[] {1, 0, 2} });
			addEvent(440, new short[][] { new short[] {1, 0, 2} });
			addEvent(490, new short[][] { new short[] {1, 0, 2} });
			addEvent(510, new short[][] { new short[] {1, 0, 2}, new short[] {1, 0, 5}, new short[] {1, 0, 6} });
			addEvent(530, new short[][] { new short[] {1, 0, 6}, new short[] {1, 0, 5} });
			addEvent(540, new short[][] { new short[] {1, 0, 2} });
			addEvent(670, new short[][] { new short[] {1, 0, 6}, new short[] {1, 0, 5} });
			addEvent(740, new short[][] { new short[] {1, 0, 6}, new short[] {1, 0, 5} });
			addEvent(780, new short[][] { new short[] {1, 0, 6}, new short[] {1, 0, 5} });
			addEvent(850, new short[][] { new short[] {7} });
			addEvent(860, new short[][] { new short[] {1, 0, 6} });
			addEvent(880, new short[][] { new short[] {1, 0, 5} });
			addEvent(900, new short[][] { new short[] {1, 0, 5}, new short[] {8, 0} });
			for (int i = 1060; i < 1151; i += 10)
			{
				addEvent(i, new short[][] { new short[] {1, 0, 1} });
			}
			addEvent(1250, new short[][] { new short[] {11, 1}, new short[] {1, 0, 6} });
			break;
		case 3:
			xLevel = 4;
			yLevel = 3;
			xTransition = 1;
			addEvent(280, new short[][] { new short[] {0, 64, 70, 2, 63, 5, -1} });
			addEvent(340, new short[][] { new short[] {1, 0, 0} });
			addEvent(360, new short[][] { new short[] {1, 0, 0} });
			addEvent(400, new short[][] { new short[] {1, 0, 2} });
			addEvent(402, new short[][] { new short[] {1, 0, 1} });
			addEvent(424, new short[][] { new short[] {1, 0, 2} });
			addEvent(428, new short[][] { new short[] {1, 0, 1} });
			addEvent(450, new short[][] { new short[] {1, 0, 2} });
			addEvent(456, new short[][] { new short[] {1, 0, 1} });
			addEvent(478, new short[][] { new short[] {1, 0, 2} });
			addEvent(482, new short[][] { new short[] {1, 0, 1} });
			addEvent(504, new short[][] { new short[] {1, 0, 2} });
			addEvent(510, new short[][] { new short[] {1, 0, 1} });
			addEvent(512, new short[][] { new short[] {1, 0, 5} });
			addEvent(540, new short[][] { new short[] {1, 0, 2} });
			addEvent(572, new short[][] { new short[] {1, 0, 2} });
			addEvent(592, new short[][] { new short[] {1, 0, 2} });
			addEvent(622, new short[][] { new short[] {1, 0, 2} });
			addEvent(638, new short[][] { new short[] {1, 0, 5}, new short[] {1, 0, 6}, new short[] {1, 0, 2} });
			addEvent(650, new short[][] { new short[] {1, 0, 1} });
			addEvent(700, new short[][] { new short[] {1, 0, 2} });
			addEvent(730, new short[][] { new short[] {1, 0, 2} });
			addEvent(760, new short[][] { new short[] {1, 0, 6}, new short[] {1, 0, 5}, new short[] {1, 0, 2} });
			addEvent(880, new short[][] { new short[] {1, 0, 5}, new short[] {1, 0, 6} });
			addEvent(905, new short[][] { new short[] {1, 0, 1} });
			addEvent(970, new short[][] { new short[] {0, -16, 200, 1, 1, 0, -1}, new short[] {1, 1, 5} });
			addEvent(1000, new short[][] { new short[] {1, 0, 6}, new short[] {1, 1, 5} });
			addEvent(1180, new short[][] { new short[] {1, 1, 1} });
			addEvent(1205, new short[][] { new short[] {1, 0, 1} });
			addEvent(1210, new short[][] { new short[] {1, 0, 2}, new short[] {1, 0, 6} });
			addEvent(1220, new short[][] { new short[] {1, 0, 1}, new short[] {1, 1, 5}, new short[] {1, 1, 1} });
			addEvent(1230, new short[][] { new short[] {1, 0, 6}, new short[] {1, 0, 1}, new short[] {8, 0}, new short[] {1, 1, 1} });
			addEvent(1240, new short[][] { new short[] {1, 1, 1} });
			addEvent(1250, new short[][] { new short[] {1, 1, 1} });
			addEvent(1270, new short[][] { new short[] {1, 1, 5}, new short[] {1, 1, 6}, new short[] {1, 1, 2} });
			addEvent(1274, new short[][] { new short[] {1, 1, 1} });
			addEvent(1278, new short[][] { new short[] {1, 0, 1}, new short[] {1, 0, 5} });
			addEvent(1285, new short[][] { new short[] {1, 0, 1}, new short[] {1, 1, 6}, new short[] {1, 1, 5}, new short[] {10, 0} });
			addEvent(1295, new short[][] { new short[] {1, 1, 1} });
			addEvent(1300, new short[][] { new short[] {1, 0, 1} });
			addEvent(1305, new short[][] { new short[] {1, 1, 1} });
			addEvent(1310, new short[][] { new short[] {1, 0, 5}, new short[] {1, 0, 6} });
			addEvent(1320, new short[][] { new short[] {1, 1, 1} });
			addEvent(1330, new short[][] { new short[] {1, 1, 5}, new short[] {1, 1, 6}, new short[] {1, 1, 1} });
			addEvent(1340, new short[][] { new short[] {1, 1, 1} });
			addEvent(1345, new short[][] { new short[] {1, 1, 1} });
			addEvent(1355, new short[][] { new short[] {1, 1, 1} });
			addEvent(1365, new short[][] { new short[] {1, 1, 1} });
			for (int i = 1370; i < 1401; i += 10)
			{
				addEvent(i, new short[][] { new short[] {1, 1, 1} });
			}
			break;
		case 4:
			xLevel = 3;
			yLevel = 1;
			xTransition = -1;
			yTransition = -1;
			addEvent(380, new short[][] { new short[] {0, 320, 200, 2, 63, 5, -1}, new short[] {1, 0, 6} });
			addEvent(420, new short[][] { new short[] {0, 320, 200, 1, 1, 0, -1}, new short[] {1, 1, 6} });
			for (int i = 440; i < 471; i += 10)
			{
				addEvent(i, new short[][] { new short[] {1, 1, 1} });
			}
			addEvent(475, new short[][] { new short[] {1, 1, 2}, new short[] {11, 1} });
			addEvent(480, new short[][] { new short[] {1, 1, 1} });
			addEvent(490, new short[][] { new short[] {1, 0, 1} });
			addEvent(510, new short[][] { new short[] {1, 0, 6} });
			addEvent(520, new short[][] { new short[] {10, 0}, new short[] {8, 0} });
			addEvent(530, new short[][] { new short[] {1, 1, 6}, new short[] {8, 0} });
			addEvent(540, new short[][] { new short[] {8, 0} });
			addEvent(550, new short[][] { new short[] {1, 0, 5} });
			addEvent(600, new short[][] { new short[] {8, 1} });
			addEvent(620, new short[][] { new short[] {1, 0, 5}, new short[] {1, 0, 6}, new short[] {1, 0, 2} });
			addEvent(630, new short[][] { new short[] {1, 1, 1} });
			addEvent(640, new short[][] { new short[] {1, 0, 6}, new short[] {2, 0, -10, -6, 5}, new short[] {9, 0, 31, 4} });
			addEvent(680, new short[][] { new short[] {1, 1, 5} });
			addEvent(695, new short[][] { new short[] {9, 1, 63, 5} });
			addEvent(700, new short[][] { new short[] {1, 0, 2}, new short[] {1, 1, 5}, new short[] {1, 1, 6} });
			addEvent(710, new short[][] { new short[] {1, 0, 6} });
			addEvent(728, new short[][] { new short[] {1, 0, 1} });
			addEvent(737, new short[][] { new short[] {1, 0, 1} });
			addEvent(745, new short[][] { new short[] {1, 0, 1} });
			addEvent(753, new short[][] { new short[] {1, 0, 1} });
			addEvent(761, new short[][] { new short[] {1, 0, 1} });
			addEvent(769, new short[][] { new short[] {1, 0, 1} });
			addEvent(777, new short[][] { new short[] {1, 0, 1} });
			addEvent(785, new short[][] { new short[] {1, 0, 1} });
			addEvent(793, new short[][] { new short[] {1, 0, 1} });
			addEvent(800, new short[][] { new short[] {1, 0, 1} });
			break;
		case 5:
			introText = "Now...";
			xLevel = 3;
			yLevel = 3;
			xTransition = -1;
			yTransition = 1;
			addEvent(280, new short[][] { new short[] {6} });
			addEvent(360, new short[][] { new short[] {0, 288, -24, 0, 32704, (short) equippedGun, (short) lastArmor}, new short[] {1, 0, 6} });
			addEvent(415, new short[][] { new short[] {4, 2}, new short[] {3, 19, 6, 0}, new short[] {3, 19, 7, 0}, new short[] {3, 19, 8, 0} });
			addEvent(420, new short[][] { new short[] {1, 0, 6}, new short[] {8, 0} });
			addEvent(430, new short[][] { new short[] {0, 320, 120, 2, 63, 5, -1}, new short[] {1, 1, 6} });
			addEvent(500, new short[][] { new short[] {1, 0, 5} });
			addEvent(505, new short[][] { new short[] {0, 320, 120, 1, 31, 4, -1}, new short[] {1, 2, 6}, new short[] {1, 2, 1} });
			addEvent(510, new short[][] { new short[] {1, 0, 2} });
			addEvent(530, new short[][] { new short[] {1, 2, 1} });
			addEvent(535, new short[][] { new short[] {1, 0, 5}, new short[] {8, 0} });
			addEvent(540, new short[][] { new short[] {1, 2, 1} });
			addEvent(550, new short[][] { new short[] {1, 1, 6}, new short[] {8, 1}, new short[] {1, 2, 1} });
			addEvent(560, new short[][] { new short[] {1, 1, 2}, new short[] {1, 2, 1} });
			addEvent(563, new short[][] { new short[] {1, 1, 1} });
			addEvent(575, new short[][] { new short[] {1, 2, 1} });
			addEvent(585, new short[][] { new short[] {1, 2, 1} });
			addEvent(588, new short[][] { new short[] {1, 1, 5} });
			addEvent(600, new short[][] { new short[] {1, 1, 2} });
			addEvent(625, new short[][] { new short[] {1, 1, 2}, new short[] {1, 2, 5}, new short[] {1, 2, 6} });
			addEvent(635, new short[][] { new short[] {1, 2, 1} });
			addEvent(645, new short[][] { new short[] {1, 2, 2}, new short[] {1, 2, 1} });
			addEvent(648, new short[][] { new short[] {1, 2, 1} });
			addEvent(650, new short[][] { new short[] {1, 1, 2} });
			addEvent(655, new short[][] { new short[] {1, 1, 5}, new short[] {1, 1, 6}, new short[] {1, 2, 1} });
			addEvent(670, new short[][] { new short[] {1, 2, 1} });
			addEvent(690, new short[][] { new short[] {1, 2, 5}, new short[] {1, 2, 6} });
			addEvent(710, new short[][] { new short[] {1, 1, 6}, new short[] {8, 1}, new short[] {1, 2, 2} });
			addEvent(720, new short[][] { new short[] {1, 1, 2} });
			addEvent(730, new short[][] { new short[] {1, 1, 1} });
			addEvent(740, new short[][] { new short[] {1, 2, 6} });
			addEvent(760, new short[][] { new short[] {10, 1}, new short[] {1, 2, 1} });
			addEvent(762, new short[][] { new short[] {9, 1, 0, 0}, new short[] {2, 1, -10, -8, 5} });
			addEvent(775, new short[][] { new short[] {8, 1} });
			addEvent(780, new short[][] { new short[] {1, 2, 2} });
			addEvent(800, new short[][] { new short[] {1, 2, 6} });
			addEvent(810, new short[][] { new short[] {11, 1}, new short[] {1, 2, 2} });
			addEvent(950, new short[][] { new short[] {1, 1, 5} });
			addEvent(959, new short[][] { new short[] {1, 1, 5} });
			addEvent(1006, new short[][] { new short[] {1, 1, 6} });
			addEvent(1012, new short[][] { new short[] {1, 1, 6} });
			addEvent(1033, new short[][] { new short[] {11, -1}, new short[] {1, 1, 5} });
			addEvent(1053, new short[][] { new short[] {1, 1, 2} });
			addEvent(1125, new short[][] { new short[] {8, 0} });
			addEvent(1180, new short[][] { new short[] {4, 2}, new short[] {3, 19, 6, 1}, new short[] {3, 19, 7, 1}, new short[] {3, 19, 8, 1} });
			break;
		case 6:
			introText = "Then...";
			xLevel = 3;
			yLevel = 2;
			xTransition = 1;
			yTransition = 1;
			addEvent(280, new short[][] { new short[] {14} });
			addEvent(400, new short[][] { new short[] {4, 2}, new short[] {3, 0, 11, 0}, new short[] {3, 0, 12, 0}, new short[] {3, 0, 13, 0} });
			addEvent(430, new short[][] { new short[] {0, -16, 200, 2, 0, 0, -1}, new short[] {1, 0, 5} });
			addEvent(500, new short[][] { new short[] {1, 0, 5} });
			for (int i = 620; i < 741; i += 10)
			{
				addEvent(i, new short[][] { new short[] {15} });
			}
			addEvent(750, new short[][] { new short[] {15}, new short[] {16, 0} });
			addEvent(760, new short[][] { new short[] {4, 3}, new short[] {15} });
			addEvent(770, new short[][] { new short[] {15} });
			addEvent(780, new short[][] { new short[] {1, 0, 6}, new short[] {16, 1} });
			addEvent(800, new short[][] { new short[] {16, 60} });
			break;
		case 7:
			introText = "And so...";
			xLevel = 0;
			yLevel = 1;
			xTransition = 1;
			yTransition = 1;
			saveGame();
			addEvent(120, new short[][] { new short[] {17} });
			addEvent(360, new short[][] { new short[] {0, 144, -24, 0, 63, 5, (short) lastArmor}, new short[] {1, 0, 3} });
			addEvent(600, new short[][] { new short[] {1, 0, 2}, new short[] {1, 0, 5}, new short[] {1, 0, 3} });
			addEvent(720, new short[][] { new short[] {1, 0, 1} });
			addEvent(761, new short[][] { new short[] {0, 192, 200, 2, 0, 0, -1}, new short[] {1, 1, 5}, new short[] {11, -1} });
			addEvent(785, new short[][] { new short[] {1, 0, 5} });
			addEvent(965, new short[][] { new short[] {5, 1}, new short[] {1, 0, 5}, new short[] {11, 1} });
			addEvent(988, new short[][] { new short[] {1, 0, 1} });
			addEvent(1043, new short[][] { new short[] {0, 224, 200, 2, 0, 0, -1}, new short[] {11, -1} });
			addEvent(1075, new short[][] { new short[] {1, 0, 1} });
			addEvent(1086, new short[][] { new short[] {1, 0, 1} });
			addEvent(1110, new short[][] { new short[] {5, 1}, new short[] {11, 1} });
			addEvent(1195, new short[][] { new short[] {0, 224, 120, 2, 0, 0, -1}, new short[] {11, -1} });
			addEvent(1250, new short[][] { new short[] {1, 0, 2} });
			addEvent(1280, new short[][] { new short[] {5, 1}, new short[] {1, 0, 2}, new short[] {11, 1} });
			addEvent(1380, new short[][] { new short[] {1, 0, 5}, new short[] {12, -1} });
			addEvent(1620, new short[][] { new short[] {1, 0, 5} });
			break;
		}
		int endFrame = 1000;
		if (cutsceneId == 1 || cutsceneId == 6) endFrame = 1200;
		else if (cutsceneId == 2) endFrame = 1460;
		else if (cutsceneId == 3) endFrame = 1500;
		else if (cutsceneId == 5) endFrame = 1280;
		else if (cutsceneId == 7) endFrame = 1670;
		events.add(new Event(endFrame, null));
		nextEvent = events.get(0);
		level = new Level(this, xLevel, yLevel, 0, 0, true);
		level.player.remove();
		Sound.resetAmbient();
	}
	public void tick(Input input)
	{
		if (!endText.isEmpty())
		{
			if (input.isKeyPressed(Constants.K_GUN)) setState(new GameState(save));
		}
		else if (++frame == nextEvent.executeFrame)
		{
			if (nextEvent.codes == null)
			{
				if (save == null) setState(new FallingState());
				else if (cutsceneId < 7) saveGame();
				else
				{
					players.clear();
					setState(new CreditsState());
				}
				return;
			}
			else
			{
				for (int i = 0; i < nextEvent.codes.length; i++)
				{
					executeEvent(nextEvent.codes[i]);
				}
				events.remove(0);
				nextEvent = events.get(0);
			}
		}
		for (int i = 0; i < players.size(); i++)
		{
			Player player = players.get(i);
			if (player.removed) continue;
			boolean a = (keyFlags[i] & 4) == 4;
			boolean b = (keyFlags[i] & 2) == 2;
			boolean gun = (keyFlags[i] & 1) == 1;
			player.tick((keyFlags[i] & 64) == 64, (keyFlags[i] & 32) == 32, (keyFlags[i] & 16) == 16, (keyFlags[i] & 8) == 8, a, b, gun);
			if (a) toggleKey(i, 2);
			if (b) toggleKey(i, 1);
			if (gun) toggleKey(i, 0);
		}
		level.tick(input);
	}
	public void render(Graphics g)
	{
		fillBackground(g);
		if (frame < 280)
		{
			if (frame >= 120) drawString(introText, g, 160 - (introText.length() << 2), 116);
		}
		else if (endText.isEmpty()) level.render(g);
		else
		{
			drawString(endText, g, 64, 116);
			String msg = "Press " + Constants.KEY_NAMES[Constants.K_GUN];
			drawString(msg, g, 160 - (msg.length() << 2), 136);
		}
		fillHud(g);
	}
	public void transition(int xa, int ya)
	{
		if (xTransition == -xa) xa = 0;
		if (yTransition == -ya) ya = 0;
		if (xa != 0 || ya != 0)
		{
			Player player = players.get(0);
			player.x -= xa * 304;
			player.y -= ya * 224;
			if (xa < 0) player.x -= 8;
			Level newLevel = new Level(this, xLevel += xa, yLevel += ya, (int) player.x, (int) player.y, true);
			newLevel.player.removed = true;
			newLevel.addSprite(player);
			level = newLevel;
		}
	}
	private void addEvent(int executeFrame, short[][] codes)
	{
		events.add(new Event(executeFrame, codes));
	}
	private void executeEvent(short[] code)
	{
		switch (code[0])
		{
		case 0:
			Player player = new Player(code[1], code[2], code[3]);
			player.gunLevel = code[4];
			player.equippedGun = code[5];
			if (code[6] >= 0) player.lastArmor = code[6];
			players.add(player);
			level.addSprite(player);
			break;
		case 1:
			toggleKey(code[1], code[2]);
			break;
		case 2:
			player = players.get(code[1]);
			GunPhysics gun = new GunPhysics(player.x - 4, player.y, code[2] / 10.0, code[3], code[4], false);
			if (code[3] == -8) gun.onConveyor = true;
			level.addSprite(gun);
			break;
		case 3:
			level.setBlock(code[1], code[2], (byte) code[3]);
			break;
		case 4:
			if (code[1] == 0) Sound.playSound("opening");
			else if (code[1] == 1) Sound.playSound("shoot_enemy");
			else if (code[1] == 2) Sound.playSound("bossdoor");
			else Sound.playSound("transform_big");
			break;
		case 5:
			players.remove(code[1]);
			break;
		case 6:
			Sound.setAmbient("motors");
			Sound.playAmbient();
			break;
		case 7:
			level.addSprite(new Wolfbane());
			break;
		case 8:
			player = players.get(code[1]);
			player.dir = -player.dir;
			break;
		case 9:
			player = players.get(code[1]);
			player.gunLevel = code[2];
			player.equippedGun = code[3];
			if (player.gunLevel == 31) player.x += 16;
			break;
		case 10:
			players.get(code[1]).health = 1;
			break;
		case 11:
			xTransition = code[1];
			break;
		case 12:
			yTransition = code[1];
			break;
		case 13:
			level.addSprite(new Bullet(code[1], code[2], code[3], code[4], -1, null));
			break;
		case 14:
			BossKhan khan = new BossKhan(false);
			khan.activated = true;
			khan.time = 0;
			khan.deadTime = 121;
			level.boss = khan;
			level.addSprite(khan);
			break;
		case 15:
			level.addSprite(new GemPhysics(124, 202, true));
			break;
		case 16:
			if (code[1] == 1) level.flashTime = 1;
			else ((BossKhan) level.boss).growTime = code[1] + 1;
			break;
		case 17:
			Music.playMusic(9);
			break;
		}
	}
	private void toggleKey(int player, int key)
	{
		keyFlags[player] ^= (1 << key);
	}
	private void saveGame()
	{
		boolean successful = LoadSave.saveGame(save);
		if (cutsceneId < 7)
		{
			if (successful)
			{
				Sound.playSound("success");
				endText = "The game has been saved.";
			}
			else
			{
				Sound.playSound("error");
				endText = "The game failed to save.";
			}
		}
	}
}