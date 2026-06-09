package com.example.cavesofzircon.builders;

import org.hexworks.zircon.api.color.TileColor;

public final class GameColors {

    private GameColors() {}

    public static final TileColor WALL_FOREGROUND = TileColor.fromString("#75715E");
    public static final TileColor WALL_BACKGROUND = TileColor.fromString("#3E3D32");

    public static final TileColor FLOOR_FOREGROUND = TileColor.fromString("#75715E");
    public static final TileColor FLOOR_BACKGROUND = TileColor.fromString("#1e2320");

    public static final TileColor ACCENT_COLOR = TileColor.fromString("#FFCD22");
    public static final TileColor UNREVEALED_COLOR = TileColor.fromString("#090909");

    public static final TileColor FUNGUS_COLOR = TileColor.fromString("#85DD1B");
    public static final TileColor BAT_COLOR = TileColor.fromString("#2348b2");
    public static final TileColor ZIRCON_COLOR = TileColor.fromString("#dddddd");
    public static final TileColor BAT_MEAT_COLOR = TileColor.fromString("#EA4861");
    public static final TileColor ZOMBIE_COLOR = TileColor.fromString("#618358");
}
