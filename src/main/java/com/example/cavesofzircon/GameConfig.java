package com.example.cavesofzircon;

import org.hexworks.zircon.api.CP437TilesetResources;
import org.hexworks.zircon.api.ColorThemes;
import org.hexworks.zircon.api.application.AppConfig;
import org.hexworks.zircon.api.component.ColorTheme;
import org.hexworks.zircon.api.data.Size3D;
import org.hexworks.zircon.api.resource.TilesetResource;

public final class GameConfig {

    private GameConfig() {}

    public static final int DUNGEON_LEVELS = 2;

    public static final TilesetResource TILESET = CP437TilesetResources.rogueYun16x16();
    public static final ColorTheme THEME = ColorThemes.zenburnVanilla();
    public static final int SIDEBAR_WIDTH = 18;
    public static final int LOG_AREA_HEIGHT = 8;

    public static final int WINDOW_WIDTH = 80;
    public static final int WINDOW_HEIGHT = 50;

    public static final int FUNGI_PER_LEVEL = 15;
    public static final int MAXIMUM_FUNGUS_SPREAD = 20;
    public static final int BATS_PER_LEVEL = 10;
    public static final int ZIRCONS_PER_LEVEL = 20;
    public static final int ZOMBIES_PER_LEVEL = 3;

    public static final Size3D WORLD_SIZE = Size3D.create(WINDOW_WIDTH * 2, WINDOW_HEIGHT * 2, DUNGEON_LEVELS);
    public static final Size3D GAME_COMPONENT_SIZE = Size3D.create(
            WINDOW_WIDTH - SIDEBAR_WIDTH,
            WINDOW_HEIGHT - LOG_AREA_HEIGHT,
            1
    );

    public static AppConfig buildAppConfig() {
        return AppConfig.newBuilder()
                .withDefaultTileset(TILESET)
                .withSize(WINDOW_WIDTH, WINDOW_HEIGHT)
                .build();
    }
}
