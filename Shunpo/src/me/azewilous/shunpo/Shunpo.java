package me.azewilous.shunpo;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Shunpo extends JavaPlugin implements Listener{

Plugin plugin;
	
	public final Logger azelogs = Logger.getLogger(getName());
	
	public HashMap<Player, Integer> cooldownTime;
	public HashMap<Player, BukkitRunnable> cooldownTask;
	
	FileConfiguration config;
	File cFile;
	
	@Override 
	public void onEnable(){
		PluginDescriptionFile pdf = this.getDescription();
		this.azelogs.info(pdf.getName() + "- Version -" + pdf.getVersion());
		getServer().getPluginManager().registerEvents(this, this);
		
		config = getConfig();
		config.options().copyDefaults(true);
		cFile = new File(getDataFolder(), "config.yml");
		
		config.addDefault("Shunpo.Material", 276);
		config.addDefault("Shunpo.Cooldown", 5);
		saveConfig();
		
		azelogs.info(this + "Config Has Been Loaded!");
		
		cooldownTime = new HashMap<Player, Integer>();
		cooldownTask = new HashMap<Player, BukkitRunnable>();
	
		this.getCommand("shreload").setExecutor(new Reload(this));
	}

	@Override
    public void onDisable(){
    	PluginDescriptionFile pdfFile = this.getDescription();
        this.azelogs.info(pdfFile.getName() + " Version " + pdfFile.getVersion());
    }
	
	@SuppressWarnings("deprecation")
	@EventHandler
    public boolean swrodTeleEvent(PlayerInteractEvent event) {
	    Player player = (Player) event.getPlayer();
		
	    
	    
        if((player.getItemInHand().getType().getId() == this.getConfig().getInt("Shunpo.Material")) && (player.hasPermission("shunpo.use") || player.isOp())){
        	if(cooldownTime.containsKey(player)){
        		player.sendMessage(ChatColor.RED + "You must wait for " + cooldownTime.get(player) + " seconds.");
        		return true;
        	} 
        	if(event.getAction() == Action.RIGHT_CLICK_AIR){
        		for (int x = 0; x < 100; x++) {
        			List<Entity> players = player.getNearbyEntities(x, 10, x);
        			for (Entity ent : players) {
        			if (ent.getType().equals(EntityType.PLAYER)) {
        				Location loc = ent.getLocation();
        				player.teleport(ent.getLocation().subtract(0.9, 0.0, 0.0));
        				player.playEffect(loc, Effect.EXTINGUISH, 100);
        				player.playSound(loc, Sound.DIG_GRASS, 10, 0.5f);
        				
        				cooldownTime.put(player, this.getConfig().getInt("Shunpo.Cooldown"));
                        cooldownTask.put(player, new BukkitRunnable() {
                                public void run() {
                                        cooldownTime.put(player, cooldownTime.get(player) - 1);
                                        if (cooldownTime.get(player) == 0) {
                                                cooldownTime.remove(player);
                                                cooldownTask.remove(player);
                                                cancel();
                                        }
                                }
                        });
                       
                        cooldownTask.get(player).runTaskTimer(this, 20, 20);
                        
                        return true;
                }
        	}
          }
        }		
     }
		return true; 
   }
}
