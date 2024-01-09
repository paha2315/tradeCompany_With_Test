package trade_company.logic.sql_object;

import SQL.DBObject;
import SQL.SSQLController;
import trade_company.logic.sql_object.Containers.AvailabilityContainer;
import trade_company.logic.sql_object.Containers.Availability_linkContainer;
import trade_company.logic.sql_object.Containers.DocumentContainer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Availability_link extends DBObject {
    Document document;
    Availability availability;
    double count;

    public Availability_link() {
    }

    public Availability_link(int id) {
        super(id);
        select();
    }

    public Availability_link(int id, Integer ID_document, int ID_availability, double count) {
        super(id);
        setDocument(ID_document);
        setAvailability(ID_availability);
        setCount(count);
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Integer id) {
        if (id == null) setDocument((Document) null);
        else setDocument(DocumentContainer.get(id));
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public String getDocumentID() {
        if (document == null)
            return null;
        return String.valueOf(getDocument().getId());
    }

    public Availability getAvailability() {
        return availability;
    }

    private void setAvailability(int idAvailability) {
        setAvailability(AvailabilityContainer.get(idAvailability));
    }

    public void setAvailability(Availability availability) {
        this.availability = availability;
    }

    public double getCount() {
        return count;
    }

    public void setCount(double count) {
        this.count = count;
    }

    @Override
    public void select() {
        try {
            ResultSet result = SSQLController.Query("Select * from availability_link where ID_Link=" + getId());
            if (!result.next()) throw new SQLException("No Availability_link found with ID = " + getId());
            set(Availability_linkContainer.getAvailability_linkFromResultSet(result));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void set(Availability_link availability_link) {
        setDocument(availability_link.getDocument());
        setAvailability(availability_link.getAvailability());
        setCount(availability_link.getCount());
    }

    @Override
    protected String getSQLUpdate() {
        return "UPDATE availability_link SET " +
               "`ID_Document` = " + getDocumentID() + ", " +
               "`ID_Availability` = " + getAvailability().getId() + ", " +
               "`Count` = " + count + " " +
               "WHERE `ID_Link` = " + getId() + ";";
    }

    @Override
    protected String getSQLInsert() {
        return "INSERT INTO availability_link VALUES " +
               "(NULL," + getDocumentID() + "," + getAvailability().getId() + "," + count + ");\n";
    }

    @Override
    protected String getSQLDelete() {
        return "DELETE FROM availability_link " +
               "WHERE `ID_Link` = " + getId() + ";";
    }
}
