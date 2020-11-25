package com.ruthlessjailer.plugin.sellblocks;

import com.google.gson.reflect.TypeToken;
import com.ruthlessjailer.api.theseus.Chat;
import com.ruthlessjailer.api.theseus.io.JSONFile;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * @author RuthlessJailer
 */
public final class SellblockRegistry extends JSONFile {

	private static final Map<UUID, List<Sellblock>> mapped   = new HashMap<>();
	@Getter
	private static final List<Sellblock>            all      = new ArrayList<>();
	private static final SellblockRegistry          registry = new SellblockRegistry();


	private SellblockRegistry() {
		super("sellblocks.json");
	}

	public static void add(@NonNull final Sellblock sellblock) {
		mapped.computeIfAbsent(sellblock.getOwner(), k -> new ArrayList<>());

		mapped.get(sellblock.getOwner()).add(sellblock);
		all.add(sellblock);
	}

	public static void remove(@NonNull final Location location) {
		final Sellblock sellblock = get(location);

		if (sellblock != null) {
			all.remove(sellblock);
			mapped.get(sellblock.getOwner()).remove(sellblock);
		}
	}

	public static boolean exists(@NonNull final Location location) {
		return get(location) != null;
	}

	public static Sellblock get(@NonNull final Location location) {
		for (final Sellblock sellblock : all) {
			if (sellblock.getLocation().toLocation().equals(location)) {
				return sellblock;
			}
		}
		return null;
	}

	public static List<Sellblock> get(@NonNull final Player player) {
		return mapped.get(player.getUniqueId());
	}

	public static int count(@NonNull final Player player) {
		return mapped.get(player.getUniqueId()).size();
	}

	public static boolean owns(@NonNull final Player player, @NonNull final Location location) {
		final Sellblock sellblock = get(location);
		return sellblock != null && sellblock.getOwner().equals(player.getUniqueId());
	}

	@SneakyThrows
	public static void load() {
		Chat.info("Loading Sellblocks...");
		final Map<UUID, List<Sellblock>> in = registry.GSON.fromJson(registry.read(), new TypeToken<Map<UUID, List<Sellblock>>>() {}.getType());
		mapped.clear();
		all.clear();

		if (in != null) {
			mapped.putAll(in);
			in.values().forEach(all::addAll);
		}
	}

	@SneakyThrows
	public static void save() {
		Chat.info("Saving Sellblocks...");
		registry.write(registry.GSON.toJson(mapped));
	}
}
