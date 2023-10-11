package trade_company.logic.sql_object.Containers;

import SQL.SSQLController;
import trade_company.logic.sql_object.Availability_link;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class Availability_linkContainer {
    static Map<Integer, Availability_link> container = new TreeMap<>();
    static ArrayList<Availability_link> notSavedObjects = new ArrayList<>();

    public static Availability_link get(int id) {
        if (!container.containsKey(id)) container.put(id, new Availability_link(id));
        return container.get(id);
    }

    public static void setAll() {
        setAll("");
    }

    public static void setAll(String where) {
        container.clear();
        try {
            ResultSet result = SSQLController.Query("Select * from Availability_link " + where);
            while (result.next()) {
                Availability_link avail = getAvailability_linkFromResultSet(result);
                container.put(avail.getId(), avail);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Availability_link getAvailability_linkFromResultSet(ResultSet result) throws SQLException {
        int id = result.getInt("{ID_Link");
        Integer documtntID;
        documtntID = result.getInt("ID_Document");
        if (result.wasNull())
            documtntID = null;
        int idAvail = result.getInt("ID_Availability");
        int count = result.getInt("Count");
        return new Availability_link(id, documtntID, idAvail, count);
    }

    public static ArrayList<Availability_link> getList() {
        ArrayList<Availability_link> list = new ArrayList<>();
        for (var entry : container.entrySet()) {
            list.add(entry.getValue());
        }
        return list;
    }

    public void add(Availability_link object) {
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
