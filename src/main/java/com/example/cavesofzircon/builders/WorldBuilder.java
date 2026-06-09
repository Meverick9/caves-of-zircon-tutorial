package com.example.cavesofzircon.builders;

import com.example.cavesofzircon.blocks.GameBlock;
import com.example.cavesofzircon.extensions.PositionExtensions;
import com.example.cavesofzircon.world.World;
import org.hexworks.zircon.api.data.Position3D;
import org.hexworks.zircon.api.data.Size3D;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class WorldBuilder {

    private final Size3D worldSize;
    private Map<Position3D, GameBlock> blocks = new HashMap<>();
    private final int depth;
    private final int width;
    private final int height;

    private static final Random RANDOM = new Random();

    public WorldBuilder(Size3D worldSize) {
        this.worldSize = worldSize;
        this.depth = worldSize.getYLength();
        this.width = worldSize.getXLength();
        this.height = worldSize.getZLength();
    }

    public WorldBuilder makeCaves() {
        return randomizeTiles().smooth(8).connectLevels();
    }

    public World build(Size3D visibleSize) {
        return new World(blocks, visibleSize, worldSize);
    }

    private WorldBuilder randomizeTiles() {
        forAllPositions(pos -> {
            blocks.put(pos, Math.random() < 0.5 ? GameBlockFactory.floor() : GameBlockFactory.wall());
        });
        return this;
    }

    private WorldBuilder smooth(int iterations) {
        for (int i = 0; i < iterations; i++) {
            var newBlocks = new HashMap<Position3D, GameBlock>();
            forAllPositions(pos -> {
                int floors = 0, rocks = 0;
                var neighbors = PositionExtensions.sameLevelNeighborsShuffled(pos);
                neighbors.add(pos);
                for (var neighbor : neighbors) {
                    var block = blocks.get(neighbor);
                    if (block != null) {
                        if (block.isEmptyFloor()) floors++;
                        else rocks++;
                    }
                }
                newBlocks.put(pos, floors >= rocks ? GameBlockFactory.floor() : GameBlockFactory.wall());
            });
            blocks = newBlocks;
        }
        return this;
    }

    private void forAllPositions(java.util.function.Consumer<Position3D> fn) {
        var iter = worldSize.fetchPositions().iterator();
        while (iter.hasNext()) {
            fn.accept(iter.next());
        }
    }

    private WorldBuilder connectLevels() {
        for (int level = height - 1; level >= 1; level--) {
            connectRegionDown(level);
        }
        return this;
    }

    private void connectRegionDown(int currentLevel) {
        Position3D posToConnect = null;
        // Keep trying until we find a position where both levels are empty floor
        while (posToConnect == null) {
            var candidate = Position3D.create(
                    RANDOM.nextInt(Math.max(1, width - 1)),
                    RANDOM.nextInt(Math.max(1, depth - 1)),
                    currentLevel
            );
            var below = candidate.withRelativeZ(-1);
            if (isEmptyFloor(blocks.get(candidate)) && isEmptyFloor(blocks.get(below))) {
                posToConnect = candidate;
            }
        }
        blocks.put(posToConnect, GameBlockFactory.stairsDown());
        blocks.put(posToConnect.withRelativeZ(-1), GameBlockFactory.stairsUp());
    }

    private boolean isEmptyFloor(GameBlock block) {
        return block != null && block.isEmptyFloor();
    }
}
