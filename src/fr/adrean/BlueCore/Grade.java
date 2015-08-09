package fr.adrean.BlueCore;

import org.bukkit.ChatColor;

public enum Grade {
	ADMIN(ChatColor.RED, 1001),
	FONDA(ChatColor.RED, 1000),
	LEGENDAIRE(ChatColor.AQUA, 40),
	DIVIN(ChatColor.YELLOW, 30),
	HEROS(ChatColor.GREEN, 20),
	JOUEUR(ChatColor.GRAY, 10);
	
	private ChatColor chatColor;
	private int level;
	
	Grade(ChatColor c, int level) {
		chatColor = c;
		this.level = level;
	}
	
	public ChatColor getChatColor() {
		return chatColor;
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
