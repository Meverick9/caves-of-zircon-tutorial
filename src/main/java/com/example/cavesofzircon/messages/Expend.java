package com.example.cavesofzircon.messages;

import com.example.cavesofzircon.attributes.types.EnergyUser;
import com.example.cavesofzircon.world.GameContext;
import org.hexworks.amethyst.api.Message;
import org.hexworks.amethyst.api.entity.Entity;

public class Expend implements Message<GameContext> {

    private final GameContext context;
    private final Entity<EnergyUser, GameContext> source;
    private final int energy;

    public Expend(GameContext context, Entity<EnergyUser, GameContext> source, int energy) {
        this.context = context;
        this.source = source;
        this.energy = energy;
    }

    @Override
    public GameContext getContext() {
        return context;
    }

    @Override
    public Entity<EnergyUser, GameContext> getSource() {
        return source;
    }

    public int getEnergy() {
        return energy;
    }
}
