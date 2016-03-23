package me.vitikc.scb;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

/**
 * Created by Vitikc on 24/Dec/15.
 */
public class SCBMessageManager {

    private SCBMain plugin;
    private FileConfiguration messages;
    private File messagesFile;
    public static HashMap<String,String> messageData = new HashMap<>();

    public SCBMessageManager(SCBMain plugin){
        this.plugin = plugin;
        createMessagesFile();
        loadDefaultMessages();
        loadUserMessages();
    }

    public void printHelp(CommandSender player){
        if(player==null){return;}
        player.sendMessage(ChatColor.YELLOW+"----------------------------------------------");
        player.sendMessage(ChatColor.DARK_AQUA + "Simple Command Blocker");
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', messageData.get("author"))
                +": "
                + ChatColor.DARK_AQUA
                + SCBMain.author);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', messageData.get("version"))
                +": "
                + ChatColor.DARK_AQUA
                + SCBMain.version);
        player.sendMessage(generateColoredMessage("usage","usage_add"));
        player.sendMessage(generateColoredMessage("desc","desc_add"));
        player.sendMessage(generateColoredMessage("usage","usage_remove"));
        player.sendMessage(generateColoredMessage("desc","desc_remove"));
        player.sendMessage(generateColoredMessage("usage","usage_list"));
        player.sendMessage(generateColoredMessage("desc","desc_list"));
        player.sendMessage(generateColoredMessage("usage","usage_reload"));
        player.sendMessage(generateColoredMessage("desc","desc_reload"));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', messageData.get("info_end")));
        player.sendMessage(ChatColor.YELLOW+"----------------------------------------------");
    }

    public void reloadMessageFile(){
        messagesFile = new File(plugin.getDataFolder(),"messages.yml");
        if(!messagesFile.exists()){
            createMessagesFile();
            loadDefaultMessages();
            return;
        }
        messages = YamlConfiguration.loadConfiguration(messagesFile);
        for(String message : messages.getConfigurationSection("").getKeys(false)){
            messageData.put(message, messages.getString(message));
        }
    }

    public String generateMessage(String arg1, String arg2, String arg3){
        StringBuilder message = new StringBuilder();
        message.append(SCBMain.announcer)
                .append(ChatColor.RED)
                .append(SCBMessageManager.messageData.get(arg1))
                .append(ChatColor.WHITE)
                .append(" ")
                .append(arg2)
                .append(" ")
                .append(ChatColor.RED)
                .append(SCBMessageManager.messageData.get(arg3));
        return message.toString();
    }

    public String generateColoredMessage(String arg1){
        StringBuilder message = new StringBuilder();
        message.append(SCBMain.announcer)
                .append(ChatColor.translateAlternateColorCodes('&',
                        SCBMessageManager.messageData.get(arg1)));
        return message.toString();
    }

    public String generateColoredMessage(String arg1, String arg2){
        StringBuilder message = new StringBuilder();
        message.append(ChatColor.translateAlternateColorCodes('&', messageData.get(arg1)))
                .append(": ")
                .append(ChatColor.translateAlternateColorCodes('&', messageData.get(arg2)));
        return message.toString();
    }

    public String generateUsageMessage(String arg1){
        StringBuilder message = new StringBuilder();
        message.append(SCBMessageManager.messageData.get("usage"))
                .append(": ")
                .append(ChatColor.translateAlternateColorCodes('&',
                        SCBMessageManager.messageData.get(arg1)));
        return ChatColor.translateAlternateColorCodes('&', message.toString());
    }

    private void createMessagesFile(){
        messagesFile = new File(plugin.getDataFolder(), "messages.yml");
        messages = YamlConfiguration.loadConfiguration(messagesFile);
        if (!messagesFile.exists()) {
            try {
                messages.save(messagesFile);
                plugin.getLogger().info("Generating new messages file...");
            } catch (IOException ex) {
                throw new IllegalStateException("Unable to create messages file ", ex);
            }
        }
    }
    private void loadDefaultMessages(){
        setMessage("nopermission","&cYou don't have permission.");
        setMessage("author","&6Author");
        setMessage("version","&6Version");
        setMessage("data_reloaded","&aData has been reloaded");
        setMessage("usage","&eUsage");
        setMessage("usage_add","&f/scb add <command> <command2> ...");
        setMessage("usage_remove","&f/scb remove <command> <command2> ...");
        setMessage("usage_list","&f/scb list");
        setMessage("usage_reload","&f/scb reload");
        setMessage("desc","&6Description");
        setMessage("desc_add","&3Blocks command <command> <command2> ...");
        setMessage("desc_remove","&3Unblocks command <command> <command2> ...");
        setMessage("desc_list","&3Shows list of blocked commands");
        setMessage("desc_reload","&3Reloads data files");
        setMessage("info_end","&cWrite command you want to block/unblock without '/'.");
        setMessage("cmd","Command");
        setMessage("cmd_has_blocked","has been blocked");
        setMessage("cmd_already_blocked","already blocked");
        setMessage("cmd_is_blocked","is blocked");
        setMessage("cmd_not_blocked","not blocked by this plugin");
        setMessage("cmd_has_unblocked","has been unblocked");
        setMessage("no_blocked_cmds","&eThere no blocked commands in database.yml");
        setMessage("blocked_cmds_list", "&6Next commands are blocked");
        setMessage("invalid_args","&cInvalid subcommand! &eRead /scb help to more info!");
        setMessage("scb","This command can't be blocked");
    }

    private void loadUserMessages(){
        messagesFile = new File(plugin.getDataFolder(), "messages.yml");
        messages = YamlConfiguration.loadConfiguration(messagesFile);
        for(String message : messages.getConfigurationSection("").getKeys(false)){
            messageData.put(message, messages.getString(message));
        }
    }
    private void setMessage(String name, String message){
        messagesFile = new File(plugin.getDataFolder(), "messages.yml");
        if(!messagesFile.exists()){return;}
        messages = YamlConfiguration.loadConfiguration(messagesFile);
        if(!messages.isSet(name)){
            messages.set(name, message);
            try{
                messages.save(messagesFile);
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

}
