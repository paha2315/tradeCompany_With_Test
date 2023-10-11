package trade_company.views.options;

import java.util.HashMap;
import java.util.Map;

public enum LoginRolesOptions {
    STOREKEEPER("Кладовщик"),
    SUPPLIER("Поставщик"),
    ADMIN("Администратор");

    private static final Map<String, LoginRolesOptions> BY_LABEL = new HashMap<>();

    static {
        for (LoginRolesOptions e : values()) {
            BY_LABEL.put(e.label, e);
        }
    }

    public final String label;

    LoginRolesOptions(String label) {
        this.label = label;
    }

    public static LoginRolesOptions valueOfLabel(String label) {
        return BY_LABEL.get(label);
    }
}