package com.example.cavesofzircon.messages;

import com.example.cavesofzircon.world.GameContext;
import org.hexworks.amethyst.api.Message;
import org.hexworks.amethyst.api.entity.Entity;
import org.hexworks.amethyst.api.entity.EntityType;

public interface EntityAction<S extends EntityType, T extends EntityType> extends Message<GameContext> {

    Entity<T, GameContext> getTarget();
}
