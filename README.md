# LovelyDrop
[![Build Status](https://img.shields.io/github/workflow/status/zrdzn/LovelyDrop/LovelyDrop%20Build)](https://github.com/zrdzn/LovelyDrop/actions/workflows/build.yml)
[![Release](https://img.shields.io/github/v/release/zrdzn/LovelyDrop?color=44CC44)](https://github.com/zrdzn/LovelyDrop/releases/latest)

LovelyDrop is a Minecraft plugin that adds a drop system to a server. You can customize what items should be dropped by specifying various properties. Besides, the whole drop menu is also configurable, you can add items that should perform specific actions when clicked, and you can name them as you want.

![](img/menu-preview.png)
## Features
- The fully customizable GUI with items that can perform few actions when clicked
  - Set a title and rows for the menu
  - Set an optional filler type and a display name for it
  - Set a display name and a lore for the menu item with few available placeholders
    - **{CHANCE}** - a chance of the item to drop
    - **{AMOUNT}** - a formatted amount of the item that could be dropped
    - **{EXPERIENCE}** - an experience that will be given to the player
    - **{SWITCH}** - a text that will be shown depending on the drop switch status
    - **{SWITCH_INVENTORY}** - a text that will be shown depending on the inventory drop switch status
    - **{HEIGHT}** - a text that will be shown depending on the customized height range
  - Set the action that item should perform after being clicked
    - **NONE** - the item will not do any action
    - **CLOSE_MENU** - the menu will be closed
    - **SWITCH_DROP** - the drop assigned to this menu item will be turned on/off
    - **SWITCH_DROP_TO_INVENTORY** - the drop assigned to this menu item will have its "drop to inventory" property switched
  - Set click types when to perform the specified action
    - **ALL** - the action will be performed on any type of the click
    - **LEFT** - the action will be performed on a left click
    - **RIGHT** - the action will be performed on a right click
    - Whole list of click types is [here](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/event/inventory/ClickType.html)!
- The configurable drop section
  - Adding as many items as you want
  - Set a **display name and lore** of the dropped item (optional)
  - Set a **chance** of the drop
  - Set an **experience** that should be given to the player
  - Set a **height** where the item could be dropped
  - Set an **amount** of possible items of the drop
  - Set an **enchantments** of the drop
## Configuration
The whole configuration of the plugin is explained in this
[default configuration file](https://github.com/zrdzn/LovelyDrop/blob/master/plugin/src/main/resources/config.yml).
## Commands and permissions
| Command | Permission | Description                                                                                 |
| ----------------|---------------|----------------------------------------------------------------------------------|
| /drop | lovelydrop.menu.open | a main command - shows the drop menu                                                  |
| /ldp reload | lovelydrop.reload | reloads the whole plugin including configuration                                 |

## Requirements
The plugin runs on the spigot servers and its forks. Minecraft versions above **1.8 up to 1.18.2** are fully supported by the plugin.

## How to download
1. Read the [requirements](#requirements)
2. Download a .jar from the [releases](https://github.com/zrdzn/LovelyDrop/releases) tab
3. Place the .jar in the `plugins` directory
4. Start your server
5. Configure the plugin if you want
