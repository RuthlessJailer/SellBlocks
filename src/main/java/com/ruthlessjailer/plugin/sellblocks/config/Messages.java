package com.ruthlessjailer.plugin.sellblocks.config;

import com.google.gson.JsonObject;
import com.ruthlessjailer.api.theseus.io.JSONFile;
import com.ruthlessjailer.plugin.sellblocks.PlayerCache;
import com.ruthlessjailer.plugin.sellblocks.Sellblock;
import com.ruthlessjailer.plugin.sellblocks.SellblockCommand;
import com.ruthlessjailer.plugin.sellblocks.SellblockListener;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.OfflinePlayer;

/**
 * @author RuthlessJailer
 */
public final class Messages extends JSONFile {

	@Getter
	private static Messages instance = new Messages();
	public final   String   ALREADY_A_SELLBLOCK;
	public final   String   LOCATION_SET;
	public final   String   INSTRUCTION_DROP_ITEM;
	public final   String   TOO_CLOSE;
	public final   String   CANNOT_SELL;
	public final   String   ITEM_SET;
	public final   String   REGISTERED_SUCCESS;
	public final   String   RUN_HELP;
	public final   String   PLAYERS_LIMIT;

	public final String SET_LIMIT;
	public final String REACHED_LIMIT;
	public final String INSTRUCTION_BREAK_BLOCK_ADD;
	public final String SAVING;
	public final String LOADING;
	public final String RELOADING;
	public final String NO_SELLBLOCKS;
	public final String LIST_ITEM;
	public final String PLAYERS_ONLY;
	public final String NOT_YOUR_SELLBLOCK;
	public final String NOT_A_SELLBLOCK;
	public final String REMOVED_SELLBLOCK;
	public final String INSTRUCTION_BREAK_BLOCK_REMOVE;


//	static {
//		fixConfig(instance);
//		final JsonElement element = new JsonParser().parse(instance.read());
//		if (element == null) {//this shouldn't happen but it's here as a fail-safe
//			RUN_HELP                       = "&eRun &b/sellblocks help &cfor a list of commands";
//			PLAYERS_LIMIT                  = "&b${PLAYER}'s &asellblock limit is &b${LIMIT}&a.";
//			SET_LIMIT                      = "&aSet &b${PLAYER}'s &asellblock limit to &b${LIMIT}&a.";
//			REACHED_LIMIT                  = "&cYou have reached the limit of &b${LIMIT} &csellblocks.";
//			INSTRUCTION_BREAK_BLOCK_ADD    = "&eBreak the block that you wish to make a sellblock";
//			INSTRUCTION_BREAK_BLOCK_REMOVE = "&eBreak the block that you wish to remove as a sellblock";
//			SAVING                         = "saving";
//			LOADING                        = "loading";
//			RELOADING                      = "reloading";
//			NO_SELLBLOCKS                  = "&cDoh! There are no sellblocks! &aRun &b/sellblock add &ato make the first one!";
//			LIST_ITEM                      = "&b${PLAYER} &3@ &b${LOCATION}&3: &b${TYPE}";
//			PLAYERS_ONLY                   = "&cPlayers only, retard";
//
//			NOT_YOUR_SELLBLOCK    = "&cThat's not one of your sellblocks idiot";
//			NOT_A_SELLBLOCK       = "&cThat's not a sellblock, dingus";
//			REMOVED_SELLBLOCK     = "&6Removed sellblock at location &8${LOCATION}";
//			ALREADY_A_SELLBLOCK   = "&cThat's already a sellblock, retard";
//			LOCATION_SET          = "&aLocation set to &8${LOCATION}";
//			INSTRUCTION_DROP_ITEM = "&eDrop the item that you wish to collect";
//			TOO_CLOSE             = "&cToo close to another sellblock of the same type; please be at least ${DISTANCE} blocks away";
//			CANNOT_SELL           = "&cYou cannot sell that type of item.";
//			ITEM_SET              = "&aItem set to ${TYPE}";
//			REGISTERED_SUCCESS    = "&6Successfully registered a sellblock to &b%s &6at &8${LOCATION} &6with item &b%s";
//
//		} else {
//			final JsonObject object = element.getAsJsonObject();
//
//			RUN_HELP                       = object.get("run_help").getAsString();
//			PLAYERS_LIMIT                  = object.get("players_limit").getAsString();
//			SET_LIMIT                      = object.get("set_limit").getAsString();
//			REACHED_LIMIT                  = object.get("reached_limit").getAsString();
//			INSTRUCTION_BREAK_BLOCK_ADD    = object.get("instruction_break_block_add").getAsString();
//			INSTRUCTION_BREAK_BLOCK_REMOVE = object.get("instruction_break_block_remove").getAsString();
//			SAVING                         = object.get("saving").getAsString();
//			LOADING                        = object.get("loading").getAsString();
//			RELOADING                      = object.get("reloading").getAsString();
//			NO_SELLBLOCKS                  = object.get("no_sellblocks").getAsString();
//			LIST_ITEM                      = object.get("list_item").getAsString();
//			PLAYERS_ONLY                   = object.get("players_only").getAsString();
//
//			NOT_YOUR_SELLBLOCK    = object.get("not_your_sellblock").getAsString();
//			NOT_A_SELLBLOCK       = object.get("not_a_sellblock").getAsString();
//			REMOVED_SELLBLOCK     = object.get("removed_sellblock").getAsString();
//			ALREADY_A_SELLBLOCK   = object.get("already_a_sellblock").getAsString();
//			LOCATION_SET          = object.get("location_set").getAsString();
//			INSTRUCTION_DROP_ITEM = object.get("instruction_drop_item").getAsString();
//			TOO_CLOSE             = object.get("too_close").getAsString();
//			CANNOT_SELL           = object.get("cannot_sell").getAsString();
//			ITEM_SET              = object.get("item_set").getAsString();
//			REGISTERED_SUCCESS    = object.get("registered_success").getAsString();
//
//		}
//	}

	private Messages() {
		super("messages.json");

		final JsonObject object = readFile();

		this.RUN_HELP                       = object.get("run_help").getAsString();
		this.PLAYERS_LIMIT                  = object.get("players_limit").getAsString();
		this.SET_LIMIT                      = object.get("set_limit").getAsString();
		this.REACHED_LIMIT                  = object.get("reached_limit").getAsString();
		this.INSTRUCTION_BREAK_BLOCK_ADD    = object.get("instruction_break_block_add").getAsString();
		this.INSTRUCTION_BREAK_BLOCK_REMOVE = object.get("instruction_break_block_remove").getAsString();
		this.SAVING                         = object.get("saving").getAsString();
		this.LOADING                        = object.get("loading").getAsString();
		this.RELOADING                      = object.get("reloading").getAsString();
		this.NO_SELLBLOCKS                  = object.get("no_sellblocks").getAsString();
		this.LIST_ITEM                      = object.get("list_item").getAsString();
		this.PLAYERS_ONLY                   = object.get("players_only").getAsString();

		this.NOT_YOUR_SELLBLOCK    = object.get("not_your_sellblock").getAsString();
		this.NOT_A_SELLBLOCK       = object.get("not_a_sellblock").getAsString();
		this.REMOVED_SELLBLOCK     = object.get("removed_sellblock").getAsString();
		this.ALREADY_A_SELLBLOCK   = object.get("already_a_sellblock").getAsString();
		this.LOCATION_SET          = object.get("location_set").getAsString();
		this.INSTRUCTION_DROP_ITEM = object.get("instruction_drop_item").getAsString();
		this.TOO_CLOSE             = object.get("too_close").getAsString();
		this.CANNOT_SELL           = object.get("cannot_sell").getAsString();
		this.ITEM_SET              = object.get("item_set").getAsString();
		this.REGISTERED_SUCCESS    = object.get("registered_success").getAsString();
	}

	public static String fillPlaceholders(@NonNull final String message, final OfflinePlayer player, final Sellblock sellblock) {
		String modified = message;

		if (player != null) {//fill in player placeholders
			final PlayerCache cache = PlayerCache.getCache(player);
			modified = modified.replace("${PLAYER}", player.getName())
							   .replace("${LIMIT}", String.valueOf((cache.getSellblockLimit() == -1)
																   ? ("default")
																   : (cache.getSellblockLimit())));
		}

		if (sellblock != null) {//fill in sellblock placeholders
			modified = modified.replace("${LOCATION}", String.format("%s,%s,%s",
																	 sellblock.getLocation().getX(),
																	 sellblock.getLocation().getY(),
																	 sellblock.getLocation().getZ()))
							   .replace("${TYPE}", sellblock.getData().toString());
		}

		return modified.replace("${DISTANCE}", String.valueOf(Config.getInstance().PICKUP_DISTANCE * 2));//x2 to eliminate overlap
	}

	public static void load() {
		instance = new Messages();
		fixConfig(instance);
		loadConfig(instance);

		SellblockCommand.setMessages(instance);
		SellblockListener.setMessages(instance);
	}
}
