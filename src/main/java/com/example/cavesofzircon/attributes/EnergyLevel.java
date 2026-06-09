package com.example.cavesofzircon.attributes;

import com.example.cavesofzircon.extensions.PropertyExtensions;
import org.hexworks.amethyst.api.base.BaseAttribute;
import org.hexworks.cobalt.databinding.api.binding.StringBindingsKt;
import org.hexworks.cobalt.databinding.api.extension.Properties;
import org.hexworks.cobalt.databinding.api.property.Property;
import org.hexworks.zircon.api.Components;
import org.hexworks.zircon.api.component.Component;

public class EnergyLevel extends BaseAttribute implements DisplayableAttribute {

    private final int maxEnergy;
    private final Property<Integer> currentValueProperty;

    public EnergyLevel(int initialEnergy, int maxEnergy) {
        this.maxEnergy = maxEnergy;
        this.currentValueProperty = Properties.createPropertyFrom(initialEnergy, v -> Boolean.TRUE);
    }

    public int getCurrentEnergy() {
        return currentValueProperty.getValue();
    }

    public void setCurrentEnergy(int value) {
        currentValueProperty.setValue(Math.min(value, maxEnergy));
    }

    public int getMaxEnergy() {
        return maxEnergy;
    }

    @Override
    public Component toComponent(int width) {
        var vbox = Components.vbox()
                .withSize(width, 5)
                .build();

        var hungerLabel = Components.label()
                .withSize(width, 1)
                .build();

        var hungerBinding = StringBindingsKt.bindPlusWith(
                StringBindingsKt.bindPlusWith(
                        PropertyExtensions.toStringProperty(currentValueProperty),
                        Properties.createPropertyFrom("/", v -> Boolean.TRUE)
                ),
                Properties.createPropertyFrom(String.valueOf(maxEnergy), v -> Boolean.TRUE)
        );
        hungerLabel.getTextProperty().updateFrom(hungerBinding, false);

        vbox.addComponent(
                Components.textBox(width)
                        .addHeader("Hunger")
        );
        vbox.addComponent(hungerLabel);

        return vbox;
    }
}
