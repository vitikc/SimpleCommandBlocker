package me.vitikc.scb;

import java.util.List;
import java.util.regex.Pattern;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

@SuppressWarnings("unused")
public class CommandListener implements Listener{

    private MessageManager mm;
    private DatabaseManager dm;

    private static FileConfiguration database;


    public CommandListener(Main plugin){
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        mm = plugin.getMessageManager();
        dm = plugin.getDatabaseManager();
        database = dm.getDatabaseFileConfiguration();
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
            String cmd = msgarr[0] + " ";
            String regex = String.format("[:/](%s )", command);
            if (Pattern.compile(regex).matcher(cmd).find()) {

                event.setCancelled(true);
                player.sendMessage(mm.generateMessage("cmd", command, "cmd_is_blocked"));
                return;
            }
        }
    }
}
