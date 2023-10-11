package trade_company.logic.sql_object;

import SQL.DBObject;
import SQL.SSQLController;
import trade_company.logic.sql_object.Containers.PostContainer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Post extends DBObject {
    String name;

    public Post(int id, String name) {
        super(id);
        setName(name);
    }

    public Post(int id) {
        super(id);
        select();
    }

    public Post() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void set(Post post) {
        setId(post.getId());
        setName(post.getName());
    }

    @Override
    public void select() {
        try {
            ResultSet result = SSQLController.Query("Select * from post where ID_Post=" + getId());
            if (!result.next()) throw new SQLException("No post found with ID = " + getId());
            set(PostContainer.getPostFromResultSet(result));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected String getSQLUpdate() {
        return "UPDATE post SET\n" + "Name = '" + getName() + "'\n" + "WHERE `ID_Post` =" + getId();
    }

    @Override
    protected String getSQLInsert() {
        return "INSERT INTO post VALUES (NULL,'" + getName() + "');";
    }

    @Override
    protected String getSQLDelete() {
        return "DELETE FROM post WHERE ID_Post = " + getId();
    }
}
