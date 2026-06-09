package com.example.cavesofzircon.extensions;

import org.hexworks.cobalt.databinding.api.binding.GenericBindingsKt;
import org.hexworks.cobalt.databinding.api.extension.Properties;
import org.hexworks.cobalt.databinding.api.property.Property;
import org.hexworks.cobalt.databinding.api.value.ObservableValue;

public final class PropertyExtensions {

    private PropertyExtensions() {}

    public static Property<String> toStringProperty(ObservableValue<Integer> intProp) {
        var strProp = Properties.createPropertyFrom("", v -> Boolean.TRUE);
        strProp.updateFrom(intProp, true, Object::toString);
        return strProp;
    }
}
