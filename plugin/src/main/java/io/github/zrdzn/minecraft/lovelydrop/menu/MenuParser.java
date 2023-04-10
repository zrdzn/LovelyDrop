package io.github.zrdzn.minecraft.lovelydrop.menu;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Logger;
import io.github.zrdzn.minecraft.lovelydrop.LovelyDropPlugin;
import io.github.zrdzn.minecraft.lovelydrop.ParserHelper;
import io.github.zrdzn.minecraft.lovelydrop.drop.DropItemCache;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MenuParser {

    private final Logger logger;
    private final DropItemCache dropItemCache;

    public MenuParser(Logger logger, DropItemCache dropItemCache) {
        this.logger = logger;
        this.dropItemCache = dropItemCache;
    }

    public Menu parse(ConfigurationSection section) throws InvalidConfigurationException {
        if (section == null) {
            throw new InvalidConfigurationException("Provided section is null.");
        }

        if (!section.getName().equals("menu")) {
            throw new InvalidConfigurationException("Provided section is not 'menu'.");
        }

        String title = LovelyDropPlugin.color(section.getString("title", "Menu"));

        int rows = section.getInt("rows");
        if (rows <= 0) {
            throw new InvalidConfigurationException("Key 'rows' cannot be 0 and lower");
        }

        ItemStack filler = null;

        boolean fillerEnabled = section.getBoolean("filler.enabled", false);
        if (fillerEnabled) {
            String fillerTypeRaw = section.getString("filler.type");
            if (fillerTypeRaw == null) {
                throw new InvalidConfigurationException("Key 'filler.type' is null.");
            }

            filler = ParserHelper.parseLegacyMaterial(fillerTypeRaw).toItemStack(1);

            String fillerName = LovelyDropPlugin.color(section.getString("filler.displayname", " "));

            ItemMeta fillerMeta = filler.getItemMeta();

            fillerMeta.setDisplayName(fillerName.equals("none") ? " " : fillerName);

            filler.setItemMeta(fillerMeta);
        }

        String dropSwitchEnabled = LovelyDropPlugin.color(section.getString("drop-switch.enabled", "&aon"));
        String dropSwitchDisabled = LovelyDropPlugin.color(section.getString("drop-switch.disabled", "&coff"));

        Entry<String, String> dropSwitch = new AbstractMap.SimpleEntry<>(dropSwitchEnabled, dropSwitchDisabled);

        String inventorySwitchEnabled = LovelyDropPlugin.color(section.getString("inventory-drop-switch.enabled", "&aon"));
        String inventorySwitchDisabled = LovelyDropPlugin.color(section.getString("inventory-drop-switch.disabled", "&coff"));

        Entry<String, String> inventorySwitch = new AbstractMap.SimpleEntry<>(inventorySwitchEnabled, inventorySwitchDisabled);

        String amountSingular = LovelyDropPlugin.color(section.getString("amount-format.singular", "&e{AMOUNT}"));
        String amountPlural = LovelyDropPlugin.color(section.getString("amount-format.plural", "&e{AMOUNT-MIN}&8-&e{AMOUNT-MAX}"));

        Entry<String, String> amountFormat = new AbstractMap.SimpleEntry<>(amountSingular, amountPlural);

        String heightSingular = LovelyDropPlugin.color(section.getString("height-format.singular", "&e{HEIGHT}"));
        String heightPlural = LovelyDropPlugin.color(section.getString("height-format.plural", "&e{HEIGHT-MIN}&8-&e{HEIGHT-MAX}"));

        Entry<String, String> heightFormat = new AbstractMap.SimpleEntry<>(heightSingular, heightPlural);

        boolean defaultDropToInventory = section.getBoolean("default-drop-to-inventory", true);

        MenuItemParser menuItemParser = new MenuItemParser(this.logger, this.dropItemCache);

        List<MenuItem> menuItems = menuItemParser.parseMany(section.getConfigurationSection("items"));

        return new Menu(title, rows, filler, dropSwitch, inventorySwitch, amountFormat, heightFormat, defaultDropToInventory,
            menuItems);
    }

}
