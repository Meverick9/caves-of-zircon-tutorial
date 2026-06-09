package com.example.cavesofzircon.messages;

import com.example.cavesofzircon.attributes.types.Combatant;
import com.example.cavesofzircon.world.GameContext;
import org.hexworks.amethyst.api.entity.Entity;

public class Attack implements EntityAction<Combatant, Combatant> {

    private final GameContext context;
    private final Entity<Combatant, GameContext> source;
    private final Entity<Combatant, GameContext> target;

    public Attack(GameContext context,
                  Entity<Combatant, GameContext> source,
                  Entity<Combatant, GameContext> target) {
        this.context = context;
        this.source = source;
        this.target = target;
    }

    @Override
    public GameContext getContext() {
        return context;
    }

    @Override
    public Entity<Combatant, GameContext> getSource() {
        return source;
    }

    @Override
    public Entity<Combatant, GameContext> getTarget() {
        return target;
    }
}
