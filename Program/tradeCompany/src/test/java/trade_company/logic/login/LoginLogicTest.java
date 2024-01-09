package trade_company.logic.login;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import trade_company.views.options.LoginRolesOptions;

import java.util.Optional;

public class LoginLogicTest {

    @InjectMocks
    LoginLogic loginLogic;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testPersonIsAuthorized() throws Exception {
        boolean result = loginLogic.personIsAuthorized("Администратор", "admin", "admin");
        Assert.assertTrue(result);
    }

    @Test
    public void testFindUserRoleByIdAdmin() throws Exception {
        Optional<LoginRolesOptions> result = loginLogic.findUserRoleById(3);
        Assert.assertEquals(LoginRolesOptions.SUPPLIER, result.get());
    }

    @Test
    public void testFindUserRoleByIdStorkeeper() throws Exception {
        Optional<LoginRolesOptions> result = loginLogic.findUserRoleById(2);
        Assert.assertEquals(LoginRolesOptions.STOREKEEPER, result.get());
    }

    @Test
    public void testFindUserRoleByIdSupplier() throws Exception {
        Optional<LoginRolesOptions> result = loginLogic.findUserRoleById(1);
        Assert.assertEquals(LoginRolesOptions.ADMIN, result.get());
    }
}
