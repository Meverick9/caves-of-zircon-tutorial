package com.example.cavesofzircon.world;

import com.example.cavesofzircon.GameConfig;
import com.example.cavesofzircon.attributes.types.EntityTypes;
import com.example.cavesofzircon.builders.EntityFactory;
import com.example.cavesofzircon.builders.WorldBuilder;
import org.hexworks.amethyst.api.entity.Entity;
import org.hexworks.amethyst.api.entity.EntityType;
import org.hexworks.zircon.api.data.Position3D;
import org.hexworks.zircon.api.data.Size;
import org.hexworks.zircon.api.data.Size3D;

public class GameBuilder {

    private final Size3D worldSize;
    private final Size3D visibleSize;
    public final World world;

    public GameBuilder(Size3D worldSize) {
        this.worldSize = worldSize;
        this.visibleSize = Size3D.create(
                GameConfig.WINDOW_WIDTH - GameConfig.SIDEBAR_WIDTH,
                GameConfig.WINDOW_HEIGHT - GameConfig.LOG_AREA_HEIGHT,
                1
        );
        this.world = new WorldBuilder(worldSize)
                .makeCaves()
                .build(visibleSize);
    }

    public Game buildGame() {
        prepareWorld();
        var player = addPlayer();
        addFungi();
        addBats();
        addZircons();
        addZombies();
        addExit();
        world.addWorldEntity((Entity<EntityType, GameContext>)(Object) EntityFactory.newFogOfWar());
        return Game.create(player, world);
    }

    private void prepareWorld() {
        world.scrollUpBy(world.getActualSize().getZLength());
    }

    @SuppressWarnings("unchecked")
    private Entity<EntityTypes.PlayerType, GameContext> addPlayer() {
        var player = EntityFactory.newPlayer();
        addToWorld(player, GameConfig.DUNGEON_LEVELS - 1, world.getVisibleSize().to2DSize());
        return (Entity<EntityTypes.PlayerType, GameContext>) player;
    }

    private void addFungi() {
        for (int level = 0; level < world.getActualSize().getZLength(); level++) {
            for (int i = 0; i < GameConfig.FUNGI_PER_LEVEL; i++) {
                addToWorld(EntityFactory.newFungus(), level, world.getActualSize().to2DSize());
            }
        }
    }

    private void addBats() {
        for (int level = 0; level < world.getActualSize().getZLength(); level++) {
            for (int i = 0; i < GameConfig.BATS_PER_LEVEL; i++) {
                addToWorld(EntityFactory.newBat(), level, world.getActualSize().to2DSize());
            }
        }
    }

    private void addZircons() {
        for (int level = 0; level < world.getActualSize().getZLength(); level++) {
            for (int i = 0; i < GameConfig.ZIRCONS_PER_LEVEL; i++) {
                addToWorld(EntityFactory.newZircon(), level, world.getActualSize().to2DSize());
            }
        }
    }

    private void addZombies() {
        for (int level = 0; level < world.getActualSize().getZLength(); level++) {
            for (int i = 0; i < GameConfig.ZOMBIES_PER_LEVEL; i++) {
                addToWorld(EntityFactory.newZombie(), level, world.getActualSize().to2DSize());
            }
        }
    }

    private void addExit() {
        addToWorld(EntityFactory.newExit(), 0, world.getActualSize().to2DSize());
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void addToWorld(Entity<? extends EntityType, GameContext> entity, int atLevel, Size atArea) {
        world.addAtEmptyPosition(
                (Entity) entity,
                Position3D.defaultPosition().withRelativeZ(atLevel),
                Size3D.from2DSize(atArea, 1)
        );
    }

    public static Game create() {
        return new GameBuilder(GameConfig.WORLD_SIZE).buildGame();
    }
}
