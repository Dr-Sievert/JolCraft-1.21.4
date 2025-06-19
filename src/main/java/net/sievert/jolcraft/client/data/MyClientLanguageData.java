package net.sievert.jolcraft.client.data;

public class MyClientLanguageData {
    private static boolean knowsLanguage = false;

    public static void setKnows(boolean value) {
        knowsLanguage = value;
        System.out.println("[CLIENT] Dwarven language set to: " + value);
    }

    public static boolean knowsLanguage() {
        return knowsLanguage;
    }
}
