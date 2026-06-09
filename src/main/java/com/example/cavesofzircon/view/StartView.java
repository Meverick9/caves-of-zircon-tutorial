package com.example.cavesofzircon.view;

import org.hexworks.zircon.api.ColorThemes;
import org.hexworks.zircon.api.ComponentDecorations;
import org.hexworks.zircon.api.Components;
import org.hexworks.zircon.api.component.ComponentAlignment;
import org.hexworks.zircon.api.grid.TileGrid;
import kotlin.Unit;
import org.hexworks.zircon.api.uievent.Processed;
import org.hexworks.zircon.api.view.base.BaseView;

public class StartView extends BaseView {

    public StartView(TileGrid grid) {
        super(grid, ColorThemes.arc());

        var msg = "Welcome to Caves of Zircon.";

        var header = Components.textBox(msg.length())
                .addHeader(msg)
                .addNewLine()
                .withAlignmentWithin(getScreen(), ComponentAlignment.CENTER)
                .build();

        var startButton = Components.button()
                .withAlignmentAround(header, ComponentAlignment.BOTTOM_CENTER)
                .withText("Start!")
                .withDecorations(ComponentDecorations.box(), ComponentDecorations.shadow())
                .build();

        startButton.onActivated(action -> {
            replaceWith(new PlayView(grid));
            return kotlin.Unit.INSTANCE;
        });

        getScreen().addComponents(header, startButton);
    }
}
