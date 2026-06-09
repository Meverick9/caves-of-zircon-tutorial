package com.example.cavesofzircon.systems;

import com.example.cavesofzircon.extensions.EntityExtensions;
import com.example.cavesofzircon.functions.Functions;
import com.example.cavesofzircon.messages.PickItemUp;
import com.example.cavesofzircon.world.GameContext;
import kotlin.coroutines.Continuation;
import kotlin.jvm.JvmClassMappingKt;
import org.hexworks.amethyst.api.Consumed;
import org.hexworks.amethyst.api.Response;
import org.hexworks.amethyst.api.base.BaseFacet;

public class ItemPicker extends BaseFacet<GameContext, PickItemUp> {

    public static final ItemPicker INSTANCE = new ItemPicker();

    private ItemPicker() {
        super(JvmClassMappingKt.getKotlinClass(PickItemUp.class));
    }

    @Override
    public Object receive(PickItemUp message, Continuation<? super Response> continuation) {
        var context = message.getContext();
        var itemHolder = message.getItemHolder();
        var position = message.getPosition();
        var world = context.getWorld();

        world.findTopItem(position).map(item -> {
            if (EntityExtensions.addItem(itemHolder, item)) {
                world.removeEntity((org.hexworks.amethyst.api.entity.Entity<org.hexworks.amethyst.api.entity.EntityType, com.example.cavesofzircon.world.GameContext>)(Object) item);
                String subject = EntityExtensions.isPlayer(itemHolder) ? "You" : "The " + itemHolder.getName();
                String verb = EntityExtensions.isPlayer(itemHolder) ? "pick up" : "picks up";
                Functions.logGameEvent(subject + " " + verb + " the " + item.getName() + ".", ItemPicker.this);
            }
            return item;
        });
        return Consumed.INSTANCE;
    }
}
