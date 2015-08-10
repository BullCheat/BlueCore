package fr.adrean.BlueCore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
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
	public static String serverName;
	public static HashMap<String, CacheEntry> apiCache = new HashMap<String, CacheEntry>();
	
	@Override
	public void onEnable() {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new RainfallManager(), this);
		pm.registerEvents(new ChatManager(), this);
		initEffectPlayer();
		switch (getServer().getPort()) {
		case 25600:
			serverName = "hub";
			break;
		case 25700:
			serverName = "survivor";
			break;
		case 25800:
			serverName = "factions";
			break;

		default:
			break;
		}
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
		Grade grade = Grade.get(p);
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
		return Grade.get(p).getChatColor() + p.getName();
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
	
	public static String readURL(String str) {
        StringBuilder b = new StringBuilder();
        String ret = null;
		URL url;
	    InputStream is = null;
	    BufferedReader br;
	    String line;

	    try {
	        url = new URL(str);
	        is = url.openStream();  // throws an IOException
	        br = new BufferedReader(new InputStreamReader(is));

	        
	        while ((line = br.readLine()) != null) {
	        	b.append(line);
	            b.append("\n");
	        }
	        ret = b.substring(0, b.length() - 1);
	        return ret;
	    } catch (MalformedURLException mue) {
	         mue.printStackTrace();
	    } catch (IOException ioe) {
	         ioe.printStackTrace();
	    } finally {
	        try {
	            if (is != null) is.close();
	        } catch (IOException ioe) {
	            // nothing to see here
	        }
	    }
	    return ret;
	}

	public static String askAPI(String string, int timeout) {
		if (apiCache.containsKey(string)) {
			CacheEntry e = apiCache.get(string);
			if (e.isStillValid(timeout)) {
				return e.getResult();
			}
		}
		String ret = readURL("http://survivormc.fr/api/" + string);
		apiCache.put(string, new CacheEntry(ret));
		return ret;
	}
	
}
