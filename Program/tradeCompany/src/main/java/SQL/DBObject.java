package SQL;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class DBObject {
    int id = 0;

    public DBObject() {
    }

    public DBObject(int id) {
        setId(id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    abstract public void select();

    public void save() throws SQLException {
        if (getId() != 0) {
            update();
            return;
        }
        SSQLController.Insert(getSQLInsert());
        ResultSet res = SSQLController.Query("SELECT LAST_INSERT_ID() as ID;");
        res.next();
        setId(res.getInt("ID"));
    }

    public void update() throws SQLException {
        if (getId() == 0) {
            save();
            return;
        }
        try {
            SSQLController.Update(getSQLUpdate());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete() {
        if (getId() != 0) {
            try {
                SSQLController.Delete(getSQLDelete());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    abstract protected String getSQLUpdate();

    abstract protected String getSQLInsert();

    abstract protected String getSQLDelete();
}
