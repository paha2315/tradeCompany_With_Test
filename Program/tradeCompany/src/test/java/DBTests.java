import SQL.SQLController;
import SQL.SSQLController;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

public class DBTests {
    @Test
    public void testConnect(){
        try {
            SQLController controller = new SQLController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
