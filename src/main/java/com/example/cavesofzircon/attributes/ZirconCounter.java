package com.example.cavesofzircon.attributes;

import com.example.cavesofzircon.extensions.PropertyExtensions;
import org.hexworks.amethyst.api.base.BaseAttribute;
import org.hexworks.cobalt.databinding.api.binding.StringBindingsKt;
import org.hexworks.cobalt.databinding.api.extension.Properties;
import org.hexworks.cobalt.databinding.api.property.Property;
import org.hexworks.zircon.api.Components;
import org.hexworks.zircon.api.component.Component;

public class ZirconCounter extends BaseAttribute implements DisplayableAttribute {

    private final Property<Integer> zirconCountProperty;

    public ZirconCounter() {
        this.zirconCountProperty = Properties.createPropertyFrom(0, v -> Boolean.TRUE);
    }

    public int getZirconCount() {
        return zirconCountProperty.getValue();
    }

    public void setZirconCount(int count) {
        zirconCountProperty.setValue(count);
    }

    @Override
    public Component toComponent(int width) {
        var zirconBinding = StringBindingsKt.bindPlusWith(
                Properties.createPropertyFrom("Zircons: ", v -> Boolean.TRUE),
                PropertyExtensions.toStringProperty(zirconCountProperty)
        );
        var header = Components.header()
                .withText(zirconBinding.getValue())
                .withSize(width, 1)
                .build();
        header.getTextProperty().updateFrom(zirconBinding, false);
        return header;
    }
}
