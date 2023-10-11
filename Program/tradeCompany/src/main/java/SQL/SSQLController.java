package SQL;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SSQLController {
    static Map<String, SQLController> controller = new HashMap<>();
    static URL filename = Objects.requireNonNull(SSQLController.class.getResource(".DB"));

    static SQLController initController() {
        SQLController controller;
        try {
            controller = new SQLController(filename);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return controller;
    }

    protected static SQLController getFrom() {
        String className = Thread.currentThread().getStackTrace()[3].getClassName();
        if (!controller.containsKey(className))
            controller.put(className, initController());
//        System.out.println("Execute from " + className + " class (" + controller.size() + ")");
        return controller.get(className);
    }

    public static ResultSet Query(String sqlQuery) throws SQLException {
//        System.out.println(sqlQuery);
        return getFrom().Query(sqlQuery);
    }

    public static int Update(String sqlUpdate) throws SQLException {
        return getFrom().Update(sqlUpdate);
    }

    public static int Delete(String sqlDelete) throws SQLException {
        return getFrom().Delete(sqlDelete);
    }

    public static int Insert(String sqlInsert) throws SQLException {
        return getFrom().Insert(sqlInsert);
    }

}
