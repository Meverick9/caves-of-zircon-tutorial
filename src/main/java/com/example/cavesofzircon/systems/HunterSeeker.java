package com.example.cavesofzircon.systems;

import com.example.cavesofzircon.extensions.EntityExtensions;
import com.example.cavesofzircon.messages.MoveTo;
import com.example.cavesofzircon.world.GameContext;
import kotlin.coroutines.Continuation;
import org.hexworks.amethyst.api.base.BaseBehavior;
import org.hexworks.amethyst.api.entity.Entity;
import org.hexworks.amethyst.api.entity.EntityType;

public class HunterSeeker extends BaseBehavior<GameContext> {

    public static final HunterSeeker INSTANCE = new HunterSeeker();

    private HunterSeeker() {
        super();
    }

    @Override
    public Object update(Entity<EntityType, GameContext> entity, GameContext context, Continuation<? super Boolean> continuation) {
        var world = context.getWorld();
        var player = context.getPlayer();
        boolean hunted = false;
        var path = world.findPath(entity, player);
        if (!path.isEmpty()) {
            var playerZ = EntityExtensions.entityPosition(player).getZ();
            entity.receiveMessage(new MoveTo(context, entity,
                    path.iterator().next().toPosition3D(playerZ)), continuation);
            hunted = true;
        }
        return hunted;
    }
}
