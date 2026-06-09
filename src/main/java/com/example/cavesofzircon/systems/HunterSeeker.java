package com.example.cavesofzircon.systems;

import com.example.cavesofzircon.extensions.EntityExtensions;
import com.example.cavesofzircon.messages.MoveTo;
import com.example.cavesofzircon.world.GameContext;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.EmptyCoroutineContext;
import org.hexworks.amethyst.api.base.BaseBehavior;
import org.hexworks.amethyst.api.entity.Entity;
import org.hexworks.amethyst.api.entity.EntityType;

public class HunterSeeker extends BaseBehavior<GameContext> {

    public static final HunterSeeker INSTANCE = new HunterSeeker();

    private static final Continuation<Object> SYNC_CONT = new Continuation<>() {
        @Override public kotlin.coroutines.CoroutineContext getContext() { return EmptyCoroutineContext.INSTANCE; }
        @Override public void resumeWith(Object result) {}
    };

    private HunterSeeker() {
        super();
    }

    @Override
    public Object update(Entity<? extends EntityType, GameContext> entity, GameContext context,
                         Continuation<? super Boolean> continuation) {
        var world = context.getWorld();
        var player = context.getPlayer();
        boolean hunted = false;
        var path = world.findPath((Entity<EntityType, GameContext>)(Object) entity, (Entity<EntityType, GameContext>)(Object) player);
        if (!path.isEmpty()) {
            var playerZ = EntityExtensions.entityPosition(player).getZ();
            entity.receiveMessage(new MoveTo(context, (Entity<EntityType, GameContext>)(Object) entity,
                    path.iterator().next().toPosition3D(playerZ)), SYNC_CONT);
            hunted = true;
        }
        return hunted;
    }
}
