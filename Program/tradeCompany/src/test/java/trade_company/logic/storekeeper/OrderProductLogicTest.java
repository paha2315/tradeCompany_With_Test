package trade_company.logic.storekeeper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

public class OrderProductLogicTest {

    @InjectMocks
    OrderProductLogic orderProductLogic;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testInitSuppliers() throws Exception {
        orderProductLogic.initSuppliers();
    }

    @Test
    public void testInitComboBoxCity() throws Exception {
        orderProductLogic.initComboBoxCity();
    }

    @Test
    public void testSearch() throws Exception {
        orderProductLogic.search(orderProductLogic.ALL_CITIES, "");
    }
}
