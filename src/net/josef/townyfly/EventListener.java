package net.josef.townyfly;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.palmergames.bukkit.towny.event.PlayerChangePlotEvent;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.TownyUniverse;

public class EventListener implements Listener {

	
	@EventHandler
	public void onLogout(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		
		if (Tracker.remove(player)) {
			TownyFly.teleportToGround(player);
		}
	}
	
	
	@EventHandler
	public void onWorldChange(PlayerChangedWorldEvent e) {
		Player player = e.getPlayer();
		
		Tracker.remove(player);
	}
	
	
	@EventHandler
	public void onChangePlot(PlayerChangePlotEvent e) {
		Player player = e.getPlayer();
		Resident resident = null;
		
		if (Tracker.has(player)) {
			try {
				resident = TownyUniverse.getDataSource().getResident(player.getName());
				
				if (resident.getTown() == e.getTo().getTownBlock().getTown()) {
					
					player.setFallDistance(0f);
					player.setAllowFlight(true);
					
				} else {
					
					player.setFallDistance(0f);
					player.setAllowFlight(false);
					player.setFlying(false);
					
				}
			} catch (NotRegisteredException e1) {
				player.setFallDistance(0f);
				
				if (player.isFlying()) {
					TownyFly.teleportToGround(player);
				}
				
				player.setAllowFlight(false);
				player.setFlying(false);
			}
		}
	}

}
