package com.example.cavesofzircon.messages;

import com.example.cavesofzircon.world.GameContext;
import org.hexworks.amethyst.api.entity.Entity;
import org.hexworks.amethyst.api.entity.EntityType;

public class Dig implements EntityAction<EntityType, EntityType> {

    private final GameContext context;
    private final Entity<EntityType, GameContext> source;
    private final Entity<EntityType, GameContext> target;

    public Dig(GameContext context,
               Entity<EntityType, GameContext> source,
               Entity<EntityType, GameContext> target) {
        this.context = context;
        this.source = source;
        this.target = target;
    }

    @Override
    public GameContext getContext() {
        return context;
    }

    @Override
    public Entity<EntityType, GameContext> getSource() {
        return source;
    }

    @Override
    public Entity<EntityType, GameContext> getTarget() {
        return target;
    }
}
