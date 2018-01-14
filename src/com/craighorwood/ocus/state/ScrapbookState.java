package com.craighorwood.ocus.state;
import java.awt.Graphics;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import com.craighorwood.ocus.*;
public class ScrapbookState extends ExtraState
{
	private BufferedImage[] images = new BufferedImage[16];
	private StateButton openPhotoButton;
	private String photoText;
	private boolean photoOpen = false;
	private int page = -1, scrollAmount = 0;
	public ScrapbookState(ExtrasState parent)
	{
		super(parent);
		openPhotoButton = new StateButton(this, 96, 252, "OPEN PHOTO");
		hideMouse = false;
		turnPage(1);
	}
	public void tick(Input input)
	{
		if (input.isKeyPressed(40))
		{
			if ((scrollAmount += 8) > 400) scrollAmount = 400;
		}
		else if (input.isKeyPressed(38))
		{
			if ((scrollAmount -= 8) < 0) scrollAmount = 0;
		}
	}
	public void render(Graphics g)
	{
		fillBackground(g);
		g.translate(0, -scrollAmount);
		switch (page)
		{
		case 0:
			drawString("Dated May 8th, 2015, I wrote the words", g, 8, 32);
			drawString("'DIAMOND GUN 2' in my school diary for", g, 8, 44);
			drawString("the first time, as well as a brief", g, 8, 56);
			drawString("outline of the plot and setting. If", g, 8, 68);
			drawString("you can read the chicken-scratch", g, 8, 80);
			drawString("handwriting, you'll notice that the", g, 8, 92);
			drawString("plot appears to resemble that of a", g, 8, 104);
			drawString("prequel. Probably because Diamond Gun", g, 8, 116);
			drawString("2 was originally a prequel. Go to the", g, 8, 128);
			drawString("next page to find out why it didn't", g, 8, 140);
			drawString("become one.", g, 8, 152);
			break;
		case 1:
			drawString("About a week later, I had decided that", g, 8, 32);
			drawString("Diamond Gun 2 would be a sequel, since", g, 8, 44);
			drawString("I was having way too much trouble", g, 8, 56);
			drawString("setting up the plot, or lack thereof,", g, 8, 68);
			drawString("of the first game. Also in this diary", g, 8, 80);
			drawString("scribble, you may be able to read that", g, 8, 92);
			drawString("at one point, it was considered that", g, 8, 104);
			drawString("you would be able to play as The", g, 8, 116);
			drawString("Adversary, who was intended to wield", g, 8, 128);
			drawString("the six guns from the first game and", g, 8, 140);
			drawString("have slightly different physics.", g, 8, 152);
			break;
		case 2:
			drawString("Some of the cubes you see in the final", g, 8, 32);
			drawString("game had very different effects in the", g, 8, 44);
			drawString("early stages of development. The", g, 8, 56);
			drawString("amethyst cube, for instance, was", g, 8, 68);
			drawString("originally supposed to", g, 8, 80);
			drawString("extend and retract amethyst", g, 8, 92);
			drawString("bridges in one direction only, and the", g, 8, 104);
			drawString("peridot cube was never supposed to", g, 8, 116);
			drawString("explode after being shot.", g, 8, 128);
			drawString("Some guns and cubes were conceived of", g, 8, 152);
			drawString("but never made the final cut, such as", g, 8, 164);
			drawString("the Turquoise Gun and Tiger's Eye Gun.", g, 8, 176);
			drawString("The turquoise cube's effect was to", g, 8, 188);
			drawString("stop time, freezing enemies, obstacles,", g, 8, 200);
			drawString("bosses and everything in place. It was", g, 8, 212);
			drawString("dropped for being too similar to the", g, 8, 224);
			drawString("quartz cube's effect. The Tiger's Eye", g, 8, 236);
			drawString("Gun was supposed to be a secret gun,", g, 8, 248);
			drawString("but was dropped for having no", g, 8, 260);
			drawString("conceivable benefit.", g, 8, 272);
			for (int i = 0; i < 4; i++)
			{
				g.drawImage(Images.sht_blocks[24][0], 232 + (i << 4), 80, null);
			}
			break;
		case 3:
			drawString("Here's a bit of level design I did one", g, 8, 32);
			drawString("Monday of the opening of the game.", g, 8, 44);
			drawString("Originally you were supposed to fall", g, 8, 56);
			drawString("from the top onto some platforms above", g, 8, 68);
			drawString("a huge pit of spikes spanning three", g, 8, 80);
			drawString("rooms. An arrow on the right points", g, 8, 92);
			drawString("you towards where the Silver... I mean,", g, 8, 104);
			drawString("Aluminium Gun, is located. Aluminium", g, 8, 116);
			drawString("was eventually switched out for iron", g, 8, 128);
			drawString("when I found that it wasn't very easy", g, 8, 140);
			drawString("to differentiate one shiny metal from", g, 8, 152);
			drawString("another.", g, 8, 164);
			break;
		case 4:
			drawString("One new addition to Diamond Gun 2 is", g, 8, 32);
			drawString("that of a health system. In the first", g, 8, 44);
			drawString("game, it was two hits and you're dead,", g, 8, 56);
			drawString("with no way to tell how many hits you", g, 8, 68);
			drawString("had left. In this game, you have a", g, 8, 80);
			drawString("health bar, with gems and armour", g, 8, 92);
			drawString("scattered across the game world to", g, 8, 104);
			drawString("beef it up. Armour was always the same,", g, 8, 116);
			drawString("allowing you to take more damage,", g, 8, 128);
			drawString("although the amount of extra health", g, 8, 140);
			drawString("certain armour types gave you changed", g, 8, 152);
			drawString("throughout development.", g, 8, 164);
			drawString("Gems had a very different function", g, 8, 188);
			drawString("to begin with. They were originally", g, 8, 200);
			drawString("supposed to be like Energy Tanks from", g, 8, 212);
			drawString("Super Metroid, i.e. when you ran out", g, 8, 224);
			drawString("of health, you would lose one gem and", g, 8, 236);
			drawString("your health would be refilled. When", g, 8, 248);
			drawString("you ran out of health and ran out of", g, 8, 260);
			drawString("gems, you would die. I thought that", g, 8, 272);
			drawString("this would make the game too easy, so", g, 8, 284);
			drawString("the function of gems was changed to", g, 8, 296);
			drawString("what they are now, acting sort of like", g, 8, 308);
			drawString("Heart Containers from the Zelda series.", g, 8, 320);
			break;
		case 5:
			drawString("There's a lot going on in this photo,", g, 8, 32);
			drawString("and it's all in red ink because my", g, 8, 44);
			drawString("trusty black pen ran dry. Anywho, here", g, 8, 56);
			drawString("you can see the first drawings of the", g, 8, 68);
			drawString("HUD, some miscellaneous level design,", g, 8, 80);
			drawString("and the plan to include water and", g, 8, 92);
			drawString("water physics in the game. Also,", g, 8, 104);
			drawString("there's some music homework in there", g, 8, 116);
			drawString("somewhere. Can you find it?", g, 8, 128);
			break;
		case 6:
			drawString("There are a number of enemies that", g, 8, 32);
			drawString("never made it into the final game.", g, 8, 44);
			drawString("Let's start with some enemies that", g, 8, 56);
			drawString("did actually make it, but had their", g, 8, 68);
			drawString("names changed. Coptop was originally", g, 8, 80);
			drawString("going to be called 'Gimpter' or", g, 8, 92);
			drawString("'Helipter', Osmosis was going to", g, 8, 104);
			drawString("be called 'Piedmont' or 'Meeba', ", g, 8, 116);
			drawString("Demidisc was going to be called", g, 8, 128);
			drawString("'RotoDisc', and Echoplasm was going to", g, 8, 140);
			drawString("be called 'Echoer'.", g, 8, 152);
			drawString("Finally, the enemies who never were.", g, 8, 176);
			drawString("At one point, there was to be an enemy", g, 8, 188);
			drawString("called 'Demidile' that lived in the", g, 8, 200);
			drawString("lava, following you and leaping out", g, 8, 212);
			drawString("when the time was right. After you got", g, 8, 224);
			drawString("the Topaz Gun, you were supposed to", g, 8, 236);
			drawString("meet an enemy called 'Cischarge', who", g, 8, 248);
			drawString("would have looked something like", g, 48, 260);
			drawString("this and would have paralysed", g, 48, 272);
			drawString("you on contact. Finally, there was an", g, 8, 284);
			drawString("enemy that was never given a name, but", g, 8, 296);
			drawString("was to behave something like the Boo", g, 8, 308);
			drawString("enemy from Mario, drifting towards you", g, 8, 320);
			drawString("while you looked away, and would have", g, 8, 332);
			drawString("been very useful with the Quartz Gun.", g, 8, 344);
			g.drawImage(Images.sht_enemies[0][0], 272, 110, null);
			g.drawImage(Images.sht_enemies[2][1], 272, 97, null);
			g.drawImage(Images.sht_enemies[2][5], 16, 260, null);
			break;
		case 7:
			drawString("These are some interesting first", g, 8, 32);
			drawString("sketches of many bosses. K'iinam was", g, 8, 44);
			drawString("always the same, and so was Agni Sarpa,", g, 8, 56);
			drawString("but Cytosis, it seems, was meant to be", g, 8, 68);
			drawString("a bit more wiggly than he ended up.", g, 8, 80);
			drawString("Voltere was also originally meant to", g, 8, 92);
			drawString("be called 'Volta', and Erebus was", g, 8, 104);
			drawString("supposed to be fought in the dark,", g, 8, 116);
			drawString("never to show his face or any other", g, 8, 128);
			drawString("part of his body. Finally, Miz Khan", g, 8, 140);
			drawString("was supposed to have spikes on his", g, 8, 152);
			drawString("back, but I'm no artist, so I decided", g, 8, 164);
			drawString("to stick to what I'm good at.", g, 8, 176);
			break;
		case 8:
			drawString("Eventually, I took to designing levels", g, 8, 32);
			drawString("in the grid paper at the back of my", g, 8, 44);
			drawString("school diary, using any of my free", g, 8, 56);
			drawString("time to do so because level design is", g, 8, 68);
			drawString("really, really hard and stressful. You", g, 8, 80);
			drawString("know, because you want it to be good.", g, 8, 92);
			drawString("Anyway, can you tell which rooms these", g, 8, 104);
			drawString("are? If so, can you spot any", g, 8, 116);
			drawString("differences between these sketches and", g, 8, 128);
			drawString("the final designs?", g, 8, 140);
			break;
		case 9:
			drawString("While I was programming Agni Sarpa, I", g, 8, 32);
			drawString("originally wanted him to change his", g, 8, 44);
			drawString("attack pattern towards the end of the", g, 8, 56);
			drawString("fight, jumping out of the lava and", g, 8, 68);
			drawString("flying around. I decided against this", g, 8, 80);
			drawString("when I tried out the fight without", g, 8, 92);
			drawString("this extra attack pattern and found", g, 8, 104);
			drawString("that I was already finding it hard", g, 8, 116);
			drawString("enough.", g, 8, 128);
			drawString("Voltere was originally meant to be a", g, 8, 152);
			drawString("completely different entity called", g, 8, 164);
			drawString("'Fuse Box' that I never decided on an", g, 8, 176);
			drawString("appearance for. In the final Voltere", g, 8, 188);
			drawString("fight, you use the topaz cubes to make", g, 8, 200);
			drawString("your way up to the platform above the", g, 8, 212);
			drawString("room and land some hits. Originally,", g, 8, 224);
			drawString("you were supposed to use the sparks", g, 8, 236);
			drawString("from the topaz cubes as your only", g, 8, 248);
			drawString("means of hurting Voltere, but this was", g, 8, 260);
			drawString("changed for being far too slow and", g, 8, 272);
			drawString("frustrating, given how often Voltere", g, 8, 284);
			drawString("moves around.", g, 8, 296);
			drawString("In the fight with Khan near the end of", g, 8, 320);
			drawString("the game, Khan was supposed to create", g, 8, 332);
			drawString("a random cube puzzle that you would", g, 8, 344);
			drawString("need to solve to get to the top of the", g, 8, 356);
			drawString("room and shoot the diamond cube. After", g, 8, 368);
			drawString("taking a certain amount of damage,", g, 8, 380);
			drawString("Khan would destroy the puzzle and", g, 8, 392);
			drawString("create a new one. This was changed", g, 8, 404);
			drawString("because it was difficult to avoid", g, 8, 416);
			drawString("projectiles and switch between many", g, 8, 428);
			drawString("different guns at the same time.", g, 8, 440);
			drawString("Finally, the final boss fight was", g, 8, 464);
			drawString("originally supposed to take place", g, 8, 476);
			drawString("across five rooms that you would run", g, 8, 488);
			drawString("between. Since it would take too long", g, 8, 500);
			drawString("to cross these rooms, the number of", g, 8, 512);
			drawString("rooms was changed to three, then", g, 8, 524);
			drawString("finally to one when the boss's attacks", g, 8, 536);
			drawString("turned out to not work so well when", g, 8, 548);
			drawString("crossing between rooms. If you're", g, 8, 560);
			drawString("reading this, you may not even have", g, 8, 572);
			drawString("fought the final boss yet, so I won't", g, 8, 584);
			drawString("spoil anything more!", g, 8, 596);
			break;
		}
		g.translate(0, scrollAmount);
		fillHud(g);
		g.fillRect(0, 0, 320, 24);
		drawString("SCRAPBOOK", g, 124, 8);
		g.drawImage(Images.sht_blocks[84][0], 8, 8, null);
		if (page > 0) g.drawImage(Images.sht_blocks[84][0], 8, 252, null);
		if (page < 9) g.drawImage(Images.sht_blocks[83][0], 296, 252, null);
		if (isPhotoPage()) drawString(photoText, g, 160 - (photoText.length() << 2), 252);
		super.render(g);
	}
	protected void buttonPressed(int id)
	{
		switch (id)
		{
		case 0:
			if (!photoOpen)
			{
				Sound.playSound("save");
				JFrame frame = new JFrame();
				frame.setSize(images[page].getWidth(), images[page].getHeight());
				javax.swing.JPanel panel = new javax.swing.JPanel()
				{
					private static final long serialVersionUID = 1L;
					public void paintComponent(Graphics g)
					{
						super.paintComponent(g);
						g.drawImage(images[page], 0, 0, null);
					}
				};
				frame.setContentPane(panel);
				frame.setResizable(false);
				frame.setLocationRelativeTo(null);
				frame.addWindowListener(new java.awt.event.WindowListener()
				{
					public void windowOpened(WindowEvent we)
					{
					}
					public void windowClosing(WindowEvent we)
					{
						photoOpen = false;
					}
					public void windowDeactivated(WindowEvent we)
					{
					}
					public void windowClosed(WindowEvent we)
					{
					}
					public void windowDeiconified(WindowEvent we)
					{
					}
					public void windowActivated(WindowEvent we)
					{
					}
					public void windowIconified(WindowEvent we)
					{
					}
				});
				frame.setVisible(true);
				photoOpen = true;
			}
			break;
		}
	}
	private int oldMouse = -1;
	public void mousePressed(int x, int y)
	{
		if (y > 32 && y < 240) oldMouse = y;
		super.mousePressed(x, y);
	}
	public void mouseMoved(int x, int y)
	{
		if (oldMouse > 0)
		{
			scrollAmount -= (y - oldMouse);
			if (scrollAmount < 0) scrollAmount = 0;
			else if (scrollAmount > 400) scrollAmount = 400;
			oldMouse = y;
		}
		super.mouseMoved(x, y);
	}
	public void mouseReleased(int x, int y)
	{
		if (x < 24 && y < 24) setState(parent);
		else if (y > 240)
		{
			if (x < 24) turnPage(-1);
			else if (x > 296) turnPage(1);
		}
		if (oldMouse > 0) oldMouse = -1;
		super.mouseReleased(x, y);
	}
	private void turnPage(int delta)
	{
		page += delta;
		if (page < 0)
		{
			page = 0;
			return;
		}
		if (page >= 10)
		{
			page = 9;
			return;
		}
		buttons.clear();
		scrollAmount = 0;
		if (isPhotoPage())
		{
			if (images[page] == null)
			{
				photoText = "Loading photo...";
				new Thread()
				{
					public void run()
					{
						try
						{
							images[page] = ImageIO.read(ScrapbookState.class.getResource("/scrap/scrapbook" + page + ".jpg"));
							buttons.add(openPhotoButton);
						}
						catch (Exception e)
						{
							photoText = "Failed to load photo.";
							e.printStackTrace();
						}
					}
				}.start();
			}
			else buttons.add(openPhotoButton);
		}
	}
	private boolean isPhotoPage()
	{
		return page == 0 || page == 1 || page == 3 || page == 5 || page == 7 || page == 8;
	}
}