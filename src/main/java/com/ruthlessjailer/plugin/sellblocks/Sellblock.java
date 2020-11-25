package com.ruthlessjailer.plugin.sellblocks;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;

import java.util.UUID;

/**
 * @author RuthlessJailer
 */
@AllArgsConstructor
@Getter
public final class Sellblock {

	private final SerializableLocation location;
	private final UUID                 owner;
	private final Material             type;
	private final MaterialData         data;

}
