package me.vitikc.scb;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandsListener implements Listener{

	Main plugin;
	
	public CommandsListener(Main plugin){
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin=plugin;
	}
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event){
		Player player =  event.getPlayer();
		if(player.hasPermission(plugin.scbbypass)){
			return;
		}
		List<String> cmds = plugin.database.getStringList("blocked_cmds");
	    for (String command : cmds) {
	    	String[] msgarr = event.getMessage().split("\\s+");
	    	String cmd=msgarr[0];
	    	if (cmd.equalsIgnoreCase("/" + command)) {
	    		event.setCancelled(true);
	    		player.sendMessage(plugin.SCBAnnouncer+ChatColor.RED+Main.messageData.get("cmd") + ChatColor.WHITE + " " + command + " " + ChatColor.RED + Main.messageData.get("cmd_is_blocked"));
				return;
	    	}
			if(cmd.startsWith("/")&&cmd.toLowerCase().endsWith(command)){
				event.setCancelled(true);
				player.sendMessage(plugin.SCBAnnouncer + ChatColor.RED + Main.messageData.get("cmd") + ChatColor.WHITE + " " + command + " " + ChatColor.RED + Main.messageData.get("cmd_is_blocked"));
				return;
			}
	    }
	}
}
