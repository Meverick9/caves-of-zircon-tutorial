package com.example.cavesofzircon.view.dialog;

import org.hexworks.zircon.api.Components;
import org.hexworks.zircon.api.component.ComponentAlignment;
import org.hexworks.zircon.api.component.Container;
import org.hexworks.zircon.api.component.Fragment;
import org.hexworks.zircon.api.component.modal.Modal;
import kotlin.Unit;
import org.hexworks.zircon.api.uievent.Processed;
import org.hexworks.zircon.internal.component.modal.EmptyModalResult;

public class CloseButtonFragment implements Fragment {

    private final org.hexworks.zircon.api.component.Button root;

    public CloseButtonFragment(Modal<EmptyModalResult> modal, Container parent) {
        root = Components.button()
                .withText("Close")
                .withAlignmentWithin(parent, ComponentAlignment.BOTTOM_RIGHT)
                .build();
        root.onActivated(action -> {
            modal.close(EmptyModalResult.INSTANCE);
            return kotlin.Unit.INSTANCE;
        });
    }

    @Override
    public org.hexworks.zircon.api.component.Component getRoot() {
        return root;
    }
}
