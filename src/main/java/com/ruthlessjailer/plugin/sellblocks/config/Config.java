package com.ruthlessjailer.plugin.sellblocks.config;

import com.google.gson.JsonObject;
import com.ruthlessjailer.api.theseus.io.JSONFile;
import com.ruthlessjailer.plugin.sellblocks.Sellblock;
import lombok.Getter;
import lombok.NonNull;

/**
 * @author RuthlessJailer
 */
public final class Config extends JSONFile {

	@Getter
	private static Config instance;

	public final Integer PICKUP_DISTANCE;
	public final String  REGION_ID_FORMAT;
	public final Integer DEFAULT_SELLBLOCK_LIMIT;
	public final Integer SAVE_INTERVAL_MINUTES;
	public final Boolean DEBUG;

	private Config() {
		super("config.json");

		fixConfig(this);

		final JsonObject object = readFile();

		this.PICKUP_DISTANCE         = object.get("pickup_distance").getAsInt();
		this.REGION_ID_FORMAT        = object.get("region_id_format").getAsString();
		this.DEFAULT_SELLBLOCK_LIMIT = object.get("default_sellblock_limit").getAsInt();
		this.SAVE_INTERVAL_MINUTES   = object.get("save_interval_minutes").getAsInt();
		this.DEBUG                   = object.get("debug").getAsBoolean();
	}

	public static String getRegionIdFormat(@NonNull final Sellblock sellblock) {
		return instance.REGION_ID_FORMAT
				.replace("${PLAYER}",
						 sellblock.getOwner().toString())
				.replace("${LOCATION}",
						 String.format("%.0f,%.0f,%.0f",
									   sellblock.getLocation().getX(),
									   sellblock.getLocation().getY(),
									   sellblock.getLocation().getZ()))
				.replace("${TYPE}",
						 sellblock.getData().toString()
								  .replaceAll(" ", "_")
								  .replaceAll("[()]", "/"));
	}

	public static void load() {
		instance = new Config();
	}

}
