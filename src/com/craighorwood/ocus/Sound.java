package com.craighorwood.ocus;
import java.net.URL;
import java.util.HashMap;
import paulscode.sound.*;
import paulscode.sound.codecs.CodecWav;
import paulscode.sound.libraries.*;
public class Sound
{
	private static java.util.Map<String, Sound> ALL_SOUNDS = new HashMap<String, Sound>();
	private static java.util.List<String> PLAYING_SOUNDS = new java.util.ArrayList<String>();
	private static String CURRENT_AMBIENT, NEXT_AMBIENT;
	private static SoundSystem soundSystem;
	private String[] sourcenames;
	private int p = 0;
	public Sound(int count)
	{
		if (count > 0) sourcenames = new String[count];
	}
	private static void load(OcusMain main, String key, int count)
	{
		URL url = Sound.class.getResource("/snd/" + key + ".wav");
		Sound sound = new Sound(count);
		for (int i = 0; i < count; i++)
		{
			sound.sourcenames[i] = key + i;
			soundSystem.newSource(false, sound.sourcenames[i], url, key + ".wav", false, 0.0f, 0.0f, 0.0f, 0, 0.0f);
		}
		if (count == 0) soundSystem.newSource(false, key, url, key + ".wav", true, 0.0f, 0.0f, 0.0f, 0, 0.0f);
		ALL_SOUNDS.put(key, sound);
		main.redraw();
	}
	public static void setAmbient(String sourcename)
	{
		NEXT_AMBIENT = sourcename;
	}
	public static void playAmbient()
	{
		if (NEXT_AMBIENT != null && NEXT_AMBIENT != CURRENT_AMBIENT)
		{
			CURRENT_AMBIENT = NEXT_AMBIENT;
			soundSystem.play(CURRENT_AMBIENT);
		}
		else if (NEXT_AMBIENT == null) stopAmbient();
	}
	public static void resetAmbient()
	{
		NEXT_AMBIENT = null;
	}
	private static void pauseAmbient()
	{
		if (CURRENT_AMBIENT != null) soundSystem.pause(CURRENT_AMBIENT);
	}
	private static void resumeAmbient()
	{
		if (CURRENT_AMBIENT != null) soundSystem.play(CURRENT_AMBIENT);
	}
	private static void stopAmbient()
	{
		if (CURRENT_AMBIENT != null)
		{
			soundSystem.stop(CURRENT_AMBIENT);
			CURRENT_AMBIENT = null;
		}
	}
	public static void playSound(String key)
	{
		Sound sound = ALL_SOUNDS.get(key);
		if (sound != null)
		{
			String sourcename = sound.sourcenames[(sound.p++) % sound.sourcenames.length];
			if (!PLAYING_SOUNDS.contains(sourcename)) PLAYING_SOUNDS.add(sourcename);
			else soundSystem.stop(sourcename);
			soundSystem.play(sourcename);
		}
	}
	private static void pauseSound(String sourcename)
	{
		soundSystem.pause(sourcename);
	}
	private static void resumeSound(String sourcename)
	{
		soundSystem.play(sourcename);
	}
	public static void stopAllSounds()
	{
		stopAmbient();
		for (int i = 0; i < PLAYING_SOUNDS.size(); i++)
		{
			soundSystem.stop(PLAYING_SOUNDS.get(i));
		}
		PLAYING_SOUNDS.clear();
	}
	public static void pauseAllSounds()
	{
		pauseAmbient();
		for (int i = 0; i < PLAYING_SOUNDS.size(); i++)
		{
			String sourcename = PLAYING_SOUNDS.get(i);
			if (soundSystem.playing(sourcename)) pauseSound(sourcename);
			else PLAYING_SOUNDS.remove(i--);
		}
	}
	public static void resumeAllSounds()
	{
		resumeAmbient();
		for (int i = 0; i < PLAYING_SOUNDS.size(); i++)
		{
			resumeSound(PLAYING_SOUNDS.get(i));
		}
	}
	public static void init(OcusMain main)
	{
		try
		{
			SoundSystemConfig.addLibrary(LibraryJOAL.class);
			SoundSystemConfig.addLibrary(LibraryJavaSound.class);
			SoundSystemConfig.setCodec("wav", CodecWav.class);
			if (SoundSystem.libraryCompatible(LibraryJOAL.class)) soundSystem = new SoundSystem(LibraryJOAL.class);
			else if (SoundSystem.libraryCompatible(LibraryJavaSound.class)) soundSystem = new SoundSystem(LibraryJavaSound.class);
			else soundSystem = new SoundSystem();
		}
		catch (SoundSystemException sse)
		{
			sse.printStackTrace();
		}
		load(main, "absorb", 1);
		load(main, "bang", 4);
		load(main, "beam", 1);
		load(main, "bossdeath", 1);
		load(main, "bossdoor", 1);
		load(main, "bosshurt", 4);
		load(main, "bubbles", 3);
		load(main, "chant0", 1);
		load(main, "chant1", 1);
		load(main, "chant2", 1);
		load(main, "chatter0", 1);
		load(main, "chatter1", 1);
		load(main, "death", 1);
		load(main, "dink", 4);
		load(main, "electric", 3);
		load(main, "electricfail", 3);
		load(main, "electrocast", 2);
		load(main, "electrocute", 2);
		load(main, "enemyjump", 1);
		load(main, "error", 2);
		load(main, "explosion", 6);
		load(main, "fiery", 0);
		load(main, "froosh", 2);
		load(main, "getarmor", 4);
		load(main, "getgem", 4);
		load(main, "getgun", 4);
		load(main, "gethealth", 4);
		load(main, "gravity", 1);
		load(main, "gunlord0", 4);
		load(main, "gunlord1", 4);
		load(main, "hurt", 1);
		load(main, "jump", 1);
		load(main, "laser", 6);
		load(main, "laugh", 1);
		load(main, "lavasplash", 8);
		load(main, "mizkhandeath", 1);
		load(main, "mizkhanroar", 1);
		load(main, "mizkhanshot", 6);
		load(main, "motors", 0);
		load(main, "onyxcube", 1);
		load(main, "opening", 1);
		load(main, "pop", 2);
		load(main, "quartz", 3);
		load(main, "rev", 2);
		load(main, "roar", 1);
		load(main, "rumble", 1);
		load(main, "rumbledeath", 1);
		load(main, "save", 1);
		load(main, "scoreup", 1);
		load(main, "scream", 1);
		load(main, "shoot_enemy", 6);
		load(main, "shoot_player", 6);
		load(main, "splash", 2);
		load(main, "spring", 2);
		load(main, "step", 3);
		load(main, "stun", 1);
		load(main, "success", 1);
		load(main, "swash", 3);
		load(main, "swash_big", 1);
		load(main, "swim", 1);
		load(main, "switchgun", 2);
		load(main, "thud", 1);
		load(main, "thunder", 1);
		load(main, "transform_big", 1);
		load(main, "transform", 1);
		load(main, "turn", 2);
		load(main, "underwater", 0);
		load(main, "waterburst", 1);
		load(main, "waterfall", 0);
	}
	public static void close()
	{
		soundSystem.cleanup();
	}
}