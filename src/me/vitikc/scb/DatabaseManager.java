package me.vitikc.scb;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;

/**
 * Created by Vitikc on 24/Dec/15.
 */
public class DatabaseManager {
    private Main plugin;

    private static FileConfiguration database;
    private File databaseFile;


    DatabaseManager(Main plugin){
        this.plugin = plugin;
        createDatabaseFile();
    }

    public FileConfiguration getDatabaseFileConfiguration(){
        return database;
    }

    public void saveDatabaseFile(){
        try{
            database.save(databaseFile);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void reloadDatabaseFile(){
        databaseFile = new File(plugin.getDataFolder(),"database.yml");
        if(!databaseFile.exists()){
            createDatabaseFile();
            return;
        }
        database = YamlConfiguration.loadConfiguration(databaseFile);
    }

    private void createDatabaseFile(){
        databaseFile = new File(plugin.getDataFolder(), "database.yml");
        database = YamlConfiguration.loadConfiguration(databaseFile);
        if (!databaseFile.exists()) {
            try {
                database.save(databaseFile);
                plugin.getLogger().info("Generating new database file...");
            } catch (IOException ex) {
                throw new IllegalStateException("Unable to create database file ", ex);
            }
        }
    }

}
