package com.craighorwood.ocus.level;
public class Save
{
	public int xLevel, yLevel, xSpawn, ySpawn, gunLevel, equippedGun, gems, armor, bosses, cutscenes;
	public byte items;
	public Save(int xLevel, int yLevel, int xSpawn, int ySpawn, int gunLevel, int equippedGun, int gems, int armor, int bosses, int cutscenes, byte items)
	{
		this.xLevel = xLevel;
		this.yLevel = yLevel;
		this.xSpawn = xSpawn;
		this.ySpawn = ySpawn;
		this.gunLevel = gunLevel;
		this.equippedGun = equippedGun;
		this.gems = gems;
		this.armor = armor;
		this.bosses = bosses;
		this.cutscenes = cutscenes;
		this.items = items;
	}
	public int getLastArmor()
	{
		int index = (int) (Math.log(Integer.highestOneBit(armor)) / Math.log(2));
		int result = index >> 3;
		if (index >= 20)
		{
			result = 3;
			if (index >= 24) result = index - 20;
		}
		return result;
	}
	public double getResistance()
	{
		double result = 1;
		for (int i = 0; i < 32; i++)
		{
			if (((armor >> i) & 1) > 0)
			{
				int armorType = i >> 3;
				if (i >= 20)
				{
					armorType = 3;
					if (i >= 24) armorType = i - 20;
				}
				switch (armorType)
				{
				case 0:
					result += 0.078125;
					break;
				case 1:
					result += 0.15625;
					break;
				case 2:
					result += 0.46875;
					break;
				case 3:
					result += 0.625;
					break;
				case 4:
					result += 1.25;
					break;
				case 5:
					result += 2.5;
					break;
				}
			}
		}
		return result;
	}
	public double getPercentComplete()
	{
		int stuff = 0;
		stuff += Integer.bitCount(gunLevel);
		stuff += Integer.bitCount(gems);
		stuff += Integer.bitCount(armor);
		stuff += bosses;
		stuff += items;
		return stuff / 60.0;
	}
}