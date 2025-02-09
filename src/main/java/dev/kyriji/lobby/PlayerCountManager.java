package dev.kyriji.lobby;

import dev.kyriji.lobby.enums.Game;
import dev.wiji.bigminecraftapi.BigMinecraftAPI;
import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.TaskSchedule;

import java.util.HashMap;
import java.util.Map;

public class PlayerCountManager {
	private static PlayerCountManager INSTANCE;
	private final Map<Game, Integer> playerCounts = new HashMap<>();

	public PlayerCountManager() {
		MinecraftServer.getSchedulerManager().scheduleTask(() -> {
			for(Game game : Game.values()) {
				int playerCount = BigMinecraftAPI.getNetworkManager().getPlayers(game.getBmcIdentifier()).size();
				playerCounts.put(game, playerCount);
			}
		}, TaskSchedule.tick(200), TaskSchedule.tick(200));
	}

	public static void init() {
		if(INSTANCE != null) throw new IllegalStateException("PlayerCountManager has already been initialized");
		INSTANCE = new PlayerCountManager();
	}

	public static PlayerCountManager get() {
		if(INSTANCE == null) throw new IllegalStateException("PlayerCountManager has not been initialized");
		return INSTANCE;
	}

	public int getPlayerCount(Game game) {
		return playerCounts.getOrDefault(game, 0);
	}
}
