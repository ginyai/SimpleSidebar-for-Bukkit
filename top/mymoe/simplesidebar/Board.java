package top.mymoe.simplesidebar;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.List;

/**
 * Created by ginya on 2017/3/28.
 */
public class Board {
    private Scoreboard scoreboard;
    private Objective objective;
    public Board(){
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.objective = this.scoreboard.registerNewObjective("MMSB","dummy");
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }
    public Board(Scoreboard board)
    {
        this.scoreboard = board;
        this.objective = board.getObjective(DisplaySlot.SIDEBAR);
    }
    public Board(Player player)
    {
        this.scoreboard = player.getScoreboard();
        if(this.scoreboard.getObjective(DisplaySlot.SIDEBAR)==null){
            if(this.scoreboard.getObjective("MMSB")==null)
                this.objective = this.scoreboard.registerNewObjective("MMSB","dummy");
            else
                this.objective = this.scoreboard.getObjective("MMSB");
            this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        }else {
            this.objective = this.scoreboard.getObjective(DisplaySlot.SIDEBAR);
        }
    }
    public void setTitle(String title){
        this.objective.setDisplayName(title);
    }
    public void update(String type,Player player){
        setTitle(PlaceholderAPI.setPlaceholders(player,SimpleSidebar.config.getTitle(type)));
        List<String> lines = PlaceholderAPI.setPlaceholders(player, SimpleSidebar.config.getLines(type));
        int spaces=0;
        for(int i=0;i<lines.size();i++){
            String line = lines.get(i);
            if(line.matches("^\\s*$")){
                updateLine("&" + spaces++,i);
            }else{
                updateLine(lines.get(i),i);
            }
        }
    }
    public void addLine(Line line,int row) {
        if (row > 0)
            row *= -1;
        OfflinePlayer op = Bukkit.getOfflinePlayer(line.name);
        if ((line.prefix != null) || (line.suffix != null))
        {
            Team team = this.scoreboard.getPlayerTeam(op);
            if (team == null)
                team = this.scoreboard.registerNewTeam(line.name);
            team.addPlayer(op);
            if (line.prefix != null)
                team.setPrefix(line.prefix);
            if (line.suffix != null)
                team.setSuffix(line.suffix);
        }
        if(this.objective==null){
            SimpleSidebar.logger.info("null objective");
        }else {
            Score score = this.objective.getScore(op);
            score.setScore(1);
            score.setScore(row);
        }
    }
    public void updateLine(String str, int row) {
        Line line = new Line(str);
        if (row > 0)
            row *= -1;
//        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(line.pluginName);
//        if (!this.scoreboard.getPlayers().contains(offlinePlayer)) {
        for (OfflinePlayer op : this.scoreboard.getPlayers())
            if (this.objective.getScore(op).getScore() == row)
            {
                this.scoreboard.resetScores(op);
                if (this.scoreboard.getTeam(op.getName()) != null)
                    this.scoreboard.getTeam(op.getName()).unregister();
            }
        addLine(line, row);
//        }else if (line.suffix!=null&&(this.scoreboard.getPlayerTeam(offlinePlayer)==null||this.scoreboard.getPlayerTeam(offlinePlayer).getPrefix()!=line.suffix)){
//            this.scoreboard.getPlayerTeam(offlinePlayer).setPrefix(line.suffix);
//        }else if (line.suffix!=null&&this.scoreboard.getPlayerTeam(offlinePlayer).getSuffix()!=line.suffix){
//            this.scoreboard.getPlayerTeam(offlinePlayer).setSuffix(line.suffix);
//        }
    }
    public Scoreboard getScoreboard()
    {
        return this.scoreboard;
    }

    private class Line{
        private String string;
        public String prefix;
        public String suffix;
        public String name;
        Line(String str){
            this.string = ChatColor.translateAlternateColorCodes('&',str);
            if (string.length() > 48)
                string = string.substring(0, 47);
            if (string.length() <= 16)
                name = string;
            else if (string.length() <= 32)
            {
                name = string.substring(0, 16);
                suffix = string.substring(16,string.length());
            }
            else
            {
                prefix = string.substring(0, 16);
                name = string.substring(16, 32);
                suffix = string.substring(32,string.length());
            }
        }

        @Override
        public String toString() {
            return prefix+name+suffix;
        }
    }
}
