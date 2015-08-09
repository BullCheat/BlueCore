package fr.adrean.BlueCore;

import mkremins.fanciful.FancyMessage;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class FactionsHelper {

	public static FancyMessage getFancyFactionBeforeName(FancyMessage msg,
			OfflinePlayer p, Player watcher) {

		String factionStar = "";
		ChatColor fColor;
		com.massivecraft.factions.entity.MPlayer player = com.massivecraft.factions.entity.MPlayer.get(p);
		com.massivecraft.factions.entity.MPlayer w = com.massivecraft.factions.entity.MPlayer.get(watcher);
		com.massivecraft.factions.Rel rel = w.getRelationTo(player);
		switch (rel) {
			case NEUTRAL:
				fColor = ChatColor.GRAY;
				break;
			case ENEMY:
				fColor = ChatColor.RED;
				break;
			case ALLY:
				fColor = ChatColor.LIGHT_PURPLE;
				break;
			case TRUCE:
				fColor = ChatColor.GREEN;
				break;
			case LEADER:
			case OFFICER:
			case MEMBER:
			case RECRUIT:
				fColor = ChatColor.GOLD;
				break;
			default:
				fColor = ChatColor.GRAY;
		}
		com.massivecraft.factions.Rel role = player.getRole();
		switch (role) {
			case OFFICER:
				factionStar = "*";
				break;
			case LEADER:
				factionStar = "**";
		default:
			break;
		}
		
		com.massivecraft.factions.entity.Faction faction = player.getFaction();
		if (faction.isNormal()) {
			msg = msg.text(factionStar + faction.getName() + " ")
					.color(fColor)
				.then();
		}
		return msg;
		
	}

}
