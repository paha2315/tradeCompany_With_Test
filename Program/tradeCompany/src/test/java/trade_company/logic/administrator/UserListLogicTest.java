package trade_company.logic.administrator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

public class UserListLogicTest {

    @InjectMocks
    UserListLogic userListLogic;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testInitTableData() throws Exception {
        userListLogic.initTableData();
    }

    @Test
    public void testRefreshTable() throws Exception {
        userListLogic.refreshTable("");
        System.out.println(userListLogic.personData.size());
        Assert.assertNotEquals(0, userListLogic.personData.size());
    }
}
