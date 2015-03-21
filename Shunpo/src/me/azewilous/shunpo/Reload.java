package me.azewilous.shunpo; 

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Reload implements CommandExecutor {

Shunpo plugin;

	public Reload(Shunpo playerAbilities) {
		this.plugin = playerAbilities;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel,
			String[] args) {

		if(args.length == 0){
			if(sender.hasPermission("sh.reload") || sender.isOp()){
			plugin.reloadConfig();
			plugin.getPluginLoader().disablePlugin(plugin);
			plugin.getPluginLoader().enablePlugin(plugin);
			Bukkit.broadcastMessage(ChatColor.BOLD + "The Plguin " + plugin + " Has Been Reloaded");
		} else {
			sender.sendMessage(ChatColor.DARK_RED + "Too Many Arguments");
		}
	} else {
		sender.sendMessage(ChatColor.GRAY + "You need the permission " + ChatColor.RED + "sh.reload" + ChatColor.GRAY + " To Use This Command");
	}
		return true;
	}

}
