package net.josef.townyfly;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.entity.Player;

public class Tracker {

	private static ArrayList<UUID> playersWithTFly;

	static {
		playersWithTFly = new ArrayList<UUID>();
	}
	
	
	public static boolean add(Player player) {
		if (playersWithTFly.contains(player.getUniqueId())) {
			return false;
		}
		
		playersWithTFly.add(player.getUniqueId());
		return true;
	}
	
	
	public static boolean remove(Player player) {
		if (!playersWithTFly.contains(player.getUniqueId())) {
			return false;
		}
		
		playersWithTFly.remove(player.getUniqueId());
		return true;
	}
	
	
	public static boolean has(Player player) {
		return playersWithTFly.contains(player.getUniqueId());
	}
	
	
	public static boolean canPlayerTogglePlayer(Player sender, Player target) {
		if (sender == null)
			return true;
		
		if (sender == target)
			return sender.hasPermission(PermissionNodes.TOGGLE_SELF);
		
		return sender.hasPermission(PermissionNodes.TOGGLE_OTHERS);
	}
	
	
	public static void allowTFlyFor(Player player) {
		
	}
}
