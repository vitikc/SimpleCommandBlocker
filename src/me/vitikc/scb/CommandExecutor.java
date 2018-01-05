package me.vitikc.scb;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by Vitikc on 24/Dec/15.
 */

public class CommandExecutor implements org.bukkit.command.CommandExecutor {

    private MessageManager mm;
    private DatabaseManager dm;

    private static FileConfiguration database;


    CommandExecutor(Main plugin){
        mm = plugin.getMessageManager();
        dm = plugin.getDatabaseManager();
        database = dm.getDatabaseFileConfiguration();
    }
    private void blockCommand(CommandSender sender, String arg){
        if (arg.equalsIgnoreCase("scb")){
            sender.sendMessage(Main.announcer + ChatColor.RED + MessageManager.messageData.get("scb"));
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
            if (sender.hasPermission("scb.allow") || sender.isOp()) {
                //Checking args
                if (args.length != 0) {
                    switch (args[0]) {
                        case "add":
                            //Adding command to blocked list
                            if (args.length == 2) {
                                blockCommand(sender, args[1]);
                            } else if (args.length < 2) {
                                sender.sendMessage(mm.generateUsageMessage("usage_add"));
                            } else {
                                for (int i = 1; i < args.length; i++) {
                                    blockCommand(sender, args[i]);
                                }
                            }
                            break;
                        case "remove":
                            //Removing command from blocked list
                            if (args.length == 2) {
                                unblockCommand(sender, args[1]);
                            } else if (args.length < 2) {
                                sender.sendMessage(mm.generateUsageMessage("usage_remove"));
                            } else {
                                for (int i = 1; i < args.length; i++) {
                                    unblockCommand(sender, args[i]);
                                }
                            }
                            break;
                        case "reload":
                            //Reloading messages and database
                            dm.reloadDatabaseFile();
                            mm.reloadMessageFile();
                            sender.sendMessage(mm.generateColoredMessage("data_reloaded"));
                            break;
                        case "list":
                            //Sending list of blocked commands
                            loadBlockedCommandsList(sender);
                            break;
                        case "help":
                            //Sending list of blocked commands
                            mm.printHelp(sender);
                            break;
                        default:
                            //Sending an invalid args message and showing help
                            sender.sendMessage(mm.generateColoredMessage("invalid_args"));
                            break;
                    }
                } else
                    mm.printHelp(sender);
            } else
                sender.sendMessage(mm.generateColoredMessage("nopermission"));
        }
        return true;
    }
}
