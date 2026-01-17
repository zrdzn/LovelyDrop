package io.github.zrdzn.minecraft.lovelydrop.drop;

import eu.okaeri.commons.range.IntRange;
import io.github.zrdzn.minecraft.lovelydrop.config.PluginConfig;
import io.github.zrdzn.minecraft.lovelydrop.config.DropConfig;
import io.github.zrdzn.minecraft.lovelydrop.config.DropConfig.FortuneConfig;
import io.github.zrdzn.minecraft.lovelydrop.message.MessageFacade;
import io.github.zrdzn.minecraft.lovelydrop.user.UserSetting;
import io.github.zrdzn.minecraft.lovelydrop.user.UserSettingFacade;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DropListener implements Listener {

    private final Logger logger = LoggerFactory.getLogger(DropListener.class);

    private final PluginConfig config;
    private final MessageFacade messageFacade;
    private final UserSettingFacade userSettingFacade;

    public DropListener(PluginConfig config, MessageFacade messageFacade,
            UserSettingFacade userSettingFacade) {
        this.config = config;
        this.messageFacade = messageFacade;
        this.userSettingFacade = userSettingFacade;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onSourceBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        ItemStack pickaxe = player.getItemInHand();
        if (!pickaxe.getType().name().contains("PICKAXE")) {
            return;
        }

        Block block = event.getBlock();

        // Get user settings from the cache.
        Optional<UserSetting> userSettingMaybe =
                this.userSettingFacade.findUserSettingByPlayerIdFromCache(player.getUniqueId());
        if (!userSettingMaybe.isPresent()) {
            this.logger.error("User settings not found for {}.", player.getName());
            this.messageFacade.sendMessageAsync(player, this.config.messages.needToJoinAgain);
            return;
        }

        UserSetting userSetting = userSettingMaybe.get();

        // Get all drops from the source.
        Set<Entry<String, DropConfig>> drops = this.config.drops.entrySet().stream()
                .filter(drop -> !userSetting.hasDisabledDrop(drop.getKey()))
                .filter(drop -> !drop.getValue().disabledBioms.contains(block.getBiome()))
                .filter(drop -> drop.getValue().sources.stream()
                        .anyMatch(item -> item.getType() == block.getType()
                                && item.getDurability() == block.getData()))
                .collect(Collectors.toSet());
        if (drops.isEmpty()) {
            return;
        }

        event.setDropItems(false);

        int blockHeight = block.getY();

        int fortuneLevel = pickaxe.getEnchantmentLevel(Enchantment.FORTUNE);

        drops.forEach(keyAndDrop -> {
            DropConfig drop = keyAndDrop.getValue();

            IntRange heightRange = drop.height;
            if (blockHeight < heightRange.getMin() || blockHeight > heightRange.getMax()) {
                return;
            }

            FortuneConfig fortune = drop.fortune.getOrDefault(fortuneLevel, drop.fortune.get(0));
            if (fortune == null) {
                return;
            }

            if (fortune.chance.getValue() <= Math.random() * 100.0F) {
                return;
            }

            ItemStack dropItem = drop.item.getItemStack();

            // If pickaxe has silk touch and drop is cobblestone, change drop to stone.
            if (dropItem.getType() == Material.COBBLESTONE
                    && pickaxe.containsEnchantment(Enchantment.SILK_TOUCH)) {
                dropItem.setType(Material.STONE);
            }

            // Give experience to player.
            player.giveExp(fortune.experience);

            // Randomize amount from range and set it for item.
            int amount = fortune.amount.getRandom();
            dropItem.setAmount(amount);

            String[] placeholders =
                    {"{DROP}", keyAndDrop.getKey(), "{AMOUNT}", String.valueOf(amount)};
            this.messageFacade.sendMessage(player, this.config.messages.dropSuccessful,
                    placeholders);

            World world = player.getWorld();

            Location location = block.getLocation();

            if (userSetting.getDropsToInventory().getOrDefault(keyAndDrop.getKey(), true)) {
                // Add items to inventory.
                Map<Integer, ItemStack> itemsLeft = player.getInventory().addItem(dropItem);

                // Drop items on the floor if player has full inventory.
                itemsLeft.forEach((index, item) -> world.dropItemNaturally(location, item));

                return;
            }

            world.dropItemNaturally(location, dropItem);
        });
    }
}
