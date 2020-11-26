package com.ruthlessjailer.plugin.sellblocks;

import com.ruthlessjailer.api.theseus.Chat;
import com.ruthlessjailer.api.theseus.Common;
import com.ruthlessjailer.plugin.sellblocks.config.Config;
import com.ruthlessjailer.plugin.sellblocks.config.Messages;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.managers.RemovalStrategy;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.math.BigDecimal;

/**
 * @author RuthlessJailer
 */
public class SellblockListener implements Listener {

	@EventHandler
	public void onJoin(final PlayerJoinEvent event) {
		this.clearCache(PlayerCache.getCache(event.getPlayer()));
	}

	@EventHandler
	public void onLeave(final PlayerQuitEvent event) {
		this.clearCache(PlayerCache.getCache(event.getPlayer()));
	}

	private void clearCache(@NonNull final PlayerCache cache) {
		cache.setRemovingSellblock(false);
		cache.setWaitingToPunchSellblock(false);
		cache.setTempSellblockLocation(null);
	}

	@EventHandler
	public void onBreak(final BlockBreakEvent event) {
		final PlayerCache cache = PlayerCache.getCache(event.getPlayer());

		if (cache.isWaitingToPunchSellblock()) {
			event.setCancelled(true);
			cache.setWaitingToPunchSellblock(false);

			if (cache.isRemovingSellblock()) {
				final Location location = event.getBlock().getLocation();

				if (!SellblockRegistry.owns(event.getPlayer(), location) && !Common.hasPermission(event.getPlayer(), "")) {
					Chat.send(event.getPlayer(), Messages.NOT_YOUR_SELLBLOCK);
					return;
				}

				if (!SellblockRegistry.exists(location)) {
					Chat.send(event.getPlayer(), Messages.NOT_A_SELLBLOCK);
					return;
				}

				disableProtection(SellblockRegistry.get(location));

				SellblockRegistry.remove(location);
				Chat.send(event.getPlayer(), Messages.fillPlaceholders(Messages.REMOVED_SELLBLOCK,
																	   event.getPlayer(),
																	   new Sellblock(SerializableLocation.fromLocation(location),
																					 event.getPlayer().getUniqueId(),
																					 Material.ACACIA_DOOR,
																					 Material.ACACIA_DOOR.getNewData((byte) 0))));//dummy sellblock
			} else {

				final Location location = event.getBlock().getLocation();

				if (SellblockRegistry.exists(location)) {
					Chat.send(event.getPlayer(), Messages.ALREADY_A_SELLBLOCK);
					return;
				}

				cache.setTempSellblockLocation(location);

				Chat.send(event.getPlayer(), Messages.fillPlaceholders(Messages.LOCATION_SET, event.getPlayer(),
																	   new Sellblock(SerializableLocation.fromLocation(location),
																					 event.getPlayer().getUniqueId(),
																					 Material.ACACIA_DOOR,
																					 Material.ACACIA_DOOR.getNewData((byte) 0))));//dummy sellblock
				Chat.send(event.getPlayer(), Messages.INSTRUCTION_DROP_ITEM);
			}
			return;
		}

	}

	private void enableProtection(@NonNull final Sellblock sellblock) {
		final ProtectedCuboidRegion region = new ProtectedCuboidRegion(Config.getRegionIdFormat(sellblock),

																	   BlockVector.toBlockPoint(sellblock.getLocation().getX() - 1,
																								sellblock.getLocation().getY() - 1,
																								sellblock.getLocation().getZ() - 1),
																	   BlockVector.toBlockPoint(sellblock.getLocation().getX() + 1,
																								sellblock.getLocation().getY() + 1,
																								sellblock.getLocation().getZ() + 1));//3x3 region or wg goes crazy

		final DefaultDomain owners = region.getOwners();
		owners.addPlayer(sellblock.getOwner());

		region.setFlag(DefaultFlag.BUILD, StateFlag.State.DENY);
		region.setFlag(DefaultFlag.BLOCK_BREAK, StateFlag.State.DENY);
		region.setFlag(DefaultFlag.BLOCK_PLACE, StateFlag.State.DENY);
		region.setFlag(DefaultFlag.PISTONS, StateFlag.State.DENY);
		region.setFlag(DefaultFlag.CREEPER_EXPLOSION, StateFlag.State.DENY);
		region.setFlag(DefaultFlag.OTHER_EXPLOSION, StateFlag.State.DENY);

		region.setDirty(true);

		final RegionContainer container = WorldGuardPlugin.inst().getRegionContainer();
		final RegionManager   regions   = container.get(sellblock.getLocation().toLocation().getWorld());
		regions.addRegion(region);

	}

	private void disableProtection(@NonNull final Sellblock sellblock) {
		final RegionContainer container = WorldGuardPlugin.inst().getRegionContainer();
		final RegionManager   regions   = container.get(sellblock.getLocation().toLocation().getWorld());
		regions.removeRegion(Config.getRegionIdFormat(sellblock), RemovalStrategy.UNSET_PARENT_IN_CHILDREN);
	}

	@EventHandler
	public void onDrop(final PlayerDropItemEvent event) {
		final PlayerCache cache = PlayerCache.getCache(event.getPlayer());
		if (cache.getTempSellblockLocation() != null) {
			event.setCancelled(true);
			final Location location = cache.getTempSellblockLocation();

			for (final Sellblock sellblock : SellblockRegistry.getAll()) {
				if (this.isClose(sellblock.getLocation().toLocation(), event.getItemDrop().getLocation())) {
					if (sellblock.getType() == event.getItemDrop().getItemStack().getType()) {
						Chat.send(event.getPlayer(), Messages.fillPlaceholders(Messages.TOO_CLOSE, null, null));
						return;
					}
				}
			}

			final BigDecimal price = Sellblocks.getEssentials().getWorth().getPrice(Sellblocks.getEssentials(), event.getItemDrop().getItemStack());

			if (price == null) {//item is not sellable
				Chat.send(event.getPlayer(), Messages.CANNOT_SELL);
				return;
			}

			final Sellblock sellblock = new Sellblock(SerializableLocation.fromLocation(location),
													  event.getPlayer().getUniqueId(),
													  event.getItemDrop().getItemStack().getType(),
													  event.getItemDrop().getItemStack().getData());

			SellblockRegistry.add(sellblock);

			enableProtection(sellblock);

			cache.setTempSellblockLocation(null);

			Chat.send(event.getPlayer(), Messages.fillPlaceholders(Messages.ITEM_SET, event.getPlayer(), sellblock));
			Chat.send(event.getPlayer(), Messages.fillPlaceholders(Messages.REGISTERED_SUCCESS, event.getPlayer(), sellblock));
		}
	}

	@EventHandler
	public void onItemCreate(final EntitySpawnEvent event) {
		if (event.getEntityType() != EntityType.DROPPED_ITEM) {
			return;
		}

		for (final Sellblock sellblock : SellblockRegistry.getAll()) {
			if (this.isTooClose(sellblock.getLocation().toLocation(), event.getLocation())) {
				if (((Item) event.getEntity()).getItemStack().getType() == sellblock.getType() &&
					((Item) event.getEntity()).getItemStack().getData().equals(sellblock.getData())) {
					final BigDecimal price = Sellblocks.getEssentials().getWorth().getPrice(Sellblocks.getEssentials(), ((Item) event.getEntity()).getItemStack());
					Sellblocks.getEconomy().depositPlayer(Bukkit.getOfflinePlayer(sellblock.getOwner()), price.doubleValue());
					event.getEntity().remove();
					return;
				}
			}
		}
	}

	private boolean isClose(@NonNull final Location location, @NonNull final Location other) {//for spacing them out
		return Math.abs(location.getX() - other.getX()) <= Config.getInstance().PICKUP_DISTANCE * 2 && Math.abs(location.getZ() - other.getZ()) <= Config.getInstance().PICKUP_DISTANCE * 2;
		//x2 so they don't overlap
	}

	private boolean isTooClose(@NonNull final Location location, @NonNull final Location other) {//for collecting
		return Math.abs(location.getX() - other.getX()) <= Config.getInstance().PICKUP_DISTANCE && Math.abs(location.getZ() - other.getZ()) <= Config.getInstance().PICKUP_DISTANCE;
	}

}
