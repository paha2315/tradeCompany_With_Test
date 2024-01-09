package trade_company.views;

import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import trade_company.views.options.AdministratorMenuOptions;
import trade_company.views.options.StorekeeperMenuOptions;
import trade_company.views.options.SupplierMenuOptions;

import java.io.IOException;

public class ViewFactory {
    private final SimpleObjectProperty<StorekeeperMenuOptions> storekeeperSelectedMenuItem;
    private final SimpleObjectProperty<AdministratorMenuOptions> administratorSelectedMenuItem;
    private final SimpleObjectProperty<SupplierMenuOptions> supplierSelectedMenuItem;
    private AnchorPane dummyViewInstance;

    /* ========== STOREKEEPER SECTION ========== */
    private AnchorPane storekeeperActionOrdersViewInstance;
    private AnchorPane storekeeperActionAvailabilityViewInstance;
    private AnchorPane storekeeperProfileViewInstance;
    private AnchorPane storekeeperReportsViewInstance;
    private AnchorPane administratorNewPersonViewInstance;
    private AnchorPane administratorProfileViewInstance;
    private AnchorPane administratorUserListViewInstance;
    private AnchorPane supplierCompanyViewInstance;
    private AnchorPane supplierOrdersViewInstance;
    private AnchorPane supplierProfileViewInstance;

    public ViewFactory() {
        this.storekeeperSelectedMenuItem = new SimpleObjectProperty<>(StorekeeperMenuOptions.DUMMY);
        this.administratorSelectedMenuItem = new SimpleObjectProperty<>(AdministratorMenuOptions.DUMMY);
        this.supplierSelectedMenuItem = new SimpleObjectProperty<>(SupplierMenuOptions.DUMMY);
    }

    public AnchorPane getDummyView() {
        if (dummyViewInstance == null) {
            try {
                dummyViewInstance = new FXMLLoader(ViewFactory.class.getResource("/FXMLs/Dummy.fxml")).load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return dummyViewInstance;
    }

    public SimpleObjectProperty<StorekeeperMenuOptions> getStorekeeperSelectedMenuItem() {
        return storekeeperSelectedMenuItem;
    }

    public AnchorPane getStorekeeperActionOrdersView() {
        if (storekeeperActionOrdersViewInstance == null) {
            try {
                storekeeperActionOrdersViewInstance = new FXMLLoader(
                        ViewFactory.class.getResource("/FXMLs/Storekeeper/OrderProduct.fxml")).load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return storekeeperActionOrdersViewInstance;
    }

    public void resetStorekeeperOrdersView() {
        storekeeperActionOrdersViewInstance = null;
    }

    public AnchorPane getStorekeeperActionAvailabilityView() {
        if (storekeeperActionAvailabilityViewInstance == null) {
            try {
                storekeeperActionAvailabilityViewInstance = new FXMLLoader(
                        ViewFactory.class.getResource("/FXMLs/Storekeeper/ActionAvailability.fxml")).load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return storekeeperActionAvailabilityViewInstance;
    }

    /* ADMINISTRATOR SECTION */

    public void resetStorekeeperActionAvailabilityView() {
        storekeeperActionAvailabilityViewInstance = null;
    }

    public AnchorPane getStorekeeperProfileView() {
        if (storekeeperProfileViewInstance == null) {
            try {
                storekeeperProfileViewInstance = new FXMLLoader(
                        ViewFactory.class.getResource("/FXMLs/Storekeeper/Profile.fxml")).load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return storekeeperProfileViewInstance;
    }

    public void resetStorekeeperProfileView() {
        storekeeperProfileViewInstance = null;
    }

    public AnchorPane getStorekeeperReportsView() {
        if (storekeeperReportsViewInstance == null) {
            try {
                storekeeperReportsViewInstance = new FXMLLoader(
                        ViewFactory.class.getResource("/FXMLs/Storekeeper/Reports.fxml")).load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return storekeeperReportsViewInstance;
    }

    public void resetStorekeeperReportsView() {
        storekeeperReportsViewInstance = null;
    }

    public SimpleObjectProperty<AdministratorMenuOptions> getAdministratorSelectedMenuItem() {
        return administratorSelectedMenuItem;
    }

    public AnchorPane getAdministratorNewPersonView() {
        if (administratorNewPersonViewInstance == null) {
            try {
                administratorNewPersonViewInstance = new FXMLLoader(
                        ViewFactory.class.getResource("/FXMLs/Administrator/NewPerson.fxml")).load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return administratorNewPersonViewInstance;
    }

    public void resetAdministratorNewPersonView() {
        administratorNewPersonViewInstance = null;
    }

    /* SUPPLIER SECTION */

    public AnchorPane getAdministratorProfileView() {
        if (administratorProfileViewInstance == null) {
            try {
                administratorProfileViewInstance = new FXMLLoader(
                        ViewFactory.class.getResource("/FXMLs/Administrator/Profile.fxml")).load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return administratorProfileViewInstance;
    }

    public void resetAdministratorProfileView() {
        administratorProfileViewInstance = null;
    }

    public AnchorPane getAdministratorUserListView() {
        if (administratorUserListViewInstance == null) {
            try {
                administratorUserListViewInstance = new FXMLLoader(
                        ViewFactory.class.getResource("/FXMLs/Administrator/UserList.fxml")).load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return administratorUserListViewInstance;
    }

    public void resetAdministratorUserListView() {
        administratorUserListViewInstance = null;
    }

    public SimpleObjectProperty<SupplierMenuOptions> getSupplierSelectedMenuItem() {
        return supplierSelectedMenuItem;
    }

    public AnchorPane getSupplierCompanyView() {
        if (supplierCompanyViewInstance == null) {
            try {
                supplierCompanyViewInstance = new FXMLLoader(
                        ViewFactory.class.getResource("/FXMLs/Supplier/Company.fxml")).load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return supplierCompanyViewInstance;
    }

    public void resetSupplierCompanyView() {
        supplierCompanyViewInstance = null;
    }

    public AnchorPane getSupplierOrdersView() {
        if (supplierOrdersViewInstance == null) {
            try {
                supplierOrdersViewInstance = new FXMLLoader(
                        ViewFactory.class.getResource("/FXMLs/Supplier/OrdersSuppliesRequests.fxml")).load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return supplierOrdersViewInstance;
    }

    public void resetSupplierOrdersView() {
        supplierOrdersViewInstance = null;
    }

    public AnchorPane getSupplierProfileView() {
        if (supplierProfileViewInstance == null) {
            try {
                supplierProfileViewInstance = new FXMLLoader(
                        ViewFactory.class.getResource("/FXMLs/Supplier/Profile.fxml")).load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return supplierProfileViewInstance;
    }

    public void resetSupplierProfileView() {
        supplierProfileViewInstance = null;
    }

}
