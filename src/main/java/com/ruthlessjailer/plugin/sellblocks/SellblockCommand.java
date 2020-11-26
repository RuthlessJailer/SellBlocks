package com.ruthlessjailer.plugin.sellblocks;

import com.ruthlessjailer.api.theseus.Chat;
import com.ruthlessjailer.api.theseus.command.CommandBase;
import com.ruthlessjailer.api.theseus.command.SubCommand;
import com.ruthlessjailer.api.theseus.command.SuperiorCommand;
import com.ruthlessjailer.plugin.sellblocks.config.Config;
import com.ruthlessjailer.plugin.sellblocks.config.Messages;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author RuthlessJailer
 */
public class SellblockCommand extends CommandBase implements SuperiorCommand {
	public SellblockCommand() {
		super("sellblock|sb");
	}

	@Override
	protected void runCommand(@NonNull final CommandSender sender, final String[] args, @NonNull final String label) {
		if (args.length == 0) {//no args
			Chat.send(sender, Messages.RUN_HELP);
		}
	}

	@SubCommand(inputArgs = "limit %p<Player> get")
	private synchronized void getLimit(final OfflinePlayer player) {
		if (!hasPermission(this.sender, getCustomPermissionSyntax() + ".limit")) {
			return;//no error message
		}

		final int limit = PlayerCache.getCache(player).getSellblockLimit();

		this.sender.sendMessage(Messages.fillPlaceholders(Messages.PLAYERS_LIMIT, player, null));
	}

	@SubCommand(inputArgs = "limit %p<Player> default")
	private synchronized void defaultLimit(final OfflinePlayer player) {
		if (!hasPermission(this.sender, getCustomPermissionSyntax() + ".limit")) {
			return;//no error message
		}

		PlayerCache.getCache(player).setSellblockLimit(-1);

		this.sender.sendMessage(Messages.fillPlaceholders(Messages.SET_LIMIT, player, null));
	}

	@SubCommand(inputArgs = "limit %p<Player> %i<Limit>")
	private synchronized void setLimit(final OfflinePlayer player, final Integer limit) {
		if (!hasPermission(this.sender, getCustomPermissionSyntax() + ".limit")) {
			return;//no error message
		}

		PlayerCache.getCache(player).setSellblockLimit(limit);

		this.sender.sendMessage(String.format(Messages.SET_LIMIT,
											  player.getName(),
											  limit));
	}

	@SubCommand(inputArgs = "add")
	private synchronized void add() {
		if (!hasPermission(this.sender, getCustomPermissionSyntax() + ".add")) {
			return;//no error message
		}
		final Player      player = getPlayer(this.sender);
		final PlayerCache cache  = PlayerCache.getCache(player);

		final int limit = (cache.getSellblockLimit() == -1
						   ? Config.getInstance().DEFAULT_SELLBLOCK_LIMIT
						   : cache.getSellblockLimit());

		if (!hasPermission(player, getCustomPermissionSyntax() + ".add.bypass") && SellblockRegistry.count(player) > limit) {
			Chat.send(player, Messages.fillPlaceholders(Messages.REACHED_LIMIT, player, null));
			return;
		}

		Chat.send(player, Messages.INSTRUCTION_BREAK_BLOCK_ADD);

		cache.setWaitingToPunchSellblock(true);
		cache.setRemovingSellblock(false);
	}

	@SubCommand(inputArgs = "remove")
	private synchronized void remove() {
		if (!hasPermission(this.sender, getCustomPermissionSyntax() + ".remove")) {
			return;//no error message
		}
		final Player      player = getPlayer(this.sender);
		final PlayerCache cache  = PlayerCache.getCache(player);

		Chat.send(player, Messages.INSTRUCTION_BREAK_BLOCK_REMOVE);

		cache.setWaitingToPunchSellblock(true);
		cache.setRemovingSellblock(true);
	}

	@SubCommand(inputArgs = "save")
	private synchronized void save() {
		if (!hasPermission(this.sender, getCustomPermissionSyntax() + ".save")) {
			return;//no error message
		}
		Chat.send(this.sender, Messages.SAVING);
		SellblockRegistry.save();
	}

	@SubCommand(inputArgs = "load")
	private synchronized void load() {
		if (!hasPermission(this.sender, getCustomPermissionSyntax() + ".load")) {
			return;//no error message
		}
		Chat.send(this.sender, Messages.LOADING);
		SellblockRegistry.load();
	}

	@SubCommand(inputArgs = "reload")
	private synchronized void reload() {
		if (!hasPermission(this.sender, getCustomPermissionSyntax() + ".reload")) {
			return;//no error message
		}
		Chat.send(this.sender, Messages.RELOADING);
	}

	@SubCommand(inputArgs = "list")
	private synchronized void list() {
		if (SellblockRegistry.getAll().isEmpty()) {
			Chat.send(this.sender, Messages.NO_SELLBLOCKS);
			return;
		}
		for (final Sellblock sellblock : SellblockRegistry.getAll()) {
			final Location location = sellblock.getLocation().toLocation();
			Chat.send(this.sender, Messages.fillPlaceholders(Messages.LIST_ITEM, Bukkit.getOfflinePlayer(sellblock.getOwner()), sellblock));
		}
	}

	private Player getPlayer(@NonNull final CommandSender sender) {
		if (!(sender instanceof Player)) {
			Chat.send(sender, Messages.PLAYERS_ONLY);
			throw new CommandException();
		}
		return (Player) sender;
	}

}
