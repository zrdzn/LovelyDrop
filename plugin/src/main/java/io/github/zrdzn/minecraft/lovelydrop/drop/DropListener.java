package io.github.zrdzn.minecraft.lovelydrop.drop;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;
import io.github.zrdzn.minecraft.lovelydrop.message.MessageService;
import io.github.zrdzn.minecraft.lovelydrop.user.User;
import io.github.zrdzn.minecraft.lovelydrop.user.UserCache;
import io.github.zrdzn.minecraft.spigot.SpigotAdapter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

public class DropListener implements Listener {

    private final Logger logger;
    private final MessageService messageService;
    private final SpigotAdapter adapter;
    private final DropItemCache dropItemCache;
    private final UserCache userCache;

    public DropListener(Logger logger, MessageService messageService, SpigotAdapter adapter,
                        DropItemCache dropItemCache, UserCache userCache) {
        this.logger = logger;
        this.messageService = messageService;
        this.adapter = adapter;
        this.dropItemCache = dropItemCache;
        this.userCache = userCache;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onSourceBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        ItemStack pickaxe = player.getItemInHand();
        if (!pickaxe.getType().name().contains("PICKAXE")) {
            return;
        }

        Block block = event.getBlock();

        MaterialData source = null;
        for (MaterialData legacyData : this.dropItemCache.getDrops().keySet()) {
            if (block.getType() == legacyData.getItemType() && block.getData() == legacyData.getData()) {
                source = legacyData;
                break;
            }
        }

        if (source == null) {
            return;
        }

        // Get optional drops from source blocks.
        Set<DropItem> sourceDrops = this.dropItemCache.getDrops(source);
        if (sourceDrops.isEmpty()) {
            return;
        }

        // Get user from the cache.
        Optional<User> userMaybe = this.userCache.getUser(player.getUniqueId());
        if (!userMaybe.isPresent()) {
            this.logger.severe("User " + player.getName() + " is not added to cached users.");
            return;
        }

        this.adapter.getBlockBreakHelper().disableDrop(event);

        User user = userMaybe.get();

        // Remove all disabled drops from the source drops list.
        sourceDrops.removeIf(sourceDrop -> user.getDisabledDrops().contains(sourceDrop));

        ThreadLocalRandom random = ThreadLocalRandom.current();

        int blockHeight = block.getY();

        int fortuneLevel = pickaxe.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);

        sourceDrops.forEach(item -> {
            Entry<Integer, Integer> height = item.getHeight();
            if (blockHeight < height.getKey() || blockHeight > height.getValue()) {
                return;
            }

            Map<Integer, DropProperty> properties = item.getProperties();

            DropProperty property = properties.getOrDefault(fortuneLevel, properties.get(0));
            if (property == null) {
                return;
            }

            if (property.getChance() <= Math.random() * 100.0D) {
                return;
            }

            MaterialData finalData = item.getType();
            if (item.getType().getItemType() == Material.COBBLESTONE && pickaxe.containsEnchantment(Enchantment.SILK_TOUCH)) {
                finalData = new MaterialData(Material.STONE);
            }

            player.giveExp(property.getExperience());

            Entry<Integer, Integer> amountEntry = property.getAmount();

            int amount = amountEntry.getKey();
            if (amount != amountEntry.getValue()) {
                amount = random.nextInt(amountEntry.getKey(), amountEntry.getValue());
            }

            ItemStack droppedItem = finalData.toItemStack(amount);

            ItemMeta droppedItemMeta = droppedItem.getItemMeta();

            droppedItemMeta.setDisplayName(item.getDisplayName());

            String[] placeholders = { "{DROP}", item.getId(), "{AMOUNT}", String.valueOf(amount) };
            this.messageService.send(player, "drop-successful", placeholders);

            droppedItemMeta.setLore(item.getLore());
            droppedItem.setItemMeta(droppedItemMeta);

            // Add additional enchantments.
            Map<Enchantment, Integer> enchantments = item.getEnchantments();
            if (enchantments.size() > 0) {
                droppedItem.addUnsafeEnchantments(enchantments);
            }

            World world = player.getWorld();

            Location location = block.getLocation();

            if (user.hasSwitchedInventoryDrop(item.getId())) {
                Map<Integer, ItemStack> itemsLeft = player.getInventory().addItem(droppedItem);

                // Drop items on the floor if player has full inventory.
                itemsLeft.forEach((key, value) ->
                    world.dropItemNaturally(location, value));

                return;
            }

            world.dropItemNaturally(location, droppedItem);
        });
    }

}
