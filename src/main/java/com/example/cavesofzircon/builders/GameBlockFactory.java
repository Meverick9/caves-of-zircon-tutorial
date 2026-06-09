package com.example.cavesofzircon.builders;

import com.example.cavesofzircon.blocks.GameBlock;

public final class GameBlockFactory {

    private GameBlockFactory() {}

    public static GameBlock floor() {
        return new GameBlock(GameTileRepository.FLOOR);
    }

    public static GameBlock wall() {
        return GameBlock.createWith(EntityFactory.newWall());
    }

    public static GameBlock stairsDown() {
        return GameBlock.createWith(EntityFactory.newStairsDown());
    }

    public static GameBlock stairsUp() {
        return GameBlock.createWith(EntityFactory.newStairsUp());
    }
}
