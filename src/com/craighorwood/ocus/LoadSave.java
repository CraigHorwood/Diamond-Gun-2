package com.craighorwood.ocus;
import java.io.*;
import com.craighorwood.ocus.Constants;
import com.craighorwood.ocus.level.Save;
public class LoadSave
{
	public static Save loadGame()
	{
		try
		{
			DataInputStream dis = new DataInputStream(new FileInputStream(Constants.GAME_DIRECTORY + Constants.SAVE_FILENAME));
			int xLevel = dis.readByte();
			int yLevel = dis.readByte();
			int xSpawn = dis.readShort();
			int ySpawn = dis.readShort();
			int gunLevel = dis.readShort();
			int equippedGun = dis.readByte();
			int gems = dis.readInt();
			int armor = dis.readInt();
			int bosses = dis.readByte();
			int cutscenes = dis.readByte();
			byte items = dis.readByte();
			int checksum = (int) (xLevel + yLevel + xSpawn + ySpawn + gunLevel + equippedGun + gems + armor + bosses + cutscenes + items);
			if (~checksum != dis.readInt())
			{
				dis.close();
				return null;
			}
			dis.close();
			Save save = new Save(xLevel, yLevel, xSpawn, ySpawn, gunLevel, equippedGun, gems, armor, bosses, cutscenes, items);
			return save;
		}
		catch (IOException ioe)
		{
			return null;
		}
	}
	public static boolean saveGame(Save save)
	{
		try
		{
			DataOutputStream dos = new DataOutputStream(new FileOutputStream(Constants.GAME_DIRECTORY + Constants.SAVE_FILENAME));
			dos.writeByte((byte) save.xLevel);
			dos.writeByte((byte) save.yLevel);
			dos.writeShort((short) save.xSpawn);
			dos.writeShort((short) save.ySpawn);
			dos.writeShort((short) save.gunLevel);
			dos.writeByte((byte) save.equippedGun);
			dos.writeInt(save.gems);
			dos.writeInt(save.armor);
			dos.writeByte((byte) save.bosses);
			dos.writeByte((byte) save.cutscenes);
			dos.writeByte(save.items);
			int checksum = (int) (save.xLevel + save.yLevel + save.xSpawn + save.ySpawn + save.gunLevel + save.equippedGun + save.gems + save.armor + save.bosses + save.cutscenes + save.items);
			dos.writeInt(~checksum);
			dos.close();
			return true;
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
			return false;
		}
	}
	public static int[] loadControls()
	{
		try
		{
			int[] controls = new int[8];
			DataInputStream dis = new DataInputStream(new FileInputStream(Constants.GAME_DIRECTORY + "/.controls"));
			for (int i = 0; i < controls.length; i++)
			{
				controls[i] = dis.readByte();
			}
			dis.close();
			return controls;
		}
		catch (IOException ioe)
		{
			return null;
		}
	}
	public static void saveControls(int[] controls)
	{
		try
		{
			DataOutputStream dos = new DataOutputStream(new FileOutputStream(Constants.GAME_DIRECTORY + "/.controls"));
			for (int i = 0; i < controls.length; i++)
			{
				dos.writeByte((byte) controls[i]);
			}
			dos.close();
		}
		catch (IOException ioe)
		{
		}
	}
}