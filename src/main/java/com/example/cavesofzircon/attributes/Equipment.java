package com.example.cavesofzircon.attributes;

import com.example.cavesofzircon.attributes.types.Armor;
import com.example.cavesofzircon.attributes.types.CombatItem;
import com.example.cavesofzircon.attributes.types.Weapon;
import com.example.cavesofzircon.world.GameContext;
import org.hexworks.amethyst.api.base.BaseAttribute;
import org.hexworks.amethyst.api.entity.Entity;
import org.hexworks.cobalt.databinding.api.extension.Properties;
import org.hexworks.cobalt.databinding.api.property.Property;
import org.hexworks.zircon.api.Components;
import org.hexworks.zircon.api.component.Component;

public class Equipment extends BaseAttribute implements DisplayableAttribute {

    private final Property<Entity<? extends Weapon, GameContext>> weaponProperty;
    private final Property<Entity<? extends Armor, GameContext>> armorProperty;

    @SuppressWarnings("unchecked")
    public Equipment(Entity<? extends Weapon, GameContext> initialWeapon,
                     Entity<? extends Armor, GameContext> initialArmor) {
        this.weaponProperty = (Property<Entity<? extends Weapon, GameContext>>)
                (Object) Properties.createPropertyFrom(initialWeapon, v -> Boolean.TRUE);
        this.armorProperty = (Property<Entity<? extends Armor, GameContext>>)
                (Object) Properties.createPropertyFrom(initialArmor, v -> Boolean.TRUE);
    }

    public int getAttackValue() {
        return getWeaponAttackValue() + getArmorAttackValue();
    }

    public int getDefenseValue() {
        return getWeaponDefenseValue() + getArmorDefenseValue();
    }

    public String getArmorName() {
        return armorProperty.getValue().getName();
    }

    public String getWeaponName() {
        return weaponProperty.getValue().getName();
    }

    private Entity<? extends Weapon, GameContext> getWeapon() {
        return weaponProperty.getValue();
    }

    private Entity<? extends Armor, GameContext> getArmor() {
        return armorProperty.getValue();
    }

    private int getWeaponAttackValue() {
        return getItemCombatStatsAttack(getWeapon());
    }

    private int getWeaponDefenseValue() {
        return getItemCombatStatsDefense(getWeapon());
    }

    private int getArmorAttackValue() {
        return getItemCombatStatsAttack(getArmor());
    }

    private int getArmorDefenseValue() {
        return getItemCombatStatsDefense(getArmor());
    }

    private static int getItemCombatStatsAttack(Entity<?, GameContext> entity) {
        return entity.findAttribute(kotlin.jvm.JvmClassMappingKt.getKotlinClass(ItemCombatStats.class))
                .map(ItemCombatStats::getAttackValue)
                .orElse(0);
    }

    private static int getItemCombatStatsDefense(Entity<?, GameContext> entity) {
        return entity.findAttribute(kotlin.jvm.JvmClassMappingKt.getKotlinClass(ItemCombatStats.class))
                .map(ItemCombatStats::getDefenseValue)
                .orElse(0);
    }

    @SuppressWarnings("unchecked")
    public Entity<? extends CombatItem, GameContext> equip(Inventory inventory,
                                                            Entity<? extends CombatItem, GameContext> combatItem) {
        if (combatItem.getType() instanceof Weapon) {
            return equipWeapon(inventory, (Entity<? extends Weapon, GameContext>) combatItem);
        }
        if (combatItem.getType() instanceof Armor) {
            return equipArmor(inventory, (Entity<? extends Armor, GameContext>) combatItem);
        }
        throw new IllegalStateException("Combat item is not Weapon or Armor.");
    }

    private Entity<? extends CombatItem, GameContext> equipWeapon(
            Inventory inventory, Entity<? extends Weapon, GameContext> newWeapon) {
        var oldWeapon = getWeapon();
        inventory.removeItem(newWeapon);
        inventory.addItem(oldWeapon);
        weaponProperty.setValue(newWeapon);
        return oldWeapon;
    }

    private Entity<? extends CombatItem, GameContext> equipArmor(
            Inventory inventory, Entity<? extends Armor, GameContext> newArmor) {
        var oldArmor = getArmor();
        inventory.removeItem(newArmor);
        inventory.addItem(oldArmor);
        armorProperty.setValue(newArmor);
        return oldArmor;
    }

    @Override
    public Component toComponent(int width) {
        var weaponIcon = Components.icon()
                .withIcon(getItemIconTile(getWeapon()))
                .build();
        var weaponNameLabel = Components.label()
                .withText(getWeaponName())
                .withSize(width - 2, 1)
                .build();
        var weaponStatsLabel = Components.label()
                .withText(" A: " + getWeaponAttackValue() + " D: " + getWeaponDefenseValue())
                .withSize(width - 1, 1)
                .build();

        var armorIcon = Components.icon()
                .withIcon(getItemIconTile(getArmor()))
                .build();
        var armorNameLabel = Components.label()
                .withText(getArmorName())
                .withSize(width - 2, 1)
                .build();
        var armorStatsLabel = Components.label()
                .withText(" A: " + getArmorAttackValue() + " D: " + getArmorDefenseValue())
                .withSize(width - 1, 1)
                .build();

        weaponProperty.onChange(change -> {
            weaponIcon.getIconProperty().setValue(getItemIconTile(getWeapon()));
            weaponNameLabel.getTextProperty().setValue(getWeapon().getName());
            weaponStatsLabel.getTextProperty().setValue(
                    " A: " + getWeaponAttackValue() + " D: " + getWeaponDefenseValue());
            return kotlin.Unit.INSTANCE;
        });

        armorProperty.onChange(change -> {
            armorIcon.getIconProperty().setValue(getItemIconTile(getArmor()));
            armorNameLabel.getTextProperty().setValue(getArmor().getName());
            armorStatsLabel.getTextProperty().setValue(
                    " A: " + getArmorAttackValue() + " D: " + getArmorDefenseValue());
            return kotlin.Unit.INSTANCE;
        });

        return Components.textBox(width)
                .addHeader("Weapon", false)
                .addInlineComponent(weaponIcon)
                .addInlineComponent(weaponNameLabel)
                .commitInlineElements()
                .addInlineComponent(weaponStatsLabel)
                .commitInlineElements()
                .addNewLine()
                .addHeader("Armor", false)
                .addInlineComponent(armorIcon)
                .addInlineComponent(armorNameLabel)
                .commitInlineElements()
                .addInlineComponent(armorStatsLabel)
                .commitInlineElements()
                .build();
    }

    private static org.hexworks.zircon.api.data.GraphicalTile getItemIconTile(Entity<?, GameContext> entity) {
        return entity.findAttribute(kotlin.jvm.JvmClassMappingKt.getKotlinClass(ItemIcon.class))
                .map(ItemIcon::getIconTile)
                .orElseThrow(() -> new NoSuchElementException("No icon tile"));
    }
}
