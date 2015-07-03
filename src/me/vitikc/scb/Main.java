package me.vitikc.scb;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{
	//ANSI constants
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	//Plugin constants
	public static String MainPermission = "scb.cmds";
	public static String BypassPermission = "scb.bypass";
	public static String pluginVersion = "1.2.2";
	//Plugin announcer
	public String SCBAnnouncer = ChatColor.DARK_AQUA+"[SCB]";
	//Permissions
	public Permission scb = new Permission(MainPermission);
	public Permission scbbypass = new Permission(BypassPermission);
	//Files
	public FileConfiguration database;
	private FileConfiguration messages;
	private File databaseFile;
	private File messagesFile;
	//Hashmaps
	public static HashMap<String,String> messageData = new HashMap<String,String>();
	
	@Override
	public void onEnable(){
		getLogger().info(ANSI_GREEN+"Simple Command Blocker enabled"+ANSI_RESET);
		
		new CommandsListener(this);
		
		PluginManager pm = getServer().getPluginManager();
		pm.addPermission(scb);
		pm.addPermission(scbbypass);
		
		//Blocked cmds file
		createDatabaseFile();
		//Messages file
		createMessagesFile();
		//Load defaults
		loadDefaultMessages();
		loadUserMessages();
	}
	@Override
	public void onDisable(){
		saveDatabaseFile();
		getLogger().info(ANSI_RED + "Simple Command Blocker disabled" + ANSI_RESET);
		
	}
	private void createDatabaseFile(){
		databaseFile = new File(getDataFolder(), "database.yml");
		database = YamlConfiguration.loadConfiguration(databaseFile);
		if (!databaseFile.exists()) {
			try {
				database.save(databaseFile);
				getLogger().info("Generating new database file...");
			} catch (IOException ex) {
				throw new IllegalStateException("Unable to create database file ", ex);
			}
		}
	}
	private void createMessagesFile(){
		messagesFile = new File(getDataFolder(), "messages.yml");
		messages = YamlConfiguration.loadConfiguration(messagesFile);
		if (!messagesFile.exists()) {
			try {
				messages.save(messagesFile);
				getLogger().info("Generating new messages file...");
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
		setMessage("cmd_not_blocked","not blocked by");
		setMessage("cmd_has_unblocked","has been unblocked");
 		setMessage("no_blocked_cmds","&eThere no blocked commands in database.yml");
        	setMessage("blocked_cmds_list", "&6Next commands are blocked");
		setMessage("invalid_args","&cInvalid subcommand");
		setMessage("can_senden_only_from","&cSorry, that command can be sended from player only");
	}
	private void loadUserMessages(){
		messagesFile = new File(getDataFolder(), "messages.yml");
		messages = YamlConfiguration.loadConfiguration(messagesFile);
		for(String message : messages.getConfigurationSection("").getKeys(false)){
			messageData.put(message, messages.getString(message));
		}
	}
	private void setMessage(String name, String message){
		messagesFile = new File(getDataFolder(), "messages.yml");
		if(!messagesFile.exists()){
			return;
		}
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
	private void reloadMessageFile(){
		messagesFile = new File(getDataFolder(),"messages.yml");
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
	private void reloadDatabaseFile(){
		databaseFile = new File(getDataFolder(),"database.yml");
		if(!databaseFile.exists()){
			createDatabaseFile();
			return;
		}
		database = YamlConfiguration.loadConfiguration(databaseFile);
	}
	private void saveDatabaseFile(){
		try{
			database.save(databaseFile);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	private void printHelp(Player player){
		if(player==null){
			return;
		}
		player.sendMessage(ChatColor.YELLOW+"----------------------------------------------");
		player.sendMessage(ChatColor.DARK_AQUA + "Simple Command Blocker");
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', messageData.get("author")) +": "+ ChatColor.DARK_AQUA +"Vitikc");
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', messageData.get("version")) +": "+ ChatColor.DARK_AQUA +" "+pluginVersion);
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', messageData.get("usage")) +": "+ ChatColor.translateAlternateColorCodes('&', messageData.get("usage_add")));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', messageData.get("desc")) +": "+ ChatColor.translateAlternateColorCodes('&', messageData.get("desc_add")));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', messageData.get("usage")) +": "+ ChatColor.translateAlternateColorCodes('&', messageData.get("usage_remove")));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', messageData.get("desc")) +": "+ ChatColor.translateAlternateColorCodes('&', messageData.get("desc_remove")));
        	player.sendMessage(ChatColor.translateAlternateColorCodes('&', messageData.get("usage")) + ": " + ChatColor.translateAlternateColorCodes('&', messageData.get("usage_list")));
        	player.sendMessage(ChatColor.translateAlternateColorCodes('&', messageData.get("desc")) + ": " + ChatColor.translateAlternateColorCodes('&', messageData.get("desc_list")));
        	player.sendMessage(ChatColor.translateAlternateColorCodes('&', messageData.get("usage")) + ": " + ChatColor.translateAlternateColorCodes('&', messageData.get("usage_reload")));
        	player.sendMessage(ChatColor.translateAlternateColorCodes('&', messageData.get("desc")) + ": " + ChatColor.translateAlternateColorCodes('&', messageData.get("desc_reload")));
        	player.sendMessage(ChatColor.translateAlternateColorCodes('&', messageData.get("info_end")));
		player.sendMessage(ChatColor.YELLOW+"----------------------------------------------");
	}
	private void blockCommand(Player player, String arg){
		List<String> list = database.getStringList("blocked_cmds");
		if(list.contains(arg.toLowerCase())){
			player.sendMessage(SCBAnnouncer+ChatColor.GREEN + messageData.get("cmd") + ChatColor.WHITE + " " + arg + " " + ChatColor.GREEN +messageData.get("cmd_already_blocked"));
			return;
		}
		list.add(arg.toLowerCase());
		database.set("blocked_cmds", list);
		saveDatabaseFile();
		player.sendMessage(SCBAnnouncer+ChatColor.GREEN + messageData.get("cmd") + ChatColor.WHITE + " " + arg + " " + ChatColor.GREEN +messageData.get("cmd_has_blocked"));
	}
	private void unblockCommand(Player player, String arg){
		List<String> list = database.getStringList("blocked_cmds");
		if(!list.contains(arg.toLowerCase())){
			player.sendMessage(SCBAnnouncer+ChatColor.YELLOW+messageData.get("cmd")+ChatColor.WHITE + " " + arg + " " + ChatColor.YELLOW+messageData.get("cmd_not_blocked")+" "+SCBAnnouncer);
			return;
		}
		list.remove(arg.toLowerCase());
		database.set("blocked_cmds", list);
		saveDatabaseFile();
		player.sendMessage(SCBAnnouncer+ChatColor.RED + messageData.get("cmd") + ChatColor.WHITE + " " + arg + " " + ChatColor.RED +messageData.get("cmd_has_unblocked"));
	}
	private void loadBlockedCommandsList(Player player){
        List<String> list = database.getStringList("blocked_cmds");
        if(list.isEmpty()){
            player.sendMessage(SCBAnnouncer+ChatColor.translateAlternateColorCodes('&',messageData.get("no_blocked_cmds")));
            return;
        }
        player.sendMessage(SCBAnnouncer+ChatColor.translateAlternateColorCodes('&',messageData.get("blocked_cmds_list"))+":");
        player.sendMessage(""+list);
    }
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player){
			Player player = (Player) sender;
			if(player.hasPermission(scb)||player.isOp()){
				if(command.getName().equalsIgnoreCase("scb")){
					//Checking args
					if(args.length!=0){
						//Adding command to blocked list
						if(args[0].equalsIgnoreCase("add")){
							if(args.length==2){
								blockCommand(player,args[1]);
							} else if (args.length<2){
								player.sendMessage(ChatColor.translateAlternateColorCodes('&', messageData.get("usage")) +": "+ ChatColor.translateAlternateColorCodes('&', messageData.get("usage_add")));
							} else if (args.length>2){
								for(int i = 1;i<args.length;i++){
									blockCommand(player,args[i]);
								}
							}
						} else if(args[0].equalsIgnoreCase("remove")){
							//Removing command from blocked list
							if(args.length==2){
								unblockCommand(player,args[1]);
							} else if (args.length<2){
								player.sendMessage(ChatColor.translateAlternateColorCodes('&', messageData.get("usage")) +": "+ ChatColor.translateAlternateColorCodes('&', messageData.get("usage_remove")));
							} else if (args.length>2){
								for(int i = 1;i<args.length;i++){
									unblockCommand(player,args[i]);
								}
							}
						} else if(args[0].equalsIgnoreCase("reload")){
							reloadDatabaseFile();
							reloadMessageFile();
							player.sendMessage(SCBAnnouncer+ChatColor.translateAlternateColorCodes('&', messageData.get("data_reloaded")));
						} else if(args[0].equalsIgnoreCase("list")){
							loadBlockedCommandsList(player);
						} else {
							player.sendMessage(SCBAnnouncer+ChatColor.translateAlternateColorCodes('&', messageData.get("invalid_args")));
							printHelp(player);
						}
						
					} else if (args.length==0){
						printHelp(player);
					}
				}
			} else {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', messageData.get("nopermission")));
			}
		} else {
			sender.getServer().getConsoleSender().sendMessage(SCBAnnouncer+ChatColor.translateAlternateColorCodes('&', messageData.get("can_senden_only_from")));
		}
		return true;
	}
}
