package trade_company.logic.sql_object.Containers;

import SQL.SSQLController;
import trade_company.logic.sql_object.OKEI;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class OkeiContainer {
    static Map<Integer, OKEI> container = new TreeMap<>();
    static ArrayList<OKEI> notSavedObjects = new ArrayList<>();

    public static OKEI get(int id) {
        if (!container.containsKey(id)) container.put(id, new OKEI(id));
        return container.get(id);
    }

    public static void setAll() {
        setAll("");
    }

    public static void setAll(String where) {
        container.clear();
        try {
            ResultSet result = SSQLController.Query("Select * from Okei_code " + where);
            while (result.next()) {
                OKEI okei = getOKEIFromResultSet(result);
                container.put(okei.getId(), okei);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static OKEI getOKEIFromResultSet(ResultSet result) throws SQLException {
        int id = result.getInt("ID_OKEI");
        String name = result.getString("Name");
        return new OKEI(id, name);
    }

    public static ArrayList<OKEI> getList() {
        ArrayList<OKEI> list = new ArrayList<>();
        for (var entry : container.entrySet()) {
            list.add(entry.getValue());
        }
        return list;
    }

    public void add(OKEI object) {
        if (object.getId() == 0) notSavedObjects.add(object);
        else container.put(object.getId(), object);
    }

    public void save() throws SQLException {
        for (var object : container.entrySet()) {
            object.getValue().update();
        }
        for (var object : notSavedObjects) {
            object.save();
            add(object);
        }
        notSavedObjects.clear();
    }
}

