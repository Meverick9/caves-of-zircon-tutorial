package com.example.cavesofzircon.view.fragment;

import com.example.cavesofzircon.attributes.types.CombatItem;
import com.example.cavesofzircon.attributes.types.Food;
import com.example.cavesofzircon.attributes.types.Item;
import com.example.cavesofzircon.extensions.EntityExtensions;
import com.example.cavesofzircon.world.GameContext;
import org.hexworks.amethyst.api.entity.Entity;
import org.hexworks.zircon.api.Components;
import org.hexworks.zircon.api.component.Button;
import org.hexworks.zircon.api.component.Component;
import org.hexworks.zircon.api.component.Fragment;

public class InventoryRowFragment implements Fragment {

    private final Button dropButton;
    private final Button eatButton;
    private final Button equipButton;
    private final Button examineButton;
    private final Component root;

    public InventoryRowFragment(int width, Entity<? extends Item, GameContext> item) {
        dropButton = Components.button().withDecorations().withText("Drop").build();
        eatButton = Components.button().withDecorations().withText("Eat").build();
        equipButton = Components.button().withDecorations().withText("Equip").build();
        examineButton = Components.button().withDecorations().withText("Examine").build();

        var hbox = Components.hbox()
                .withSpacing(1)
                .withSize(width, 1)
                .build();

        hbox.addComponent(Components.icon().withIcon(EntityExtensions.getItemIconTile(item)));
        hbox.addComponent(Components.label()
                .withSize(InventoryFragment.NAME_COLUMN_WIDTH, 1)
                .withText(item.getName())
        );
        hbox.addComponent(dropButton);
        hbox.addComponent(examineButton);

        EntityExtensions.whenTypeIs(item, Food.class, food -> {
            hbox.addComponent(eatButton);
        });
        EntityExtensions.whenTypeIs(item, CombatItem.class, combatItem -> {
            hbox.addComponent(equipButton);
        });

        this.root = hbox;
    }

    public Button getDropButton() { return dropButton; }
    public Button getEatButton() { return eatButton; }
    public Button getEquipButton() { return equipButton; }
    public Button getExamineButton() { return examineButton; }

    @Override
    public Component getRoot() {
        return root;
    }
}
