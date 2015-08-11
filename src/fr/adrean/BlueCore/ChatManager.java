package fr.adrean.BlueCore;

import java.util.logging.Level;

import mkremins.fanciful.FancyMessage;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ChatManager implements Listener {
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		e.setCancelled(true);
		for (Player p : Bukkit.getOnlinePlayers()) {
			BlueCore.getFancyName(new FancyMessage(), e.getPlayer(), p, true)
				.then(" » ")
					.color(ChatColor.GRAY)
				.then(e.getMessage())
					.color(Grade.get(e.getPlayer()).isAtLeast(Grade.FONDA) ? ChatColor.WHITE : ChatColor.GRAY)
				.send(p);
		}

	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerDie(final PlayerDeathEvent e) {
		new BukkitRunnable() {
			public void run() {
				Player p = (Player) e.getEntity();
				EntityDamageEvent edme = e.getEntity().getLastDamageCause();
				if (edme == null) return;
				DamageCause edmc = edme.getCause();
				for (Player watcher : Bukkit.getOnlinePlayers()) {
					try {
						FancyMessage msg;
						FancyMessage pname = BlueCore.getFancyName(new FancyMessage(), p, watcher, true);
						FancyMessage defaultmsg = pname.clone().then(" est mort").color(ChatColor.YELLOW);
						if (edmc.equals(DamageCause.BLOCK_EXPLOSION)) {
							msg = pname.clone().then(" a explosé").color(ChatColor.YELLOW);
						} else if (edmc.equals(DamageCause.CONTACT)) {
							msg = pname.clone().then(" s'est piqué").color(ChatColor.YELLOW);
						} else if (edmc.equals(DamageCause.CUSTOM)) {
							msg = defaultmsg.clone();
						} else if (edmc.equals(DamageCause.DROWNING)) {
							msg = pname.clone().then(" s'est noyé").color(ChatColor.YELLOW);
						} else if (edmc.equals(DamageCause.ENTITY_ATTACK) || edmc.equals(DamageCause.THORNS)) {
							if (edme instanceof EntityDamageByEntityEvent) {
								EntityDamageByEntityEvent edbee = (EntityDamageByEntityEvent) edme;
								if (edbee.getDamager() instanceof Player) {
									Player damager = (Player) edbee.getDamager();
									msg = BlueCore.getFancyName(pname.clone().then(" a été tué par ").color(ChatColor.YELLOW), damager, watcher, true);
								} else {
									msg = pname.clone().then(" a été tué par un ").color(ChatColor.YELLOW).then(edbee.getDamager().getName()).color(ChatColor.RED);
								}
							} else {
								msg = defaultmsg.clone();
							}
						} else if (edmc.equals(DamageCause.ENTITY_EXPLOSION)) {
							msg = pname.clone().then(" a explosé").color(ChatColor.YELLOW);
						} else if (edmc.equals(DamageCause.FALL)) {
							msg = pname.clone().then(" est tombé de haut").color(ChatColor.YELLOW);
						} else if (edmc.equals(DamageCause.FALLING_BLOCK)) {
							msg = defaultmsg.clone();
						} else if (edmc.equals(DamageCause.FIRE) || edmc.equals(DamageCause.FIRE_TICK)) {
							msg = pname.clone().then(" est mort brulé").color(ChatColor.YELLOW);
						} else if (edmc.equals(DamageCause.LAVA)) {
							msg = pname.clone().then(" ne sait pas nager dans la lave").color(ChatColor.YELLOW);
						} else if (edmc.equals(DamageCause.LIGHTNING)) {
							msg = defaultmsg.clone();
						} else if (edmc.equals(DamageCause.MAGIC)) {
							msg = pname.clone().then(" s'est pris de la magie noire").color(ChatColor.YELLOW);
						} else if (edmc.equals(DamageCause.MELTING)) {
							msg = defaultmsg.clone();
						} else if (edmc.equals(DamageCause.POISON)) {
							msg = pname.clone().then(" a été empoisonné").color(ChatColor.YELLOW);
						} else if (edmc.equals(DamageCause.PROJECTILE)) {
							msg = pname.clone().then(" s'est pris une flèche").color(ChatColor.YELLOW);
						} else if (edmc.equals(DamageCause.STARVATION)) {
							msg = pname.clone().then(" est mort de faim").color(ChatColor.YELLOW);
						} else if (edmc.equals(DamageCause.SUFFOCATION)) {
							msg = pname.clone().then(" a suffoqué").color(ChatColor.YELLOW);
						} else if (edmc.equals(DamageCause.SUICIDE)) {
							msg = pname.clone().then(" s'est suicidé").color(ChatColor.YELLOW);
						} else if (edmc.equals(DamageCause.VOID)) {
							msg = pname.clone().then(" est tombé dans le vide").color(ChatColor.YELLOW);
						} else if (edmc.equals(DamageCause.WITHER)) {
							msg = defaultmsg.clone();
						} else {
							msg = defaultmsg;
						}
						msg.send(watcher);
					} catch (Exception e) {
						Bukkit.getLogger().log(Level.SEVERE, "Oulah, apparemment le fancymessage y sclone pas !");
						e.printStackTrace();
					}
				}
			}
		}.run();
		e.setDeathMessage(null);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) throws CloneNotSupportedException {
		Player player = e.getPlayer();
		FancyMessage msg = new FancyMessage("[").color(ChatColor.DARK_GRAY)
				.then("+").color(ChatColor.GREEN)
				.then("] ").color(ChatColor.DARK_GRAY);
		for (Player p : Bukkit.getOnlinePlayers()) {
			BlueCore.getFancyName(msg.clone(), player, p, false).send(p);
		}
		e.setJoinMessage(null);
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) throws CloneNotSupportedException {
		Player player = e.getPlayer();
		FancyMessage msg = new FancyMessage("[").color(ChatColor.DARK_GRAY)
				.then("-").color(ChatColor.RED)
				.then("] ").color(ChatColor.DARK_GRAY);
		for (Player p : Bukkit.getOnlinePlayers()) {
			BlueCore.getFancyName(msg.clone(), player, p, false).send(p);
		}
		e.setQuitMessage(null);
	}
	
}
