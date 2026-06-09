package com.example.cavesofzircon.builders;

import com.example.cavesofzircon.blocks.GameBlock;
import com.example.cavesofzircon.world.GameContext;
import org.hexworks.amethyst.api.entity.Entity;
import org.hexworks.amethyst.api.entity.EntityType;

@SuppressWarnings({"unchecked", "rawtypes"})
public final class GameBlockFactory {

    private GameBlockFactory() {}

    public static GameBlock floor() {
        return new GameBlock(GameTileRepository.FLOOR);
    }

    public static GameBlock wall() {
        return GameBlock.createWith((Entity<EntityType, GameContext>)(Object) EntityFactory.newWall());
    }

    public static GameBlock stairsDown() {
        return GameBlock.createWith((Entity<EntityType, GameContext>)(Object) EntityFactory.newStairsDown());
    }

    public static GameBlock stairsUp() {
        return GameBlock.createWith((Entity<EntityType, GameContext>)(Object) EntityFactory.newStairsUp());
    }
}
