package com.ruthlessjailer.plugin.sellblocks;

import com.ruthlessjailer.api.theseus.Chat;
import com.ruthlessjailer.api.theseus.Common;
import com.ruthlessjailer.api.theseus.MinecraftVersion;
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

		if (MinecraftVersion.CURRENT_VERSION != MinecraftVersion.v1_12) {
			Chat.warning("Unsupported server version detected. Proceed with caution.");
		}

		economy    = this.getServer().getServicesManager().getRegistration(Economy.class).getProvider();
		essentials = (IEssentials) Bukkit.getPluginManager().getPlugin("Essentials");

		if (economy == null) {
			Chat.severe("Vault not found! Please download it from https://www.spigotmc.org/resources/vault.34315/");
			setEnabled(false);
			return;
		}
		if (essentials == null) {
			Chat.severe("Essentials not found! Please download it from https://www.spigotmc.org/resources/essentialsx.9089/");
			setEnabled(false);
			return;
		}

		Common.runLaterAsync(() -> {
			Chat.debug("Config", "Initializing config files.");
			Config.load();
			Messages.load();
			Common.runLaterTimerAsync(20 * 60 * Config.getInstance().SAVE_INTERVAL_MINUTES,
									  20 * 60 * Config.getInstance().SAVE_INTERVAL_MINUTES,
									  SellblockRegistry::save);
		});

		Common.runLaterAsync(SellblockRegistry::load);


		registerEvents(new SellblockListener());
		registerCommands(new SellblockCommand());
	}
}
