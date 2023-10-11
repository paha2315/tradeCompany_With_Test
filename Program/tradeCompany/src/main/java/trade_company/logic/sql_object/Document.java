package trade_company.logic.sql_object;

import SQL.DBObject;
import SQL.SSQLController;
import trade_company.logic.sql_object.Containers.DocumentContainer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Document extends DBObject {

    Date date;

    public Document() {
    }

    public Document(int id) {
        super(id);
        select();
    }

    public Document(int id, Date date) {
        super(id);
        setDate(date);
    }

    public Date getDate() {
        return date;
    }


    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public void select() {
        try {
            ResultSet result = SSQLController.Query("Select * from document where ID_Document=" + getId());
            if (!result.next()) throw new SQLException("No document found with ID = " + getId());
            set(DocumentContainer.getDocumentFromResultSet(result));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void set(Document document) {
        setDate(document.getDate());
    }

    @Override
    protected String getSQLUpdate() {
        DateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
        String sqlDate = formatter.format(date);
        return "UPDATE document SET " +
                "Date = '" + sqlDate + "' " +
                "WHERE ID_Document = " + getId() + ";";
    }

    @Override
    protected String getSQLInsert() {
        DateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
        String sqlDate = formatter.format(date);
        return "INSERT INTO document VALUES " +
                "(NULL, '" + sqlDate + "');";
    }

    @Override
    protected String getSQLDelete() {
        return "DELETE FROM document " +
                "WHERE ID_Document = " + getId() + ";";
    }


    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static String getFormattedDate(LocalDate date) {
        return date.format(dtf);
    }
}
