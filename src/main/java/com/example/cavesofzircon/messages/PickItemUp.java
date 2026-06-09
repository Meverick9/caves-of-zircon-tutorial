package com.example.cavesofzircon.messages;

import com.example.cavesofzircon.attributes.types.ItemHolder;
import com.example.cavesofzircon.world.GameContext;
import org.hexworks.amethyst.api.Message;
import org.hexworks.amethyst.api.entity.Entity;
import org.hexworks.zircon.api.data.Position3D;

public class PickItemUp implements Message<GameContext> {

    private final GameContext context;
    private final Entity<? extends ItemHolder, GameContext> source;
    private final Position3D position;

    public PickItemUp(GameContext context,
                      Entity<? extends ItemHolder, GameContext> source,
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
    @SuppressWarnings("unchecked")
    public Entity<org.hexworks.amethyst.api.entity.EntityType, GameContext> getSource() {
        return (Entity<org.hexworks.amethyst.api.entity.EntityType, GameContext>)(Object) source;
    }

    @SuppressWarnings("unchecked")
    public Entity<? extends ItemHolder, GameContext> getItemHolder() {
        return source;
    }

    public Position3D getPosition() {
        return position;
    }
}
