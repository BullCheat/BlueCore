package fr.adrean.BlueCore;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class RainfallManager implements Listener {
	
	@EventHandler
	public void onRainfallToggle(WeatherChangeEvent e) {
		if (e.toWeatherState()) {
			e.setCancelled(true);
		}
	}
	
}
