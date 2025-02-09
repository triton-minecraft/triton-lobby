package dev.kyriji.lobby;

import dev.kyriji.lobby.enums.Game;
import dev.kyriji.tritonstom.world.TritonWorld;
import dev.wiji.bigminecraftapi.BigMinecraftAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.entity.metadata.display.ItemDisplayMeta;
import net.minestom.server.entity.metadata.display.TextDisplayMeta;
import net.minestom.server.event.entity.EntityAttackEvent;
import net.minestom.server.event.player.PlayerEntityInteractEvent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.timer.TaskSchedule;

import java.util.HashMap;
import java.util.Map;

public class HologramManager {

	private final Map<Game, Entity> hitboxes = new HashMap<>();
	private final Map<Game, Entity> nameEntities = new HashMap<>();

	public HologramManager() {
		System.out.println("HologramManager initialized!");

		createSpawnHologram();
		spawnGameHolograms();
	}

	private void createSpawnHologram() {
		TritonWorld world = TritonLobby.world;

		Entity entity = new Entity(EntityType.TEXT_DISPLAY);
		entity.setInstance(world.getInstance(), new Pos(0.5, 10, 20, 180, 0));
		TextDisplayMeta meta = (TextDisplayMeta) entity.getEntityMeta();

		TextComponent text = Component.text("Triton").color(NamedTextColor.GOLD).decorate(TextDecoration.BOLD)
				.append(Component.text("MC\n").color(NamedTextColor.DARK_AQUA).decorate(TextDecoration.BOLD))
				.append(Component.text("Welcome!").color(NamedTextColor.GRAY).decoration(TextDecoration.BOLD, false))
				.append(Component.text("\n\n"))
				.append(Component.text("Use the compass to play!").color(NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, false));

		meta.setText(text);
		meta.setAlignment(TextDisplayMeta.Alignment.CENTER);
		meta.setScale(new Vec(7, 7, 7));

		entity.setNoGravity(true);
	}

	private void spawnGameHolograms() {
		for(Game value : Game.values()) {
			Entity entity = new Entity(EntityType.ITEM_DISPLAY);
			ItemDisplayMeta meta = (ItemDisplayMeta) entity.getEntityMeta();

			entity.setInstance(TritonLobby.world.getInstance(), value.getSpawnLocation());
			ItemStack item = ItemStack.builder(value.getIcon()).amount(1).build();
			meta.setItemStack(item);
			meta.setScale(new Vec(8, 8, 8));
			meta.setDisplayContext(ItemDisplayMeta.DisplayContext.GROUND);
			meta.setShadowRadius(0);
			entity.setNoGravity(true);

			Entity hologram = new Entity(EntityType.TEXT_DISPLAY);
			hologram.setInstance(TritonLobby.world.getInstance(), value.getSpawnLocation().add(0, 3, 0));
			TextDisplayMeta hologramMeta = (TextDisplayMeta) hologram.getEntityMeta();

			hologramMeta.setScale(new Vec(2, 2,2));
			hologram.setNoGravity(true);

			nameEntities.put(value, hologram);


			LivingEntity villager = new LivingEntity(EntityType.VILLAGER);
			villager.setBoundingBox(5, 10, 5);
			villager.setNoGravity(true);
			villager.setInstance(TritonLobby.world.getInstance(), value.getSpawnLocation());
			villager.setInvisible(true);
			villager.getAttribute(Attribute.SCALE).setBaseValue(5);

			hitboxes.put(value, villager);
		}

		MinecraftServer.getGlobalEventHandler().addListener(PlayerEntityInteractEvent.class, event -> {
			Entity entity = event.getTarget();
			if(entity.getEntityType() != EntityType.VILLAGER) return;

			findAndQueueGame(event.getPlayer(), entity);
		});

		MinecraftServer.getGlobalEventHandler().addListener(EntityAttackEvent.class, event -> {
			Entity entity = event.getTarget();
			Entity attacker = event.getEntity();

			if(entity.getEntityType() != EntityType.VILLAGER || attacker.getEntityType() != EntityType.PLAYER) return;

			findAndQueueGame((Player) attacker, entity);
		});

		MinecraftServer.getSchedulerManager().scheduleTask(() -> {
			for(Game game : Game.values()) {
				int playerCount = PlayerCountManager.get().getPlayerCount(game);
				Entity hologram = nameEntities.get(game);
				TextDisplayMeta meta = (TextDisplayMeta) hologram.getEntityMeta();


				Component text = game.getDisplayName().decorate(TextDecoration.BOLD)
						.append(Component.text("\n"))
						.append(Component.text(playerCount + " Playing").color(NamedTextColor.YELLOW).decoration(TextDecoration.BOLD, false));

				meta.setText(text);
			}
		}, TaskSchedule.tick(1), TaskSchedule.tick(20));
	}

	private void findAndQueueGame(Player player, Entity entity) {
		Game game = hitboxes.entrySet().stream()
				.filter(entry -> entry.getValue().equals(entity))
				.map(Map.Entry::getKey)
				.findFirst().orElse(null);

		if(game == null) return;
		BigMinecraftAPI.getNetworkManager().queuePlayer(player.getUuid(), game.getBmcIdentifier());
	}
}
