package top.mymoe.simplesidebar;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by ginya on 2017/3/29.
 */
public class Timer {
    public void start() {
       Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(SimpleSidebar.plugin, new Runnable()
        {
            @Override
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers())
                    SimpleSidebar.updateScoreBoard(p);
            }
        }, 0, SimpleSidebar.config.getUpdateTime());

     }
    public void stop() {
        Bukkit.getScheduler().cancelTasks(SimpleSidebar.plugin);
    }
}
