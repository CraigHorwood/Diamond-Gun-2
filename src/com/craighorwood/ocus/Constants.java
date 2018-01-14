package com.craighorwood.ocus;
import java.awt.Color;
import java.io.*;
public class Constants
{
	public static final Color[] GUN_COLORS = { new Color(255, 255, 255), new Color(127, 127, 127), new Color(255, 216, 0), new Color(255, 0, 0), new Color(0, 128, 0), new Color(0, 0, 255), new Color(0, 255, 255), new Color(141, 114, 114), new Color(177, 109, 73), new Color(178, 0, 255), new Color(0, 162, 232), new Color(63, 63, 63), new Color(255, 127, 39), new Color(255, 255, 127), new Color(255, 174, 201), new Color(167, 252, 0) };
	public static final Color LOGO_COLOR = new Color(255, 204, 0);
	public static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!?#.,'%/ ";
	public static final String[] GUN_NAMES = { "SILVER", "GOLD", "RUBY", "EMERALD", "SAPPHIRE", "DIAMOND", "IRON", "BRONZE", "AMETHYST", "LARIMAR", "ONYX", "GARNET", "TOPAZ", "QUARTZ", "PERIDOT" };
	public static final String[] KEY_NAMES = new String[256];
	public static final String[] CONTROL_NAMES = { "Move left", "Move right", "Climb up", "Climb down", "Jump", "Shoot", "Change gun", "Pause" };
	public static final int DEFAULT_LEFT = 37;
	public static final int DEFAULT_RIGHT = 39;
	public static final int DEFAULT_UP = 38;
	public static final int DEFAULT_DOWN = 40;
	public static final int DEFAULT_A = 90;
	public static final int DEFAULT_B = 88;
	public static final int DEFAULT_GUN = 17;
	public static final int DEFAULT_PAUSE = 27;
	public static File GAME_DIRECTORY;
	public static String SAVE_FILENAME = "/.save";
	public static int STUFF_LOADED = 0;
	public static int GEMS_COLLECTED = 0;
	public static int ARMOR_COLLECTED = 0;
	public static int BOSSES_KILLED = 0;
	public static int CUTSCENES_WATCHED = 0;
	public static byte ITEM_FLAG = 0;
	public static int K_LEFT = DEFAULT_LEFT;
	public static int K_RIGHT = DEFAULT_RIGHT;
	public static int K_UP = DEFAULT_UP;
	public static int K_DOWN = DEFAULT_DOWN;
	public static int K_A = DEFAULT_A;
	public static int K_B = DEFAULT_B;
	public static int K_GUN = DEFAULT_GUN;
	public static int K_PAUSE = DEFAULT_PAUSE;
	public static void init()
	{
		String home = System.getProperty("user.home", ".");
		GAME_DIRECTORY = new File(home, "DiamondGun/DiamondGun2/");
		int os = 4;
		String osName = System.getProperty("os.name").toLowerCase();
		if (osName.contains("win")) os = 0;
		else if (osName.contains("mac")) os = 1;
		else if (osName.contains("solaris") || osName.contains("sunos")) os = 2;
		else if (osName.contains("linux") || osName.contains("unix")) os = 3;
		switch (os)
		{
		case 0:
			String appData = System.getenv("APPDATA");
			if (appData != null) GAME_DIRECTORY = new File(appData, "DiamondGun/DiamondGun2/");
			else GAME_DIRECTORY = new File(home, "DiamondGun/");
			break;
		case 1:
			GAME_DIRECTORY = new File(home, "Library/Application Support/DiamondGun/DiamondGun2/");
			break;
		}
		if (!GAME_DIRECTORY.exists() && !GAME_DIRECTORY.mkdirs())
		{
			throw new RuntimeException("The working directory could not be created: " + GAME_DIRECTORY);
		}
		try
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(Constants.class.getResourceAsStream("/data/keynames.dat")));
			String line = "";
			int i = 0;
			while ((line = br.readLine()) != null)
			{
				KEY_NAMES[i++] = line;
			}
			br.close();
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
	}
}