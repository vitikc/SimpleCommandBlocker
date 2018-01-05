package me.vitikc.scb;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{
    //ANSI constants
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static String author = "Vitikc";
    public static String version = "1.3.0";
    public static String announcer = ChatColor.DARK_AQUA+"[SCB]";
    private MessageManager mm;
    private DatabaseManager dm;
    private CommandListener cl;
    private CommandExecutor ce;

    @Override
    public void onEnable(){
        getLogger().info(ANSI_GREEN + "Simple Command Blocker enabled" + ANSI_RESET);
        mm = new MessageManager(this);
        dm = new DatabaseManager(this);
        cl = new CommandListener(this);
        ce = new CommandExecutor(this);
        getCommand("scb").setExecutor(ce);
    }
    @Override
    public void onDisable(){
        dm.saveDatabaseFile();
        getLogger().info(ANSI_RED + "Simple Command Blocker disabled" + ANSI_RESET);

    }

    public MessageManager getMessageManager() {
        return mm;
    }
    public DatabaseManager getDatabaseManager(){
        return dm;
    }

}