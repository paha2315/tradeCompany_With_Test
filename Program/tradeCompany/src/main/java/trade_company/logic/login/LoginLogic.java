package trade_company.logic.login;

import SQL.SSQLController;
import trade_company.logic.Hasher;
import trade_company.logic.sql_object.Containers.PersonContainer;
import trade_company.logic.sql_object.Containers.WarehouseContainer;
import trade_company.logic.sql_object.Warehouse;
import trade_company.models.UserDataModel;
import trade_company.views.options.LoginRolesOptions;

import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class LoginLogic {
    protected ArrayList<String> mRolesList;

    protected ArrayList<Warehouse> warehouses;
    protected ArrayList<String> warehouseNames;

    public LoginLogic() {
        mRolesList = new ArrayList<>();
        initRolesList();
        initWarehouses();
    }

    private void initRolesList() {
        for (var role_type : LoginRolesOptions.values())
            mRolesList.add(role_type.label);
    }

    private void initWarehouses() {
        warehouseNames = new ArrayList<>();
        WarehouseContainer.setAll();
        warehouses = WarehouseContainer.getList();
        for (var warehouse : warehouses)
            warehouseNames.add(warehouse.getName());
    }

    public boolean personIsAuthorized(String post, String login, String password) throws SQLException, NoSuchAlgorithmException {
        String hashPassword = Hasher.createSHAHash(password);

        ResultSet result = SSQLController.Query("SELECT ID_Person, Login, Password, ID_Post FROM person");
        if (!result.next())
            return false;

        var allPersons = new ArrayList<personHelper>();
        do {
            allPersons.add(new personHelper(
                    result.getInt("ID_Person"),
                    result.getInt("ID_Post"),
                    result.getString("Login"),
                    result.getString("Password")
            ));
        } while (result.next());
        result.close();

        boolean authorized = false;
        int authorized_person_id = -1;
        for (var item : allPersons) {
            authorized_person_id = item.id;
            result = SSQLController.Query("SELECT Name FROM post WHERE ID_Post=" + item.id_post);
            if (!result.next())
                continue;
            String truePost = result.getString("Name");
            result.close();
            authorized = item.login.equals(login) && item.password.equals(hashPassword) && truePost.equals(post);
            if (authorized)
                break;
        }
        if (authorized) {
            result = SSQLController.Query("SELECT * FROM person WHERE ID_Person=" + authorized_person_id);
            result.next();
            UserDataModel.getInstance().setPerson(PersonContainer.getPersonFromResultSet(result));
        } else UserDataModel.getInstance().setPerson(null);

        return authorized;
    }

    protected Optional<LoginRolesOptions> findUserRoleById(int user_id) throws SQLException {
        Optional<LoginRolesOptions> result = Optional.empty();
        ResultSet resultSetUserRole = SSQLController.Query("""
                SELECT * FROM post
                WHERE ID_Post IN (
                    SELECT ID_Post FROM person
                    WHERE ID_Person=%d
                )""".formatted(user_id));
        var found = resultSetUserRole.next();
        if (found) {
            var role_type = LoginRolesOptions.valueOfLabel(resultSetUserRole.getString("Name"));
            if (role_type != null)
                result = Optional.of(role_type);
        }
        return result;
    }

    private static class personHelper {
        int id;
        int id_post;
        String login;
        String password;

        public personHelper(int id, int id_post, String login, String password) {
            this.id = id;
            this.id_post = id_post;
            this.login = login;
            this.password = password;
        }
    }

}
