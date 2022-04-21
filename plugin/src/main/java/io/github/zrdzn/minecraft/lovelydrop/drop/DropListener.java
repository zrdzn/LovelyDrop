package io.github.zrdzn.minecraft.lovelydrop.drop;

import io.github.zrdzn.minecraft.lovelydrop.item.Item;
import io.github.zrdzn.minecraft.lovelydrop.item.ItemCache;
import io.github.zrdzn.minecraft.lovelydrop.user.User;
import io.github.zrdzn.minecraft.lovelydrop.user.UserCache;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

public class DropListener implements Listener {

    private final Logger logger;
    private final ItemCache itemCache;
    private final UserCache userCache;

    public DropListener(Logger logger, ItemCache itemCache, UserCache userCache) {
        this.logger = logger;
        this.itemCache = itemCache;
        this.userCache = userCache;
    }

    @EventHandler
    public void onSourceBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        Material source = event.getBlock().getType();

        Set<Item> sourceDrops = this.itemCache.getDrops(source);
        if (sourceDrops.isEmpty()) {
            return;
        }

        Optional<User> userMaybe = this.userCache.getUser(player.getUniqueId());
        if (!userMaybe.isPresent()) {
            this.logger.severe("User " + player.getName() + " is not added to cached users.");
            return;
        }

        event.setDropItems(false);

        User user = userMaybe.get();

        sourceDrops.removeIf(sourceDrop -> user.getDisabledDrops().contains(sourceDrop));

        ThreadLocalRandom random = ThreadLocalRandom.current();

        sourceDrops.forEach(item -> {
            if (item.getChance() <= Math.random() * 100.0D) {
                return;
            }

            player.giveExp(item.getExperience());

            Entry<Integer, Integer> amountEntry = item.getAmount();
            int minimumAmount = amountEntry.getKey();
            int maximumAmount = amountEntry.getValue();

            int amount;
            if (minimumAmount == maximumAmount) {
                amount = minimumAmount;
            } else {
                amount = random.nextInt(item.getAmount().getKey(), item.getAmount().getValue());
            }

            ItemStack droppedItem = new ItemStack(item.getType(), amount);
            ItemMeta droppedItemMeta = droppedItem.getItemMeta();
            droppedItemMeta.setDisplayName(item.getDisplayName());
            droppedItemMeta.setLore(item.getLore());
            droppedItem.setItemMeta(droppedItemMeta);

            Map<Integer, ItemStack> itemsLeft = player.getInventory().addItem(droppedItem);

            itemsLeft.forEach((key, value) ->
                player.getWorld().dropItemNaturally(player.getEyeLocation(), value));
        });
    }

}
