package trade_company.logic.administrator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

public class NewPersonLogicTest {

    @InjectMocks
    NewPersonLogic newPersonLogic;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCheckIfLoginUnique() throws Exception {
        boolean result = newPersonLogic.checkIfLoginUnique("login");
        Assert.assertTrue(result);
    }
}