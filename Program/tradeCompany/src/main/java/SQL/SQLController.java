package SQL;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Objects;
import java.util.Scanner;

public class SQLController {
    private Connection connection = null;
    private Statement statement = null;

    private String db;

    private String login;

    private String password;

    SQLController(String DB, String Login, String Password) {
        setDb(DB);
        setLogin(Login);
        setPassword(Password);
    }

    SQLController(URL filename) throws IOException {
        load(filename);
    }

    SQLController() throws IOException {
        load(Objects.requireNonNull(SSQLController.class.getResource(".DB")));
    }

    private void load(URL filename) throws IOException {
        Scanner scanner = new Scanner(filename.openStream());
        setDb(scanner.nextLine());
        setLogin(scanner.nextLine());
        setPassword(scanner.nextLine());
        scanner.close();
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
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

    public Connection getConnection() {
        if (connection == null) setConnection();
        return connection;
    }

    public void setConnection() {
        statement = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + getDb(), getLogin(), getPassword());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Statement getStatement() throws SQLException {
        if (statement == null) statement = getConnection().createStatement();
        return statement;
    }

    private ResultSet executeQuery(String sqlQuery) throws SQLException {
        return getStatement().executeQuery(sqlQuery);
    }

    private int executeUpdate(String sqlUpdate) throws SQLException {
        return getStatement().executeUpdate(sqlUpdate);
    }

    public ResultSet Query(String sqlQuery) throws SQLException {
        return executeQuery(sqlQuery);
    }

    public int Update(String sqlUpdate) throws SQLException {
        return executeUpdate(sqlUpdate);
    }

    public int Delete(String sqlDelete) throws SQLException {
        return executeUpdate(sqlDelete);
    }

    public int Insert(String sqlInsert) throws SQLException {
        return executeUpdate(sqlInsert);
    }
}
