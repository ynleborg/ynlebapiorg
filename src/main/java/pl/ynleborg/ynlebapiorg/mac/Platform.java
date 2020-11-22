package pl.ynleborg.ynlebapiorg.mac;

import java.util.HashMap;
import java.util.Map;

//FIXME dodac tutaj drugi parametr z indetyfikatorem minecrafta na platformie
public enum Platform {
    X("XboxOne"),
    W("WindowsOneCore"),
    A("Android"),
    G("GearVR"),
    N("Nintendo"),
    I("iOS");

    private static final Map<String, Platform> BY_LABEL = new HashMap<>();

    static {
        for (Platform e: values()) {
            BY_LABEL.put(e.label, e);
        }
    }

    public final String label;

    Platform(String label) {
        this.label = label;
    }
    public static Platform valueOfLabel(String label) {
        return BY_LABEL.get(label);
    }
}
