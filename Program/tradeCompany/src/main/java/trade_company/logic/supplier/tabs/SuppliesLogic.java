package trade_company.logic.supplier.tabs;

import trade_company.logic.sql_object.Containers.WarehouseContainer;
import trade_company.logic.sql_object.Order;
import trade_company.logic.sql_object.Warehouse;
import trade_company.models.UserDataModel;

import java.sql.SQLException;
import java.util.ArrayList;

public class SuppliesLogic {
    protected ArrayList<String> warehouseNamesList;

    public SuppliesLogic() {
        warehouseNamesList = new ArrayList<>();
        WarehouseContainer.setAll();
        for (var item : WarehouseContainer.getList())
            warehouseNamesList.add(item.getName());
    }

    protected SupplyCreationStatus doSupply(String warehouseName, String priceStr, String countStr) {
        // Проверка выбора склада
        Warehouse warehouse = null;
        for (var item : WarehouseContainer.getList()) {
            if (item.getName().equals(warehouseName)) {
                warehouse = item;
                break;
            }
        }
        if (warehouse == null)
            return SupplyCreationStatus.WAREHOUSE_INCOMPLETE;
        UserDataModel.getInstance().setWarehouse(warehouse);

        // Проверка заполнения полей
        if (priceStr.isBlank() || countStr.isBlank())
            return SupplyCreationStatus.FIELDS_INCOMPLETE;

        // Проверка правильности введённых значений
        double price;
        double count;
        try {
            price = Double.parseDouble(priceStr);
            count = Double.parseDouble(countStr);
        } catch (NumberFormatException e) {
            return SupplyCreationStatus.FIELDS_STR_TO_NUMBER_CONVERT_EXCEPTION;
        }

        // Запись предложения в таблицу заказов
        try {
            var supplier = UserDataModel.getInstance().getPerson().getSupplier();
            Order order = new Order(0, null, supplier.getId(), warehouse.getId(), price, count, 1);
            order.save();
        } catch (SQLException e) {
            return SupplyCreationStatus.SQL_EXCEPTION;
        }

        return SupplyCreationStatus.OK;
    }

    public enum SupplyCreationStatus {
        OK,
        WAREHOUSE_INCOMPLETE,
        FIELDS_INCOMPLETE,
        FIELDS_STR_TO_NUMBER_CONVERT_EXCEPTION,
        SQL_EXCEPTION
    }
}
