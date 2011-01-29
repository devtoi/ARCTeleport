package org.toi.arcteleport;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

import org.bukkit.block.Block;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerItemEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.config.Configuration;

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
	private Configuration cfg = new Configuration(new File("ARCTeleport" + File.separator + "arct.cfg"));
	private Hashtable<String, Boolean> mode = new Hashtable<String, Boolean>();
	
	public tProperties getProps() {
		return props;
	}

	public tPermissions getPerms() {
		return perms;
	}

	public ARCTPListener(ARCTeleport arcTeleport) {
		this.plugin = arcTeleport;
	}
	
	public void loadConfigg()
	{
		cfg.load();
		fallDamage = cfg.getBoolean("arc.fall-damage", true);
        teleDoCost = cfg.getBoolean("arc.teleport-do-cost", false);
        teleCost = cfg.getInt("arc.tele-cost", 1);
        teleporterID = cfg.getInt("arc.tele-block-id", 288);
        blocksToIgnore = new ArrayList<String>(Arrays.asList(cfg.getString("blocks-to-ignore", "6,8,9,10,11,37,38,39,40,50,51,52,55,65,66,69,70,72,75,76,77,78,295,321,323,324,330").split(",")));
		blocksToIgnore.add("0"); // Must be here
	}
	
	public void loadConfig()
	{
		try {
            props.load();
        } catch (IOException e) {
            ARCTeleport.log.warning("[ARCT] Failed to load configuration");
        }
        fallDamage = props.getBoolean("fall-damage", true);
        teleDoCost = props.getBoolean("teleport-do-cost", false);
        teleCost = props.getInt("tele-cost", 1);
        teleporterID = props.getInt("tele-block-id", 288);
        blocksToIgnore = new ArrayList<String>(Arrays.asList(props.getString("blocks-to-ignore", "6,8,9,10,11,37,38,39,40,50,51,52,55,65,66,69,70,72,75,76,77,78,295,321,323,324,330").split(",")));
		blocksToIgnore.add("0"); // Must be here
        props.save();
    	for (Player plr : this.plugin.getServer().getOnlinePlayers())
    	{
			if (!this.mode.containsKey(plr.getName()))
				this.mode.put(plr.getName(), true);
    	}
	}
	
	public void onPlayerJoin(PlayerEvent event)
	{
		Player player = event.getPlayer();
		if (!this.mode.containsKey(player.getName()))
			this.mode.put(player.getName(), true);
	}
	
	public void onPlayerQuit(PlayerEvent event)
	{
		Player player = event.getPlayer();
		if (this.mode.containsKey(player.getName()))
			this.mode.remove(player.getName());
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
					ARCTeleport.log.info(player.getName() + " disabled falldamage");
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
					ARCTeleport.log.info("Teleport cost disabled by: " + player.getName());
				}
				else
					player.sendMessage(arcString + "Teleport cost aleady disabled!");
			}
			else
			{
				player.sendMessage(arcString + "Unknown feature!, use falldamage or teledocost");
			}
			event.setCancelled(true);
        }
		else if(split[0].equalsIgnoreCase("/arce") && perms.canPlayerUseCommand(player.getName(), "/arce"))
        {
			if (split[1].equalsIgnoreCase("falldamage"))
			{
				if (fallDamage == false)
				{
					fallDamage = true;
					player.sendMessage(arcString + "You enabled falldamage!");
					ARCTeleport.log.info(player.getName() + " enabled falldamage");
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
					ARCTeleport.log.info("Teleport cost enabled by: " + player.getName());
				}
				else
					player.sendMessage(arcString + "Teleport cost already enabled!");
			}
			else
			{
				player.sendMessage(arcString + "Unknown feature!, use falldamage or teledocost");
			}
			event.setCancelled(true);
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
						ARCTeleport.log.info(player.getName() + " changed teleportcost to " + split[2]);
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
			event.setCancelled(true);
		}
		else if (split[0].equalsIgnoreCase("/arcm"))
		{
			if (this.mode.containsKey(player.getName()))
			{
				boolean inm = this.mode.get(player.getName());
				this.mode.put(player.getName(), !inm);
				if (inm)
					player.sendMessage(arcString + "You enabled tunneling teleport");
				else
					player.sendMessage(arcString + "You enabled normal teleport");
			}
			else
				this.mode.put(player.getName(), true);
			event.setCancelled(true);
		}
	}
	
	public void onPlayerItem (PlayerItemEvent event)
	{
		Player player = event.getPlayer();
		if (player.getItemInHand().getTypeId() == this.teleporterID)
		{
			this.telePlayer(player, event.getItem());
		}
	}
	
	private boolean telePlayer(Player player, ItemStack inh)
	{
		if (perms.canPlayerUseCommand(player.getName(), "/arct"))
		{
			Location playerLoc = player.getLocation().clone();
			TargetBlock ab = new TargetBlock(player, 300, 0.3, blocksToIgnore);
			Block blk = ab.getTargetBlock();
			
			if (blk != null)
            {
				if (this.mode.get(player.getName()))
				{
	                for(int i = 0; i < 128; i++)
	                {
	                	int yPos = ab.getCurrentBlock().getY() + i;
	                	if (yPos - 2 < 128)
						{
		                    int current = playerLoc.getWorld().getBlockAt(ab.getCurrentBlock().getX(), yPos, ab.getCurrentBlock().getZ()).getTypeId();
		                    int above = playerLoc.getWorld().getBlockAt(ab.getCurrentBlock().getX(), yPos + 1, ab.getCurrentBlock().getZ()).getTypeId();
		                    if ((blocksToIgnore.contains(String.valueOf(current)) && blocksToIgnore.contains(String.valueOf(above))))
		                    {
		                    	playerLoc.setX(ab.getCurrentBlock().getX() + .5);
		                        playerLoc.setY(ab.getCurrentBlock().getY() + i);
		                        playerLoc.setZ(ab.getCurrentBlock().getZ() + .5);
		                        player.teleportTo(playerLoc);
		                        i = 128;
		                        if (teleDoCost)
				                {
		                        	int or = 0;
		                        	if (inh.getAmount() - teleCost <= 0)
		                        	{
		                        		or = (inh.getAmount() - teleCost) * -1;
		                        		player.getInventory().remove(inh);
		                        	}
		                        	else
		                        		inh.setAmount(inh.getAmount() - teleCost);
		                        	while (or > 0)
		                        	{
		                        		if (player.getInventory().contains(inh.getTypeId()))
		                        		{
		                        			player.getInventory().removeItem(new ItemStack(inh.getTypeId(), 1));
		                        		}
		                        		else
		                        			break;
		                        		or--;
		                        	}
			                    }
		                        else
		                            player.teleportTo(playerLoc);
		                    }
	                    }
	                	else
	                		i = 128;
	                }
				}
				else
				{
					for(int i = 0; i < 128; i++)
	                {
						int yPos = ab.getCurrentBlock().getY() - i;
						if (yPos + 1 > 0)
						{
		                    int current = playerLoc.getWorld().getBlockAt(ab.getCurrentBlock().getX(), yPos + 1, ab.getCurrentBlock().getZ()).getTypeId();
		                    int below = playerLoc.getWorld().getBlockAt(ab.getCurrentBlock().getX(), yPos, ab.getCurrentBlock().getZ()).getTypeId();
		                    if ((blocksToIgnore.contains(String.valueOf(current)) && blocksToIgnore.contains(String.valueOf(below))))
		                    {
		                    	for (int o = 0; o < 128; o++)
		                    	{
		                    		int yPosi = ab.getCurrentBlock().getY() - i - o;
		    						if (yPosi + 1 > 0)
		    						{
		    							int abovei = playerLoc.getWorld().getBlockAt(ab.getCurrentBlock().getX(), yPosi + 2, ab.getCurrentBlock().getZ()).getTypeId();
		    		                    int currenti = playerLoc.getWorld().getBlockAt(ab.getCurrentBlock().getX(), yPosi + 1, ab.getCurrentBlock().getZ()).getTypeId();
		    		                    int belowi = playerLoc.getWorld().getBlockAt(ab.getCurrentBlock().getX(), yPosi, ab.getCurrentBlock().getZ()).getTypeId();
		    		                    if (!blocksToIgnore.contains(String.valueOf(belowi)) && blocksToIgnore.contains(String.valueOf(currenti)) && blocksToIgnore.contains(String.valueOf(abovei)))
		    		                    {
		    		                    	playerLoc.setX(ab.getCurrentBlock().getX() + .5);
		    		                        playerLoc.setY(ab.getCurrentBlock().getY() - i - o + 1);
		    		                        playerLoc.setZ(ab.getCurrentBlock().getZ() + .5);
		    		                        player.teleportTo(playerLoc);
		    		                        i = 128;
		    		                        if (teleDoCost)
		    				                {
		    		                        	int or = 0;
		    		                        	if (inh.getAmount() - teleCost <= 0)
		    		                        	{
		    		                        		or = (inh.getAmount() - teleCost) * -1;
		    		                        		player.getInventory().remove(inh);
		    		                        	}
		    		                        	else
		    		                        		inh.setAmount(inh.getAmount() - teleCost);
		    		                        	while (or > 0)
		    		                        	{
		    		                        		if (player.getInventory().contains(inh.getTypeId()))
		    		                        		{
		    		                        			player.getInventory().removeItem(new ItemStack(inh.getTypeId(), 1));
		    		                        		}
		    		                        		else
		    		                        			break;
		    		                        		or--;
		    		                        	}
		    			                    }
		    		                        else
		    		                            player.teleportTo(playerLoc);
		    		                    }
		    						}
		    						else
		    							o = 128;
		                    	}
		                    }
	                    }
						else
							i = 128;
	                }
				}
            }
            return true;
		}
		else
			return false;
	}
}
