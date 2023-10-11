package trade_company.logic.sql_object;

import SQL.DBObject;
import SQL.SSQLController;
import trade_company.logic.sql_object.Containers.PersonContainer;
import trade_company.logic.sql_object.Containers.PostContainer;
import trade_company.logic.sql_object.Containers.SupplierContainer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Person extends DBObject {
    Post post;
    Supplier supplier;
    String firstName;
    String secondName;
    String thirdName;
    String login;
    String password;

    public Person() {
        super();
    }

    public Person(int id) {
        super(id);
        select();
    }

    public Person(int id, int id_post, int id_supplier, String firstName, String secondName, String thirdName, String login, String password) {
        super(id);
        setPost(id_post);
        setSupplier(id_supplier);
        setFirstName(firstName);
        setSecondName(secondName);
        setThirdName(thirdName);
        setLogin(login);
        setPassword(password);
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public void setPost(int id_post) {
        setPost(PostContainer.get(id_post));
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public void setSupplier(int id_supplier) {
        if (id_supplier != 0)
            setSupplier(SupplierContainer.get(id_supplier));
        else
            setSupplier(null);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getThirdName() {
        return thirdName;
    }

    public void setThirdName(String thirdName) {
        this.thirdName = thirdName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFIO() {
        String res = getSecondName() + " " + getFirstName().charAt(0) + ".";
        if (getThirdName().length() > 0)
            res += getThirdName().charAt(0) + ".";
        return res;
    }

    public String getFullName() {
        String res = getSecondName() + " " + getFirstName();
        if (getThirdName().length() > 0)
            res += " " + getThirdName();
        return res;
    }

    public void set(Person person) {
        setId(person.getId());
        setPost(person.getPost());
        setSupplier(person.getSupplier());
        setFirstName(person.getFirstName());
        setSecondName(person.getSecondName());
        setThirdName(person.getThirdName());
        setLogin(person.getLogin());
        setPassword(person.getPassword());
    }

    @Override
    public void select() {
        try {
            ResultSet result = SSQLController.Query("SELECT * FROM person WHERE ID_Post=" + getId());
            if (!result.next()) throw new SQLException("No person found with ID = " + getId());
            set(PersonContainer.getPersonFromResultSet(result));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected String getSQLUpdate() {
        String supp_text = (supplier == null) ? "NULL" : Integer.toString(supplier.getId());
        return "UPDATE person SET "
               + "ID_Post=" + post.getId() + ","
               + "ID_Supplier=" + supp_text + ","
               + "FirstName='" + firstName + "',"
               + "SecondName='" + secondName + "',"
               + "ThirdName='" + thirdName + "',"
               + "Login='" + login + "',"
               + "Password='" + password + "' "
               + "WHERE ID_Person=" + getId() + ";";
    }

    @Override
    protected String getSQLInsert() {
        String supp_text = (supplier == null) ? "NULL" : Integer.toString(supplier.getId());
        return "INSERT INTO person VALUES ("
               + "NULL,"
               + post.getId() + ","
               + supp_text + ","
               + "'" + firstName + "',"
               + "'" + secondName + "',"
               + "'" + thirdName + "',"
               + "'" + login + "',"
               + "'" + password + "')";
    }

    @Override
    protected String getSQLDelete() {
        return "DELETE FROM person WHERE ID_Person=" + getId();
    }
}
