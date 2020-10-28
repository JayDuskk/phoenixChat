package me.jj9playz.phoenixchat;

import me.clip.placeholderapi.PlaceholderAPI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;


public final class Phoenixchat extends JavaPlugin implements Listener, CommandExecutor {

    public String pluginPrefix = ChatColor.translateAlternateColorCodes('&', "&6&lPhoenix Chat >>&r");


    @Override
    public void onEnable() {
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[me.jj9playz.PhoenixChat] Phoenix Chat is enabled");

        getServer().getPluginManager().registerEvents(this, this);


        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        String joinText = getConfig().getString("pchat-join-msg");
        assert joinText != null;
        joinText = ChatColor.translateAlternateColorCodes('&', joinText);

        String withPlaceholdersSet = PlaceholderAPI.setPlaceholders(event.getPlayer(), joinText);

        event.setJoinMessage(withPlaceholdersSet);

        String tabName = "%vault_rank% %player_name%";
        String tabListName = PlaceholderAPI.setPlaceholders(event.getPlayer(), tabName);

        event.getPlayer().setPlayerListName(tabListName);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChatEvent(AsyncPlayerChatEvent e){
        String msg = e.getMessage();
        Player player = e.getPlayer();

        String formatmsg = getConfig().getString("pchat-chat-format");
        assert formatmsg != null;
        formatmsg = ChatColor.translateAlternateColorCodes('&', formatmsg);
        formatmsg = formatmsg.replaceAll("%msg%", msg);

        String withPlaceholdersSet = PlaceholderAPI.setPlaceholders(player, formatmsg);

        e.setFormat(withPlaceholdersSet);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command cmd, String label, String[] args) {

        Player player = (Player) sender;

        if(cmd.getName().equalsIgnoreCase("rank")){

            String rankMsg = pluginPrefix + ChatColor.GREEN + " Your Current Rank : %vault_rank%";
            rankMsg = PlaceholderAPI.setPlaceholders(player, rankMsg);

            player.sendMessage(rankMsg);
            return true;
        }

        if(cmd.getName().equalsIgnoreCase("msg")){
            if(args.length == 2){

                        Player target = Bukkit.getPlayerExact(args[0]);
                        String message = args[1];
                if(target != null){
                        sender.sendMessage(ChatColor.GOLD + "[me " + ChatColor.RED + "-> " + ChatColor.GOLD + target.getDisplayName() + "] " + ChatColor.WHITE + message);
                        target.sendMessage(ChatColor.GOLD + "[" + player.getDisplayName() + ChatColor.RED + " -> " + ChatColor.GOLD + "me] " + ChatColor.WHITE + message);

                        for(Player online : Bukkit.getOnlinePlayers()){
                            if(online.hasPermission("pchat.spy")){
                                online.sendMessage(pluginPrefix + ChatColor.GRAY + " [" + player.getDisplayName() + " -> " + target.getDisplayName() + "] " + message);
                            }
                        }
                }else{
                    sender.sendMessage(pluginPrefix + ChatColor.RED + " Please Specify an online player");
                }
                return true;
            }
        }

        return false;
    }
}
