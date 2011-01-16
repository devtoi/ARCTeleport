package org.toi.arcteleport;
import java.io.File;

import org.bukkit.Server;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.java.JavaPlugin;


public class ARCTeleport extends JavaPlugin{
    private String name;
    private String version;
    private final ARCTPListener playerListener = new ARCTPListener(this);

    public ARCTeleport(PluginLoader pluginLoader, Server instance, PluginDescriptionFile desc, File folder, File plugin, ClassLoader cLoader) {
        super(pluginLoader, instance, desc, folder, plugin, cLoader);

        name = "ARC Teleport";
        version = "v0.3.0 (Lilliad)";
        
        this.createFolder();
        playerListener.getPerms().addCmd("/arct");
        playerListener.getPerms().addCmd("/arcd");
        playerListener.getPerms().addCmd("/arcs");
        playerListener.getPerms().addCmd("/arce");
        playerListener.getPerms().loadPermissions();
        playerListener.getPerms().savePermissions();
        playerListener.loadConfig();
        registerEvents();
    }
    
    public void onEnable()
    {
    	System.out.println(name + " " + version + " initialized!");
    }

    public void onDisable()
    {
    	
    }

    private void createFolder()
    {
    	File main = new File("ARCTeleport"); 
    	if (!main.exists())
    		main.mkdirs();
    		
    }
    
    private void registerEvents() {
        getServer().getPluginManager().registerEvent(Event.Type.PLAYER_COMMAND, playerListener, Priority.Normal, this);
        getServer().getPluginManager().registerEvent(Event.Type.PLAYER_ITEM, playerListener, Priority.Normal, this);
    }
}
