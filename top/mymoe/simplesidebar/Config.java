package top.mymoe.simplesidebar;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.List;

/**
 * Created by ginya on 2017/3/31.
 */
public class Config {
    private FileConfiguration config;
    private Set<String> types;

    public Config() {
        this.config= SimpleSidebar.plugin.getConfig();
        types = config.getConfigurationSection("Sidebars").getValues(false).keySet();
    }
    public void reload(){
        SimpleSidebar.plugin.reloadConfig();
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

    public List<String> getLines(String type) {
        return config.getStringList("Sidebars."+type+".lines");
    }

    public String getTitle(String type) {
        return config.getString("Sidebars."+type+".title");
    }
}
