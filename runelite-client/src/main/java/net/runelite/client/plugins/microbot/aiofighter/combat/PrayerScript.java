package net.runelite.client.plugins.microbot.aiofighter.combat;

import lombok.extern.slf4j.Slf4j;
import net.runelite.client.plugins.microbot.Microbot;
import net.runelite.client.plugins.microbot.Script;
import net.runelite.client.plugins.microbot.aiofighter.AIOFighterConfig;
import net.runelite.client.plugins.microbot.aiofighter.enums.AttackStyle;
import net.runelite.client.plugins.microbot.aiofighter.enums.AttackStyleMapper;
import net.runelite.client.plugins.microbot.aiofighter.enums.PrayerStyle;
import net.runelite.client.plugins.microbot.util.combat.Rs2Combat;
import net.runelite.client.plugins.microbot.util.npc.Rs2Npc;
import net.runelite.client.plugins.microbot.util.npc.Rs2NpcManager;
import net.runelite.client.plugins.microbot.util.npc.Rs2NpcModel;
import net.runelite.client.plugins.microbot.util.prayer.Rs2Prayer;
import net.runelite.client.plugins.microbot.util.prayer.Rs2PrayerEnum;
import net.runelite.client.plugins.microbot.util.slayer.Rs2Slayer;

import java.util.concurrent.TimeUnit;

@Slf4j
public class PrayerScript extends Script {
    public boolean run(AIOFighterConfig config) {
        try {
            Rs2NpcManager.loadJson();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        mainScheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(() -> {
            try {
                if (!Microbot.isLoggedIn() || !config.togglePrayer()) return;
                PrayerStyle prayStyle = config.prayerStyle();
                if (prayStyle == PrayerStyle.ALWAYS_ON) {
                    handlePrayer(config);
                } else if (prayStyle == PrayerStyle.CONTINUOUS) {
                    boolean underAttack = Rs2Npc.getNpcsForPlayer(npc -> !npc.isDead() && npc.getCombatLevel() > 1).findAny().isPresent() || Rs2Combat.inCombat();
                    if (!underAttack) {
                        Rs2Prayer.disableAllPrayers();
                    }
                    handlePrayer(config);
                }
            } catch (Exception ex) {
                Microbot.logStackTrace(this.getClass().getSimpleName(), ex);
            }
        }, 0, 500, TimeUnit.MILLISECONDS);
        return true;
    }

    private void handlePrayer(AIOFighterConfig config) {
        Rs2NpcModel npc = Rs2Npc.getNpcsForPlayer(n -> !n.isDead() && n.getCombatLevel() > 1).findFirst().orElse(null);
        if (config.toggleQuickPray() && !Rs2Slayer.isSuperior(npc)) {
            Rs2Prayer.toggleQuickPrayer(true);
        } else if (npc != null) {
            togglePrayerAccordingToNpcStyle(npc);
        }
    }

    private void togglePrayerAccordingToNpcStyle(Rs2NpcModel npc) {
        AttackStyle attackStyle = AttackStyleMapper
                .mapToAttackStyle(Rs2NpcManager.getAttackStyle(npc.getId()));
        if (attackStyle != null) {
            switch (attackStyle) {
                case MAGE:
                    Rs2Prayer.toggle(Rs2PrayerEnum.PROTECT_MAGIC, true);
                    break;
                case MELEE:
                    Rs2Prayer.toggle(Rs2PrayerEnum.PROTECT_MELEE, true);
                    break;
                case RANGED:
                    Rs2Prayer.toggle(Rs2PrayerEnum.PROTECT_RANGE, true);
                    break;
            }
        }
    }

    @Override
    public void shutdown() {
        super.shutdown();
    }
}
