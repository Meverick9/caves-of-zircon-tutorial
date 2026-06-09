package com.example.cavesofzircon.attributes;

import com.example.cavesofzircon.extensions.PropertyExtensions;
import org.hexworks.amethyst.api.base.BaseAttribute;
import org.hexworks.cobalt.databinding.api.binding.StringBindingsKt;
import org.hexworks.cobalt.databinding.api.extension.Properties;
import org.hexworks.cobalt.databinding.api.property.Property;
import org.hexworks.zircon.api.Components;
import org.hexworks.zircon.api.component.Component;

public class CombatStats extends BaseAttribute implements DisplayableAttribute {

    private final Property<Integer> maxHpProperty;
    private final Property<Integer> hpProperty;
    private final Property<Integer> attackValueProperty;
    private final Property<Integer> defenseValueProperty;

    private CombatStats(Property<Integer> maxHpProperty,
                        Property<Integer> hpProperty,
                        Property<Integer> attackValueProperty,
                        Property<Integer> defenseValueProperty) {
        this.maxHpProperty = maxHpProperty;
        this.hpProperty = hpProperty;
        this.attackValueProperty = attackValueProperty;
        this.defenseValueProperty = defenseValueProperty;
    }

    public static CombatStats create(int maxHp, int attackValue, int defenseValue) {
        return create(maxHp, maxHp, attackValue, defenseValue);
    }

    public static CombatStats create(int maxHp, int hp, int attackValue, int defenseValue) {
        return new CombatStats(
                Properties.createPropertyFrom(maxHp, v -> Boolean.TRUE),
                Properties.createPropertyFrom(hp, v -> Boolean.TRUE),
                Properties.createPropertyFrom(attackValue, v -> Boolean.TRUE),
                Properties.createPropertyFrom(defenseValue, v -> Boolean.TRUE)
        );
    }

    public Property<Integer> getMaxHpProperty() {
        return maxHpProperty;
    }

    public Property<Integer> getHpProperty() {
        return hpProperty;
    }

    public Property<Integer> getAttackValueProperty() {
        return attackValueProperty;
    }

    public Property<Integer> getDefenseValueProperty() {
        return defenseValueProperty;
    }

    public int getMaxHp() {
        return maxHpProperty.getValue();
    }

    public int getHp() {
        return hpProperty.getValue();
    }

    public void setHp(int hp) {
        hpProperty.setValue(hp);
    }

    public int getAttackValue() {
        return attackValueProperty.getValue();
    }

    public int getDefenseValue() {
        return defenseValueProperty.getValue();
    }

    @Override
    public Component toComponent(int width) {
        var vbox = Components.vbox()
                .withSize(width, 5)
                .build();

        var hpLabel = Components.label()
                .withSize(width, 1)
                .build();
        var attackLabel = Components.label()
                .withSize(width, 1)
                .build();
        var defenseLabel = Components.label()
                .withSize(width, 1)
                .build();

        var hpBinding = StringBindingsKt.bindPlusWith(
                StringBindingsKt.bindPlusWith(
                        StringBindingsKt.bindPlusWith(
                                Properties.createPropertyFrom("HP: ", v -> Boolean.TRUE),
                                PropertyExtensions.toStringProperty(hpProperty)
                        ),
                        Properties.createPropertyFrom("/", v -> Boolean.TRUE)
                ),
                PropertyExtensions.toStringProperty(maxHpProperty)
        );
        hpLabel.getTextProperty().updateFrom(hpBinding, false);

        var attackBinding = StringBindingsKt.bindPlusWith(
                Properties.createPropertyFrom("Att: ", v -> Boolean.TRUE),
                PropertyExtensions.toStringProperty(attackValueProperty)
        );
        attackLabel.getTextProperty().updateFrom(attackBinding, false);

        var defenseBinding = StringBindingsKt.bindPlusWith(
                Properties.createPropertyFrom("Def: ", v -> Boolean.TRUE),
                PropertyExtensions.toStringProperty(defenseValueProperty)
        );
        defenseLabel.getTextProperty().updateFrom(defenseBinding, false);

        vbox.addComponent(
                Components.textBox(width)
                        .addHeader("Combat Stats")
        );
        vbox.addComponent(hpLabel);
        vbox.addComponent(attackLabel);
        vbox.addComponent(defenseLabel);

        return vbox;
    }
}
