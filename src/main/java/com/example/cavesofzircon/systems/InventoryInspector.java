package com.example.cavesofzircon.systems;

import com.example.cavesofzircon.GameConfig;
import com.example.cavesofzircon.attributes.Inventory;
import com.example.cavesofzircon.attributes.types.*;
import com.example.cavesofzircon.extensions.EntityExtensions;
import com.example.cavesofzircon.messages.DropItem;
import com.example.cavesofzircon.messages.Eat;
import com.example.cavesofzircon.messages.InspectInventory;
import com.example.cavesofzircon.view.dialog.ExamineDialog;
import com.example.cavesofzircon.view.fragment.InventoryFragment;
import com.example.cavesofzircon.world.GameContext;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlin.jvm.JvmClassMappingKt;
import org.hexworks.amethyst.api.Consumed;
import org.hexworks.amethyst.api.Response;
import org.hexworks.amethyst.api.base.BaseFacet;
import org.hexworks.amethyst.api.entity.Entity;
import org.hexworks.cobalt.datatypes.Maybe;
import org.hexworks.zircon.api.ComponentDecorations;
import org.hexworks.zircon.api.Components;
import org.hexworks.zircon.api.builder.component.ModalBuilder;
import org.hexworks.zircon.api.component.ComponentAlignment;
import org.hexworks.zircon.api.data.Size;
import org.hexworks.zircon.api.uievent.Processed;
import org.hexworks.zircon.internal.component.modal.EmptyModalResult;

public class InventoryInspector extends BaseFacet<GameContext, InspectInventory> {

    public static final InventoryInspector INSTANCE = new InventoryInspector();

    private static final Size DIALOG_SIZE = Size.create(40, 15);

    private static final Continuation<Object> SYNC_CONT = new Continuation<>() {
        @Override public kotlin.coroutines.CoroutineContext getContext() { return EmptyCoroutineContext.INSTANCE; }
        @Override public void resumeWith(Object result) {}
    };

    private InventoryInspector() {
        super(JvmClassMappingKt.getKotlinClass(InspectInventory.class));
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Object receiveMessage(InspectInventory message, Continuation<? super Response> continuation) {
        var context = message.getContext();
        var itemHolder = message.getSource();
        var position = message.getPosition();
        var screen = context.getScreen();

        var panel = Components.panel()
                .withSize(DIALOG_SIZE)
                .withDecorations(ComponentDecorations.box(org.hexworks.zircon.api.ComponentDecorations.box("Inventory")),
                                 ComponentDecorations.shadow())
                .build();

        var inventory = EntityExtensions.getInventory(itemHolder);

        var fragment = new InventoryFragment(
                inventory,
                DIALOG_SIZE.getWidth() - 3,
                item -> {
                    itemHolder.receiveMessage(new DropItem(context, itemHolder, item, position), SYNC_CONT);
                },
                item -> {
                    EntityExtensions.whenTypeIs(itemHolder, EnergyUser.class, eater -> {
                        EntityExtensions.whenTypeIs(item, Food.class, food -> {
                            inventory.removeItem(food);
                            eater.receiveMessage(new Eat(context, (Entity<EnergyUser, GameContext>) eater, (Entity<Food, GameContext>) food), SYNC_CONT);
                        });
                    });
                },
                item -> {
                    final Maybe[] result = {Maybe.empty()};
                    EntityExtensions.whenTypeIs(itemHolder, EquipmentHolder.class, equipmentHolder -> {
                        EntityExtensions.whenTypeIs(item, CombatItem.class, combatItem -> {
                            result[0] = Maybe.of(EntityExtensions.equip(equipmentHolder, inventory, combatItem));
                        });
                    });
                    return result[0];
                },
                item -> {
                    screen.openModal(new ExamineDialog(screen, item));
                }
        );

        panel.addFragment(fragment);

        var modal = ModalBuilder.newBuilder()
                .withParentSize(screen.getSize())
                .withComponent(panel)
                .withCenteredDialog(true)
                .build();

        panel.addComponent(Components.button()
                .withText("Close")
                .withAlignmentWithin(panel, ComponentAlignment.BOTTOM_LEFT)
                .build());
        // Close button activation
        panel.getChildren().forEach(child -> {
            if (child instanceof org.hexworks.zircon.api.component.Button btn) {
                if (btn.getText().equals("Close")) {
                    btn.onActivated(uiAction -> {
                        modal.close(EmptyModalResult.INSTANCE);
                        return Processed.INSTANCE;
                    });
                }
            }
        });

        modal.setTheme(GameConfig.THEME);
        screen.openModal(modal);
        return Consumed.INSTANCE;
    }
}
