package com.example.cavesofzircon.view.dialog;

import com.example.cavesofzircon.GameConfig;
import org.hexworks.zircon.api.builder.component.ModalBuilder;
import org.hexworks.zircon.api.component.Container;
import org.hexworks.zircon.api.component.modal.Modal;
import org.hexworks.zircon.api.component.modal.ModalFragment;
import org.hexworks.zircon.api.screen.Screen;
import org.hexworks.zircon.internal.component.modal.EmptyModalResult;

public abstract class Dialog implements ModalFragment<EmptyModalResult> {

    private final Screen screen;
    private final boolean withClose;
    private Modal<EmptyModalResult> rootModal;

    protected Dialog(Screen screen, boolean withClose) {
        this.screen = screen;
        this.withClose = withClose;
    }

    protected Dialog(Screen screen) {
        this(screen, true);
    }

    public abstract Container getContainer();

    @Override
    public Modal<EmptyModalResult> getRoot() {
        if (rootModal == null) {
            rootModal = ModalBuilder.<EmptyModalResult>newBuilder()
                    .withComponent(getContainer())
                    .withParentSize(screen.getSize())
                    .withCenteredDialog(true)
                    .build();
            if (withClose) {
                getContainer().addFragment(new CloseButtonFragment(rootModal, getContainer()));
            }
            getContainer().setTheme(GameConfig.THEME);
        }
        return rootModal;
    }
}
