package com.craighorwood.ocus.sprite;
import java.awt.Graphics;
import com.craighorwood.ocus.*;
public class Player extends Sprite
{
	private static Sprite DAMAGE_PATSY = new Sprite();
	public int playerId = 0;
	public boolean enabled = true;
	public boolean climbing = false, climbUp = false, climbDown = false;
	public boolean bouncing = false;
	public boolean inWater = false;
	public boolean carryingBlock = false;
	public int gunLevel, equippedGun = 0, lastArmor = -1;
	public int gems = 0, maxGems = 0;
	public int onConveyor = 0;
	public double health = 1, breath = 1, resistance = 1;
	public int frame = 0, dir = 1;
	public int invincibleTime = 0;
	public Player(double x, double y, int playerId)
	{
		this.x = x;
		this.y = y;
		this.playerId = playerId;
		h = 22;
	}
	private int walkTime = 0;
	public void tick(boolean left, boolean right, boolean up, boolean down, boolean a, boolean b, boolean gun)
	{
		if (!enabled) left = right = up = down = a = b = gun = false;
		if (invincibleTime > 0) invincibleTime--;
		if (inWater && Constants.ITEM_FLAG < 2)
		{
			if (breath > 0) breath -= 0.0015;
			else
			{
				DAMAGE_PATSY.power = resistance / 2.0;
				damage(DAMAGE_PATSY);
			}
		}
		if (level.hot && Constants.ITEM_FLAG == 0)
		{
			DAMAGE_PATSY.power = resistance / 4.0;
			damage(DAMAGE_PATSY);
		}
		byte standing = level.getBlock(xSlot, ySlot);
		if (standing == 4 && !inWater)
		{
			Sound.playSound("splash");
			inWater = true;
			for (int i = 0; i < 6; i++)
			{
				level.addSprite(new Particle(5, (int) (x + 8), (int) (y + 12), (i / 3.0 - 1) * 3.0 + 1, -ya / 3.0));
			}
			ya = 0;
		}
		else if (standing == 0 && inWater)
		{
			ya = -8;
			breath = 1;
			inWater = false;
		}
		if (!climbing)
		{
			boolean walk = false;
			if (left || right)
			{
				walk = true;
				walkTime++;
				if (left)
				{
					xa -= (inWater ? 0.2 : 2) * (bouncing ? 2 : 1);
					dir = -1;
				}
				if (right)
				{
					xa += (inWater ? 0.2 : 2) * (bouncing ? 2 : 1);
					dir = 1;
				}
			}
			if (walkTime > 7) walkTime = 0;
			if (walk && walkTime < 2) frame = 1;
			else frame = 0;
			if (a && (onGround || inWater))
			{
				if (inWater) Sound.playSound("swim");
				else Sound.playSound("jump");
				ya = (inWater ? -2 : -12) * (playerId == 3 && !inWater ? -1 : 1);
			}
			if (b && gunLevel > 0)
			{
				if (!carryingBlock)
				{
					Sound.playSound("shoot_player");
					level.addSprite(new Bullet(x + 4, y + 12, dir * 6, 0, equippedGun, this));
				}
				else
				{
					level.explode(xSlot + dir, ySlot);
					carryingBlock = false;
				}
			}
		}
		else
		{
			if (a || !climbUp || !climbDown)
			{
				climbing = false;
				if (!climbUp) y -= 16;
			}
		}
		if (climbUp && up)
		{
			climbing = true;
			ya -= 2;
		}
		else if (climbDown && down)
		{
			if (!climbing && level.getBlock(xSlot, ySlot + 1) == 2) y += 16;
			climbing = true;
			ya += 2;
		}
		climbUp = false;
		climbDown = false;
		onConveyor = 0;
		move(xa, ya);
		if (onGround) bouncing = false;
		if (!climbing && ya < 16) ya += (inWater ? 0.1 : 1) * (playerId == 3 ? -1 : 1);
		xa *= inWater ? 0.8 : 0;
		if (onConveyor != 0) xa = onConveyor;
		if (climbing) ya = 0;
		if (gun && gunLevel > 1)
		{
			int oldEquippedGun = equippedGun;
			do
			{
				if (++equippedGun == 32) equippedGun = 0;
			}
			while ((gunLevel & (1 << equippedGun)) == 0);
			if (equippedGun != oldEquippedGun) Sound.playSound("switchgun");
		}
		if (y < -12) level.transition(0, -1);
		else if (y > 216) level.transition(0, 1);
		else if (x < 8) level.transition(-1, 0);
		else if (x > 312) level.transition(1, 0);
	}
	public void render(Graphics g)
	{
		if ((invincibleTime & 3) == 0)
		{
			if (!climbing) g.drawImage(Images.sht_player[frame + (dir == 1 ? 0 : 2)][playerId], (int) x, (int) y, null);
			else g.drawImage(Images.sht_player[4][playerId], (int) x, (int) y, null);
			if (lastArmor >= 0)
			{
				g.setColor(Constants.GUN_COLORS[lastArmor + 1]);
				g.fillRect((int) (x + 6), (int) (y + (playerId == 3 ? 2 : 12)), 4, 10);
			}
			if (gunLevel > 0)
			{
				g.setColor(Constants.GUN_COLORS[equippedGun + 1]);
				if (!climbing) g.fillRect((int) (dir == 1 ? (x + 8) : (x + 1)), (int) (y + (playerId == 3 ? 6 : 15)), 7, 3);
				else g.fillRect((int) (x + 9), (int) (y + (playerId == 3 ? 6 : 15)), 3, 3);
			}
		}
	}
	public boolean damage(Sprite damager)
	{
		if (damager instanceof Echoplasm)
		{
			enabled = true;
			die();
			return true;
		}
		if (invincibleTime > 0) return false;
		if (damager instanceof Enemy)
		{
			if (x < damager.x + (damager.w >> 1)) xa = -16;
			else xa = 16;
		}
		else if (damager instanceof Fireball || damager instanceof Bullet) move(damager.xa * 2, damager.ya * 2);
		if (gems > 0)
		{
			gems--;
			invincibleTime = 120;
		}
		else
		{
			if ((health -= damager.power / resistance) > 0.001)
			{
				invincibleTime = 120;
			}
			else die();
		}
		if (!removed) Sound.playSound("hurt");
		return true;
	}
	public void hitSpikes()
	{
		DAMAGE_PATSY.power = 1;
		damage(DAMAGE_PATSY);
	}
	public void die()
	{
		if (removed) return;
		Music.stopMusic();
		Sound.playSound("death");
		health = 0;
		super.die();
	}
}