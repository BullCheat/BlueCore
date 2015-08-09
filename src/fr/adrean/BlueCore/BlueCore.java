package fr.adrean.BlueCore;

import java.util.HashMap;

import mkremins.fanciful.FancyMessage;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class BlueCore extends JavaPlugin {
	
	private static HashMap<String, HashMap<Effect, Integer>> playerEffects = new HashMap<String, HashMap<Effect, Integer>>();
	
	@Override
	public void onEnable() {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new RainfallManager(), this);
		pm.registerEvents(new ChatManager(), this);
		initEffectPlayer();
	}
	
	private void initEffectPlayer() {
		new BukkitRunnable() {
			@Override
			public void run() {
				for (String p : playerEffects.keySet()) {
					if (Bukkit.getPlayer(p) == null) {
						playerEffects.remove(p);
					}
					HashMap<Effect, Integer> a = playerEffects.get(p);
					if (a.size() == 0) {
						playerEffects.remove(p);
						continue;
					}
					for (Effect e : a.keySet()) {
						Bukkit.getPlayer(p).getWorld().playEffect(Bukkit.getPlayer(p).getLocation(), e, 0);
						if (a.get(e) > 1) {
							a.put(e, a.get(e) - 1);
						} else {
							a.remove(e);
						}
					}
				}
			}
		}.runTaskTimer(this, 1, 1);
		
	}

	public static FancyMessage getFancyName(FancyMessage msg, OfflinePlayer p, Player watcher) {
		if (msg.latest().hasText()) msg = msg.then();
		Grade grade = getGrade(p);
		ChatColor color = grade.getChatColor();
		if (Bukkit.getPluginManager().isPluginEnabled("Factions")) {
			msg = FactionsHelper.getFancyFactionBeforeName(msg, p, watcher);
		}
		msg = msg.text(p.getName())
				.color(color)
				.tooltip(color + p.getName(),
					"§6Joueurs tués : §b" + 1,
					"§6Nombre de morts : §b" + 12)
					.suggest("/msg " + p.getName() + " ");
			
		return msg;
	}
	
	public static String getColorName(OfflinePlayer p) {
		return getGrade(p).getChatColor() + p.getName();
	}

	public static Grade getGrade(OfflinePlayer p) {
		if (p.getName().equalsIgnoreCase("BullCheat")) {
			return Grade.ADMIN;
		} else if (p.getName().equalsIgnoreCase("ouroulboulouk")) {
			return Grade.FONDA;
		} else if (p.getName().equalsIgnoreCase("caluce")) {
			return Grade.FONDA;
		} else if (p.getName().equalsIgnoreCase("d01t")) {
			return Grade.HEROS;
		} else {
			return Grade.JOUEUR;
		}
	}

	public static void boost(Player player, int i) {
		//i += i;
		Location l = player.getLocation().clone();
		float f = l.getPitch();
		f*=f;
		f = 8100 - f;
		f /= 8100;
		f += 0.2;
		l.setPitch(0);
		player.setVelocity(l.getDirection().multiply(i*f).add(new Vector(Math.random() / 5, 0.75 + Math.random() / 5, Math.random() / 5)));
	}
	
	public static void verticalBoost(Player player, int i) {
		//i += i;
		player.setVelocity(player.getVelocity().add(new Vector(0, i, 0)));
	}
	
	public static void spawnParticles(Player p, Effect effect, int ticks) {
		HashMap<Effect, Integer> hm = null;
		if (playerEffects.containsKey(p.getName())) {
			hm = playerEffects.get(p);
		}
		if (hm == null) {
			hm = new HashMap<Effect, Integer>();
		}
		if (hm.containsKey(effect)) {
			if (hm.get(effect) < ticks) {
				hm.put(effect, ticks);
			}
		} else {
			hm.put(effect, ticks);
		}
		playerEffects.put(p.getName(), hm);
	}

	public static boolean isGonnaTouchGround(Player player) {
		Vector v = player.getVelocity();
		Location l = player.getLocation();
		if (l.getWorld().getBlockAt(l.add(v)).getType() != Material.AIR) {
			return true;
		}
		return false;
	}
	
}
