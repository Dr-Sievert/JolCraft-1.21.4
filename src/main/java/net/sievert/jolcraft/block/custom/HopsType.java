package net.sievert.jolcraft.block.custom;

import net.minecraft.util.StringRepresentable;

public enum HopsType implements StringRepresentable {
    NONE("none"),  // Represents no hop type selected
    ASGARNIAN("asgarnian_hop"),
    YANILLIAN("yanillian_hop"),
    DUSKHOLD("duskhold_hop"),
    KRANDONIAN("krandonian_hop");

    private final String name;

    HopsType(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return name; // The name of the hop type as a string
    }
}