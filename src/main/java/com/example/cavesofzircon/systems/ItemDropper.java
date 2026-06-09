package com.example.cavesofzircon.systems;

import com.example.cavesofzircon.extensions.EntityExtensions;
import com.example.cavesofzircon.functions.Functions;
import com.example.cavesofzircon.messages.DropItem;
import com.example.cavesofzircon.world.GameContext;
import kotlin.coroutines.Continuation;
import kotlin.jvm.JvmClassMappingKt;
import org.hexworks.amethyst.api.Consumed;
import org.hexworks.amethyst.api.Response;
import org.hexworks.amethyst.api.base.BaseFacet;

public class ItemDropper extends BaseFacet<GameContext, DropItem> {

    public static final ItemDropper INSTANCE = new ItemDropper();

    private ItemDropper() {
        super(JvmClassMappingKt.getKotlinClass(DropItem.class));
    }

    @Override
    public Object receiveMessage(DropItem message, Continuation<? super Response> continuation) {
        var context = message.getContext();
        var itemHolder = message.getSource();
        var item = message.getItem();
        var position = message.getPosition();

        if (EntityExtensions.removeItem(itemHolder, item)) {
            context.getWorld().addEntity(item, position);
            String subject = EntityExtensions.isPlayer(itemHolder) ? "You" : "The " + itemHolder.getName();
            String verb = EntityExtensions.isPlayer(itemHolder) ? "drop" : "drops";
            Functions.logGameEvent(subject + " " + verb + " the " + item.getName() + ".", this);
        }
        return Consumed.INSTANCE;
    }
}
