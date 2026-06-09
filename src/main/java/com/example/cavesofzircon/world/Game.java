package com.example.cavesofzircon.world;

import com.example.cavesofzircon.attributes.types.EntityTypes;
import org.hexworks.amethyst.api.entity.Entity;

public class Game {

    private final World world;
    private final Entity<EntityTypes.PlayerType, GameContext> player;

    public Game(World world, Entity<EntityTypes.PlayerType, GameContext> player) {
        this.world = world;
        this.player = player;
    }

    public World getWorld() {
        return world;
    }

    public Entity<EntityTypes.PlayerType, GameContext> getPlayer() {
        return player;
    }

    public static Game create(Entity<EntityTypes.PlayerType, GameContext> player, World world) {
        return new Game(world, player);
    }
}
