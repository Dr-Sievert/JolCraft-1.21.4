package net.sievert.jolcraft.client.data;

public class MyClientLanguageData {
    private static boolean knowsLanguage = false;

    public static void setKnows(boolean value) {
        knowsLanguage = value;
    }

    public static boolean knowsLanguage() {
        return knowsLanguage;
    }
}
