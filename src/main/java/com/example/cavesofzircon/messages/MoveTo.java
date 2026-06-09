package com.example.cavesofzircon.messages;

import com.example.cavesofzircon.world.GameContext;
import org.hexworks.amethyst.api.Message;
import org.hexworks.amethyst.api.entity.Entity;
import org.hexworks.amethyst.api.entity.EntityType;
import org.hexworks.zircon.api.data.Position3D;

public class MoveTo implements Message<GameContext> {

    private final GameContext context;
    private final Entity<EntityType, GameContext> source;
    private final Position3D position;

    public MoveTo(GameContext context,
                  Entity<EntityType, GameContext> source,
                  Position3D position) {
        this.context = context;
        this.source = source;
        this.position = position;
    }

    @Override
    public GameContext getContext() {
        return context;
    }

    @Override
    public Entity<EntityType, GameContext> getSource() {
        return source;
    }

    public Position3D getPosition() {
        return position;
    }
}
