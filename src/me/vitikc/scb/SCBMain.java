package me.vitikc.scb;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SCBMain extends JavaPlugin{
	//ANSI constants
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static String version = "1.3.0";
	public static String announcer = ChatColor.DARK_AQUA+"[SCB]";
	private SCBMessageManager mm;
	private SCBDatabaseManager dm;
	private SCBCommandListener cl;
	private SCBCommandExecutor ce;

	@Override
	public void onEnable(){
		getLogger().info(ANSI_GREEN + "Simple Command Blocker enabled" + ANSI_RESET);
		mm = new SCBMessageManager(this);
		dm = new SCBDatabaseManager(this);
		cl = new SCBCommandListener(this);
		ce = new SCBCommandExecutor(this);
		getCommand("scb").setExecutor(ce);
	}
	@Override
	public void onDisable(){
		dm.saveDatabaseFile();
		getLogger().info(ANSI_RED + "Simple Command Blocker disabled" + ANSI_RESET);

	}

	public SCBMessageManager getMessageManager() {
		return mm;
	}
	public SCBDatabaseManager getDatabaseManager(){
		return dm;
	}

}