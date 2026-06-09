package com.example.cavesofzircon.view.dialog;

import org.hexworks.zircon.api.ComponentDecorations;
import org.hexworks.zircon.api.Components;
import org.hexworks.zircon.api.component.Container;
import org.hexworks.zircon.api.graphics.BoxType;
import org.hexworks.zircon.api.screen.Screen;

public class HelpDialog extends Dialog {

    private final Container container;

    public HelpDialog(Screen screen) {
        super(screen);
        var panel = Components.panel()
                .withDecorations(ComponentDecorations.box("Help", BoxType.TOP_BOTTOM_DOUBLE))
                .withSize(50, 30)
                .build();

        var vbox = Components.vbox()
                .withSize(panel.getContentSize().getWidth(), panel.getContentSize().getHeight() - 1)
                .withSpacing(2)
                .build();

        vbox.addComponent(Components.textBox(vbox.getContentSize().getWidth())
                .addNewLine()
                .addHeader("Caves of Zircon")
                .addParagraph("Descend to the Caves Of Zircon and collect as many Zircons as you can.\n" +
                              "Find the exit (+) to win the game. Use what you find to avoid dying.")
        );

        vbox.addComponent(Components.textBox(40)
                .addHeader("Navigation:")
                .addListItem("[Tab]: Focus next")
                .addListItem("[Shift] + [Tab]: Focus previous")
                .addListItem("[Space]: Activate focused item")
        );

        var hbox = Components.hbox()
                .withSize(vbox.getWidth(), 10)
                .build();

        hbox.addComponent(Components.textBox(vbox.getWidth() / 2)
                .addHeader("Movement:")
                .addListItem("wasd: Movement")
                .addListItem("r: Move up")
                .addListItem("f: Move down")
        );

        hbox.addComponent(Components.textBox(vbox.getWidth() / 2)
                .addHeader("Actions:")
                .addListItem("(i)nventory")
                .addListItem("(p)ick up")
                .addListItem("(h)elp")
        );

        vbox.addComponent(hbox);
        panel.addComponent(vbox);

        this.container = panel;
    }

    @Override
    public Container getContainer() {
        return container;
    }
}
