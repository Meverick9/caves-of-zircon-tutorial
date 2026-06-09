package com.example.cavesofzircon.messages;

import com.example.cavesofzircon.world.GameContext;
import org.hexworks.amethyst.api.Message;
import org.hexworks.amethyst.api.entity.Entity;
import org.hexworks.amethyst.api.entity.EntityType;

public class MoveUp implements Message<GameContext> {

    private final GameContext context;
    private final Entity<EntityType, GameContext> source;

    public MoveUp(GameContext context, Entity<EntityType, GameContext> source) {
        this.context = context;
        this.source = source;
    }

    @Override
    public GameContext getContext() {
        return context;
    }

    @Override
    public Entity<EntityType, GameContext> getSource() {
        return source;
    }
}
