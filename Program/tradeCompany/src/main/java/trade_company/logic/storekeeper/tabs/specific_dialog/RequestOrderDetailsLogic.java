package trade_company.logic.storekeeper.tabs.specific_dialog;

import trade_company.models.DialogDataModel;

public class RequestOrderDetailsLogic {
    public RequestOrderDetailsLogic() {
        DialogDataModel.getInstance().getRequestOrderDetailsDialogData().setOk(false);
    }

    public void init(String productName) {
        DialogDataModel.getInstance().getRequestOrderDetailsDialogData().setProductName(productName);
    }

    protected void onAcceptPressed(String countStr, String priceStr) {
        var data = DialogDataModel.getInstance().getRequestOrderDetailsDialogData();
        try {
            data.setCount(Double.parseDouble(countStr));
            data.setPrice(Double.parseDouble(priceStr));
            data.setOk(true);
        } catch (NumberFormatException e) {
            data.setWasException(true);
            data.setOk(false);
        }
    }

    protected String getProductName() {
        return DialogDataModel.getInstance().getRequestOrderDetailsDialogData().getProductName();
    }
}
