package trade_company.logic.sql_object.Containers;

import SQL.SSQLController;
import trade_company.logic.sql_object.Person;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class PersonContainer {

    static Map<Integer, Person> container = new TreeMap<>();
    static ArrayList<Person> notSavedObjects = new ArrayList<>();

    public static Person getPersonFromResultSet(ResultSet result) throws SQLException {
        Person person = new Person();
        person.setId(result.getInt("ID_Person"));
        person.setPost(result.getInt("ID_Post"));
        person.setSupplier(result.getInt("ID_Supplier"));
        person.setFirstName(result.getString("FirstName"));
        person.setSecondName(result.getString("SecondName"));
        person.setThirdName(result.getString("ThirdName"));
        person.setLogin(result.getString("Login"));
        person.setPassword(result.getString("Password"));
        return person;
    }

    public static Person get(int id) {
        if (!container.containsKey(id)) container.put(id, new Person(id));
        return container.get(id);
    }

    public static void setAll() {
        setAll("");
    }

    public static void setAll(String where) {
        container.clear();
        try {
            ResultSet result = SSQLController.Query("SELECT * FROM person " + where);
            while (result.next()) {
                Person person = getPersonFromResultSet(result);
                container.put(person.getId(), person);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<Person> getList() {
        ArrayList<Person> list = new ArrayList<>();
        for (var entry : container.entrySet()) {
            list.add(entry.getValue());
        }
        return list;
    }

    public void add(Person object) {
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
