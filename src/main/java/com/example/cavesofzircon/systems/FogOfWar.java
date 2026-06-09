package com.example.cavesofzircon.systems;

import com.example.cavesofzircon.builders.GameTileRepository;
import com.example.cavesofzircon.extensions.EntityExtensions;
import com.example.cavesofzircon.world.GameContext;
import kotlin.coroutines.Continuation;
import org.hexworks.amethyst.api.base.BaseBehavior;
import org.hexworks.amethyst.api.entity.Entity;
import org.hexworks.amethyst.api.entity.EntityType;
import org.hexworks.zircon.api.data.Position3D;

public class FogOfWar extends BaseBehavior<GameContext> {

    public static final FogOfWar INSTANCE = new FogOfWar();

    private FogOfWar() {
        super();
    }

    @Override
    public Object update(Entity<EntityType, GameContext> entity, GameContext context, Continuation<? super Boolean> continuation) {
        var world = context.getWorld();
        var player = context.getPlayer();
        var playerZ = EntityExtensions.entityPosition(player).getZ();

        for (var pos : world.findVisiblePositionsFor(player)) {
            world.fetchBlockAt(Position3D.create(pos.getX(), pos.getY(), playerZ)).map(block -> {
                block.setTop(GameTileRepository.EMPTY);
                return block;
            });
        }
        return true;
    }
}
