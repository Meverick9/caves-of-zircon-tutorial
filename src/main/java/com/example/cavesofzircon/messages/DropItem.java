package com.example.cavesofzircon.messages;

import com.example.cavesofzircon.attributes.types.Item;
import com.example.cavesofzircon.attributes.types.ItemHolder;
import com.example.cavesofzircon.world.GameContext;
import org.hexworks.amethyst.api.Message;
import org.hexworks.amethyst.api.entity.Entity;
import org.hexworks.zircon.api.data.Position3D;

public class DropItem implements Message<GameContext> {

    private final GameContext context;
    private final Entity<? extends ItemHolder, GameContext> source;
    private final Entity<? extends Item, GameContext> item;
    private final Position3D position;

    public DropItem(GameContext context,
                    Entity<? extends ItemHolder, GameContext> source,
                    Entity<? extends Item, GameContext> item,
                    Position3D position) {
        this.context = context;
        this.source = source;
        this.item = item;
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
    public Entity<ItemHolder, GameContext> getItemHolder() {
        return (Entity<ItemHolder, GameContext>) source;
    }

    public Entity<? extends Item, GameContext> getItem() {
        return item;
    }

    public Position3D getPosition() {
        return position;
    }
}
