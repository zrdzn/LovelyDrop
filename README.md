# LovelyDrop
LovelyDrop is a minecraft plugin that adds drop system to a server.
You can customize what items should drop by specifying properties such as
chance, minimum and maximum possible amount, meta (display name and lore),
source block (from which should it drop), experience that player should be given
and of course a type of the drop item itself. Besides, whole drop menu is also
configurable, you can add items that should perform specific actions when clicked, 
the most important one is the drop-switch which will turn on/off specific drop for player
that clicks it.
## Configuration
Whole configuration of the plugin is explained in this
[default configuration file](https://github.com/zrdzn/LovelyDrop/blob/master/plugin/src/main/resources/config.yml).
## Setup
Plugin after being uploaded to the server is generally good to go, there is no need to 
set a database or something like that because it does not rely on any.

If you made changes to the configuration file, you can use `/ldp reload` command
to reload your plugin - it requires `lovelydrop.reload` permission.

To open the drop menu, you need to execute `/drop` command - it requires `lovelydrop.menu.open`
permission to open the menu though (it is given by default).
