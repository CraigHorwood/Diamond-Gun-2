package com.craighorwood.ocus.level;
import java.awt.Graphics;
import java.util.*;

import com.craighorwood.ocus.*;
import com.craighorwood.ocus.sprite.*;
import com.craighorwood.ocus.state.*;
public class Level
{
	public List<Sprite> sprites = new ArrayList<Sprite>();
	public List<Sprite>[] spriteMap;
	private State state;
	private int xLevel, yLevel, xSpawn, ySpawn;
	private boolean isCutscene;
	private int saveCooldown = 0, topazCooldown = 0;
	public Player player;
	public boolean hot = false, underwater = false;
	public int dark = -1;
	private byte[] blocks;
	public Boss boss = null;
	@SuppressWarnings("unchecked")
	public Level(State state, int xLevel, int yLevel, int xSpawn, int ySpawn, boolean isCutscene)
	{
		this.state = state;
		this.xLevel = xLevel;
		this.yLevel = yLevel;
		this.xSpawn = xSpawn;
		this.ySpawn = ySpawn;
		this.isCutscene = isCutscene;
		int[] pixels = new int[300];
		if (isCutscene) Images.dat_levels_cutscene.getRGB(xLevel * 20, yLevel * 15, 20, 15, pixels, 0, 20);
		else Images.dat_levels.getRGB(xLevel * 20, yLevel * 15, 20, 15, pixels, 0, 20);
		blocks = new byte[300];
		spriteMap = new ArrayList[300];
		Sound.resetAmbient();
		underwater = true;
		boolean[] slots = null;
		for (int y = 0; y < 15; y++)
		{
			for (int x = 0; x < 20; x++)
			{
				spriteMap[x + y * 20] = new ArrayList<Sprite>();
				int col = pixels[x + y * 20] & 0xFFFFFF;
				byte block = isInWater(pixels, x, y);
				if (col == 0xFFFFFF) block = 1;
				else if (col == 0xFFFF00) block = 2;
				else if (col == 0x880015) block = 3;
				else if (col == 0xFF) block = 4;
				else if (col == 0xEFE4B0) block = 5;
				else if (col == 0xC8BFE7) block = 6;
				else if (col == 0xFF7F27)
				{
					Sound.setAmbient("fiery");
					hot = true;
				}
				else if (col == 0xA349A4 && Constants.BOSSES_KILLED < 5) dark = 0;
				else if (col == 0xFFC90E)
				{
					if (yLevel > 0 || xLevel <= 8 || xLevel >= 12 || Constants.BOSSES_KILLED < 2) block = 8;
				}
				else if (col == 0x22B14C)
				{
					Sound.setAmbient("motors");
					block = 11;
				}
				else if (col == 0xB5E61D)
				{
					Sound.setAmbient("motors");
					block = 12;
				}
				else if (col == 0x7F7F7F) block = 13;
				else if (col == 0x99D9EA) addSprite(new GemPhysics(x << 4, y << 4, false));
				else
				{
					byte r = (byte) ((col & 0xFF0000) >> 16);
					byte g = (byte) ((col & 0xFF00) >> 8);
					byte b = (byte) (col & 0xFF);
					if (r == -61 && g == -61)
					{
						block = (byte) (79 + b);
					}
					else if (r == -1 && g == -82)
					{
						switch (b)
						{
						case 0:
							block = 32;
							break;
						case 51:
							block = 33;
							break;
						case 102:
							block = 34;
							break;
						case -103:
							block = 35;
							break;
						case -52:
							block = 36;
							break;
						case -1:
							block = 37;
							break;
						}
					}
					else if (r == -1 && b == 0)
					{
						block = (byte) (64 + g);
					}
					else if (r == -1 && b == -1)
					{
						block = (byte) (16 + g);
					}
					else if (g == -1 && b == -1)
					{
						if (r < 16 && r != 6 && r != 10 && r != 12 && r != 14 && Constants.SAVE_FILENAME == "/.rsave")
						{
							do
							{
								r = (byte) (Math.random() * 16);
							}
							while (r == 6 || r == 10 || r == 12 || r == 14);
						}
						switch (r)
						{
						case 0:
							addSprite(new Gimp(x << 4, y << 4));
							break;
						case 1:
							addSprite(new Custfire(x << 4, y << 4, 1));
							break;
						case 2:
							addSprite(new Custfire(x << 4, y << 4, -1));
							break;
						case 3:
							addSprite(new Stalactike(x << 4, y << 4));
							break;
						case 4:
							addSprite(new Osmosis(x << 4, y << 4));
							break;
						case 5:
							addSprite(new Coptop(x << 4, y << 4));
							break;
						case 6:
							if (Constants.BOSSES_KILLED < 1)
							{
								boss = new BossKiinam(x << 4, (y << 4) - 64);
								addSprite(boss);
							}
							break;
						case 7:
							block = 10;
							addSprite(new Demidisc((x << 4) + 8, (y << 4) + 8));
							break;
						case 8:
							addSprite(new Wheelix(x << 4, y << 4));
							break;
						case 9:
							addSprite(new GarnetGimp(x << 4, y << 4));
							break;
						case 10:
							if (Constants.BOSSES_KILLED < 2)
							{
								boss = new BossAgniSarpa();
								addSprite(boss);
							}
							break;
						case 11:
							addSprite(new Mitosis(x << 4, y << 4, 0));
							break;
						case 12:
							block = 4;
							if (Constants.BOSSES_KILLED < 3)
							{
								boss = new BossCytosis((x << 4) - 64, (y << 4) - 64);
								addSprite(boss);
							}
							break;
						case 13:
							addSprite(new Lowlock(x << 4, y << 4, false));
							break;
						case 14:
							if (Constants.BOSSES_KILLED < 4)
							{
								boss = new BossVoltere();
								addSprite(boss);
							}
							break;
						case 15:
							addSprite(new Lowlock(x << 4, y << 4, true));
							break;
						case 16:
							addSprite(new GreatGimp((x << 4) - 80, (y << 4) - 80));
							break;
						case 17:
							if (Constants.BOSSES_KILLED < 5)
							{
								if (slots == null) slots = new boolean[300];
								slots[x + y * 20] = true;
							}
							break;
						case 18:
							if (Constants.BOSSES_KILLED < 5)
							{
								boss = new BossErebus();
								addSprite(boss);
							}
							break;
						case 19:
							addSprite(new Adversary(x << 4, (y << 4) - 8));
							break;
						case 20:
							if (Constants.BOSSES_KILLED < 7)
							{
								boss = new BossMizKhan();
								addSprite(boss);
							}
							break;
						}
					}
					else if (r == 0 && g == -1)
					{
						block = isInWater(pixels, x, y);
						byte type = (byte) (b >> 5);
						addSprite(new Collectible(x << 4, y << 4, type, b));
					}
				}
				if (block == 0) underwater = false;
				blocks[x + y * 20] = block;
			}
		}
		if (slots != null) addSprite(new Echoplasm(slots));
		if (underwater) Sound.setAmbient("underwater");
		if (Integer.bitCount(Constants.GEMS_COLLECTED) > 3 && Integer.bitCount(Constants.ARMOR_COLLECTED) > 10 && xLevel == 12 && yLevel == 10)
		{
			for (int i = 0; i < 2; i++)
			{
				blocks[i * 20 + 244] = 0;
			}
			sprites.remove(sprites.size() - 1);
			sprites.remove(sprites.size() - 1);
			sprites.remove(sprites.size() - 1);
		}
		else if (Constants.CUTSCENES_WATCHED > 5 && xLevel == 6 && yLevel == 13)
		{
			blocks[121] = (byte) 1;
		}
		player = new Player(xSpawn, ySpawn, 0);
		addSprite(player);
	}
	private byte isInWater(int[] pixels, int x, int y)
	{
		if ((x - 1) < 0 || (x + 1) >= 20) return 0;
		y *= 20;
		if (pixels[(x + 1) + y] == 0xFF0000FF && pixels[(x - 1) + y] == 0xFF0000FF) return 4;
		return 0;
	}
	public void addSprite(Sprite spr)
	{
		sprites.add(spr);
		spr.init(this);
		spr.xSlot = (int) ((spr.x + spr.w / 2.0) / 16);
		spr.ySlot = (int) ((spr.y + spr.h / 2.0) / 16);
		if (spr.xSlot >= 0 && spr.ySlot >= 0 && spr.xSlot < 20 && spr.ySlot < 15)
		{
			spriteMap[spr.xSlot + spr.ySlot * 20].add(spr);
		}
	}
	private int time = 0;
	public int explosionTime = 0, flashTime = 0;
	private int pendingDelay = 8;
	public void tick(Input input)
	{
		time++;
		if (explosionTime > 0) explosionTime--;
		if (flashTime > 0) flashTime++;
		if (saveCooldown > 0) saveCooldown--;
		if (topazCooldown > 0) topazCooldown--;
		if (dark > 0) dark--;
		if (pendingSpaceBlocks.size() > 0)
		{
			if (--pendingDelay == 0)
			{
				Sound.playSound("pop");
				int bb = pendingSpaceBlocks.remove(0);
				setBlock(bb % 20, bb / 20, (byte) 7);
				pendingDelay = 8;
			}
		}
		for (int i = 0; i < sprites.size(); i++)
		{
			Sprite spr = sprites.get(i);
			int xSlotOld = spr.xSlot;
			int ySlotOld = spr.ySlot;
			if (!spr.removed) spr.tick();
			spr.xSlot = (int) ((spr.x + spr.w / 2.0) / 16);
			spr.ySlot = (int) ((spr.y + spr.h / 2.0) / 16);
			if (spr.removed)
			{
				if (xSlotOld >= 0 && ySlotOld >= 0 && xSlotOld < 20 && ySlotOld < 15)
				{
					spriteMap[xSlotOld + ySlotOld * 20].remove(spr);
				}
				sprites.remove(i--);
			}
			else
			{
				if (spr.xSlot != xSlotOld || spr.ySlot != ySlotOld)
				{
					if (xSlotOld >= 0 && ySlotOld >= 0 && xSlotOld < 20 && ySlotOld < 15)
					{
						spriteMap[xSlotOld + ySlotOld * 20].remove(spr);
					}
					if (spr.xSlot >= 0 && spr.ySlot >= 0 && spr.xSlot < 20 && spr.ySlot < 15)
					{
						spriteMap[spr.xSlot + spr.ySlot * 20].add(spr);
					}
					else spr.remove();
				}
			}
		}
	}
	private List<Sprite> hits = new ArrayList<Sprite>();
	public List<Sprite> getHits(double xc, double yc, double w, double h)
	{
		hits.clear();
		int r = 32;
		int x0 = (int) ((xc - r) / 16.0);
		int y0 = (int) ((yc - r) / 16.0);
		int x1 = (int) ((xc + w + r) / 16.0);
		int y1 = (int) ((yc + h + r) / 16.0);
		for (int x = x0; x <= x1; x++)
		{
			for (int y = y0; y <= y1; y++)
			{
				if (x >= 0 && y >= 0 && x < 20 && y < 15)
				{
					List<Sprite> sprs = spriteMap[x + y * 20];
					for (int i = 0; i < sprs.size(); i++)
					{
						Sprite spr = sprs.get(i);
						double xx0 = spr.x;
						double yy0 = spr.y;
						double xx1 = spr.x + spr.w;
						double yy1 = spr.y + spr.h;
						if (xx0 > xc + w || yy0 > yc + h || xx1 < xc || yy1 < yc) continue;
						hits.add(spr);
					}
				}
			}
		}
		return hits;
	}
	public void render(Graphics g)
	{
		if (isCutscene && xLevel == 4 && yLevel == 4) g.drawImage(Images.bg_sky, 0, 0, null);
		int xOffs = 0;
		int yOffs = 0;
		boolean isExploding = explosionTime > 0;
		if (isExploding)
		{
			xOffs = (int) ((Math.random() - 0.5) * explosionTime / 5.0);
			yOffs = (int) ((Math.random() - 0.5) * explosionTime / 5.0);
			g.translate(xOffs, yOffs);
		}
		if (boss instanceof BossMizKhan) ((BossMizKhan) boss).renderBackground(g);
		for (int y = isExploding ? -1 : 0; y < (isExploding ? 16 : 15); y++)
		{
			for (int x = isExploding ? -1 : 0; x < (isExploding ? 21 : 20); x++)
			{
				byte b = isExploding ? getBlock(x, y) : blocks[x + y * 20];
				if (dark == 0 && b != 5 && (b < 17 || b > 30)) continue;
				if (b > 0)
				{
					int xImg = b;
					int yImg = 0;
					if (b == 3)
					{
						if (blocks[x + y * 20 - 20] == 4) yImg = 1;
					}
					else if (b == 4)
					{
						if (getBlock(x, y - 1) > 0) yImg = 1;
					}
					else if (b == 5)
					{
						int t = getBlock(x, y - 1);
						if (t == 5) yImg = 1;
						else if (t == 4) xImg = 7;
					}
					else if (b == 7) xImg = 24;
					else if (b == 8)
					{
						int frame = (x + (time >> 4)) & 3;
						if (frame > 1) xImg = 9;
						yImg = frame & 1;
					}
					else if (b == 11 || b == 12)
					{
						xImg = 11;
						int frame = (time >> 1) & 3;
						if (b == 12) frame = 3 - frame;
						if (frame > 1) xImg = 12;
						yImg = frame & 1;
					}
					g.drawImage(Images.sht_blocks[xImg][yImg], x << 4, y << 4, null);
				}
			}
		}
		for (int i = 0; i < sprites.size(); i++)
		{
			Sprite spr = sprites.get(i);
			if (dark != 0 || (dark == 0 && (spr instanceof Player || spr instanceof Bullet || spr instanceof Explosion || spr instanceof Collectible || spr instanceof Echoplasm || spr instanceof BossErebus))) spr.render(g);
		}
		if (boss instanceof BossMizKhan) ((BossMizKhan) boss).renderBullets(g, true);
		if (Math.random() < flashTime / 200.0)
		{
			g.setColor(Constants.GUN_COLORS[0]);
			g.fillRect(0, 0, 320, 240);
		}
		if (Constants.BOSSES_KILLED < 2)
		{
			if (xLevel == 9 && yLevel == 0)
			{
				for (int y = 13; y < 15; y++)
				{
					for (int x = 0; x < 20; x++)
					{
						byte b = blocks[x + y * 20];
						if (b > 0)
						{
							int xImg = b;
							int yImg = 0;
							if (b == 8)
							{
								int frame = (x + (time >> 4)) & 3;
								if (frame > 1) xImg = 9;
								yImg = frame & 1;
							}
							g.drawImage(Images.sht_blocks[xImg][yImg], x << 4, y << 4, null);
						}
					}
				}
			}
		}
		if (explosionTime > 0) g.translate(-xOffs, -yOffs);
		if (hot) g.drawImage(Images.bg_overlay_hot, 0, 0, null);
	}
	public boolean isFree(Sprite spr, double xc, double yc, int w, int h, double xa, double ya)
	{
		if (spr.destroyer) return isDestroyerFree(spr, xc, yc, w, h);
		double pixel = 1 / 16.0;
		int xx0 = (int) (xc / 16.0);
		int yy0 = (int) (yc / 16.0);
		int xx1 = (int) ((xc + w - pixel) / 16.0);
		int yy1 = (int) ((yc + h - pixel) / 16.0);
		int x0 = xx0;
		int y0 = yy0;
		int x1 = xx1;
		int y1 = yy1;
		if (xa < 0)
		{
			x0 = xx1;
			x1 = xx0;
		}
		if (ya < 0)
		{
			y0 = yy1;
			y1 = yy0;
		}
		int xi = xa < 0 ? -1 : 1;
		int yi = ya < 0 ? -1 : 1;
		boolean ok = true;
		for (int x = x0; x != x1 + xi; x += xi)
		{
			for (int y = y0; y != y1 + yi; y += yi)
			{
				if (x >= 0 && y >= 0 && x < 20 && y < 15)
				{
					byte b = blocks[x + y * 20];
					if (b > 0 && b != 2 && b != 4 && b != 6 && b != 8 && (b < 32 || b >= 64)) ok = false;
					Player p = null;
					if (spr instanceof Player) p = (Player) spr;
					if (b == 1)
					{
						if (p != null && ya > 0)
						{
							if (p.climbing) p.climbing = false;
						}
					}
					else if (b == 2)
					{
						if (getBlock(x, y - 1) == 2)
						{
							if (p != null) p.climbUp = true;
						}
						if (getBlock(x, y + 1) == 2)
						{
							if (getBlock(x, y - 1) == 0 && y1 < y + 1) ok = false;
							if (p != null) p.climbDown = true;
						}
						if (p != null)
						{
							if (p.climbing)
							{
								ok = true;
								p.x = x << 4;
							}
						}
					}
					else if (b == 3 && ya != 0 && !spr.removed) spr.hitSpikes();
					else if (b == 8 && p != null) p.die();
					else if (b == 11 && p != null) p.onConveyor = 1;
					else if (b == 12 && p != null) p.onConveyor = -1;
				}
			}
		}
		return ok;
	}
	public boolean isDestroyerFree(Sprite spr, double xc, double yc, int w, int h)
	{
		double pixel = 1 / 16.0;
		int x0 = (int) (xc / 16.0);
		int y0 = (int) (yc / 16.0);
		int x1 = (int) ((xc + w - pixel) / 16.0);
		int y1 = (int) ((yc + h - pixel) / 16.0);
		boolean isBullet = spr instanceof Bullet;
		boolean ok = true;
		for  (int x = x0; x <= x1; x++)
		{
			for (int y = y0; y <= y1; y++)
			{
				if (x >= 0 && y >= 0 && x < 20 && y < 15)
				{
					byte b = blocks[x + y * 20];
					if (isBullet)
					{
						if (b > 0 && b != 2 && b != 4 && b != 6 && b != 8 && (b < 32 || b >= 64)) ok = false;
						Bullet bb = (Bullet) spr;
						if (bb.shooter instanceof Player)
						{
							if (b == 5 && saveCooldown == 0)
							{
								saveCooldown = 60;
								state.setState(new SaveGameState((GameState) state, new Save(xLevel, yLevel, xSpawn, ySpawn, player.gunLevel, player.equippedGun, Constants.GEMS_COLLECTED, Constants.ARMOR_COLLECTED, Constants.BOSSES_KILLED, Constants.CUTSCENES_WATCHED, Constants.ITEM_FLAG)));
							}
							else if (b >= 64 && (b == 64 + bb.gunLevel || bb.gunLevel == 5))
							{
								blocks[x + y * 20] = (byte) (player.inWater ? 4 : 0);
								addSprite(new Explosion(x << 4, y << 4));
								bb.removed = true;
							}
							else if (b >= 16 && b == 16 + bb.gunLevel)
							{
								switch (b)
								{
								case 17:
									if (getBlock(x, y + 1) == 0)
									{
										blocks[x + y * 20] = 0;
										addSprite(new GoldCube(x << 4, y << 4));
									}
									break;
								case 21:
									if (boss instanceof BossKhan)
									{
										Sound.playSound("shoot_enemy");
										addSprite(new Bullet((x << 4) + 8, (y << 4) + 16, 0, 6, -1, null));
									}
									else if (boss instanceof BossMizKhan)
									{
										if (x == 2) ((BossMizKhan) boss).push();
										else boss.damage(null);
									}
									break;
								case 23:
									blocks[x + y * 20] = 0;
									addSprite(new BronzeCube(x << 4, y << 4, bb.x < x << 4 ? 1 : -1));
									break;
								case 24:
									pendingSpaceBlocks.clear();
									setSpaceBlock(x, y);
									break;
								case 25:
									Sound.playSound("spring");
									player.bouncing = true;
									player.ya = player.playerId == 3 ? 18 : -18;
									break;
								case 26:
									Sound.playSound("absorb");
									setBlock(x, y, (byte) 0);
									player.carryingBlock = true;
									break;
								case 27:
									Sound.playSound("gravity");
									player.playerId ^= 3;
									break;
								case 28:
									if (topazCooldown == 0)
									{
										Sound.playSound("electric");
										byte l = getBlock(x - 1, y);
										byte r = getBlock(x + 1, y);
										byte u = getBlock(x, y - 1);
										byte d = getBlock(x, y + 1);
										if ((l >= 32 && l < 38) || (l >= 79 && l < 83)) addSprite(new Charge(x << 4, (y << 4) + 8, 3));
										else if ((r >= 32 && r < 38) || (r >= 79 && r < 83)) addSprite(new Charge((x << 4) + 16, (y << 4) + 8, 1));
										else if ((u >= 32 && u < 38) || (u >= 79 && u < 83)) addSprite(new Charge((x << 4) + 8, y << 4, 0));
										else if ((d >= 32 && d < 38) || (d >= 79 && d < 83)) addSprite(new Charge((x << 4) + 8, (y << 4) + 16, 2));
										topazCooldown = 30;
									}
									break;
								case 29:
									Sound.playSound("quartz");
									if (quartzedEnemies.size() == 0)
									{
										for (int i = 0; i < sprites.size(); i++)
										{
											Sprite sp = sprites.get(i);
											if (sp instanceof Enemy && !(sp instanceof Demidisc))
											{
												setBlock(sp.xSlot, sp.ySlot, (byte) 1);
												sp.remove();
												quartzedEnemies.add((Enemy) sp);
											}
										}
									}
									else
									{
										for (int i = 0; i < quartzedEnemies.size(); i++)
										{
											Enemy en = quartzedEnemies.remove(i--);
											setBlock(en.xSlot, en.ySlot, (byte) (xLevel == 14 && yLevel == 6 && en.ySlot > 4 ? 4 : 0));
											en.removed = false;
											addSprite(en);
										}
									}
									break;
								case 30:
									Explosion ex = new Explosion(x << 4, y << 4);
									ex.destroyer = false;
									addSprite(ex);
									if (Constants.BOSSES_KILLED < 5) dark = 100;
									setBlock(x, y, (byte) 0);
									break;
								}
							}
							else if (b >= 79)
							{
								Sound.playSound("turn");
								if (++b > 82) b = 79;
								setBlock(x, y, b);
							}
						}
					}
					else if (spr instanceof Explosion && b >= 64)
					{
						blocks[x + y * 20] = (byte) (player.inWater ? 4 : 0);
						addSprite(new Explosion(x << 4, y << 4));
					}
				}
			}
		}
		return ok;
	}
	public void showCollectibleDialog(int type)
	{
		if (state instanceof GameState) ((GameState) state).showCollectibleDialog(type);
	}
	private List<Integer> pendingSpaceBlocks = new LinkedList<Integer>();
	private List<Enemy> quartzedEnemies = new ArrayList<Enemy>();
	private void setSpaceBlock(int x, int y)
	{
		pendingSpaceBlocks.add(x + y * 20);
		for (int xo = -1; xo < 2; xo++)
		{
			for (int yo = -1; yo < 2; yo++)
			{
				if ((xo != 0 && yo != 0) || (xo == 0 && yo == 0)) continue;
				int xx = x + xo;
				int yy = y + yo;
				if (xx >= 0 && yy >= 0 && xx < 20 && yy < 15)
				{
					if (!pendingSpaceBlocks.contains(xx + yy * 20) && getBlock(xx, yy) == 6) setSpaceBlock(xx, yy);
				}
			}
		}
	}
	public byte getBlock(int x, int y)
	{
		if (x < 0) x = 0;
		else if (x > 19) x = 19;
		if (y < 0) y = 0;
		else if (y > 14) y = 14;
		return blocks[x + y * 20];
	}
	public void setBlock(int x, int y, byte b)
	{
		if (x >= 0 && y >= 0 && x < 20 && y < 15)
		{
			blocks[x + y * 20] = b;
			if (b > 0 && b != 2 && b != 4 && b != 6 && b != 8)
			{
				int xx = x << 4;
				int yy = y << 4;
				if (player.x < xx + 16 && player.x + player.w > xx && player.y < yy + 16 && player.y + player.h > yy + 16)
				{
					player.die();
				}
			}
		}
	}
	public void explode(int x, int y)
	{
		Sound.playSound("onyxcube");
		addSprite(new BigExplosion((x << 4) + 8, (y << 4) + 8));
		int x0 = x - 2;
		int y0 = y - 2;
		int x1 = x + 2;
		int y1 = y + 2;
		for (int yy = y0; yy <= y1; yy++)
		{
			for (int xx = x0; xx <= x1; xx++)
			{
				if (xx > 0 && yy > 0 && xx < 19 && yy < 14)
				{
					for (int i = 0; i < spriteMap[xx + yy * 20].size(); i++)
					{
						Sprite spr = spriteMap[xx + yy * 20].get(i);
						if (spr instanceof Enemy) spr.die();
					}
					setBlock(xx, yy, (byte) 0);
				}
			}
		}
		explosionTime = 60;
	}
	public void transition(int xa, int ya)
	{
		if (state instanceof GameState) ((GameState) state).transition(xa, ya);
		else if (state instanceof CutsceneState) ((CutsceneState) state).transition(xa, ya);
	}
}