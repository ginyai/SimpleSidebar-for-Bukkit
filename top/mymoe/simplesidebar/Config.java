package top.mymoe.simplesidebar;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.List;

/**
 * Created by ginya on 2017/3/31.
 */
public class Config {
    private FileConfiguration config;
    private FileConfiguration messages = null;
    private File messagesFile = null;
    private Set<String> types;

    public Config() {
        this.config= SimpleSidebar.plugin.getConfig();
        reloadMessages();
        types = config.getConfigurationSection("Sidebars").getValues(false).keySet();
    }
    public void reload(){
        SimpleSidebar.plugin.reloadConfig();
        reloadMessages();
        this.config = SimpleSidebar.plugin.getConfig();
    }

    public int getUpdateTime(){return config.getInt("UpdateTime");}

    public String getType(Player player) {
        for(String type:types){
            String premission = config.getString("Sidebars."+type+".permission");
            if(premission!=""&&!player.hasPermission(premission)){
                break;
            }
            return type;
        }
        return null;
    }
    private void reloadMessages(){
        if (messagesFile == null) {
            messagesFile = new File(SimpleSidebar.plugin.getDataFolder(), "messages.yml");
        }
        if (!messagesFile.exists()) {
            SimpleSidebar.plugin.saveResource("messages.yml", false);
        }
        messages = YamlConfiguration.loadConfiguration(messagesFile);
        // Look for defaults in the jar
        Reader defConfigStream = null;
        try {
            defConfigStream = new InputStreamReader(SimpleSidebar.plugin.getResource("messages.yml"), "UTF8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            messages.setDefaults(defConfig);
        }
    }
    public String getMessage(String key){
        return messages.getString(key);
    }

    public List<String> getLines(String type) {
        return config.getStringList("Sidebars."+type+".lines");
    }

    public String getTitle(String type) {
        return config.getString("Sidebars."+type+".title");
    }
}
