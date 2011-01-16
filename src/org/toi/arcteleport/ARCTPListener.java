package org.toi.arcteleport;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.block.Block;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerItemEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.plugin.Plugin;

public class ARCTPListener extends PlayerListener{
	
	private boolean fallDamage = true;
	private boolean teleDoCost = false;
	private int teleCost = 1;
	private ArrayList<String> blocksToIgnore = new ArrayList<String>();
	private int teleporterID = 288;
	private String arcString = ChatColor.AQUA + "[ARCT] " + ChatColor.YELLOW;
	private Plugin plugin;
	private tProperties props = new tProperties("ARCTeleport" + File.separator + "arct.properties");
	private tPermissions perms = new tPermissions("ARCTeleport" + File.separator + "arct.perms");
	
	public tProperties getProps() {
		return props;
	}

	public tPermissions getPerms() {
		return perms;
	}

	public ARCTPListener(ARCTeleport arcTeleport) {
		this.plugin = arcTeleport;
	}
	
	public void loadConfig()
	{
		try {
            props.load();
        } catch (IOException e) {
            System.out.println("[ARCT] Failed to load configuration");
        }
        fallDamage = props.getBoolean("fall-damage", true);
        teleDoCost = props.getBoolean("teleport-do-cost", false);
        teleCost = props.getInt("tele-cost", 1);
        teleporterID = props.getInt("tele-block-id", 288);
        blocksToIgnore = new ArrayList<String>(Arrays.asList(props.getString("blocks-to-ignore", "6,8,9,10,11,37,38,39,40,50,51,52,55,65,66,69,70,72,75,76,77,78,295,321,323,324,330").split(",")));
		blocksToIgnore.add("0"); // Must be here
        props.save();
	}
	
	public void onPlayerCommand(PlayerChatEvent event)
	{
		Player player = event.getPlayer();
		String split[] = event.getMessage().split(" ");

		if(split[0].equalsIgnoreCase("/arcd") && perms.canPlayerUseCommand(player.getName(), "/arcd"))
        {
			if (split[1].equalsIgnoreCase("falldamage"))
			{
				if (fallDamage == true)
				{
					fallDamage = false;
					player.sendMessage(arcString + "You disabled falldamage!");
					System.out.println(player.getName() + " disabled falldamage");
				}
				else
					player.sendMessage(arcString + "Falldamage already disabled!");
			}
			else if (split[1].equalsIgnoreCase("teledocost"))
			{
				if (teleDoCost == true)
				{
					teleDoCost = false;
					player.sendMessage(arcString + "Teleport is now free!");
					System.out.println("Teleport cost disabled by: " + player.getName());
				}
				else
					player.sendMessage(arcString + "Teleport cost aleady disabled!");
			}
			else
			{
				player.sendMessage(arcString + "Unknown feature!, use falldamage or teledocost");
			}
        }
		else if(split[0].equalsIgnoreCase("/arce") && perms.canPlayerUseCommand(player.getName(), "/arce"))
        {
			if (split[1].equalsIgnoreCase("falldamage"))
			{
				if (fallDamage == false)
				{
					fallDamage = true;
					player.sendMessage(arcString + "You enabled falldamage!");
					System.out.println(player.getName() + " enabled falldamage");
				}
				else
					player.sendMessage(arcString + "Falldamage already enabled!");
			}
			else if (split[1].equalsIgnoreCase("teledocost"))
			{
				if (teleDoCost == false)
				{
					teleDoCost = true;
					player.sendMessage(arcString + "Teleport now cost " + teleCost + " feathers");
					System.out.println("Teleport cost enabled by: " + player.getName());
				}
				else
					player.sendMessage(arcString + "Teleport cost already enabled!");
			}
			else
			{
				player.sendMessage(arcString + "Unknown feature!, use falldamage or teledocost");
			}
        }
		else if(split[0].equalsIgnoreCase("/arcs") && perms.canPlayerUseCommand(player.getName(), "/arcs"))
		{
			if (split[1].equalsIgnoreCase("telecost"))
			{
				try
				{
					if (0 < new Integer(split[2]).intValue())
					{
						teleCost = new Integer(split[2]).intValue();
						player.sendMessage(arcString + "Teleport now cost " + teleCost + " feathers");
						System.out.println(player.getName() + " changed teleportcost");
						if (teleDoCost == false)
						{
							player.sendMessage("Don't forget to enable teleport cost!");
						}
					}
					else
						player.sendMessage(arcString + "Failed to read amount!");
				}
				catch (Exception ex)
				{
					player.sendMessage(arcString + "Failed to read amount!");
				}
			}
			else
				player.sendMessage(arcString + "Unknown feature!");
		}
	}
	
	/*public boolean onDamage(PluginLoader.DamageType type, BaseEntity attacker, BaseEntity defender, int amount)
	{
		if (enabled)
		{
			if (type == PluginLoader.DamageType.FALL && fallDamage == false)
			{
				return true;
			}
			else
				return false;
		}
		else return true;
	}*/
	
	public void onPlayerItem (PlayerItemEvent event)
	{
		Player player = event.getPlayer();
		if (player.getItemInHand().getTypeId() == this.teleporterID)
		{
			this.telePlayer(player);
		}
	}
	
	private boolean telePlayer(Player player)
	{
		if (perms.canPlayerUseCommand(player.getName(), "/arct"))
		{
			Location playerLoc = player.getLocation().clone();
			TargetBlock ab = new TargetBlock(player, 300, 0.3, blocksToIgnore);
			Block blk = ab.getTargetBlock();
			if (blk != null)
            {
                for(int i = 0; i < 128; i++)
                {
                    int current = playerLoc.getWorld().getBlockAt(ab.getCurrentBlock().getX(), ab.getCurrentBlock().getY() + i, ab.getCurrentBlock().getZ()).getTypeId();
                    int above = playerLoc.getWorld().getBlockAt(ab.getCurrentBlock().getX(), ab.getCurrentBlock().getY() + i + 1, ab.getCurrentBlock().getZ()).getTypeId();
                    if ((blocksToIgnore.contains(String.valueOf(current)) && blocksToIgnore.contains(String.valueOf(above))))
                    {
                    	playerLoc.setX(ab.getCurrentBlock().getX() + .5);
                        playerLoc.setY(ab.getCurrentBlock().getY() + i);
                        playerLoc.setZ(ab.getCurrentBlock().getZ() + .5);
                        player.teleportTo(playerLoc);
                        i = 128;/*
                        if (teleDoCost)
		                {
                        	PlayerInventory inv = player.getInventory();
                        	ItemStack stack = new ItemStack(teleporterID, 64);
                        	if (inv.contains(stack))
                        	{
                        		inv.remove(stack);
	                            player.teleportTo(playerLoc);
                        	}
                    		else
                        		player.sendMessage(arcString + "Not enough teleporting material!");
	                    }
                        else
                            player.teleportTo(playerLoc);*/
                    }
                }
            }
            return true;
		}
		else
			return false;
	}
}
