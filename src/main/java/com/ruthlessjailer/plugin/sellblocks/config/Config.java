package com.ruthlessjailer.plugin.sellblocks.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ruthlessjailer.api.theseus.Chat;
import com.ruthlessjailer.api.theseus.Checks;
import com.ruthlessjailer.api.theseus.io.JSONFile;
import com.ruthlessjailer.plugin.sellblocks.Sellblock;
import lombok.NonNull;

/**
 * @author RuthlessJailer
 */
public final class Config extends JSONFile {


	public static final Integer PICKUP_DISTANCE;
	public static final String  REGION_ID_FORMAT;
	public static final Integer DEFAULT_SELLBLOCK_LIMIT;
	public static final Integer SAVE_INTERVAL_MINUTES;
	public static final Boolean DEBUG;

	private static final JSONFile instance = new Config();

	static {
		fixConfig(instance);
		final JsonElement element = new JsonParser().parse(instance.read());
		if (element == null) {//this shouldn't happen but it's here as a fail-safe
			Chat.warning("Could not load config properly; loading default values. If this problem persists please contact the developer.");
			PICKUP_DISTANCE         = 25;
			REGION_ID_FORMAT        = "sellblock_${PLAYER}_/${LOCATION}/_${TYPE}";
			DEFAULT_SELLBLOCK_LIMIT = 10;
			SAVE_INTERVAL_MINUTES   = 10;
			DEBUG                   = false;
		} else {
			final JsonObject object = element.getAsJsonObject();
			PICKUP_DISTANCE         = object.get("pickup_distance").getAsInt();
			REGION_ID_FORMAT        = object.get("region_id_format").getAsString();
			DEFAULT_SELLBLOCK_LIMIT = object.get("default_sellblock_limit").getAsInt();
			SAVE_INTERVAL_MINUTES   = object.get("save_interval_minutes").getAsInt();
			DEBUG                   = object.get("debug").getAsBoolean();
		}

	}


	private Config() {
		super("config.json");
	}

	public static String getRegionIdFormat(@NonNull final Sellblock sellblock) {
		return REGION_ID_FORMAT
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

	/**
	 * Attempts to reload all constants.
	 */
	public static void reload() {
		fixConfig(instance);
		reloadConfig(instance);
	}

	/**
	 * Useless method to call so that class loads.
	 */
	public static void init() {
		Checks.verify(instance != null, "Could not initialize class!");
	}

}
