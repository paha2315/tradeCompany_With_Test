package trade_company.logic.sql_object.Containers;

import SQL.SSQLController;
import trade_company.logic.sql_object.Document;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class DocumentContainer {
    static Map<Integer, Document> container = new TreeMap<>();
    static ArrayList<Document> notSavedObjects = new ArrayList<>();

    public static Document get(int id) {
        if (!container.containsKey(id)) container.put(id, new Document(id));
        return container.get(id);
    }

    public static void setAll() {
        setAll("");
    }

    public static void setAll(String where) {
        container.clear();
        try {
            ResultSet result = SSQLController.Query("Select * from document " + where);

            while (result.next()) {
                Document document = getDocumentFromResultSet(result);
                container.put(document.getId(), document);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Document getDocumentFromResultSet(ResultSet result) throws SQLException {
        int id = result.getInt("ID_Document");
        Date date = result.getDate("Date");
        return new Document(id, date);
    }

    public static ArrayList<Document> getList() {
        ArrayList<Document> list = new ArrayList<>();
        for (var entry : container.entrySet()) {
            list.add(entry.getValue());
        }
        return list;
    }

    public void add(Document object) {
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
