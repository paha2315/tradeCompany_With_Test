package trade_company.logic.supplier.tabs;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

public class RequestsControllerLogicTest {

    @InjectMocks
    RequestsControllerLogic requestsControllerLogic;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testInitWarehouses() throws Exception {
        requestsControllerLogic.initWarehouses();
    }
}