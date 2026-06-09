package com.example.cavesofzircon.messages;

import com.example.cavesofzircon.attributes.types.EnergyUser;
import com.example.cavesofzircon.attributes.types.Food;
import com.example.cavesofzircon.world.GameContext;
import org.hexworks.amethyst.api.entity.Entity;

public class Eat implements EntityAction<EnergyUser, Food> {

    private final GameContext context;
    private final Entity<EnergyUser, GameContext> source;
    private final Entity<Food, GameContext> target;

    public Eat(GameContext context,
               Entity<EnergyUser, GameContext> source,
               Entity<Food, GameContext> target) {
        this.context = context;
        this.source = source;
        this.target = target;
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

    public Entity<EnergyUser, GameContext> getEater() {
        return source;
    }

    @Override
    public Entity<Food, GameContext> getTarget() {
        return target;
    }

    public Entity<Food, GameContext> getFood() {
        return target;
    }
}
