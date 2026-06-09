package com.example.cavesofzircon.systems;

import com.example.cavesofzircon.attributes.EnergyLevel;
import com.example.cavesofzircon.extensions.EntityExtensions;
import com.example.cavesofzircon.functions.Functions;
import com.example.cavesofzircon.messages.Eat;
import com.example.cavesofzircon.world.GameContext;
import kotlin.coroutines.Continuation;
import kotlin.jvm.JvmClassMappingKt;
import org.hexworks.amethyst.api.Consumed;
import org.hexworks.amethyst.api.Response;
import org.hexworks.amethyst.api.base.BaseFacet;

public class DigestiveSystem extends BaseFacet<GameContext, Eat> {

    public static final DigestiveSystem INSTANCE = new DigestiveSystem();

    private DigestiveSystem() {
        super(JvmClassMappingKt.getKotlinClass(Eat.class),
              JvmClassMappingKt.getKotlinClass(EnergyLevel.class));
    }

    @Override
    public Object receiveMessage(Eat message, Continuation<? super Response> continuation) {
        var entity = message.getSource();
        var food = message.getFood();

        int foodEnergy = EntityExtensions.getFoodEnergy(food);
        var energyLevel = EntityExtensions.getEnergyLevel(entity);
        energyLevel.setCurrentEnergy(energyLevel.getCurrentEnergy() + foodEnergy);

        String verb = EntityExtensions.isPlayer(entity) ? "You eat" : "The " + entity.getName() + " eats";
        Functions.logGameEvent(verb + " the " + food.getName() + ".", this);
        return Consumed.INSTANCE;
    }
}
