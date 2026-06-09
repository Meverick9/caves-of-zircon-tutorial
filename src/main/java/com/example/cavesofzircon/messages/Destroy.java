package com.example.cavesofzircon.messages;

import com.example.cavesofzircon.world.GameContext;
import org.hexworks.amethyst.api.Message;
import org.hexworks.amethyst.api.entity.Entity;
import org.hexworks.amethyst.api.entity.EntityType;

public class Destroy implements Message<GameContext> {

    private final GameContext context;
    private final Entity<EntityType, GameContext> source;
    private final Entity<EntityType, GameContext> target;
    private final String cause;

    public Destroy(GameContext context,
                   Entity<EntityType, GameContext> source,
                   Entity<EntityType, GameContext> target,
                   String cause) {
        this.context = context;
        this.source = source;
        this.target = target;
        this.cause = cause;
    }

    public Destroy(GameContext context,
                   Entity<EntityType, GameContext> source,
                   Entity<EntityType, GameContext> target) {
        this(context, source, target, "natural causes.");
    }

    @Override
    public GameContext getContext() {
        return context;
    }

    @Override
    public Entity<EntityType, GameContext> getSource() {
        return source;
    }

    public Entity<EntityType, GameContext> getTarget() {
        return target;
    }

    public String getCause() {
        return cause;
    }
}
