package dev.kyriji.lobby.feature.selector;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerHand;
import net.minestom.server.event.entity.EntityDamageEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.event.item.ItemDropEvent;
import net.minestom.server.event.player.*;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;

public class PlayerListener {
	private static PlayerListener INSTANCE;

	public static Tag<Boolean> COMPASS_KEY = Tag.Boolean("game_compass");

	private PlayerListener() {
		MinecraftServer.getGlobalEventHandler().addListener(PlayerSpawnEvent.class, event -> {
			Player player = event.getPlayer();

			ItemStack.Builder builder = ItemStack.builder(Material.COMPASS);
			builder.setTag(COMPASS_KEY, true);
			builder.customName(Component.text("Game Selector").color(NamedTextColor.AQUA).decoration(TextDecoration.ITALIC, false));

			ItemStack compass = builder.build();
			player.getInventory().addItemStack(compass);
		});

		MinecraftServer.getGlobalEventHandler().addListener(PlayerUseItemEvent.class, event -> {
			Player player = event.getPlayer();
			ItemStack itemStack = player.getItemInHand(PlayerHand.MAIN);
			if(itemStack.material() != Material.COMPASS) return;

			if(itemStack.getTag(COMPASS_KEY)) {
				SelectorMenu.openMenu(player);
			}
		});

		MinecraftServer.getGlobalEventHandler().addListener(ItemDropEvent.class, event -> event.setCancelled(true));

		MinecraftServer.getGlobalEventHandler().addListener(InventoryPreClickEvent.class, event -> event.setCancelled(true));

		MinecraftServer.getGlobalEventHandler().addListener(EntityDamageEvent.class, event -> event.setCancelled(true));
	}

	public static void init() {
		if(INSTANCE != null) throw new IllegalStateException("PlayerListener has already been initialized");
		INSTANCE = new PlayerListener();
	}

	public static PlayerListener get() {
		if(INSTANCE == null) throw new IllegalStateException("PlayerListener has not been initialized");
		return INSTANCE;
	}
}
