package com.example.cavesofzircon.world;

import com.example.cavesofzircon.attributes.Vision;
import com.example.cavesofzircon.attributes.types.Item;
import com.example.cavesofzircon.blocks.GameBlock;
import com.example.cavesofzircon.extensions.EntityExtensions;
import com.example.cavesofzircon.extensions.PositionExtensions;
import kotlinx.collections.immutable.ExtensionsKt;
import org.hexworks.amethyst.api.Engine;
import org.hexworks.amethyst.api.entity.Entity;
import org.hexworks.amethyst.api.entity.EntityType;
import org.hexworks.amethyst.internal.TurnBasedEngine;
import org.hexworks.cobalt.datatypes.Maybe;
import org.hexworks.zircon.api.data.Position;
import org.hexworks.zircon.api.data.Position3D;
import org.hexworks.zircon.api.data.Size3D;
import org.hexworks.zircon.api.data.Tile;
import org.hexworks.zircon.api.game.base.BaseGameArea;
import org.hexworks.zircon.api.screen.Screen;
import org.hexworks.zircon.api.shape.EllipseFactory;
import org.hexworks.zircon.api.shape.LineFactory;
import org.hexworks.zircon.api.uievent.UIEvent;
import org.hexworks.zircon.internal.behavior.impl.DefaultScrollable3D;
import kotlin.jvm.JvmClassMappingKt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"unchecked", "rawtypes"})
public class World extends BaseGameArea<Tile, GameBlock> {

    private final TurnBasedEngine<GameContext> engine;

    public World(Map<Position3D, GameBlock> startingBlocks, Size3D visibleSize, Size3D actualSize) {
        super(
                visibleSize,
                actualSize,
                Position3D.defaultPosition(),
                ExtensionsKt.persistentMapOf(),
                Collections.emptyList(),
                new DefaultScrollable3D(visibleSize, actualSize)
        );
        this.engine = (TurnBasedEngine<GameContext>)(Object) Engine.Companion.create();

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
        Maybe<Position3D> position = Maybe.Companion.empty();
        int maxTries = 10;
        int currentTry = 0;
        while (!position.isPresent() && currentTry < maxTries) {
            var pos = Position3D.create(
                    (int)(Math.random() * size.getXLength()) + offset.getX(),
                    (int)(Math.random() * size.getYLength()) + offset.getY(),
                    (int)(Math.random() * size.getZLength()) + offset.getZ()
            );
            var blockOpt = fetchBlockAt(pos);
            if (blockOpt.isPresent() && blockOpt.get().isEmptyFloor()) {
                position = Maybe.Companion.of(pos);
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

    public Iterable<Position> findVisiblePositionsFor(Entity<?, GameContext> entity) {
        var centerPos = EntityExtensions.entityPosition(entity).to2DPosition();
        var visionOpt = entity.findAttribute(JvmClassMappingKt.getKotlinClass(Vision.class));
        if (!visionOpt.isPresent()) {
            return List.of();
        }
        var radius = visionOpt.get().getRadius();
        var ellipsePositions = EllipseFactory.INSTANCE.buildEllipse(
                centerPos,
                centerPos.withRelativeX(radius).withRelativeY(radius)
        ).getPositions();
        var result = new ArrayList<Position>();
        var entityZ = EntityExtensions.entityPosition(entity).getZ();
        for (var ringPos : ellipsePositions) {
            var iter = LineFactory.INSTANCE.buildLine(centerPos, ringPos).iterator();
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
            var path = LineFactory.INSTANCE.buildLine(lookerPos.to2DPosition(), targetPos.to2DPosition());
            boolean blocked = false;
            var positions = path.getPositions();
            for (var pos : positions) {
                if (isVisionBlockedAt(pos.toPosition3D(lookerPos.getZ()))) {
                    blocked = true;
                    break;
                }
            }
            if (!blocked) {
                boolean first = true;
                for (var pos : positions) {
                    if (first) { first = false; continue; }
                    result.add(pos);
                }
            }
        }
        return result;
    }

    public Maybe<Entity<? extends Item, GameContext>> findTopItem(Position3D position) {
        return fetchBlockAt(position).flatMap(block -> {
            @SuppressWarnings({"unchecked","rawtypes"})
            Iterable<Entity<?, GameContext>> entities = (Iterable)(Object) block.getEntities();
            var filtered = EntityExtensions.filterType(entities, Item.class);
            return Maybe.Companion.ofNullable(filtered.isEmpty() ? null : filtered.get(0));
        });
    }

    private boolean isWithinRangeOf(Position3D a, Position3D b, int radius) {
        return !a.isUnknown() && !b.isUnknown()
                && a.getZ() == b.getZ()
                && Math.abs(a.getX() - b.getX()) + Math.abs(a.getY() - b.getY()) <= radius;
    }
}
