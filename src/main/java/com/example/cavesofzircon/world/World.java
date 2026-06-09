package com.example.cavesofzircon.world;

import com.example.cavesofzircon.attributes.Vision;
import com.example.cavesofzircon.attributes.types.Item;
import com.example.cavesofzircon.blocks.GameBlock;
import com.example.cavesofzircon.extensions.EntityExtensions;
import com.example.cavesofzircon.extensions.PositionExtensions;
import org.hexworks.amethyst.api.Engine;
import org.hexworks.amethyst.api.entity.Entity;
import org.hexworks.amethyst.api.entity.EntityType;
import org.hexworks.amethyst.internal.TurnBasedEngine;
import org.hexworks.cobalt.datatypes.Maybe;
import org.hexworks.zircon.api.builder.game.GameAreaBuilder;
import org.hexworks.zircon.api.data.Position;
import org.hexworks.zircon.api.data.Position3D;
import org.hexworks.zircon.api.data.Size3D;
import org.hexworks.zircon.api.data.Tile;
import org.hexworks.zircon.api.game.GameArea;
import org.hexworks.zircon.api.screen.Screen;
import org.hexworks.zircon.api.shape.EllipseFactory;
import org.hexworks.zircon.api.shape.LineFactory;
import org.hexworks.zircon.api.uievent.UIEvent;
import kotlin.jvm.JvmClassMappingKt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class World implements GameArea<Tile, GameBlock> {

    private final GameArea<Tile, GameBlock> delegate;
    @SuppressWarnings("unchecked")
    private final TurnBasedEngine<GameContext> engine = (TurnBasedEngine<GameContext>) Engine.Companion.create();

    @SuppressWarnings({"unchecked", "rawtypes"})
    public World(Map<Position3D, GameBlock> startingBlocks, Size3D visibleSize, Size3D actualSize) {
        this.delegate = (GameArea<Tile, GameBlock>) GameAreaBuilder.newBuilder()
                .withVisibleSize(visibleSize)
                .withActualSize(actualSize)
                .build();

        for (var entry : startingBlocks.entrySet()) {
            var pos = entry.getKey();
            var block = entry.getValue();
            setBlockAt(pos, block);
            for (var entity : block.getEntities()) {
                engine.addEntity(entity);
                EntityExtensions.setPosition(entity, pos);
            }
        }
    }

    // Delegate all GameArea methods
    @Override public Size3D getActualSize() { return delegate.getActualSize(); }
    @Override public Size3D getVisibleSize() { return delegate.getVisibleSize(); }
    @Override public Position3D getVisibleOffset() { return delegate.getVisibleOffset(); }
    @Override public Maybe<GameBlock> fetchBlockAt(Position3D position) { return delegate.fetchBlockAt(position); }
    @Override public GameBlock fetchBlockAtOrNull(Position3D position) { return delegate.fetchBlockAtOrNull(position); }
    @Override public void setBlockAt(Position3D position, GameBlock block) { delegate.setBlockAt(position, block); }
    @Override public Iterable<GameBlock> fetchBlocks() { return delegate.fetchBlocks(); }
    @Override public Iterable<org.hexworks.zircon.api.game.Cell<Tile, GameBlock>> fetchBlocksWithPositions() { return delegate.fetchBlocksWithPositions(); }
    @Override public Iterable<org.hexworks.zircon.api.game.Cell<Tile, GameBlock>> fetchBlocksAt(Position3D offset, Size3D size) { return delegate.fetchBlocksAt(offset, size); }
    @Override public Iterable<org.hexworks.zircon.api.game.Cell<Tile, GameBlock>> fetchBlocksWithPositionsAt(Position3D offset, Size3D size) { return delegate.fetchBlocksWithPositionsAt(offset, size); }
    @Override public boolean hasBlockAt(Position3D position) { return delegate.hasBlockAt(position); }
    @Override public void scrollOneBackward() { delegate.scrollOneBackward(); }
    @Override public void scrollOneForward() { delegate.scrollOneForward(); }
    @Override public void scrollOneLeft() { delegate.scrollOneLeft(); }
    @Override public void scrollOneRight() { delegate.scrollOneRight(); }
    @Override public void scrollOneUp() { delegate.scrollOneUp(); }
    @Override public void scrollOneDown() { delegate.scrollOneDown(); }
    @Override public void scrollUpBy(int z) { delegate.scrollUpBy(z); }
    @Override public void scrollDownBy(int z) { delegate.scrollDownBy(z); }
    @Override public void scrollRightBy(int x) { delegate.scrollRightBy(x); }
    @Override public void scrollLeftBy(int x) { delegate.scrollLeftBy(x); }
    @Override public void scrollForwardBy(int y) { delegate.scrollForwardBy(y); }
    @Override public void scrollBackwardBy(int y) { delegate.scrollBackwardBy(y); }

    public void update(Screen screen, UIEvent uiEvent, Game game) {
        engine.executeTurn(new GameContext(this, screen, uiEvent, game.getPlayer()));
    }

    public boolean moveEntity(Entity<EntityType, GameContext> entity, Position3D position) {
        boolean success = false;
        var oldBlock = fetchBlockAt(EntityExtensions.entityPosition(entity));
        var newBlock = fetchBlockAt(position);
        if (oldBlock.isPresent() && newBlock.isPresent()) {
            success = true;
            oldBlock.get().removeEntity(entity);
            EntityExtensions.setPosition(entity, position);
            newBlock.get().addEntity(entity);
        }
        return success;
    }

    public void addEntity(Entity<EntityType, GameContext> entity, Position3D position) {
        EntityExtensions.setPosition(entity, position);
        engine.addEntity(entity);
        fetchBlockAt(position).map(block -> {
            block.addEntity(entity);
            return block;
        });
    }

    public boolean addAtEmptyPosition(Entity<EntityType, GameContext> entity,
                                       Position3D offset, Size3D size) {
        var location = findEmptyLocationWithin(offset, size);
        if (location.isPresent()) {
            addEntity(entity, location.get());
            return true;
        }
        return false;
    }

    public boolean addAtEmptyPosition(Entity<EntityType, GameContext> entity) {
        return addAtEmptyPosition(entity, Position3D.create(0, 0, 0), getActualSize());
    }

    public void removeEntity(Entity<EntityType, GameContext> entity) {
        fetchBlockAt(EntityExtensions.entityPosition(entity)).map(block -> {
            block.removeEntity(entity);
            return block;
        });
        engine.removeEntity(entity);
        EntityExtensions.setPosition(entity, Position3D.unknown());
    }

    public Maybe<Position3D> findEmptyLocationWithin(Position3D offset, Size3D size) {
        Maybe<Position3D> position = Maybe.empty();
        int maxTries = 10;
        int currentTry = 0;
        while (!position.isPresent() && currentTry < maxTries) {
            var pos = Position3D.create(
                    (int)(Math.random() * size.getXLength()) + offset.getX(),
                    (int)(Math.random() * size.getYLength()) + offset.getY(),
                    (int)(Math.random() * size.getZLength()) + offset.getZ()
            );
            fetchBlockAt(pos).map(block -> {
                if (block.isEmptyFloor()) {
                    return block;
                }
                return null;
            });
            // We need to check if block is empty floor
            var blockOpt = fetchBlockAt(pos);
            if (blockOpt.isPresent() && blockOpt.get().isEmptyFloor()) {
                position = Maybe.of(pos);
            }
            currentTry++;
        }
        return position;
    }

    public boolean isVisionBlockedAt(Position3D pos) {
        return fetchBlockAt(pos).fold(
                () -> false,
                block -> {
                    for (var entity : block.getEntities()) {
                        if (EntityExtensions.blocksVision(entity)) return true;
                    }
                    return false;
                }
        );
    }

    public Iterable<Position> findVisiblePositionsFor(Entity<EntityType, GameContext> entity) {
        var centerPos = EntityExtensions.entityPosition(entity).to2DPosition();
        var visionOpt = entity.findAttribute(JvmClassMappingKt.getKotlinClass(Vision.class));
        if (!visionOpt.isPresent()) {
            return List.of();
        }
        var radius = visionOpt.get().getRadius();
        var ellipsePositions = EllipseFactory.buildEllipse(
                centerPos,
                centerPos.withRelativeX(radius).withRelativeY(radius)
        ).getPositions();
        var result = new ArrayList<Position>();
        var entityZ = EntityExtensions.entityPosition(entity).getZ();
        for (var ringPos : ellipsePositions) {
            var iter = LineFactory.buildLine(centerPos, ringPos).iterator();
            do {
                var next = iter.next();
                result.add(next);
                if (isVisionBlockedAt(Position3D.from2DPosition(next, entityZ))) break;
            } while (iter.hasNext());
        }
        return result;
    }

    public void addWorldEntity(Entity<EntityType, GameContext> entity) {
        engine.addEntity(entity);
    }

    public List<Position> findPath(Entity<EntityType, GameContext> looker,
                                    Entity<EntityType, GameContext> target) {
        var result = new ArrayList<Position>();
        var visionOpt = looker.findAttribute(JvmClassMappingKt.getKotlinClass(Vision.class));
        if (!visionOpt.isPresent()) return result;
        var radius = visionOpt.get().getRadius();
        var lookerPos = EntityExtensions.entityPosition(looker);
        var targetPos = EntityExtensions.entityPosition(target);
        if (isWithinRangeOf(lookerPos, targetPos, radius)) {
            var path = LineFactory.buildLine(lookerPos.to2DPosition(), targetPos.to2DPosition());
            boolean blocked = false;
            for (var pos : path.getPositions()) {
                if (isVisionBlockedAt(pos.toPosition3D(lookerPos.getZ()))) {
                    blocked = true;
                    break;
                }
            }
            if (!blocked) {
                var positions = path.getPositions();
                boolean first = true;
                for (var pos : positions) {
                    if (first) { first = false; continue; }
                    result.add(pos);
                }
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public Maybe<Entity<? extends Item, GameContext>> findTopItem(Position3D position) {
        return fetchBlockAt(position).flatMap(block -> {
            var filtered = EntityExtensions.filterType(block.getEntities(), Item.class);
            return Maybe.ofNullable(filtered.isEmpty() ? null : (Entity<? extends Item, GameContext>) filtered.get(0));
        });
    }

    private boolean isWithinRangeOf(Position3D a, Position3D b, int radius) {
        return !a.isUnknown() && !b.isUnknown()
                && a.getZ() == b.getZ()
                && Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY()) <= radius;
    }
}
