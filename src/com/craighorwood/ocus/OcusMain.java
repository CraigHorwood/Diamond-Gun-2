package com.craighorwood.ocus;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.craighorwood.ocus.state.*;
public class OcusMain extends Canvas implements Runnable, MouseListener, MouseMotionListener
{
	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 320;
	private static final int HEIGHT = 280;
	public static final double VERSION = 1.01;
	private static int SCALE;
	private boolean running = false;
	private Thread thread;
	private State state;
	private Input input = new Input();
	private Cursor defaultCursor, emptyCursor;
	public OcusMain()
	{
		Dimension size = new Dimension(WIDTH * SCALE, HEIGHT * SCALE);
		setSize(size);
		setPreferredSize(size);
		setMaximumSize(size);
		setMinimumSize(size);
		addKeyListener(input);
		addMouseListener(this);
		addMouseMotionListener(this);
		addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent fe)
			{
			}
			public void focusLost(FocusEvent fe)
			{
				input.releaseAll();
			}
		});
		defaultCursor = getCursor();
		emptyCursor = Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "empty");
	}
	public void start()
	{
		if (running) return;
		running = true;
		thread = new Thread(this);
		thread.setName("DiamondGun2-Main-Thread");
		thread.start();
	}
	public void stop()
	{
		if (!running) return;
		running = false;
		Sound.close();
		Music.close();
		try
		{
			thread.join();
		}
		catch (InterruptedException ie)
		{
			ie.printStackTrace();
		}
	}
	private boolean started = false;
	private static Image SPLASH_IMAGE;
	public void redraw()
	{
		Constants.STUFF_LOADED++;
		repaint();
	}
	public void paint(Graphics g)
	{
		if (started) return;
		else if (SPLASH_IMAGE != null)
		{
			g.drawImage(SPLASH_IMAGE, 0, 0, null);
			g.setColor(Constants.LOGO_COLOR);
			g.fillRect(0, HEIGHT * SCALE - WIDTH * SCALE / 10, (int) (WIDTH * SCALE * (Constants.STUFF_LOADED / 109.0)), WIDTH * SCALE / 10);
		}
	}
	public void run()
	{
		requestFocus();
		Constants.init();
		Images.init(this);
		Sound.init(this);
		Music.init(this);
		setState(new TitleState());
		Image image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		try
		{
			Thread.sleep(500);
		}
		catch (InterruptedException ie)
		{
			ie.printStackTrace();
		}
		long then = System.nanoTime();
		double unprocessed = 0;
		double nanoseconds = 1000000000.0 / 60.0;
		while (running)
		{
			long now = System.nanoTime();
			unprocessed += (now - then) / nanoseconds;
			then = now;
			while (unprocessed >= 1.0)
			{
				state.tick(input);
				input.tick();
				unprocessed -= 1.0;
			}
			Graphics g = image.getGraphics();
			state.render(g);
			g.dispose();
			try
			{
				started = true;
				g = getGraphics();
				g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, 0, 0, WIDTH, HEIGHT, null);
				g.dispose();
			}
			catch (Throwable t)
			{
			}
			try
			{
				Thread.sleep(1);
			}
			catch (InterruptedException ie)
			{
				ie.printStackTrace();
			}
		}
	}
	public void setState(State newState)
	{
		state = newState;
		state.init(this);
		if (state.hideMouse) setCursor(emptyCursor);
		else setCursor(defaultCursor);
	}
	public void mousePressed(MouseEvent me)
	{
		if (state != null) state.mousePressed(me.getX() / SCALE, me.getY() / SCALE);
	}
	public void mouseReleased(MouseEvent me)
	{
		if (state != null) state.mouseReleased(me.getX() / SCALE, me.getY() / SCALE);
	}
	public void mouseClicked(MouseEvent me)
	{
	}
	public void mouseEntered(MouseEvent me)
	{
	}
	public void mouseExited(MouseEvent me)
	{
	}
	public void mouseMoved(MouseEvent me)
	{
		if (state != null) state.mouseMoved(me.getX() / SCALE, me.getY() / SCALE);
	}
	public void mouseDragged(MouseEvent me)
	{
		mouseMoved(me);
	}
	public static void main(String[] args)
	{
		SCALE = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 640);
		if (SCALE < 2) SCALE = 2;
		BufferedImage icon = null;
		try
		{
			SPLASH_IMAGE = ImageIO.read(OcusMain.class.getResource("/img/splash.png"));
			SPLASH_IMAGE = SPLASH_IMAGE.getScaledInstance(WIDTH * SCALE, HEIGHT * SCALE, BufferedImage.SCALE_AREA_AVERAGING);
			icon = ImageIO.read(OcusMain.class.getResource("/img/icon.png"));
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
		JFrame frame = new JFrame("Diamond Gun 2 (version " + VERSION + ")");
		final OcusMain main = new OcusMain();
		frame.setLayout(new BorderLayout());
		frame.add(main, BorderLayout.CENTER);
		frame.setResizable(false);
		frame.addWindowListener(new WindowListener()
		{
			public void windowOpened(WindowEvent we)
			{
			}
			public void windowClosing(WindowEvent we)
			{
				main.stop();
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
		frame.pack();
		if (icon != null) frame.setIconImage(icon);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		main.start();
	}
}