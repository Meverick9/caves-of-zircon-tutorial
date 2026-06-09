package com.example.cavesofzircon.view.dialog;

import com.example.cavesofzircon.attributes.types.Item;
import com.example.cavesofzircon.extensions.EntityExtensions;
import com.example.cavesofzircon.world.GameContext;
import org.hexworks.amethyst.api.entity.Entity;
import org.hexworks.zircon.api.ComponentDecorations;
import org.hexworks.zircon.api.Components;
import org.hexworks.zircon.api.GraphicalTilesetResources;
import org.hexworks.zircon.api.component.Container;
import org.hexworks.zircon.api.graphics.BoxType;
import org.hexworks.zircon.api.screen.Screen;

public class ExamineDialog extends Dialog {

    private final Container container;

    public ExamineDialog(Screen screen, Entity<? extends Item, GameContext> item) {
        super(screen);
        var panel = Components.panel()
                .withDecorations(ComponentDecorations.box("Examining " + item.getName(), BoxType.TOP_BOTTOM_DOUBLE))
                .withSize(25, 15)
                .build();

        panel.addComponent(Components.textBox(23)
                .addHeader("Name", false)
                .addInlineComponent(Components.icon()
                        .withIcon(EntityExtensions.getItemIconTile(item))
                        .withTileset(GraphicalTilesetResources.nethack16x16())
                        .build())
                .addInlineComponent(Components.label()
                        .withText(" " + item.getName())
                        .build())
                .commitInlineElements()
                .addNewLine()
                .addHeader("Description", false)
                .addParagraph(item.getDescription(), false)
        );

        this.container = panel;
    }

    @Override
    public Container getContainer() {
        return container;
    }
}
