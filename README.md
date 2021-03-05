SellBlocks lets you configure an area where items that are dropped (such as a farm), are automatically sold and the money credited to your account. No chests or storage systems needed!

## Setting up

### Dependencies
This plugin depends on [Essentials](https://www.spigotmc.org/resources/essentialsx.9089/), [Vault](https://www.spigotmc.org/resources/vault.34315/), [WorldEdit 6.1.9](https://dev.bukkit.org/projects/worldedit/files/2597538), and [WorldGuard 6.2.2](https://dev.bukkit.org/projects/worldguard/files/2610618).
Make sure to add each plugin to your server's plugin folder.

### Installation
1. Installing this plugin is just like installing any other plugin:
2. Download the jar
3. Locate the folder your server.jar is in
4. Find the plugins folder
5. Drag and drop the downloaded jar into your plugins folder and restart the server

### Usage
To begin using SellBlocks, find an area where you wish to collect items. Place a block that will become your sellblock.
Once you have located a suitable place run /sellblock add.
You will be prompted to break a block.

Next, drop the item that you wish to collect. After doing so, you will have successfully registered a sellblock!

![creating a sellblock](https://i.imgur.com/eGxrfhy.gif)

To remove a sellblock, follow the same procedure, but instead run /sellblock remove.

SellBlocks will automatically hook into WorldGuard to protect your sellblocks.

![protection](https://i.imgur.com/rX1NBKF.gif)

Now, when an item of the type you set is dropped within the pickup distance (configurable), it will be automatically sold and credited to your account.

![a sellblock in action](https://i.imgur.com/pNxU33Z.gif) 

## Other info

### Commands

#### Basic
```
/sellblock add - adds a sellblock
/sellblock remove - removes a sellblock
/sellblock list - lists all current sellblocks with their location, owner, and type
```

#### Admin
```
/sellblock limit <Player> <Limit> - set a custom limit for a player
/sellblock save - saves all sellblocks
/sellblock load - loads all sellblocks
/sellblock reload - reloads configs
```

### Permissions

#### General permissions
The permission syntax is based solely on the command's name. The permission syntax base is `SellBlocks.command.sellblock.<command_name>`. For example, permission to run `/sellblock limit Notch 15` would be `SellBlocks.command.sellblock.limit`. `/sellblock add` is `SellBlocks.command.sellblock.limit`.
Permissions are ignored if a player is an operator.

#### Special permissions

##### Star `*` permission
The star permission is `SellBlocks.command.*`, which grants permission to run all commands. If a player is an operator then permissions are ignored.

##### Bypass limit permission
The permission to bypass the sellblock creation limit is `SellBlocks.command.sellblock.add.bypass`.

### Configs
Every aspect of this plugin is configurable.
<details>
  <summary>config.json</summary>
  
```json
{
  "pickup_distance": 25,
  "region_id_format": "sellblock_${PLAYER}_/${LOCATION}/_${TYPE}",
  "default_sellblock_limit": 10,
  "save_interval_minutes": 5,
  "debug": false
}
```
</details>

<details>
  <summary>messages.json</summary>
 
```json
{
  "run_help": "&eRun &b/sellblocks help &cfor a list of commands",
  "players_limit": "&b${PLAYER}'s &asellblock limit is &b${LIMIT}&a.",
  "set_limit": "&aSet &b${PLAYER}'s &asellblock limit to &b${LIMIT}&a.",
  "reached_limit": "&cYou have reached the limit of &b${LIMIT} &csellblocks.",
  "instruction_break_block_add": "&eBreak the block that you wish to make a sellblock",
  "instruction_break_block_remove": "&eBreak the block that you wish to remove as a sellblock",
  "saving": "saving",
  "loading": "loading",
  "reloading": "reloading",
  "no_sellblocks": "&cDoh! There are no sellblocks! &aRun &b/sellblock add &ato make the first one!",
  "list_item": "&b${PLAYER} &3@ &b${LOCATION}&3: &b${TYPE}",
  "players_only": "&cPlayers only, retard",
  "not_your_sellblock": "&cThat's not one of your sellblocks idiot",
  "not_a_sellblock": "&cThat's not a sellblock, dingus",
  "removed_sellblock": "&6Removed sellblock at location &8${LOCATION}",
  "already_a_sellblock": "&cThat's already a sellblock, retard",
  "location_set": "&aLocation set to &8${LOCATION}",
  "instruction_drop_item": "&eDrop the item that you wish to collect",
  "too_close": "&cToo close to another sellblock of the same type; please be at least ${DISTANCE} blocks away",
  "cannot_sell": "&cYou cannot sell that type of item.",
  "item_set": "&aItem set to ${TYPE}",
  "registered_success": "&6Successfully registered a sellblock to &b${PLAYER} &6at &8${LOCATION} &6with item &b${TYPE}"
}
```
</details>

## Support
If you need help, hop on my [Discord server](https://discord.gg/vvKuHBVGMK) and I'll be glad to help you!
