package dev.kyriji.lobby.feature.selector;

import dev.kyriji.lobby.enums.Game;
import dev.wiji.bigminecraftapi.BigMinecraftAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class SelectorMenu {
	private static final AtomicReference<Inventory> inventory = new AtomicReference<>();
	private static Task updateTask;

	public static final List<Integer> SLOTS = List.of(11, 15);

	static {
		createInventory();
		scheduleUpdates();
	}

	private static void createInventory() {
		Inventory newInventory = new Inventory(InventoryType.CHEST_3_ROW, Component.text("Game Selector"));
		updateInventoryItems(newInventory);

		newInventory.addInventoryCondition((player, slot, clickType, result) -> {
			result.setCancel(true);

			ItemStack clickedItem = newInventory.getItemStack(slot);
			if(clickedItem.isAir()) return;

			for(Game game : Game.values()) {
				if(clickedItem.material() == game.getIcon()) {
					handleGameSelection(player, game);
					break;
				}
			}
		});

		inventory.set(newInventory);
	}

	private static void updateInventoryItems(Inventory inv) {
		List<ItemStack> gameItems = new ArrayList<>();
		for(Game value : Game.values()) {
			List<Component> lore = new ArrayList<>(value.getDescription());
			int playerCount = BigMinecraftAPI.getNetworkManager().getPlayers(value.getBmcIdentifier()).size();
			lore.add(Component.text(""));
			lore.add(Component.text(playerCount + " playing").color(NamedTextColor.YELLOW));

			List<Component> updatedLore = lore.stream().map(component -> component.decoration(TextDecoration.ITALIC, false)).toList();

			gameItems.add(ItemStack.builder(value.getIcon())
					.customName(value.getDisplayName().decoration(TextDecoration.ITALIC, false))
					.lore(updatedLore)
					.build());
		}

		for(int i = 0; i < gameItems.size(); i++) {
			int slot = SLOTS.get(i);
			inv.setItemStack(slot, gameItems.get(i));
		}
	}


	private static void scheduleUpdates() {
		if(updateTask != null) updateTask.cancel();

		updateTask = MinecraftServer.getSchedulerManager().scheduleTask(() -> {
			Inventory current = inventory.get();
			if(current != null) updateInventoryItems(current);
		}, TaskSchedule.tick(200), TaskSchedule.tick(200));
	}


	private static void handleGameSelection(Player player, Game game) {
		player.closeInventory();
		BigMinecraftAPI.getNetworkManager().queuePlayer(player.getUuid(), game.getBmcIdentifier());
	}

	public static void openMenu(Player player) {
		player.openInventory(inventory.get());
	}
}