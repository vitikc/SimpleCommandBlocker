package me.vitikc.scb;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class SCBCommandListener implements Listener{

	private SCBMain plugin;

	private SCBMessageManager mm;
	private SCBDatabaseManager dm;

	private String SCBAnnouncer;

	private static FileConfiguration database;


	public SCBCommandListener(SCBMain plugin){
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin=plugin;
		mm = plugin.getMessageManager();
		dm = plugin.getDatabaseManager();
		database = dm.getDatabaseFileConfiguration();
		SCBAnnouncer = SCBMain.announcer;
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event){
		Player player =  event.getPlayer();
		if(player.hasPermission("scb.bypass")){
			return;
		}
		List<String> cmds = database.getStringList("blocked_cmds");
	    for (String command : cmds) {
	    	String[] msgarr = event.getMessage().split("\\s+");
	    	String cmd=msgarr[0];
			if (cmd.equalsIgnoreCase("/" + command)) {
	    		event.setCancelled(true);
	    		player.sendMessage(mm.generateMessage("cmd", command, "cmd_is_blocked"));
				return;
	    	}
			if(cmd.startsWith("/")&&cmd.toLowerCase().endsWith(command)){
				event.setCancelled(true);
				player.sendMessage(mm.generateMessage("cmd", command, "cmd_is_blocked"));
				return;
			}
	    }
	}
}
