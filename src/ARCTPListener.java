import java.util.ArrayList;

import org.bukkit.Block;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Player;
import org.bukkit.event.player.PlayerItemEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.plugin.Plugin;

public class ARCTPListener extends PlayerListener{
	
	private boolean fallDamage = true;
	private boolean teleDoCost = false;
	private int teleCost = 1;
	private ArrayList<String> blocksToIgnore = new ArrayList<String>();
	private int teleporterID = 288;
	private String arcString = Color.AQUA + "[ARCT] " + Color.YELLOW;
	private Plugin plugin;
	
	public ARCTPListener(ARCTeleport arcTeleport) {
		this.plugin = arcTeleport;
		blocksToIgnore.add("0"); // Must be here
		blocksToIgnore.add("8");
		blocksToIgnore.add("9");
		//loadConfig();
	}
	
	/*private void loadConfig()
	{
		try {
            properties.load();
        } catch (IOException e) {
            logger.log(Level.WARNING, "Feather teleport: Failed to load configuration: "
                    + e.getMessage());
        }
        fallDamage = properties.getBoolean("fall-damage", true);
        teleDoCost = properties.getBoolean("teleport-do-cost", false);
        teleCost = properties.getInt("tele-cost", 1);
        teleporterID = properties.getInt("tele-block-id", 288);
        blocksToIgnore = Arrays.asList(properties.getString("blocks-to-ignore", "0,6,8,9,10,11,37,38,39,40,50,51,52,55,65,66,69,70,72,75,76,77,78,295,321,323,324,330").split(","));
	}
	
	public void onPlayerCommand(PlayerChatEvent event)
	{
		Player player = event.getPlayer();
		String split[] = event.getMessage().split(" ");

		if(split[0].equalsIgnoreCase("/ftdisable")/* && player.canUseCommand("/ftdisable"))
        {
			if (split[1].equalsIgnoreCase("falldamage"))
			{
				if (fallDamage == true)
				{
					fallDamage = false;
					player.sendMessage("Feather teleport: You disabled falldamage!");
					logger.log(Level.INFO, player.getName() + " disabled falldamage");
				}
				else
					player.sendMessage("Feather teleport: Falldamage already disabled!");
			}
			else if (split[1].equalsIgnoreCase("teledocost"))
			{
				if (teleDoCost == true)
				{
					teleDoCost = false;
					player.sendMessage("Feather teleport: Teleport is now free!");
					logger.log(Level.INFO, "Feather cost disabled by: " + player.getName());
				}
				else
					player.sendMessage("Feather teleport: Teleport cost aleady disabled!");
			}
			else
			{
				player.sendMessage("Feather teleport: Unknown feature!, use falldamage or teledocost");
			}
        }
		else if(split[0].equalsIgnoreCase("/ftenable")/* && player.canUseCommand("/ftenable"))
        {
			if (split[1].equalsIgnoreCase("falldamage"))
			{
				if (fallDamage == false)
				{
					fallDamage = true;
					player.sendMessage("Feather teleport: You enabled falldamage!");
					logger.log(Level.INFO, player.getName() + " enabled falldamage");
				}
				else
					player.sendMessage("Feather teleport: Falldamage already enabled!");
			}
			else if (split[1].equalsIgnoreCase("teledocost"))
			{
				if (teleDoCost == false)
				{
					teleDoCost = true;
					player.sendMessage("Feather teleport: Teleport now cost " + teleCost + " feathers");
					logger.log(Level.INFO, "Feather cost enabled by: " + player.getName());
				}
				else
					player.sendMessage("Feather teleport: Teleport cost already enabled!");
			}
			else
			{
				player.sendMessage("Feather teleport: Unknown feature!, use falldamage or teledocost");
			}
        }
		else if(split[0].equalsIgnoreCase("/ftset")/* && player.canUseCommand("/ftset"))
		{
			if (split[1].equalsIgnoreCase("telecost"))
			{
				try
				{
					if (0 < new Integer(split[2]).intValue())
					{
						teleCost = new Integer(split[2]).intValue();
						player.sendMessage("Feather teleport: Teleport now cost " + teleCost + " feathers");
						logger.log(Level.INFO, player.getName() + " changed teleportcost");
						if (teleDoCost == false)
						{
							player.sendMessage("Don't forget to enable teleport cost!");
						}
					}
					else
						player.sendMessage("Feather teleport: Failed to read amount!");
				}
				catch (Exception ex)
				{
					player.sendMessage("Feather teleport: Failed to read amount!");
				}
			}
			else
				player.sendMessage("Feather teleport: Unknown feature!");
		}
	}*/
	
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
		if (player.getSelectedItem().getTypeID() == this.teleporterID)
		{
			this.telePlayer(player);
		}
	}
	/*
	public void printDistance (Player player)
	{
		TargetBlock ab = new TargetBlock(player, 300, 0.2);
		player.sendMessage("Total distance: " + ab.getDistanceToBlock() + " X:" + ab.getXDistanceToBlock() + " Y:" + ab.getYDistanceToBlock() + " Z:" + ab.getZDistanceToBlock());
	}
	
	public void printDistanceFloored (Player player)
	{
		TargetBlock ab = new TargetBlock(player, 300, 0.1);
		player.sendMessage("Total distance: " + ab.getDistanceToBlockRounded() + " X:" + ab.getXDistanceToBlock() + " Y:" + ab.getYDistanceToBlock() + " Z:" + ab.getZDistanceToBlock());
	}
	*/
	public boolean telePlayer(Player player)
	{
		/*if (player.canUseCommand("/ftele"))
		{*/
			Location playerLoc = player.getLocation().clone();
			TargetBlock ab = new TargetBlock(player, 300, 0.3, blocksToIgnore);
			Block blk = ab.getTargetBlock();
			if (blk != null)
            {
                for(int i = 0; i < 128; i++)
                {
                    int current = playerLoc.getWorld().getBlockAt(ab.getCurrentBlock().getX(), ab.getCurrentBlock().getY() + i, ab.getCurrentBlock().getZ()).getTypeID();
                    int above = playerLoc.getWorld().getBlockAt(ab.getCurrentBlock().getX(), ab.getCurrentBlock().getY() + i + 1, ab.getCurrentBlock().getZ()).getTypeID();
                    if ((blocksToIgnore.contains(String.valueOf(current)) && blocksToIgnore.contains(String.valueOf(above))))
                    {
                    	playerLoc.setX(ab.getCurrentBlock().getX() + .5);
                        playerLoc.setY(ab.getCurrentBlock().getY() + i);
                        playerLoc.setZ(ab.getCurrentBlock().getZ() + .5);
                        player.teleportTo(playerLoc);
                        i = 128;
                        /*
                        if (teleDoCost == true)
		                {
                        	PlayerInventory inv = new PlayerInventory(player);
                        	if (inv.hasItem(teleporterID, teleCost))
                        	{
                        		inv.removeItem(this.teleporterID, teleCost);
	                            player.teleportTo(playerLoc);
                        	}
                    		else
                        		player.sendMessage("Feather teleport: Not enough teleporting material!");
                        	
                        	int numOfFeathersRemoved = 0;
                        	int slot = 0;
                        	Inventory inv = player.getInventory();
                        	Item itm = inv.getItemFromId(288);
                        	while(itm != null && numOfFeathersRemoved < teleCost)
                        	{
                            	slot = itm.getSlot();
                            	inv.removeItem(slot);
                            	for(int o = 0; o < teleCost; o++)
	                            {
	                            	itm.setAmount(itm.getAmount() - 1);
	                            	if(itm.getAmount() >= 0)
	                            	{
		                            	numOfFeathersRemoved++;
		                            	if(itm.getAmount() > 0)
		                            	{
		                            		inv.setSlot(itm,slot);
		                            	}
		                            	if(numOfFeathersRemoved >= teleCost)
		                            		break;
	                            	}
                            	}
                            	inv.updateInventory();
                            	itm = inv.getItemFromId(288);
                            	if(numOfFeathersRemoved >= teleCost)
                            		break;
                        	}
	                    }
                        else
                            player.teleportTo(playerLoc);*/
                    }
                }
            //}
            return true;
		}
		else
			return false;
	}
}
