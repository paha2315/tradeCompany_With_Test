package trade_company.views.diagram;

import java.util.HashMap;
import java.util.Map;

public enum ReportDiagramTypes {
    COUNT_SALES("Продажи товаров"), // Количество проданных товаров
    MONEY_MASS_GET("Выручка"),  // Сумма выручки по товарам
    MONEY_MASS_GIVEN("Затраты");  // Сумма трат по товарам

    private static final Map<String, ReportDiagramTypes> BY_LABEL = new HashMap<>();

    static {
        for (ReportDiagramTypes e : values()) {
            BY_LABEL.put(e.label, e);
        }
    }


    public final String label;

    ReportDiagramTypes(String label) {
        this.label = label;
    }

    public static ReportDiagramTypes valueOfLabel(String label) {
        return BY_LABEL.get(label);
    }
}
