package com.example.cavesofzircon.attributes;

import org.hexworks.amethyst.api.base.BaseAttribute;
import org.hexworks.zircon.api.Components;
import org.hexworks.zircon.api.component.Component;

public class ItemCombatStats extends BaseAttribute implements DisplayableAttribute {

    private final int attackValue;
    private final int defenseValue;
    private final String combatItemType;

    public ItemCombatStats(int attackValue, int defenseValue, String combatItemType) {
        this.attackValue = attackValue;
        this.defenseValue = defenseValue;
        this.combatItemType = combatItemType;
    }

    public ItemCombatStats(String combatItemType) {
        this(0, 0, combatItemType);
    }

    public ItemCombatStats(int attackValue, String combatItemType) {
        this(attackValue, 0, combatItemType);
    }

    public int getAttackValue() {
        return attackValue;
    }

    public int getDefenseValue() {
        return defenseValue;
    }

    public String getCombatItemType() {
        return combatItemType;
    }

    @Override
    public Component toComponent(int width) {
        return Components.textBox(width)
                .addParagraph("Type: " + combatItemType, false)
                .addParagraph("Attack: " + attackValue, false)
                .addParagraph("Defense: " + defenseValue, false)
                .build();
    }
}
