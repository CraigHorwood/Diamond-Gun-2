package com.craighorwood.ocus.state;
import java.awt.Graphics;
import com.craighorwood.ocus.*;
import com.craighorwood.ocus.level.*;
public class GameState extends State
{
	public Save save;
	private int xLevel, yLevel;
	private Level level;
	public GameState(Save save)
	{
		this.save = save;
		hideMouse = true;
		xLevel = save.xLevel;
		yLevel = save.yLevel;
		Constants.GEMS_COLLECTED = save.gems;
		Constants.ARMOR_COLLECTED = save.armor;
		Constants.BOSSES_KILLED = save.bosses;
		Constants.CUTSCENES_WATCHED = save.cutscenes;
		Constants.ITEM_FLAG = save.items;
		level = new Level(this, xLevel, yLevel, save.xSpawn, save.ySpawn, false);
		level.player.gunLevel = save.gunLevel;
		level.player.equippedGun = save.equippedGun;
		level.player.inWater = level.getBlock(level.player.xSlot, level.player.ySlot) == 4;
		level.player.lastArmor = save.getLastArmor();
		level.player.resistance = save.getResistance();
		level.player.maxGems = Integer.bitCount(Constants.GEMS_COLLECTED);
		level.player.gems = level.player.maxGems;
		if (save.xLevel == 7 && save.yLevel == 0) level.player.ya = 16;
		enterLevel(xLevel + yLevel * 15);
	}
	private int deadTime = 0;
	public void tick(Input input)
	{
		if (collectibleDialogTime > 0) collectibleDialogTime--;
		if (collectibleDialog > 0)
		{
			if (collectibleDialogTime == 0 && input.isKeyPressed(Constants.K_GUN)) collectibleDialog = 0;
			return;
		}
		if (!level.player.removed) level.player.tick(input.isKeyDown(Constants.K_LEFT), input.isKeyDown(Constants.K_RIGHT), input.isKeyDown(Constants.K_UP), input.isKeyDown(Constants.K_DOWN), input.isKeyPressed(Constants.K_A), input.isKeyPressed(Constants.K_B), input.isKeyPressed(Constants.K_GUN));
		else
		{
			if (++deadTime > 60 && input.isKeyPressed(Constants.K_GUN)) respawn();
		}
		level.tick(input);
		if (!level.player.removed && input.isKeyPressed(Constants.K_PAUSE))
		{
			Music.pauseMusic();
			Sound.pauseAllSounds();
			setState(new PauseState(this));
		}
	}
	public void render(Graphics g)
	{
		fillBackground(g);
		level.render(g);
		if (level.player.removed)
		{
			fillCenteredDialog(g, 160, 128);
			g.drawImage(Images.bg_youdead, 127, 64, null);
			if (((Constants.GEMS_COLLECTED > 0 && Constants.GEMS_COLLECTED < 1023) || (Constants.ARMOR_COLLECTED > 0 && Constants.ARMOR_COLLECTED < 67108863)) && deadTime > 20)
			{
				drawString("Having trouble?", g, 100, 108);
				drawString("Look hard for gems", g, 88, 128);
				drawString("and armour!", g, 116, 138);
			}
			if (deadTime > 60)
			{
				String msg = "Press " + Constants.KEY_NAMES[Constants.K_GUN];
				drawString(msg, g, 160 - (msg.length() << 2), 168);
			}
		}
		if (collectibleDialog > 0)
		{
			fillCenteredDialog(g, 160, 128);
			String msg = "";
			switch (collectibleDialog)
			{
			case 1:
				drawString("YOU GOT A GEM!", g, 108, 70);
				drawString("Gems appear as", g, 104, 90);
				drawString("white squares below", g, 84, 100);
				drawString("your health bar.", g, 96, 110);
				drawString("They will grant you", g, 84, 130);
				drawString("one extra hit.", g, 104, 140);
				break;
			case 2:
				drawString("YOU GOT ARMOUR!", g, 100, 70);
				drawString("Armour lessens the", g, 88, 90);
				drawString("amount of damage", g, 96, 100);
				drawString("you take.", g, 124, 110);
				drawString("Just one chestplate", g, 84, 130);
				drawString("isn't enough. Look", g, 88, 140);
				drawString("for more!", g, 124, 150);
				break;
			case 3:
				drawString("YOU GOT THE", g, 116, 70);
				drawString("RED GEM!", g, 128, 80);
				drawString("This special gem", g, 96, 100);
				drawString("will allow you to", g, 92, 110);
				drawString("survive in extreme", g, 88, 120);
				drawString("heat.", g, 140, 130);
				break;
			case 4:
				drawString("YOU GOT THE", g, 116, 70);
				drawString("BLUE GEM!", g, 124, 80);
				drawString("This special gem", g, 96, 100);
				drawString("will allow you to", g, 92, 110);
				drawString("breathe underwater.", g, 84, 120);
				break;
			}
			if (collectibleDialogTime == 0)
			{
				msg = "Press " + Constants.KEY_NAMES[Constants.K_GUN];
				drawString(msg, g, 160 - (msg.length() << 2), 172);
			}
		}
		fillHud(g);
	}
	protected void fillHud(Graphics g)
	{
		super.fillHud(g);
		boolean bossActivated = false;
		if (level.boss != null) bossActivated = level.boss.activated;
		String msg = "";
		if (level.player.gunLevel > 0)
		{
			g.drawImage(Images.sht_hud[0][bossActivated ? 2 : 1], 0, 240, null);
			for (int i = -1; i < 2; i++)
			{
				if ((level.player.gunLevel & (1 << (level.player.equippedGun + i))) > 0)
				{
					g.drawImage(Images.sht_guns[level.player.equippedGun + i][0], 152 + 60 * i, 254, null);
				}
			}
			msg = Constants.GUN_NAMES[level.player.equippedGun] + " GUN";
			drawString(msg, g, 160 - (msg.length() << 2), 241);
			if ((level.player.gunLevel & -level.player.gunLevel) != level.player.gunLevel)
			{
				msg = Constants.KEY_NAMES[Constants.K_GUN];
				drawString(msg, g, 160 - (msg.length() << 2), 271);
			}
			
		}
		else g.drawImage(Images.sht_hud[0][0], 0, 240, null);
		g.setColor(Constants.GUN_COLORS[level.player.invincibleTime > 104 && (level.player.invincibleTime & 3) == 0 ? 0 : 3]);
		g.fillRect(5, 255, (int) (78 * level.player.health), level.player.inWater && Constants.ITEM_FLAG < 2 ? 7 : 14);
		if (level.player.inWater && Constants.ITEM_FLAG < 2)
		{
			g.setColor(Constants.GUN_COLORS[5]);
			g.fillRect(5, 262, (int) (78 * level.player.breath), 7);
		}
		for (int i = 0; i < level.player.gems + Constants.ITEM_FLAG; i++)
		{
			g.setColor(Constants.GUN_COLORS[i < Constants.ITEM_FLAG ? (i == 1 ? 5 : 3) : 0]);
			g.fillRect(4 + i * 6, 272, 4, 4);
		}
		if (bossActivated)
		{
			msg = level.boss.name;
			drawString(msg, g, 316 - (msg.length() << 3), 244);
			g.setColor(Constants.GUN_COLORS[level.boss.invincibleTime > 6 ? 0 : 5]);
			g.fillRect(237, 255, (int) (78 * level.boss.health), 14);
		}
		else if (yLevel < 14 && Constants.BOSSES_KILLED == 6)
		{
			if (((System.currentTimeMillis() / 500) & 1) == 0) drawString("UP!", g, 274, 254);
		}
	}
	private int collectibleDialog = 0, collectibleDialogTime = 0;
	public void showCollectibleDialog(int type)
	{
		switch (type)
		{
		case 1:
			if (Integer.bitCount(Constants.GEMS_COLLECTED) == 1)
			{
				collectibleDialog = 1;
				collectibleDialogTime = 60;
			}
			break;
		case 2:
			if (Integer.bitCount(Constants.ARMOR_COLLECTED) == 1)
			{
				collectibleDialog = 2;
				collectibleDialogTime = 60;
			}
			break;
		case 3:
			collectibleDialog = 3;
			collectibleDialogTime = 60;
			break;
		case 4:
			collectibleDialog = 4;
			collectibleDialogTime = 60;
			break;
		}
	}
	private void enterLevel(int l)
	{
		if (Constants.BOSSES_KILLED == 6) Music.playMusic(7);
		switch (l)
		{
		case 8:
			if (Constants.BOSSES_KILLED == 7 && Constants.CUTSCENES_WATCHED < 7)
			{
				setState(new CutsceneState(new Save(7, 0, 152, -12, level.player.gunLevel, level.player.equippedGun, Constants.GEMS_COLLECTED, Constants.ARMOR_COLLECTED, Constants.BOSSES_KILLED, 7, Constants.ITEM_FLAG), 7, level.player.equippedGun, level.player.lastArmor));
			}
			break;
		case 9:
		case 10:
		case 11:
			if (Constants.CUTSCENES_WATCHED < 2) Music.stopMusic();
			break;
		case 26:
			if (Constants.CUTSCENES_WATCHED < 2)
			{
				setState(new CutsceneState(new Save(xLevel, yLevel, (int) level.player.x, (int) level.player.y, level.player.gunLevel, level.player.equippedGun, Constants.GEMS_COLLECTED, Constants.ARMOR_COLLECTED, Constants.BOSSES_KILLED, 2, Constants.ITEM_FLAG), 2));
			}
			break;
		case 60:
			if (Constants.CUTSCENES_WATCHED < 1) Music.stopMusic();
			break;
		case 75:
			if (Constants.CUTSCENES_WATCHED < 1)
			{
				setState(new CutsceneState(new Save(xLevel, yLevel, (int) level.player.x, (int) level.player.y, level.player.gunLevel, level.player.equippedGun, Constants.GEMS_COLLECTED, Constants.ARMOR_COLLECTED, Constants.BOSSES_KILLED, 1, Constants.ITEM_FLAG), 1));
			}
			else Music.playMusic(0);
			break;
		case 88:
		case 89:
			Music.playMusic(1);
			break;
		case 100:
			if (Constants.BOSSES_KILLED != 6 && level.player.inWater)
			{
				Music.playMusic(3);
				Sound.setAmbient("underwater");
			}
			break;
		case 103:
		case 104:
		case 116:
		case 117:
		case 118:
		case 119:
		case 131:
		case 132:
		case 133:
		case 134:
			if (Constants.BOSSES_KILLED != 6) Music.playMusic(1);
			break;
		case 124:
			if (Constants.CUTSCENES_WATCHED < 3) Music.stopMusic();
			Sound.resetAmbient();
			break;
		case 138:
		case 139:
			if (Constants.CUTSCENES_WATCHED < 3)
			{
				setState(new CutsceneState(new Save(xLevel, yLevel, (int) level.player.x, (int) level.player.y, level.player.gunLevel, level.player.equippedGun, Constants.GEMS_COLLECTED, Constants.ARMOR_COLLECTED, Constants.BOSSES_KILLED, 3, Constants.ITEM_FLAG), 3));
				break;
			}
		case 140:
			Sound.setAmbient("waterfall");
			break;
		case 167:
			Music.playMusic(1);
			break;
		case 169:
			if (Constants.CUTSCENES_WATCHED < 4) Music.stopMusic();
			break;
		case 170:
			if (Constants.CUTSCENES_WATCHED < 4)
			{
				Sound.resetAmbient();
				setState(new CutsceneState(new Save(xLevel, yLevel, (int) level.player.x, (int) level.player.y, level.player.gunLevel, level.player.equippedGun, Constants.GEMS_COLLECTED, Constants.ARMOR_COLLECTED, Constants.BOSSES_KILLED, 4, Constants.ITEM_FLAG), 4));
			}
			else Music.playMusic(1);
			break;
		case 183:
			Music.stopMusic();
			break;
		case 201:
			if (Constants.BOSSES_KILLED > 5 && Constants.CUTSCENES_WATCHED < 6)
			{
				Sound.resetAmbient();
				setState(new CutsceneState(new Save(xLevel, yLevel, 16, 72, level.player.gunLevel, level.player.equippedGun, Constants.GEMS_COLLECTED, Constants.ARMOR_COLLECTED, Constants.BOSSES_KILLED, 6, Constants.ITEM_FLAG), 6));
			}
			else if (Constants.BOSSES_KILLED != 6) Music.stopMusic();
			break;
		case 202:
		case 203:
			if (Constants.BOSSES_KILLED != 6) Music.stopMusic();
			break;
		case 204:
			if (Constants.BOSSES_KILLED != 6) Music.stopMusic();
			if (Constants.CUTSCENES_WATCHED < 5)
			{
				Sound.resetAmbient();
				setState(new CutsceneState(new Save(xLevel, yLevel, 256, 56, level.player.gunLevel, level.player.equippedGun, Constants.GEMS_COLLECTED, Constants.ARMOR_COLLECTED, Constants.BOSSES_KILLED, 5, Constants.ITEM_FLAG), 5, level.player.equippedGun, level.player.lastArmor));
			}
			break;
		case 216:
			Music.stopMusic();
			break;
		default:
			if (level.player.gunLevel > 0 && Constants.BOSSES_KILLED != 6)
			{
				int x = l % 15;
				int y = l / 15;
				if (level.hot && x > 4) Music.playMusic(2);
				else if (level.underwater) Music.playMusic(3);
				else if (level.dark >= 0) Music.playMusic(4);
				else
				{
					if (y > 8 && x > 2) Music.playMusic(1);
					else Music.playMusic(0);
				}
			}
			break;
		}
		Sound.playAmbient();
	}
	public void transition(int xa, int ya)
	{
		xLevel += xa;
		yLevel += ya;
		level.player.x -= xa * 304;
		level.player.y -= ya * 224;
		if (xa < 0) level.player.x -= 8;
		if (yLevel < 0) yLevel = 14;
		else if (yLevel > 14) yLevel = 0;
		Level newLevel = new Level(this, xLevel, yLevel, (int) level.player.x, (int) (level.player.y) + (ya << 4), false);
		newLevel.player.remove();
		newLevel.player = level.player;
		newLevel.player.carryingBlock = false;
		newLevel.addSprite(newLevel.player);
		level = newLevel;
		enterLevel(xLevel + yLevel * 15);
	}
	private void respawn()
	{
		xLevel = save.xLevel;
		yLevel = save.yLevel;
		Constants.GEMS_COLLECTED = save.gems;
		Constants.ARMOR_COLLECTED = save.armor;
		Constants.BOSSES_KILLED = save.bosses;
		Constants.CUTSCENES_WATCHED = save.cutscenes;
		Constants.ITEM_FLAG = save.items;
		Level newLevel = new Level(this, xLevel, yLevel, save.xSpawn, save.ySpawn, false);
		newLevel.player.gunLevel = save.gunLevel;
		newLevel.player.dir = level.player.dir;
		newLevel.player.equippedGun = save.equippedGun;
		newLevel.player.inWater = newLevel.getBlock(newLevel.player.xSlot, newLevel.player.ySlot) == 4;
		newLevel.player.lastArmor = save.getLastArmor();
		newLevel.player.resistance = save.getResistance();
		newLevel.player.maxGems = Integer.bitCount(Constants.GEMS_COLLECTED);
		newLevel.player.gems = newLevel.player.maxGems;
		newLevel.player.climbing = newLevel.getBlock(newLevel.player.xSlot, newLevel.player.ySlot) == 2;
		level = newLevel;
		enterLevel(xLevel + yLevel * 15);
		deadTime = 0;
	}
}