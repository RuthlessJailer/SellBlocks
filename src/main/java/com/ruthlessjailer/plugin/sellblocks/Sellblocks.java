package com.ruthlessjailer.plugin.sellblocks;

import com.ruthlessjailer.api.theseus.Chat;
import com.ruthlessjailer.api.theseus.Common;
import com.ruthlessjailer.api.theseus.PluginBase;
import com.ruthlessjailer.plugin.sellblocks.config.Config;
import com.ruthlessjailer.plugin.sellblocks.config.Messages;
import lombok.Getter;
import lombok.SneakyThrows;
import net.ess3.api.IEssentials;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;

public final class Sellblocks extends PluginBase {

	@Getter
	private static Economy     economy;
	@Getter
	private static IEssentials essentials;

	static {
		Chat.setDebugMode(true);
	}

	@SneakyThrows
	@Override
	protected void onStart() {

		Common.runLaterAsync(() -> {
			Chat.debug("Config", "Initializing config files.");
			Config.init();
			Messages.init();
		});

		economy    = this.getServer().getServicesManager().getRegistration(Economy.class).getProvider();
		essentials = (IEssentials) Bukkit.getPluginManager().getPlugin("Essentials");

		SellblockRegistry.load();

		Common.runLaterTimerAsync(20 * 60 * Config.SAVE_INTERVAL_MINUTES, 20 * 60 * Config.SAVE_INTERVAL_MINUTES, SellblockRegistry::save);

		this.registerEvents(new SellblockListener());
		this.registerCommands(new SellblockCommand());
	}
}
