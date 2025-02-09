package dev.kyriji.lobby.enums;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.item.Material;

import java.util.List;

public enum Game {
	CREATIVE(Component.text("Creative").color(NamedTextColor.GREEN),
			List.of(Component.text("Build whatever you want!").color(NamedTextColor.GRAY)),
			"creative",
			Material.NETHER_STAR,
			new Pos(5.5, -1, 10.5, 150, 0)),
	THE_FLOOR_IS_LAVA(Component.text("The Floor is Lava").color(NamedTextColor.GOLD),
			List.of(Component.text("Survive with a partner as the").color(NamedTextColor.GRAY),
					Component.text("floor rises with lava!").color(NamedTextColor.GRAY)),
			"the-floor-is-lava",
			Material.LAVA_BUCKET,
			new Pos(-4.5, -1, 10.5, -150, 0)),
	;

	private final Component displayName;
	private final List<Component> description;
	private final String bmcIdentifier;
	private final Material icon;
	private final Pos spawnLocation;

	Game(Component displayName, List<Component> description, String bmcIdentifier, Material icon, Pos spawnLocation) {
		this.displayName = displayName;
		this.description = description;
		this.bmcIdentifier = bmcIdentifier;
		this.icon = icon;
		this.spawnLocation = spawnLocation;
	}

	public Component getDisplayName() {
		return displayName;
	}

	public List<Component> getDescription() {
		return description;
	}

	public String getBmcIdentifier() {
		return bmcIdentifier;
	}

	public Material getIcon() {
		return icon;
	}

	public Pos getSpawnLocation() {
		return spawnLocation;
	}
}
