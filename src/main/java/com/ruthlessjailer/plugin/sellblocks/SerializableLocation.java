package com.ruthlessjailer.plugin.sellblocks;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.UUID;

/**
 * @author RuthlessJailer
 */
@Getter
@AllArgsConstructor
public final class SerializableLocation {

	private final UUID   world;
	private final double x, y, z;
	private final float pitch, yaw;

	public static SerializableLocation fromLocation(@NonNull final Location location) {
		return new SerializableLocation(location.getWorld().getUID(), location.getX(), location.getY(), location.getZ(), location.getPitch(), location.getYaw());
	}

	public Location toLocation() {
		return new Location(Bukkit.getWorld(this.world), this.x, this.y, this.z, this.pitch, this.yaw);
	}

}
