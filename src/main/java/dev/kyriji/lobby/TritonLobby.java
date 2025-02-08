package dev.kyriji.lobby;

import dev.kyriji.lobby.feature.selector.PlayerListener;
import dev.kyriji.minestom.TritonCoreMinestom;
import dev.kyriji.tritonstom.TritonStom;
import dev.kyriji.tritonstom.world.TritonWorld;
import dev.kyriji.tritonstom.world.WorldManager;
import dev.kyriji.tritonstom.world.spawn.SpawnLocation;
import dev.kyriji.tritonstom.world.spawn.SpawnManager;
import dev.kyriji.tritonstom.world.time.TimeManager;
import dev.kyriji.tritonstom.world.time.TimeStrategy;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;

public class TritonLobby {
	public static TritonWorld world;

	public static void main(String[] args) {
		MinecraftServer server = MinecraftServer.init();

		TritonCoreMinestom.init();

		world = WorldManager.get().buildWorld("lobby")
				.timeKeeper(TimeManager.get().buildTimeKeeper().strategy(TimeStrategy.ALWAYS_NOON))
				.allowModification(false)
				.build();

		TritonStom.builder(server)
				.enableWorldEdit()
				.defaultGameMode(GameMode.ADVENTURE)
				.playerSpawner(SpawnManager.get().buildPlayerSpawner()
						.fixed(new SpawnLocation(world, new Pos(0.5, 1, 0.5, 90, 0))))
				.start();

		PlayerListener.init();

		server.start("0.0.0.0", 25565);
	}
}
