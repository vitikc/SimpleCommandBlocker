package me.vitikc.scb;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by Vitikc on 24/Dec/15.
 */
public class SCBCommandExecutor implements CommandExecutor {

    private SCBMessageManager mm;
    private SCBDatabaseManager dm;

    private String SCBAnnouncer;

    private static FileConfiguration database;


    public SCBCommandExecutor(SCBMain plugin){
        mm = plugin.getMessageManager();
        dm = plugin.getDatabaseManager();
        database = dm.getDatabaseFileConfiguration();
        SCBAnnouncer = SCBMain.announcer;
    }
    private void blockCommand(Player player, String arg){
        List<String> list = database.getStringList("blocked_cmds");
        if(list.contains(arg.toLowerCase())){
            player.sendMessage(SCBAnnouncer+ChatColor.GREEN + SCBMessageManager.messageData.get("cmd") + ChatColor.WHITE + " " + arg + " " + ChatColor.GREEN +SCBMessageManager.messageData.get("cmd_already_blocked"));
            return;
        }
        list.add(arg.toLowerCase());
        database.set("blocked_cmds", list);
        dm.saveDatabaseFile();
        player.sendMessage(SCBAnnouncer+ChatColor.GREEN + SCBMessageManager.messageData.get("cmd") + ChatColor.WHITE + " " + arg + " " + ChatColor.GREEN +SCBMessageManager.messageData.get("cmd_has_blocked"));
    }
    private void unblockCommand(Player player, String arg){
        List<String> list = database.getStringList("blocked_cmds");
        if(!list.contains(arg.toLowerCase())){
            player.sendMessage(SCBAnnouncer+ChatColor.YELLOW+SCBMessageManager.messageData.get("cmd")+ChatColor.WHITE + " " + arg + " " + ChatColor.YELLOW+SCBMessageManager.messageData.get("cmd_not_blocked")+" "+SCBAnnouncer);
            return;
        }
        list.remove(arg.toLowerCase());
        database.set("blocked_cmds", list);
        dm.saveDatabaseFile();
        player.sendMessage(SCBAnnouncer+ChatColor.RED + SCBMessageManager.messageData.get("cmd") + ChatColor.WHITE + " " + arg + " " + ChatColor.RED +SCBMessageManager.messageData.get("cmd_has_unblocked"));
    }
    private void loadBlockedCommandsList(Player player){
        List<String> list = database.getStringList("blocked_cmds");
        if(list.isEmpty()){
            player.sendMessage(SCBAnnouncer+ChatColor.translateAlternateColorCodes('&',SCBMessageManager.messageData.get("no_blocked_cmds")));
            return;
        }
        player.sendMessage(SCBAnnouncer+ChatColor.translateAlternateColorCodes('&',SCBMessageManager.messageData.get("blocked_cmds_list"))+":");
        player.sendMessage(""+list);
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(player.hasPermission("scb.allow")||player.isOp()){
                //Checking args
                if(args.length!=0){
                    switch(args[0]){
                        case "add":
                            //Adding command to blocked list
                            if(args.length==2){blockCommand(player,args[1]);
                            } else if (args.length<2){player.sendMessage(ChatColor.translateAlternateColorCodes('&', SCBMessageManager.messageData.get("usage")) +": "+ ChatColor.translateAlternateColorCodes('&', SCBMessageManager.messageData.get("usage_add")));
                            } else if (args.length>2){for(int i = 1;i<args.length;i++){blockCommand(player,args[i]);}
                            }
                            break;
                        case "remove":
                            //Removing command from blocked list
                            if(args.length==2){unblockCommand(player,args[1]);
                            } else if (args.length<2){player.sendMessage(ChatColor.translateAlternateColorCodes('&', SCBMessageManager.messageData.get("usage")) +": "+ ChatColor.translateAlternateColorCodes('&', SCBMessageManager.messageData.get("usage_remove")));
                            } else if (args.length>2){for(int i = 1;i<args.length;i++){unblockCommand(player,args[i]);}
                            }
                            break;
                        case "reload":
                            //Reloading messages and database
                            dm.reloadDatabaseFile();
                            mm.reloadMessageFile();
                            player.sendMessage(SCBAnnouncer+ChatColor.translateAlternateColorCodes('&', SCBMessageManager.messageData.get("data_reloaded")));
                            break;
                        case "list":
                            //Sending list of blocked commands
                            loadBlockedCommandsList(player);
                            break;
                        default:
                            //Sending an invalid args message and showing help
                            player.sendMessage(SCBAnnouncer + ChatColor.translateAlternateColorCodes('&', SCBMessageManager.messageData.get("invalid_args")));
                            mm.printHelp(player);
                            break;
                    }
                } else {mm.printHelp(player);}
            } else {player.sendMessage(ChatColor.translateAlternateColorCodes('&', SCBMessageManager.messageData.get("nopermission")));}
        } else {sender.getServer().getConsoleSender().sendMessage(SCBAnnouncer+ChatColor.translateAlternateColorCodes('&', SCBMessageManager.messageData.get("can_senden_only_from")));}
        return true;
    }
}
