package net.sievert.jolcraft.entity.custom;

import java.util.Arrays;
import java.util.Comparator;

public enum DwarfVariant {
    GREY(0),
    BLUE(1),
    GREEN(2),
    RED(3);

    private static final DwarfVariant[] BY_ID = Arrays.stream(values()).sorted(
            Comparator.comparingInt(DwarfVariant::getId)).toArray(DwarfVariant[]::new);
    private final int id;

    DwarfVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static DwarfVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
