# LovelyDrop
[![Build Status](https://img.shields.io/github/actions/workflow/status/zrdzn/LovelyDrop/build.yml?branch=master)](https://github.com/zrdzn/LovelyDrop/actions/workflows/build.yml)
[![Release](https://img.shields.io/github/v/release/zrdzn/LovelyDrop?color=44CC44)](https://github.com/zrdzn/LovelyDrop/releases/latest)

LovelyDrop is a Minecraft plugin that enhances the drop system on your server. It allows you to customize the items that are dropped by defining a range of properties. 
Furthermore, the entire drop menu is fully configurable, enabling you to incorporate items with designated actions upon clicking, all of which can be given personalized names as desired.

![](img/menu-preview.png)
## Features
- The fully customizable GUI features items that can perform various actions when clicked
  - Set a title and rows for the menu
  - Set an optional filler type and its properties
  - Configure menu items with various options
- The configurable drop section allows you to
  - Add as many items as you desire
  - Set a **display name and lore** for the dropped item (optional)
  - Specify an **experience** granted to the player
  - Set a **height range** for item drops
  - Apply **enchantments** to the drop
  - Assign **NBT** data to the drop
  - ...and many more!
## Configuration
The whole configuration of the plugin is explained in this
[default configuration file](https://gist.github.com/zrdzn/79176e10335e78d4c9e9011c189376ce).
## Storage
The plugin allows to store the drops in either a memory or a database.
The default storage is a SQLite database, but you can change it yourself in the configuration's storage section.
### Supported storages
- [SQLite](https://www.sqlite.org/index.html)
- [PostgreSQL](https://www.postgresql.org/) (**Recommended**)
- Memory
## Commands and permissions
| Command     | Permission           | Description                                      |
|-------------|----------------------|--------------------------------------------------|
| /drop       | lovelydrop.menu.open | a main command - shows the drop menu             |
| /ldp reload | lovelydrop.reload    | reloads the whole plugin including configuration |

## Requirements
### Java Version
- Java 8 or higher
### Server Software
- [CraftBukkit](https://dev.bukkit.org/)
- [Spigot](https://www.spigotmc.org/)
- [PaperMC](https://papermc.io/)
- ...and other server software based on Spigot
### Server Version
- 1.8 or higher

## How to download
1. Read the [requirements](#requirements)
2. Download a .jar from the [releases](https://github.com/zrdzn/LovelyDrop/releases) tab
3. Place the .jar in the `plugins` directory
4. Start your server
5. Configure the plugin
6. Use /ldp reload command to reload new configuration
7. You're done!

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
