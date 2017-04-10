package top.mymoe.simplesidebar;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;

import java.util.logging.Logger;

/**
 * Created by ginya on 2017/3/28.
 */
public class SimpleSidebar extends JavaPlugin implements Listener {
    public static Plugin plugin;
    public static Config config;
    public static Logger logger;
    public static String pluginName = "SimpleSidebar";
    Timer timer= new Timer();
    @Override
    public void onEnable() {
        logger = getLogger();
        plugin = this ;
        this.saveDefaultConfig();
        if(config == null) {
            config = new Config();
        }
        config.reload();
        getServer().getPluginManager().registerEvents(this, this);
        for (Player player:Bukkit.getOnlinePlayers()){
            createScoreboard(player);
        }
        timer.start();
        logger.info(pluginName +" enabled.");
    }

    @Override
    public void onDisable() {
//        this.saveConfig();
        timer.stop();
        for (Player player:Bukkit.getOnlinePlayers()){
            player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
        }
        logger.info(pluginName +" disabled.");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        createScoreboard(event.getPlayer());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("simplesidebar")){
            if(args.length!=0){
                if(args[0].equalsIgnoreCase("reload")) {
                    if(sender.hasPermission("simplesidebar.reload")){
                        timer.stop();
                        config.reload();
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            createScoreboard(player);
                        }
                        timer.start();
                        sender.sendMessage(pluginName + " has reloaded.");
                        logger.info(pluginName + " has reloaded.");
                    }else{
                        sender.sendMessage("Don't have permission:simplesidebar.reload");
                    }
                }
            }else {
                sender.sendMessage("Use \"/simplesidebar reload \"to reload.");
            }
            return true;
        }
        return false;
    }
    public static boolean createScoreboard(Player player) {
        Board board = new Board();
        String type = config.getType(player);
        if (type != null) {
            board.update(type,player);
            player.setScoreboard(board.getScoreboard());
            return true;
        }else {
            return false;
        }
    }
    public static boolean updateScoreBoard(Player player){
        Board board = new Board(player);
        String type = config.getType(player);
        if (type != null) {
            board.update(type,player);
            return true;
        }else {
            player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
            return false;
        }
    }


}

