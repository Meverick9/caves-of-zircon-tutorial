package com.example.cavesofzircon.messages;

import com.example.cavesofzircon.world.GameContext;
import org.hexworks.amethyst.api.Message;
import org.hexworks.amethyst.api.entity.Entity;
import org.hexworks.amethyst.api.entity.EntityType;

public class EntityDestroyed implements Message<GameContext> {

    private final GameContext context;
    private final Entity<EntityType, GameContext> source;
    private final Entity<EntityType, GameContext> destroyer;

    public EntityDestroyed(GameContext context,
                            Entity<EntityType, GameContext> source,
                            Entity<EntityType, GameContext> destroyer) {
        this.context = context;
        this.source = source;
        this.destroyer = destroyer;
    }

    @Override
    public GameContext getContext() {
        return context;
    }

    @Override
    public Entity<EntityType, GameContext> getSource() {
        return source;
    }

    public Entity<EntityType, GameContext> getDestroyer() {
        return destroyer;
    }
}
