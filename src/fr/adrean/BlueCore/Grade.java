package fr.adrean.BlueCore;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

public enum Grade {
	MANAGER(ChatColor.DARK_RED, 1400, "Manager"),
	ADMIN(ChatColor.DARK_RED, 1200, "Administrateur"),
	FONDA(ChatColor.RED, 1000, "Fondateur"),
	STAFF(ChatColor.YELLOW, 800, "Staff"),
	GUIDE(ChatColor.BLUE, 500, "Guide"),
	ARCHITECTE(ChatColor.DARK_GREEN, 250, "Architecte"),
	LEGENDAIRE(ChatColor.AQUA, 40, "Légendaire"),
	DIVIN(ChatColor.GOLD, 30, "Divin"),
	HEROS(ChatColor.GREEN, 20, "Héros"),
	JOUEUR(ChatColor.GRAY, 10, "Joueur");
	
	private ChatColor chatColor;
	private int level;
	private String prefix;
	
	Grade(ChatColor c, int level, String name) {
		chatColor = c;
		this.level = level;
		this.prefix = name;
	}
	
	public static Grade getByID(int id) {
		for (Grade g : Grade.values()) {
			if (g.getLevel() == id) {
				return g;
			}
		}
		Bukkit.getLogger().log(Level.SEVERE, "==== BLUECORE / GET GRADE BY ID ====");
		Bukkit.getLogger().log(Level.SEVERE, "Can't find grade by id " + id);
		Bukkit.getLogger().log(Level.SEVERE, "Returning null, errors will occur!");
		Bukkit.getLogger().log(Level.SEVERE, "==== BLUECORE / GET GRADE BY ID ====");
		return null;
	}
	
	public static Grade get(OfflinePlayer p) {
		return getByID(Integer.parseInt(BlueCore.askAPI("player/" + p.getUniqueId().toString().replaceAll("-", "") + "/grade/" + BlueCore.serverName, 10*1000)));
	}
	
	public ChatColor getChatColor() {
		return chatColor;
	}
	
	public String getPrefix() {
		return this.prefix;
	}
	
	public int getLevel() {
		return this.level;
	}
	
	public boolean isEqual(Grade g) {
		if (this.level == g.level) 
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public boolean isGreaterThan(Grade g) {
		if (this.level > g.level)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public boolean isAtLeast(Grade g) {
		if (this.level >= g.level)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	public boolean isLowerThan(Grade g) {
		if (this.level < g.level)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	public boolean isAtMost(Grade g) {
		if (this.level <= g.level)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
