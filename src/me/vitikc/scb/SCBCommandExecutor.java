package me.vitikc.scb;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by Vitikc on 24/Dec/15.
 */

public class SCBCommandExecutor implements CommandExecutor {

    private SCBMessageManager mm;
    private SCBDatabaseManager dm;

    private static FileConfiguration database;


    public SCBCommandExecutor(SCBMain plugin){
        mm = plugin.getMessageManager();
        dm = plugin.getDatabaseManager();
        database = dm.getDatabaseFileConfiguration();
    }
    private void blockCommand(CommandSender sender, String arg){
        if (arg.equalsIgnoreCase("scb")){
            sender.sendMessage(SCBMain.announcer + ChatColor.RED + SCBMessageManager.messageData.get("scb"));
        }
        List<String> list = database.getStringList("blocked_cmds");
        if(list.contains(arg.toLowerCase())){
            sender.sendMessage(mm.generateMessage("cmd",arg,"cmd_is_blocked"));
            return;
        }
        list.add(arg.toLowerCase());
        database.set("blocked_cmds", list);
        dm.saveDatabaseFile();
        sender.sendMessage(mm.generateMessage("cmd",arg,"cmd_has_blocked"));
    }
    private void unblockCommand(CommandSender sender, String arg){
        List<String> list = database.getStringList("blocked_cmds");
        StringBuilder message = new StringBuilder();
        if(!list.contains(arg.toLowerCase())){
            sender.sendMessage(mm.generateMessage("cmd",arg,"cmd_not_blocked"));
            return;
        }
        list.remove(arg.toLowerCase());
        database.set("blocked_cmds", list);
        dm.saveDatabaseFile();
        sender.sendMessage(mm.generateMessage("cmd",arg,"cmd_has_unblocked"));
    }
    private void loadBlockedCommandsList(CommandSender sender){
        List<String> list = database.getStringList("blocked_cmds");
        if(list.isEmpty()){
            sender.sendMessage(mm.generateColoredMessage(("no_blocked_cmds")));
            return;
        }
        sender.sendMessage(mm.generateColoredMessage("blocked_cmds_list")+":");
        sender.sendMessage(list.toString());
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("scb.allow") || player.isOp()) {
                //Checking args
                if (args.length != 0) {
                    switch (args[0]) {
                        case "add":
                            //Adding command to blocked list
                            if (args.length == 2) {
                                blockCommand(player, args[1]);
                            } else if (args.length < 2) {
                                player.sendMessage(mm.generateUsageMessage("usage_add"));
                            } else if (args.length > 2) {
                                for (int i = 1; i < args.length; i++) {
                                    blockCommand(player, args[i]);
                                }
                            }
                            break;
                        case "remove":
                            //Removing command from blocked list
                            if (args.length == 2) {
                                unblockCommand(player, args[1]);
                            } else if (args.length < 2) {
                                player.sendMessage(mm.generateUsageMessage("usage_remove"));
                            } else if (args.length > 2) {
                                for (int i = 1; i < args.length; i++) {
                                    unblockCommand(player, args[i]);
                                }
                            }
                            break;
                        case "reload":
                            //Reloading messages and database
                            dm.reloadDatabaseFile();
                            mm.reloadMessageFile();
                            player.sendMessage(mm.generateColoredMessage("data_reloaded"));
                            break;
                        case "list":
                            //Sending list of blocked commands
                            loadBlockedCommandsList(player);
                            break;
                        case "help":
                            //Sending list of blocked commands
                            mm.printHelp(player);
                            break;
                        default:
                            //Sending an invalid args message and showing help
                            player.sendMessage(mm.generateColoredMessage("invalid_args"));
                            break;
                    }
                } else
                    mm.printHelp(player);
            } else
                player.sendMessage(mm.generateColoredMessage("nopermission"));
        }else {
            ConsoleCommandSender console = sender.getServer().getConsoleSender();
            if (args.length != 0) {
                switch (args[0]) {
                    case "add":
                        //Adding command to blocked list
                        if (args.length == 2) {
                            blockCommand(console, args[1]);
                        } else if (args.length < 2) {
                            console.sendMessage(mm.generateUsageMessage("usage_add"));
                        } else if (args.length > 2) {
                            for (int i = 1; i < args.length; i++) {
                                blockCommand(console, args[i]);
                            }
                        }
                        break;
                    case "remove":
                        //Removing command from blocked list
                        if (args.length == 2) {
                            unblockCommand(console, args[1]);
                        } else if (args.length < 2) {
                            console.sendMessage(mm.generateUsageMessage("usage_remove"));
                        } else if (args.length > 2) {
                            for (int i = 1; i < args.length; i++) {
                                unblockCommand(console, args[i]);
                            }
                        }
                        break;
                    case "reload":
                        //Reloading messages and database
                        dm.reloadDatabaseFile();
                        mm.reloadMessageFile();
                        console.sendMessage(mm.generateColoredMessage("data_reloaded"));
                        break;
                    case "list":
                        //Sending list of blocked commands
                        loadBlockedCommandsList(console);
                        break;
                    case "help":
                        //Sending list of blocked commands
                        mm.printHelp(console);
                        break;
                    default:
                        //Sending an invalid args message and showing help
                        console.sendMessage(mm.generateColoredMessage("invalid_args"));
                        break;
                }
            } else
                mm.printHelp(console);
        }
        return true;
    }
}
