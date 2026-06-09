package com.example.cavesofzircon.systems;

import com.example.cavesofzircon.attributes.EnergyLevel;
import com.example.cavesofzircon.attributes.types.EnergyUser;
import com.example.cavesofzircon.extensions.EntityExtensions;
import com.example.cavesofzircon.messages.Destroy;
import com.example.cavesofzircon.messages.Expend;
import com.example.cavesofzircon.world.GameContext;
import kotlin.coroutines.Continuation;
import kotlin.jvm.JvmClassMappingKt;
import org.hexworks.amethyst.api.Consumed;
import org.hexworks.amethyst.api.Response;
import org.hexworks.amethyst.api.base.BaseActor;
import org.hexworks.amethyst.api.entity.Entity;
import org.hexworks.amethyst.api.entity.EntityType;

public class EnergyExpender extends BaseActor<GameContext, Expend> {

    public static final EnergyExpender INSTANCE = new EnergyExpender();

    private EnergyExpender() {
        super(JvmClassMappingKt.getKotlinClass(Expend.class),
              JvmClassMappingKt.getKotlinClass(EnergyLevel.class));
    }

    @Override
    public Object receiveMessage(Expend message, Continuation<? super Response> continuation) {
        var context = message.getContext();
        var entity = message.getSource();
        int energy = message.getEnergy();

        var energyLevel = EntityExtensions.tryToFindAttribute(entity, EnergyLevel.class);
        energyLevel.setCurrentEnergy(energyLevel.getCurrentEnergy() - energy);
        checkStarvation(context, entity, energyLevel, continuation);
        return Consumed.INSTANCE;
    }

    @Override
    public Object update(Entity<EntityType, GameContext> entity, GameContext context, Continuation<? super Boolean> continuation) {
        EntityExtensions.whenTypeIs(entity, EnergyUser.class, energyUser -> {
            entity.receiveMessage(new Expend(context, entity, 2), continuation);
        });
        return true;
    }

    private void checkStarvation(GameContext context, Entity<EntityType, GameContext> entity,
                                   EnergyLevel energyLevel, Continuation<?> continuation) {
        if (energyLevel.getCurrentEnergy() <= 0) {
            entity.receiveMessage(new Destroy(context, entity, entity, "because of starvation"), continuation);
        }
    }
}
