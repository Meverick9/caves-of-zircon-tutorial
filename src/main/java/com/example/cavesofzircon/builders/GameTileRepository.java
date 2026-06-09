package com.example.cavesofzircon.builders;

import org.hexworks.zircon.api.color.ANSITileColor;
import org.hexworks.zircon.api.data.CharacterTile;
import org.hexworks.zircon.api.data.Tile;
import org.hexworks.zircon.api.graphics.Symbols;

public final class GameTileRepository {

    private GameTileRepository() {}

    public static final CharacterTile EMPTY = Tile.empty();

    public static final CharacterTile FLOOR = Tile.newBuilder()
            .withCharacter(Symbols.INTERPUNCT)
            .withForegroundColor(GameColors.FLOOR_FOREGROUND)
            .withBackgroundColor(GameColors.FLOOR_BACKGROUND)
            .buildCharacterTile();

    public static final CharacterTile WALL = Tile.newBuilder()
            .withCharacter('#')
            .withForegroundColor(GameColors.WALL_FOREGROUND)
            .withBackgroundColor(GameColors.WALL_BACKGROUND)
            .buildCharacterTile();

    public static final CharacterTile STAIRS_UP = Tile.newBuilder()
            .withCharacter('<')
            .withForegroundColor(GameColors.ACCENT_COLOR)
            .withBackgroundColor(GameColors.FLOOR_BACKGROUND)
            .buildCharacterTile();

    public static final CharacterTile STAIRS_DOWN = Tile.newBuilder()
            .withCharacter('>')
            .withForegroundColor(GameColors.ACCENT_COLOR)
            .withBackgroundColor(GameColors.FLOOR_BACKGROUND)
            .buildCharacterTile();

    public static final CharacterTile UNREVEALED = Tile.newBuilder()
            .withCharacter(' ')
            .withBackgroundColor(GameColors.UNREVEALED_COLOR)
            .buildCharacterTile();

    public static final CharacterTile PLAYER = Tile.newBuilder()
            .withCharacter('@')
            .withBackgroundColor(GameColors.FLOOR_BACKGROUND)
            .withForegroundColor(GameColors.ACCENT_COLOR)
            .buildCharacterTile();

    public static final CharacterTile FUNGUS = Tile.newBuilder()
            .withCharacter('f')
            .withBackgroundColor(GameColors.FLOOR_BACKGROUND)
            .withForegroundColor(GameColors.FUNGUS_COLOR)
            .buildCharacterTile();

    public static final CharacterTile BAT = Tile.newBuilder()
            .withCharacter('b')
            .withBackgroundColor(GameColors.FLOOR_BACKGROUND)
            .withForegroundColor(GameColors.BAT_COLOR)
            .buildCharacterTile();

    public static final CharacterTile ZIRCON = Tile.newBuilder()
            .withCharacter(',')
            .withBackgroundColor(GameColors.FLOOR_BACKGROUND)
            .withForegroundColor(GameColors.ZIRCON_COLOR)
            .buildCharacterTile();

    public static final CharacterTile BAT_MEAT = Tile.newBuilder()
            .withCharacter('m')
            .withBackgroundColor(GameColors.FLOOR_BACKGROUND)
            .withForegroundColor(GameColors.BAT_MEAT_COLOR)
            .buildCharacterTile();

    public static final CharacterTile CLUB = Tile.newBuilder()
            .withCharacter('(')
            .withForegroundColor(ANSITileColor.GRAY)
            .withBackgroundColor(GameColors.FLOOR_BACKGROUND)
            .buildCharacterTile();

    public static final CharacterTile DAGGER = Tile.newBuilder()
            .withCharacter('(')
            .withForegroundColor(ANSITileColor.WHITE)
            .withBackgroundColor(GameColors.FLOOR_BACKGROUND)
            .buildCharacterTile();

    public static final CharacterTile SWORD = Tile.newBuilder()
            .withCharacter('(')
            .withForegroundColor(ANSITileColor.BRIGHT_WHITE)
            .withBackgroundColor(GameColors.FLOOR_BACKGROUND)
            .buildCharacterTile();

    public static final CharacterTile STAFF = Tile.newBuilder()
            .withCharacter('(')
            .withForegroundColor(ANSITileColor.YELLOW)
            .withBackgroundColor(GameColors.FLOOR_BACKGROUND)
            .buildCharacterTile();

    public static final CharacterTile JACKET = Tile.newBuilder()
            .withCharacter('[')
            .withForegroundColor(ANSITileColor.GRAY)
            .withBackgroundColor(GameColors.FLOOR_BACKGROUND)
            .buildCharacterTile();

    public static final CharacterTile LIGHT_ARMOR = Tile.newBuilder()
            .withCharacter('[')
            .withForegroundColor(ANSITileColor.GREEN)
            .withBackgroundColor(GameColors.FLOOR_BACKGROUND)
            .buildCharacterTile();

    public static final CharacterTile MEDIUM_ARMOR = Tile.newBuilder()
            .withCharacter('[')
            .withForegroundColor(ANSITileColor.WHITE)
            .withBackgroundColor(GameColors.FLOOR_BACKGROUND)
            .buildCharacterTile();

    public static final CharacterTile HEAVY_ARMOR = Tile.newBuilder()
            .withCharacter('[')
            .withForegroundColor(ANSITileColor.BRIGHT_WHITE)
            .withBackgroundColor(GameColors.FLOOR_BACKGROUND)
            .buildCharacterTile();

    public static final CharacterTile ZOMBIE = Tile.newBuilder()
            .withCharacter('z')
            .withForegroundColor(GameColors.ZOMBIE_COLOR)
            .withBackgroundColor(GameColors.FLOOR_BACKGROUND)
            .buildCharacterTile();

    public static final CharacterTile EXIT = Tile.newBuilder()
            .withCharacter('+')
            .withForegroundColor(GameColors.ACCENT_COLOR)
            .withBackgroundColor(GameColors.FLOOR_BACKGROUND)
            .buildCharacterTile();
}
