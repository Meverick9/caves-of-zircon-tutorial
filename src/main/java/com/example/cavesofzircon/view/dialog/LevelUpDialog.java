package com.example.cavesofzircon.view.dialog;

import com.example.cavesofzircon.attributes.CombatStats;
import com.example.cavesofzircon.attributes.Vision;
import com.example.cavesofzircon.attributes.types.EntityTypes;
import com.example.cavesofzircon.extensions.EntityExtensions;
import com.example.cavesofzircon.functions.Functions;
import com.example.cavesofzircon.world.GameContext;
import org.hexworks.amethyst.api.entity.Entity;
import org.hexworks.zircon.api.ComponentDecorations;
import org.hexworks.zircon.api.Components;
import org.hexworks.zircon.api.component.Container;
import org.hexworks.zircon.api.graphics.BoxType;
import org.hexworks.zircon.api.screen.Screen;
import kotlin.Unit;
import org.hexworks.zircon.api.uievent.Processed;
import org.hexworks.zircon.internal.component.modal.EmptyModalResult;

public class LevelUpDialog extends Dialog {

    private final Container container;

    public LevelUpDialog(Screen screen, Entity<EntityTypes.PlayerType, GameContext> player) {
        super(screen, false);
        var vbox = Components.vbox()
                .withDecorations(ComponentDecorations.box(BoxType.TOP_BOTTOM_DOUBLE, "Ding!"))
                .withSize(30, 15)
                .build();

        var stats = EntityExtensions.tryToFindAttribute(player, CombatStats.class);
        var vision = EntityExtensions.tryToFindAttribute(player, Vision.class);

        vbox.addComponent(Components.textBox(27)
                .addHeader("Congratulations, you leveled up!")
                .addParagraph("Pick an improvement from the options below:")
        );

        var maxHpBtn = Components.button().withText("Max HP").build();
        maxHpBtn.onActivated(action -> {
            stats.getMaxHpProperty().setValue(stats.getMaxHp() + 10);
            Functions.logGameEvent("You look healthier.", maxHpBtn);
            getRoot().close(EmptyModalResult.INSTANCE);
            return kotlin.Unit.INSTANCE;
        });
        vbox.addComponent(maxHpBtn);

        var attackBtn = Components.button().withText("Attack").build();
        attackBtn.onActivated(action -> {
            stats.getAttackValueProperty().setValue(stats.getAttackValue() + 2);
            Functions.logGameEvent("You look stronger.", attackBtn);
            getRoot().close(EmptyModalResult.INSTANCE);
            return kotlin.Unit.INSTANCE;
        });
        vbox.addComponent(attackBtn);

        var defenseBtn = Components.button().withText("Defense").build();
        defenseBtn.onActivated(action -> {
            stats.getDefenseValueProperty().setValue(stats.getDefenseValue() + 2);
            Functions.logGameEvent("You look tougher.", defenseBtn);
            getRoot().close(EmptyModalResult.INSTANCE);
            return kotlin.Unit.INSTANCE;
        });
        vbox.addComponent(defenseBtn);

        var visionBtn = Components.button().withText("Vision").build();
        visionBtn.onActivated(action -> {
            vision.setRadius(vision.getRadius() + 1);
            Functions.logGameEvent("You look more perceptive.", visionBtn);
            getRoot().close(EmptyModalResult.INSTANCE);
            return kotlin.Unit.INSTANCE;
        });
        vbox.addComponent(visionBtn);

        this.container = vbox;
    }

    @Override
    public Container getContainer() {
        return container;
    }
}
