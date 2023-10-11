package trade_company.logic.administrator;

import SQL.SSQLController;
import trade_company.logic.Hasher;
import trade_company.logic.sql_object.Containers.PostContainer;
import trade_company.logic.sql_object.Person;

import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class NewPersonLogic {
    protected ArrayList<String> comboboxPostList;

    public NewPersonLogic() {
        comboboxPostList = new ArrayList<>();
        PostContainer.setAll("WHERE Name!='Администратор'");
    }

    protected void initCombobox() {
        for (var item : PostContainer.getList())
            comboboxPostList.add(item.getName());
    }

    protected boolean checkIfLoginUnique(String login) throws SQLException {
        ResultSet result = SSQLController.Query("SELECT * FROM person WHERE Login='" + login + "'");
        boolean yes = !result.next();
        result.close();
        return yes;
    }

    protected void createNewPerson(String firstName, String secondName, String thirdName, String post, String login, String password) throws SQLException, NoSuchAlgorithmException {
        ResultSet result = SSQLController.Query("SELECT * FROM post WHERE Name='%s'".formatted(post));
        result.next();
        int post_id = result.getInt("ID_Post");
        result.close();
        var passwordHash = Hasher.createSHAHash(password);
        var person = new Person(0, post_id, 0, firstName, secondName, thirdName, login, passwordHash);
        person.save();
    }
}
