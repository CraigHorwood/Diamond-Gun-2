package com.craighorwood.ocus.sprite;
import java.awt.Graphics;
import com.craighorwood.ocus.*;
public class Collectible extends Sprite
{
	private int type, index, armorType;
	public Collectible(double x, double y, byte type, byte index)
	{
		this.x = x;
		this.y = y;
		this.type = type;
		this.index = index - (type << 5);
		armorType = this.index >> 3;
		if (this.index >= 20)
		{
			armorType = 3;
			if (this.index >= 24) armorType = this.index - 20;
		}
		h = 10;
	}
	private int animTime = 0;
	public void tick()
	{
		animTime++;
		if (type < 3)
		{
			int l = level.player.gunLevel;
			if (type == 1) l = Constants.GEMS_COLLECTED;
			else if (type == 2) l = Constants.ARMOR_COLLECTED;
			if ((l & (1 << index)) != 0)
			{
				removed = true;
				return;
			}
		}
		else if (type == 3 && Constants.ITEM_FLAG > index)
		{
			removed = true;
			return;
		}
		java.util.List<Sprite> hits = level.getHits(x, y, w, h);
		for (int i = 0; i < hits.size(); i++)
		{
			Sprite spr = hits.get(i);
			if (spr instanceof Player)
			{
				Player player = (Player) spr;
				if (player == level.player) player.health = 1;
				if (type == 0)
				{
					if (index == 6) Music.playMusic(0);
					Sound.playSound("getgun");
					player.gunLevel |= (1 << index);
					if (player != level.player)
					{
						player.health += 0.5;
						remove();
					}
					else player.equippedGun = index;
				}
				else if (type == 1)
				{
					Sound.playSound("getgem");
					Constants.GEMS_COLLECTED |= (1 << index);
					player.maxGems++;
					player.gems = player.maxGems;
				}
				else if (type == 2)
				{
					Sound.playSound("getarmor");
					Constants.ARMOR_COLLECTED |= (1 << index);
					if (armorType > player.lastArmor) player.lastArmor = armorType;
					switch (armorType)
					{
					case 0:
						player.resistance += 0.078125;
						break;
					case 1:
						player.resistance += 0.15625;
						break;
					case 2:
						player.resistance += 0.46875;
						break;
					case 3:
						player.resistance += 0.625;
						break;
					case 4:
						player.resistance += 1.25;
						break;
					case 5:
						player.resistance += 2.5;
						break;
					}
				}
				else if (type == 3)
				{
					Sound.playSound("getgem");
					Constants.ITEM_FLAG++;
				}
				player.gems = player.maxGems;
				if (type > 0) level.showCollectibleDialog(type == 3 ? type + index : type);
				break;
			}
		}
	}
	public void render(Graphics g)
	{
		g.drawImage(Images.sht_guns[(type == 1 || type == 3) ? ((animTime >> 3) & 3) + (type == 3 ? (index << 2) + 4 : 0) : (type == 0 ? index : armorType)][type == 3 ? 1 : type], (int) x, (int) y, null);
	}
}