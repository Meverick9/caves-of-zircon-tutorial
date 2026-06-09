package com.example.cavesofzircon.systems;

import com.example.cavesofzircon.attributes.types.ItemHolder;
import com.example.cavesofzircon.extensions.EntityExtensions;
import com.example.cavesofzircon.messages.Destroy;
import com.example.cavesofzircon.messages.DropItem;
import com.example.cavesofzircon.world.GameContext;
import kotlin.coroutines.Continuation;
import kotlin.jvm.JvmClassMappingKt;
import org.hexworks.amethyst.api.Pass;
import org.hexworks.amethyst.api.Response;
import org.hexworks.amethyst.api.base.BaseFacet;

public class LootDropper extends BaseFacet<GameContext, Destroy> {

    public static final LootDropper INSTANCE = new LootDropper();

    private LootDropper() {
        super(JvmClassMappingKt.getKotlinClass(Destroy.class));
    }

    @Override
    public Object receiveMessage(Destroy message, Continuation<? super Response> continuation) {
        var context = message.getContext();
        var target = message.getTarget();

        EntityExtensions.whenTypeIs(target, ItemHolder.class, entity -> {
            var inventory = EntityExtensions.getInventory(entity);
            var items = inventory.getItems();
            var pos = EntityExtensions.entityPosition(entity);
            for (var item : items) {
                entity.receiveMessage(new DropItem(context, entity, item, pos), continuation);
            }
        });
        return Pass.INSTANCE;
    }
}
