package com.example.cavesofzircon.blocks;

import com.example.cavesofzircon.builders.GameTileRepository;
import com.example.cavesofzircon.extensions.EntityExtensions;
import com.example.cavesofzircon.world.GameContext;
import kotlinx.collections.immutable.ExtensionsKt;
import kotlinx.collections.immutable.PersistentMap;
import kotlin.TuplesKt;
import org.hexworks.amethyst.api.entity.Entity;
import org.hexworks.amethyst.api.entity.EntityType;
import org.hexworks.cobalt.datatypes.Maybe;
import org.hexworks.zircon.api.data.BlockTileType;
import org.hexworks.zircon.api.data.Tile;
import org.hexworks.zircon.api.data.base.BaseBlock;

import java.util.ArrayList;
import java.util.List;

public class GameBlock extends BaseBlock<Tile> {

    private Tile defaultTile;
    private final List<Entity<EntityType, GameContext>> currentEntities;

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static PersistentMap<BlockTileType, Tile> tilesFor(Tile defaultTile) {
        return (PersistentMap<BlockTileType, Tile>)(Object)
                ExtensionsKt.persistentMapOf(TuplesKt.to(BlockTileType.CONTENT, defaultTile));
    }

    public GameBlock(Tile defaultTile) {
        super(Tile.empty(), tilesFor(defaultTile));
        this.defaultTile = defaultTile;
        this.currentEntities = new ArrayList<>();
        setTop(GameTileRepository.UNREVEALED);
        updateContent();
    }

    public GameBlock() {
        this(GameTileRepository.FLOOR);
    }

    // Used by createWith
    private GameBlock(Tile defaultTile, List<Entity<EntityType, GameContext>> entities) {
        super(Tile.empty(), tilesFor(defaultTile));
        this.defaultTile = defaultTile;
        this.currentEntities = new ArrayList<>(entities);
        setTop(GameTileRepository.UNREVEALED);
        updateContent();
    }

    public static GameBlock createWith(Entity<EntityType, GameContext> entity) {
        var list = new ArrayList<Entity<EntityType, GameContext>>();
        list.add(entity);
        return new GameBlock(GameTileRepository.FLOOR, list);
    }

    public boolean isFloor() {
        return defaultTile.equals(GameTileRepository.FLOOR);
    }

    public boolean isWall() {
        return defaultTile.equals(GameTileRepository.WALL);
    }

    public boolean isEmptyFloor() {
        return currentEntities.isEmpty();
    }

    public Iterable<Entity<EntityType, GameContext>> getEntities() {
        return new ArrayList<>(currentEntities);
    }

    public Maybe<Entity<EntityType, GameContext>> getOccupier() {
        return Maybe.Companion.ofNullable(
                currentEntities.stream()
                        .filter(EntityExtensions::occupiesBlock)
                        .findFirst()
                        .orElse(null)
        );
    }

    public boolean isOccupied() {
        return getOccupier().isPresent();
    }

    public void addEntity(Entity<EntityType, GameContext> entity) {
        currentEntities.add(entity);
        updateContent();
    }

    public void removeEntity(Entity<EntityType, GameContext> entity) {
        currentEntities.remove(entity);
        updateContent();
    }

    private void updateContent() {
        var entityTiles = currentEntities.stream()
                .map(EntityExtensions::getTile)
                .toList();
        if (entityTiles.contains(GameTileRepository.PLAYER)) {
            setContent(GameTileRepository.PLAYER);
        } else if (!entityTiles.isEmpty()) {
            setContent(entityTiles.get(0));
        } else {
            setContent(defaultTile);
        }
    }
}
