package pl.ynleborg.ynlebapiorg.mac;

import java.util.HashMap;
import java.util.Map;

//FIXME dodac tutaj drugi parametr z indetyfikatorem minecrafta na platformie
public enum Platform {
    X("XboxOne", 1828326430L),
    W("WindowsOneCore", 896928775L),
    A("Android", 1739947436L),
    G("GearVR", 1909043648L),
    N("Nintendo", 2047319603L),
    I("iOS", 1810924247L);

    private static final Map<String, Platform> BY_LABEL = new HashMap<>();

    static {
        for (Platform e : values()) {
            BY_LABEL.put(e.label, e);
        }
    }

    public final String label;
    public final Long titleId;

    Platform(String label, Long titleId) {
        this.label = label;
        this.titleId = titleId;
    }

    public static Platform valueOfLabel(String label) {
        return BY_LABEL.get(label);
    }
}
