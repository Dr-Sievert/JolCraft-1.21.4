package net.sievert.jolcraft.network.client.data;

public class MyClientAncientLanguageData {
    private static boolean knowsLanguage = false;

    public static void setKnows(boolean value) {
        knowsLanguage = value;
    }

    public static boolean knowsLanguage() {
        return knowsLanguage;
    }
}
