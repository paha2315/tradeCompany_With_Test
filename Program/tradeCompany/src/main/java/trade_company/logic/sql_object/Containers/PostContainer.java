package trade_company.logic.sql_object.Containers;

import SQL.SSQLController;
import trade_company.logic.sql_object.Post;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class PostContainer {
    static Map<Integer, Post> container = new TreeMap<>();
    static ArrayList<Post> notSavedObjects = new ArrayList<>();

    public static Post getPostFromResultSet(ResultSet result) throws SQLException {
        Post post = new Post();
        post.setId(result.getInt("ID_Post"));
        post.setName(result.getString("Name"));
        return post;
    }

    public static Post get(int id) {
        if (!container.containsKey(id)) container.put(id, new Post(id));
        return container.get(id);
    }

    public static void setAll() {
        setAll("");
    }

    public static void setAll(String where) {
        container.clear();
        try {
            ResultSet result = SSQLController.Query("Select * from post " + where);
            while (result.next()) {
                Post post = getPostFromResultSet(result);
                container.put(post.getId(), post);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<Post> getList() {
        ArrayList<Post> list = new ArrayList<>();
        for (var entry : container.entrySet()) {
            list.add(entry.getValue());
        }
        return list;
    }

    public void add(Post object) {
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
