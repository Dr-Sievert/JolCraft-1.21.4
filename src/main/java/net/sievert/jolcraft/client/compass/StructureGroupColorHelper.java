package net.sievert.jolcraft.client.compass;

import java.util.Map;

public class StructureGroupColorHelper {

    public static final Map<String, Integer> GROUP_COLORS = Map.of(
            "dwarven_structures", 0x252525,
            "ancient_structures", 0x111b21
    );

    public static int getColor(String group) {
        return GROUP_COLORS.getOrDefault(group, 0xAAAAAA);
    }
}
