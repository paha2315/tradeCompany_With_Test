package trade_company.logic.supplier.tabs;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import trade_company.logic.sql_object.Warehouse;

import static org.mockito.Mockito.when;

public class OrdersLogicTest {
    @Mock
    Warehouse curWarehouse;
    @InjectMocks
    OrdersLogic ordersLogic;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testInitWarehouses() throws Exception {
        when(curWarehouse.getName()).thenReturn("getNameResponse");
        ordersLogic.initWarehouses();
    }

    @Test
    public void testInitProducts() throws Exception {
        ordersLogic.initProducts();
    }

    @Test
    public void testSearch() throws Exception {
        when(curWarehouse.getId()).thenReturn(0);

        ordersLogic.search();
    }
}
