package com.example.cavesofzircon.systems;

import com.example.cavesofzircon.extensions.EntityExtensions;
import com.example.cavesofzircon.extensions.PositionExtensions;
import com.example.cavesofzircon.messages.MoveTo;
import com.example.cavesofzircon.world.GameContext;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.EmptyCoroutineContext;
import org.hexworks.amethyst.api.base.BaseBehavior;
import org.hexworks.amethyst.api.entity.Entity;
import org.hexworks.amethyst.api.entity.EntityType;

public class Wanderer extends BaseBehavior<GameContext> {

    public static final Wanderer INSTANCE = new Wanderer();

    private Wanderer() {
        super();
    }

    @Override
    public Object update(Entity<EntityType, GameContext> entity, GameContext context, Continuation<? super Boolean> continuation) {
        var pos = EntityExtensions.entityPosition(entity);
        if (!pos.isUnknown()) {
            var neighbors = PositionExtensions.sameLevelNeighborsShuffled(pos);
            entity.receiveMessage(new MoveTo(context, entity, neighbors.get(0)), continuation);
            return true;
        }
        return false;
    }
}
