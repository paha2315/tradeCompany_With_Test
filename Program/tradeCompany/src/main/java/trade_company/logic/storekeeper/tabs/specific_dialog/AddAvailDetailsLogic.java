package trade_company.logic.storekeeper.tabs.specific_dialog;

import trade_company.logic.sql_object.Product;
import trade_company.models.DialogDataModel;
import trade_company.models.UserDataModel;

public class AddAvailDetailsLogic {
    public AddAvailDetailsLogic() {
        DialogDataModel.getInstance().getAddProductDetailsDialogData().setOk(false);
    }

    public void init(String productName) {
        DialogDataModel.getInstance().getAddProductDetailsDialogData().setProductName(productName);
    }

    protected void onAcceptPressed(String packType, String packWidthStr, String packHeightStr, String packDepthStr,
                                   String sectionStr, String stackStr) {

        var data = DialogDataModel.getInstance().getAddProductDetailsDialogData();
        try {
            data.setPackageType(packType);
            data.setPackageWidth(Double.parseDouble(packWidthStr));
            data.setPackageHeight(Double.parseDouble(packHeightStr));
            data.setPackageDepth(Double.parseDouble(packDepthStr));
            data.setSection(Integer.parseInt(sectionStr));
            data.setStack(Integer.parseInt(stackStr));
            data.setOk(true);
        } catch (NumberFormatException e) {
            data.setWasException(true);
            data.setOk(false);
        }
    }

    protected String getProductName() {
        return DialogDataModel.getInstance().getAddProductDetailsDialogData().getProductName();
    }
}
