package com.example.cavesofzircon.attributes;

import com.example.cavesofzircon.extensions.PropertyExtensions;
import org.hexworks.amethyst.api.base.BaseAttribute;
import org.hexworks.cobalt.databinding.api.binding.StringBindingsKt;
import org.hexworks.cobalt.databinding.api.extension.Properties;
import org.hexworks.cobalt.databinding.api.property.Property;
import org.hexworks.zircon.api.Components;
import org.hexworks.zircon.api.component.Component;

public class Experience extends BaseAttribute implements DisplayableAttribute {

    private final Property<Integer> currentXPProperty;
    private final Property<Integer> currentLevelProperty;

    public Experience() {
        this.currentXPProperty = Properties.createPropertyFrom(0, v -> Boolean.TRUE);
        this.currentLevelProperty = Properties.createPropertyFrom(1, v -> Boolean.TRUE);
    }

    public int getCurrentXP() {
        return currentXPProperty.getValue();
    }

    public void setCurrentXP(int xp) {
        currentXPProperty.setValue(xp);
    }

    public int getCurrentLevel() {
        return currentLevelProperty.getValue();
    }

    public void setCurrentLevel(int level) {
        currentLevelProperty.setValue(level);
    }

    public Property<Integer> getCurrentXPProperty() {
        return currentXPProperty;
    }

    public Property<Integer> getCurrentLevelProperty() {
        return currentLevelProperty;
    }

    @Override
    public Component toComponent(int width) {
        var vbox = Components.vbox()
                .withSize(width, 3)
                .build();

        var xpLabel = Components.label()
                .withSize(width, 1)
                .build();
        var levelLabel = Components.label()
                .withSize(width, 1)
                .build();

        var xpBinding = StringBindingsKt.bindPlusWith(
                Properties.createPropertyFrom("XP:  ", v -> Boolean.TRUE),
                PropertyExtensions.toStringProperty(currentXPProperty)
        );
        xpLabel.getTextProperty().updateFrom(xpBinding, true);

        var levelBinding = StringBindingsKt.bindPlusWith(
                Properties.createPropertyFrom("Lvl: ", v -> Boolean.TRUE),
                PropertyExtensions.toStringProperty(currentLevelProperty)
        );
        levelLabel.getTextProperty().updateFrom(levelBinding, true);

        vbox.addComponent(
                Components.textBox(width)
                        .addHeader("Experience", false)
        );
        vbox.addComponent(xpLabel);
        vbox.addComponent(levelLabel);

        return vbox;
    }
}
