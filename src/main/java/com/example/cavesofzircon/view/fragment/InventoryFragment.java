package com.example.cavesofzircon.view.fragment;

import com.example.cavesofzircon.GameConfig;
import com.example.cavesofzircon.attributes.Inventory;
import com.example.cavesofzircon.attributes.types.Item;
import com.example.cavesofzircon.world.GameContext;
import org.hexworks.amethyst.api.entity.Entity;
import org.hexworks.cobalt.datatypes.Maybe;
import org.hexworks.zircon.api.Components;
import org.hexworks.zircon.api.component.Component;
import org.hexworks.zircon.api.component.Fragment;
import org.hexworks.zircon.api.component.VBox;
import kotlin.Unit;
import org.hexworks.zircon.api.uievent.Processed;

import java.util.function.Consumer;
import java.util.function.Function;

public class InventoryFragment implements Fragment {

    public static final int NAME_COLUMN_WIDTH = 15;
    public static final int ACTIONS_COLUMN_WIDTH = 10;

    private final Component root;

    public InventoryFragment(Inventory inventory,
                              int width,
                              Consumer<Entity<? extends Item, GameContext>> onDrop,
                              Consumer<Entity<? extends Item, GameContext>> onEat,
                              Function<Entity<? extends Item, GameContext>, Maybe<Entity<? extends Item, GameContext>>> onEquip,
                              Consumer<Entity<? extends Item, GameContext>> onExamine) {
        var vbox = Components.vbox()
                .withSize(width, inventory.getSize() + 1)
                .build();

        var header = Components.hbox()
                .withSpacing(1)
                .withSize(width, 1)
                .build();
        header.addComponent(Components.label().withText("").withSize(1, 1));
        header.addComponent(Components.header().withText("Name").withSize(NAME_COLUMN_WIDTH, 1));
        header.addComponent(Components.header().withText("Actions").withSize(ACTIONS_COLUMN_WIDTH, 1));
        vbox.addComponent(header);

        for (var item : inventory.getItems()) {
            addRow(width, item, vbox, onDrop, onEat, onEquip, onExamine);
        }

        this.root = vbox;
    }

    private void addRow(int width,
                         Entity<? extends Item, GameContext> item,
                         VBox list,
                         Consumer<Entity<? extends Item, GameContext>> onDrop,
                         Consumer<Entity<? extends Item, GameContext>> onEat,
                         Function<Entity<? extends Item, GameContext>, Maybe<Entity<? extends Item, GameContext>>> onEquip,
                         Consumer<Entity<? extends Item, GameContext>> onExamine) {
        var row = new InventoryRowFragment(width, item);
        var fragmentComponent = list.addFragment(row);

        row.getDropButton().onActivated(action -> {
            fragmentComponent.detach();
            onDrop.accept(item);
            return kotlin.Unit.INSTANCE;
        });

        row.getEatButton().onActivated(action -> {
            fragmentComponent.detach();
            onEat.accept(item);
            return kotlin.Unit.INSTANCE;
        });

        row.getEquipButton().onActivated(action -> {
            onEquip.apply(item).map(oldItem -> {
                fragmentComponent.detach();
                addRow(width, oldItem, list, onDrop, onEat, onEquip, onExamine);
                return oldItem;
            });
            return kotlin.Unit.INSTANCE;
        });

        row.getExamineButton().onActivated(action -> {
            onExamine.accept(item);
            return kotlin.Unit.INSTANCE;
        });

        list.setTheme(GameConfig.THEME);
    }

    @Override
    public Component getRoot() {
        return root;
    }
}
