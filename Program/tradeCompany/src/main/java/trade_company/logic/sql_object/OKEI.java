package trade_company.logic.sql_object;

import SQL.DBObject;
import SQL.SSQLController;
import trade_company.logic.general.IOKEI;
import trade_company.logic.sql_object.Containers.OkeiContainer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OKEI extends DBObject implements IOKEI {
    String name;

    public OKEI() {
        super();
    }

    public OKEI(int id) {
        super(id);
        select();
    }

    public OKEI(int id, String name) {
        super(id);
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void set(OKEI okei) {
        setName(okei.getName());
    }

    @Override
    public void select() {
        try {
            ResultSet result = SSQLController.Query("Select * from okei_code where ID_OKEI=" + getId());
            if (!result.next()) throw new SQLException("No okei_code found with ID = " + getId());
            set(OkeiContainer.getOKEIFromResultSet(result));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getSQLUpdate() {
        return "UPDATE okei_code SET\n" + "Name = '" + getName() + "'\n" + "WHERE ID_OKEI = " + getId() + ";";
    }

    @Override
    public String getSQLInsert() {
        return "INSERT INTO okei_code VALUES\n" + "(NULL,'" + getName() + "');";
    }

    @Override
    public String getSQLDelete() {
        return "DELETE FROM okei_code WHERE ID_OKEI = " + getId() + ";";
    }
}
