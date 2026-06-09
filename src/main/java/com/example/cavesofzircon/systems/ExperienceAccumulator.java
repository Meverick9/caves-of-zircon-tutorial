package com.example.cavesofzircon.systems;

import com.example.cavesofzircon.attributes.CombatStats;
import com.example.cavesofzircon.attributes.types.ExperienceGainer;
import com.example.cavesofzircon.events.PlayerGainedLevel;
import com.example.cavesofzircon.extensions.EntityExtensions;
import com.example.cavesofzircon.functions.Functions;
import com.example.cavesofzircon.messages.EntityDestroyed;
import com.example.cavesofzircon.world.GameContext;
import kotlin.coroutines.Continuation;
import kotlin.jvm.JvmClassMappingKt;
import org.hexworks.amethyst.api.Consumed;
import org.hexworks.amethyst.api.Response;
import org.hexworks.amethyst.api.base.BaseFacet;
import org.hexworks.cobalt.events.internal.ApplicationScope;
import org.hexworks.zircon.internal.Zircon;

public class ExperienceAccumulator extends BaseFacet<GameContext, EntityDestroyed> {

    public static final ExperienceAccumulator INSTANCE = new ExperienceAccumulator();

    private ExperienceAccumulator() {
        super(JvmClassMappingKt.getKotlinClass(EntityDestroyed.class));
    }

    @Override
    public Object receive(EntityDestroyed message, Continuation<? super Response> continuation) {
        var defender = message.getSource();
        var attacker = message.getDestroyer();

        EntityExtensions.whenTypeIs(attacker, ExperienceGainer.class, experienceGainer -> {
            var xp = EntityExtensions.getExperience(experienceGainer);
            var stats = EntityExtensions.getExperienceGainerCombatStats(experienceGainer);
            int defenderHp = defender.findAttribute(JvmClassMappingKt.getKotlinClass(CombatStats.class))
                    .map(CombatStats::getMaxHp).orElse(0);
            int amount = (defenderHp + EntityExtensions.getAttackValue(defender) + EntityExtensions.getDefenseValue(defender))
                    - xp.getCurrentLevel() * 2;

            if (amount > 0) {
                xp.setCurrentXP(xp.getCurrentXP() + amount);
                while (xp.getCurrentXP() > Math.pow(xp.getCurrentLevel(), 1.5) * 20) {
                    xp.setCurrentLevel(xp.getCurrentLevel() + 1);
                    Functions.logGameEvent(attacker.getName() + " advanced to level " + xp.getCurrentLevel() + ".", ExperienceAccumulator.this);
                    int newHp = Math.min(stats.getHp() + xp.getCurrentLevel() * 2, stats.getMaxHp());
                    stats.getHpProperty().setValue(newHp);
                    if (EntityExtensions.isPlayer(attacker)) {
                        Zircon.INSTANCE.getEventBus().publish(
                                new PlayerGainedLevel(ExperienceAccumulator.this),
                                ApplicationScope.INSTANCE
                        );
                    }
                }
            }
        });
        return Consumed.INSTANCE;
    }
}
