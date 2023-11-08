import SQL.SQLController;
import org.junit.jupiter.api.Test;
import trade_company.logic.login.LoginLogic;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DBTests {
    @Test
    public void testConnect() {
        try {
            SQLController controller = new SQLController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void CheckAdmin() {
        LoginLogic logic = new LoginLogic();
        try {
            assertTrue(logic.personIsAuthorized("Администратор", "admin", "admin"));
        } catch (SQLException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
