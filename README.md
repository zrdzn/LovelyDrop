# LovelyDrop
LovelyDrop is a minecraft plugin that adds a drop system to a server.
You can customize what items should be dropped by specifying properties such as
chance, minimum and maximum possible amount, meta (display name and lore),
source block (from which block it should drop from), experience that a player should be given
and of course the type of item to drop itself. Besides, the whole drop menu is also
configurable, you can add items that should perform specific actions when clicked, 
the most important one is the drop-switch which will turn on/off the specific drop for the player
that clicks it.

![1](img/menu-preview.png)
## Configuration
The whole configuration of the plugin is explained in this
[default configuration file](https://github.com/zrdzn/LovelyDrop/blob/master/plugin/src/main/resources/config.yml).
## Setup
The plugin is generally good to go after being uploaded to the server, there is no need to 
set a database or something like that because it does not rely on any.

If you made any changes to the configuration file, you can use the `/ldp reload` command
to reload your plugin - it requires the `lovelydrop.reload` permission.

To open the drop menu, you need to execute the `/drop` command - it requires the `lovelydrop.menu.open`
permission (it is given by default).

## Requirements
The plugin runs on the spigot servers and its forks. At the moment it has not been tested on any version other than 1.18.2,
so it may not work on older ones.

## Download
Before downloading the plugin, read the [requirements](#requirements) first. You can find the latest version
in the [releases](https://github.com/zrdzn/LovelyDrop/releases) tab.
