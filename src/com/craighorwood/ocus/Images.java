package com.craighorwood.ocus;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
public class Images
{
	public static BufferedImage bg_boss2;
	public static BufferedImage bg_boss4;
	public static BufferedImage bg_credits;
	public static BufferedImage bg_fireball;
	public static BufferedImage bg_overlay;
	public static BufferedImage bg_overlay_hot;
	public static BufferedImage bg_sky;
	public static BufferedImage bg_title;
	public static BufferedImage bg_update;
	public static BufferedImage bg_youdead;
	public static BufferedImage dat_levels;
	public static BufferedImage dat_levels_cutscene;
	public static BufferedImage[][] sht_beam;
	public static BufferedImage[][] sht_blocks;
	public static BufferedImage[][] sht_blueflame;
	public static BufferedImage[][] sht_boss0;
	public static BufferedImage[][] sht_boss1;
	public static BufferedImage[][] sht_boss6;
	public static BufferedImage[][] sht_button;
	public static BufferedImage[][] sht_enemies;
	public static BufferedImage[][] sht_energy;
	public static BufferedImage[][] sht_explosion;
	public static BufferedImage[][] sht_explosion_big;
	public static BufferedImage[][] sht_fire;
	public static BufferedImage[][] sht_font;
	public static BufferedImage[][] sht_gunlords;
	public static BufferedImage[][] sht_guns;
	public static BufferedImage[][] sht_health;
	public static BufferedImage[][] sht_hud;
	public static BufferedImage[][] sht_lightning;
	public static BufferedImage[][] sht_player;
	public static void init(OcusMain main)
	{
		bg_boss2 = load(main, "/img/boss2.png");
		bg_boss4 = load(main, "/img/boss4.png");
		bg_credits = load(main, "/img/credits.png");
		bg_fireball = load(main, "/img/fireball.png");
		bg_overlay = load(main, "/img/overlay.png");
		bg_overlay_hot = getHotOverlay();
		bg_sky = load(main, "/img/sky.png");
		bg_title = load(main, "/img/title.png");
		bg_update = load(main, "/img/update.png");
		bg_youdead = load(main, "/img/youdead.png");
		dat_levels = load(main, "/data/levels.dat");
		dat_levels_cutscene = load(main, "/data/cutscenery.dat");
		sht_beam = sheet(load(main, "/img/beam.png"), 48, 240);
		sht_blocks = sheet(load(main, "/img/blocks.png"), 16, 16);
		sht_blueflame = sheet(load(main, "/img/blueflame.png"), 28, 100);
		sht_boss0 = sheet(load(main, "/img/boss0.png"), 20, 16);
		sht_boss1 = sheet(load(main, "/img/boss1.png"), 32, 32);
		sht_boss6 = sheet(load(main, "/img/boss6.png"), 213, 200);
		sht_button = sheet(load(main, "/img/buttons.png"), 128, 16);
		sht_enemies = sheet(load(main, "/img/enemies.png"), 16, 16);
		sht_energy = sheet(load(main, "/img/energy.png"), 12, 12);
		sht_explosion = sheet(load(main, "/img/explosion.png"), 24, 24);
		sht_explosion_big = sheet(load(main, "/img/onyxplosion.png"), 80, 80);
		sht_fire = sheet(load(main, "/img/fire.png"), 16, 16);
		sht_font = sheet(load(main, "/img/font.png"), 8, 8);
		sht_gunlords = sheet(load(main, "/img/gunlords.png"), 32, 32);
		sht_guns = sheet(load(main, "/img/guns.png"), 16, 12);
		sht_health = sheet(load(main, "/img/health.png"), 4, 4);
		sht_hud = sheet(load(main, "/img/hud.png"), 320, 40);
		sht_lightning = sheet(load(main, "/img/lightning.png"), 11, 32);
		sht_player = sheet(load(main, "/img/player.png"), 16, 24);
	}
	private static BufferedImage load(OcusMain main, String fileName)
	{
		try
		{
			BufferedImage org = ImageIO.read(Images.class.getResource(fileName));
			int w = org.getWidth();
			int h = org.getHeight();
			BufferedImage res = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			Graphics g = res.getGraphics();
			g.drawImage(org, 0, 0, w, h, null, null);
			g.dispose();
			main.redraw();
			return res;
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	private static BufferedImage[][] sheet(BufferedImage src, int xs, int ys)
	{
		int xSlices = src.getWidth() / xs;
		int ySlices = src.getHeight() / ys;
		BufferedImage[][] res = new BufferedImage[xSlices][ySlices];
		for (int x = 0; x < xSlices; x++)
		{
			for (int y = 0; y < ySlices; y++)
			{
				res[x][y] = new BufferedImage(xs, ys, BufferedImage.TYPE_INT_ARGB);
				Graphics g = res[x][y].getGraphics();
				g.drawImage(src, -x * xs, -y * ys, null);
				g.dispose();
			}
		}
		return res;
	}
	private static BufferedImage getHotOverlay()
	{
		int w = bg_overlay.getWidth();
		int h = bg_overlay.getHeight();
		int[] pixels = new int[w * h];
		bg_overlay.getRGB(0, 0, w, h, pixels, 0, w);
		for (int i = 0; i < pixels.length; i++)
		{
			pixels[i] = 0xFF3F00;
			int a = (int) ((Math.abs((i / w - (h >> 1)) / (h * 2.0)) + 0.5) * 127);
			pixels[i] |= (a << 24);
		}
		BufferedImage res = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		res.setRGB(0, 0, w, h, pixels, 0, w);
		return res;
	}
}