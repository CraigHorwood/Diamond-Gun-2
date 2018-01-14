package com.craighorwood.ocus;
import javax.sound.sampled.*;
public class Music
{
	private String fileName;
	private Clip clip;
	private int loopStart, loopEnd;
	public Music(OcusMain main, String fileName, int loopStart, int loopEnd)
	{
		this.fileName = fileName;
		this.loopStart = loopStart;
		this.loopEnd = loopEnd;
		main.redraw();
	}
	public void load(final boolean play)
	{
		new Thread()
		{
			public void run()
			{
				try
				{
					AudioInputStream ais = AudioSystem.getAudioInputStream(Music.class.getResourceAsStream(fileName));
					AudioInputStream din = null;
					if (ais != null)
					{
						AudioFormat baseFormat = ais.getFormat();
						AudioFormat playbackFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
						din = AudioSystem.getAudioInputStream(playbackFormat, ais);
						clip = AudioSystem.getClip();
						clip.open(din);
						if (loopEnd != 0) clip.setLoopPoints(loopStart, loopEnd);
						din.close();
					}
					if (play) play();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				catch (OutOfMemoryError oome)
				{
					oome.printStackTrace();
				}
			}
		}.start();
	}
	public void unload()
	{
		clip.close();
		clip = null;
	}
	public void play()
	{
		clip.stop();
		clip.setFramePosition(0);
		if (loopEnd != 0) clip.loop(Clip.LOOP_CONTINUOUSLY);
		else clip.start();
	}
	public void pause()
	{
		clip.stop();
	}
	public void resume()
	{
		if (loopEnd != 0) clip.loop(Clip.LOOP_CONTINUOUSLY);
		else clip.start();
	}
	public void stop()
	{
		clip.stop();
	}
	private static Music[] ALL_SONGS;
	private static Music CURRENT_SONG, LAST_SONG;
	public static void init(OcusMain main)
	{
		ALL_SONGS = new Music[10];
		ALL_SONGS[0] = new Music(main, "/mus/mus_t1.ogg", 0, -1);
		ALL_SONGS[1] = new Music(main, "/mus/mus_t2.ogg", 0, -1);
		ALL_SONGS[2] = new Music(main, "/mus/mus_t3.ogg", 0, -1);
		ALL_SONGS[3] = new Music(main, "/mus/mus_t4.ogg", 0, -1);
		ALL_SONGS[4] = new Music(main, "/mus/mus_t5.ogg", 0, -1);
		ALL_SONGS[5] = new Music(main, "/mus/mus_t6.ogg", 0, -1);
		ALL_SONGS[6] = new Music(main, "/mus/mus_t7.ogg", 0, -1);
		ALL_SONGS[7] = new Music(main, "/mus/mus_t8.ogg", 1722326, -1);
		ALL_SONGS[8] = new Music(main, "/mus/mus_t9.ogg", 0, -1);
		ALL_SONGS[9] = new Music(main, "/mus/mus_ta.ogg", 0, 0);
	}
	public static void playMusic(int id)
	{
		if (ALL_SONGS[id] != CURRENT_SONG)
		{
			if (CURRENT_SONG != null) CURRENT_SONG.stop();
			Music nextSong = ALL_SONGS[id];
			if (nextSong.clip != null) nextSong.play();
			else
			{
				if (LAST_SONG != null) LAST_SONG.unload();
				nextSong.load(true);
				if (id == 7)
				{
					ALL_SONGS[8].load(false);
					ALL_SONGS[9].load(false);
				}
			}
			LAST_SONG = CURRENT_SONG;
			CURRENT_SONG = nextSong;
		}
	}
	public static void pauseMusic()
	{
		if (CURRENT_SONG != null) CURRENT_SONG.pause();
	}
	public static void resumeMusic()
	{
		if (CURRENT_SONG != null) CURRENT_SONG.resume();
	}
	public static void stopMusic()
	{
		if (CURRENT_SONG != null)
		{
			CURRENT_SONG.stop();
			CURRENT_SONG = null;
		}
	}
	public static void unloadMusic(int id)
	{
		if (ALL_SONGS[id].clip != null) ALL_SONGS[id].unload();
	}
	public static void close()
	{
		if (CURRENT_SONG != null) CURRENT_SONG.stop();
		for (int i = 0; i < ALL_SONGS.length; i++)
		{
			Music.unloadMusic(i);
		}
	}
}