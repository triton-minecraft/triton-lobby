package dev.kyriji.lobby.enums;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.item.Material;

import java.util.List;

public enum Game {
	CREATIVE(Component.text("Creative").color(NamedTextColor.GREEN),
			List.of(Component.text("Build whatever you want!").color(NamedTextColor.GRAY)),
			"creative",
			Material.GRASS_BLOCK),
	THE_FLOOR_IS_LAVA(Component.text("The Floor is Lava").color(NamedTextColor.GOLD),
			List.of(Component.text("Survive with a partner as the").color(NamedTextColor.GRAY),
					Component.text("floor rises with lava!").color(NamedTextColor.GRAY)),
			"the-floor-is-lava",
			Material.LAVA_BUCKET),
	;

	private final Component displayName;
	private final List<Component> description;
	private final String bmcIdentifier;
	private final Material icon;

	Game(Component displayName, List<Component> description, String bmcIdentifier, Material icon) {
		this.displayName = displayName;
		this.description = description;
		this.bmcIdentifier = bmcIdentifier;
		this.icon = icon;
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
}
