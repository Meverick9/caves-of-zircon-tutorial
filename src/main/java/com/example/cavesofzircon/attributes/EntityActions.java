package com.example.cavesofzircon.attributes;

import com.example.cavesofzircon.messages.EntityAction;
import com.example.cavesofzircon.world.GameContext;
import org.hexworks.amethyst.api.base.BaseAttribute;
import org.hexworks.amethyst.api.entity.Entity;
import org.hexworks.amethyst.api.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public class EntityActions extends BaseAttribute {

    private final Class<?>[] actionClasses;

    @SafeVarargs
    public EntityActions(Class<? extends EntityAction<?, ?>>... actionClasses) {
        this.actionClasses = actionClasses;
    }

    @SuppressWarnings("unchecked")
    public Iterable<EntityAction<?, ?>> createActionsFor(
            GameContext context,
            Entity<EntityType, GameContext> source,
            Entity<EntityType, GameContext> target) {
        var result = new ArrayList<EntityAction<?, ?>>();
        for (var cls : actionClasses) {
            try {
                var action = (EntityAction<?, ?>) cls.getConstructors()[0]
                        .newInstance(context, source, target);
                result.add(action);
            } catch (Exception e) {
                throw new IllegalArgumentException(
                        "Can't create EntityAction. Does it have the proper constructor?");
            }
        }
        return result;
    }
}
