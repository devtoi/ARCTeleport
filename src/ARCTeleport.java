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

    public ARCTeleport(PluginLoader pluginLoader, Server instance, PluginDescriptionFile desc, File plugin, ClassLoader cLoader) {
        super(pluginLoader, instance, desc, plugin, cLoader);

        name = "ARC Teleport";
        version = "v0.1 (Grimslaw)";
        
        registerEvents();
    }
    
    public void onEnable()
    {
    	/*
    	etc.getInstance().addCommand("/ftdisable <feature>", " - Disables ex. falldamage, telecost");
    	etc.getInstance().addCommand("/ftenable <feature>", " - Disables ex. falldamage, telecost");
    	etc.getInstance().addCommand("/ftset <feature> <value>", " - Sets different features ex. telecost");
        listener.setEnabled(true);
        */
    	System.out.println(name + " " + version + " initialized!");
    }

    public void onDisable()
    {
    	/*
    	etc.getInstance().removeCommand("/ftdisable");
    	etc.getInstance().removeCommand("/ftenable");
    	etc.getInstance().removeCommand("/ftset");
    	listener.setEnabled(false);
    	*/
    }

    private void registerEvents() {
        //getServer().getPluginManager().registerEvent(Event.Type.PLAYER_COMMAND, playerListener, Priority.Normal, this);
        getServer().getPluginManager().registerEvent(Event.Type.PLAYER_ITEM, playerListener, Priority.Normal, this);
    }
}
