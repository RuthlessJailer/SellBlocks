package com.ruthlessjailer.plugin.sellblocks;

import lombok.*;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author RuthlessJailer
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PlayerCache {

	private static final Map<UUID, PlayerCache> CACHE_MAP = new HashMap<>();

	@Getter
	@Setter
	private boolean isWaitingToPunchSellblock = false;

	@Getter
	@Setter
	private Location tempSellblockLocation = null;

	@Getter
	@Setter
	private boolean isRemovingSellblock = false;

	@Getter
	@Setter
	private int sellblockLimit = -1;//-1 is default

	public static PlayerCache getCache(@NonNull final OfflinePlayer player) {
		PlayerCache cache = CACHE_MAP.get(player.getUniqueId());

		if (cache == null) {
			cache = new PlayerCache();
			CACHE_MAP.put(player.getUniqueId(), cache);
		}

		return cache;
	}

}
