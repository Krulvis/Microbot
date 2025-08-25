package net.runelite.client.plugins.microbot.aiofighter.enums;

import net.runelite.api.NPC;
import net.runelite.api.Prayer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Superior {
    CRUSHING_HAND,
    CHASM_CRAWLER,
    SCREAMING_BANSHEE,
    SCREAMING_TWISTED_BANSHEE,
    GIANT_ROCKSLUG,
    COCKATHRICE,
    FLAMING_PYRELORD,
    INFERNAL_PYRELORD,
    MONSTROUS_BASILISK,
    MALEVOLENT_MAGE(Prayer.PROTECT_FROM_MAGIC),
    INSATIABLE_BLOODVELD,
    INSATIABLE_MUTATED_BLOODVELD,
    VITREOUS_JELLY,
    VITREOUS_WARPED_JELLY,
    SPIKED_TUROTH,
    MUTATED_TERRORBIRD,
    MUTATED_TORTOISE,
    CAVE_ABOMINATION,
    ABHORRENT_SPECTRE(Prayer.PROTECT_FROM_MAGIC),
    REPUGNANT_SPECTRE(Prayer.PROTECT_FROM_MAGIC),
    BASILISK_SENTINEL,
    SHADOW_WYRM,
    CHOKE_DEVIL,
    KING_KURASK,
    MARBLE_GARGOYLE,
    NECHRYARCH,
    GUARDIAN_DRAKE,
    GREATER_ABYSSAL_DEMON,
    NIGHT_BEAST,
    NUCLEAR_SMOKE_DEVIL(Prayer.PROTECT_FROM_MISSILES),
    COLOSSAL_HYDRA(Prayer.PROTECT_FROM_MAGIC);

    public final Prayer prayer;

    Superior(Prayer prayer) {
        this.prayer = prayer;
    }

    Superior() {
        this.prayer = Prayer.PROTECT_FROM_MELEE;
    }

    public static List<String> allNames() {
        return Arrays.stream(values()).map(x -> x.name().replaceAll("_", " ").toLowerCase()).collect(Collectors.toList());
    }

    public static Superior forNpc(NPC npc) {
        String name = npc.getName();
        if (name == null) return false;
        String lowerCaseName = name.toLowerCase();
        return allNames().stream().filter(lowerCaseName::contains).;
    }
}
