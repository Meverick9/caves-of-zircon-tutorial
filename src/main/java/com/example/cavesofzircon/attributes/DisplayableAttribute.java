package com.example.cavesofzircon.attributes;

import org.hexworks.amethyst.api.Attribute;
import org.hexworks.zircon.api.component.Component;

public interface DisplayableAttribute extends Attribute {
    Component toComponent(int width);
}
