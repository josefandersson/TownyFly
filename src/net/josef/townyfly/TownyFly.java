package net.josef.townyfly;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.palmergames.bukkit.towny.Towny;
import com.palmergames.bukkit.towny.event.PlayerChangePlotEvent;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.TownBlock;
import com.palmergames.bukkit.towny.object.TownyUniverse;

import net.md_5.bungee.api.ChatColor;

public class TownyFly extends JavaPlugin implements Listener {
	
	private ArrayList<String> playersWithTFly;
	
	
	@Override
	public void onEnable() {
		playersWithTFly = new ArrayList<String>();
		
		Towny towny = (Towny) getServer().getPluginManager().getPlugin("Towny");
		
		if (towny == null) {
			getPluginLoader().disablePlugin(this);
			getLogger().info("TownyFly needs Towny to work.");
		} else {
			getLogger().info("TownyFly running as should.");
		}
		
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	
	@Override
	public void onDisable() {
		Player p;
		for (String name : playersWithTFly) {
			p = getServer().getPlayer(name);
			if (p != null) {
				teleportToGround(p);
				p.setAllowFlight(false);
			}
		}
	}
	
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {		
		Player p = null;
		if (sender instanceof Player) {
			p = (Player) sender;
		}
		
		if (args.length > 0) {
			if (p == null || p.hasPermission(PermissionNodes.TOGGLE_OTHERS)) {
				Player target = Bukkit.getPlayer(args[0]);
				if (target != null) {
					toggleTFlyFor(target);
				}
				return true;
			}
		} else {
			if (p.hasPermission(PermissionNodes.TOGGLE_SELF)) {
				toggleTFlyFor(p);
				return true;
			}
		}
		return false;
	}
	
	
	public static void teleportToGround(Player player) {
		Location loc = player.getLocation();
		for (int y = loc.getBlockY(); 0 < y; y--) {
			loc.setY(y);
			if (loc.getBlock().getType() != Material.AIR) {
				player.teleport(loc.add(0, 2, 0));
				break;
			}
		}
	}

	
	
	public void disableTFlyFor(Player player, boolean silent) {
		playersWithTFly.remove(player.getName());

		if (!silent)
			player.sendMessage(ChatColor.AQUA + "Disabled townyfly for player " + ChatColor.BLUE + player.getName() + ChatColor.AQUA + ".");
		
		player.setFallDistance(0f);
		player.setAllowFlight(false);
		player.setFlying(false);
	}
	
	
	public void enableTFlyFor(Player player, boolean silent) {
		playersWithTFly.add(player.getName());
		
		if (!silent)
			player.sendMessage(ChatColor.AQUA + "Enabled townyfly for player " + ChatColor.BLUE + player.getName() + ChatColor.AQUA + ".");
		
		Resident resident = null;
		TownBlock townBlock = null;
		
		try {
			resident = TownyUniverse.getDataSource().getResident(player.getName());
			townBlock = TownyUniverse.getTownBlock(player.getLocation());
			
			if (townBlock.getTown() == resident.getTown()) {
				
				player.setFallDistance(0f);
				player.setAllowFlight(true);
				
			}
		} catch (NotRegisteredException|NullPointerException e) {
		}
	}
	
	
	public void toggleTFlyFor(Player player) {
		if (playersWithTFly.contains(player.getName())) {
			disableTFlyFor(player, false);
		} else {
			enableTFlyFor(player, false);
		}
	}
	
	
	public void enableIfAllowed(Player player) {
		
	}
	

	
}
