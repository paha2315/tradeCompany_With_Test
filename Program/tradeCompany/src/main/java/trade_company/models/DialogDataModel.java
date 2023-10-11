package trade_company.models;

import trade_company.models.data_objects.AddAvailDetailsDialogData;
import trade_company.models.data_objects.RequestOrderDetailsDialogData;

public class DialogDataModel {
    private static volatile DialogDataModel dialogDataModelInstance;

    private final AddAvailDetailsDialogData addAvailDetailsDialogData;
    private final RequestOrderDetailsDialogData requestOrderDetailsDialogData;

    private DialogDataModel() {
        this.addAvailDetailsDialogData = new AddAvailDetailsDialogData();
        this.requestOrderDetailsDialogData = new RequestOrderDetailsDialogData();
    }

    public static DialogDataModel getInstance() {
        DialogDataModel localInstance = dialogDataModelInstance;
        if (localInstance == null) {
            synchronized (DialogDataModel.class) {
                localInstance = dialogDataModelInstance;
                if (localInstance == null) {
                    dialogDataModelInstance = localInstance = new DialogDataModel();
                }
            }
        }
        return localInstance;
    }

    public AddAvailDetailsDialogData getAddProductDetailsDialogData() {
        return addAvailDetailsDialogData;
    }

    public RequestOrderDetailsDialogData getRequestOrderDetailsDialogData() {
        return requestOrderDetailsDialogData;
    }

}
