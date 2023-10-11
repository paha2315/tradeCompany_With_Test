package trade_company.models.data_objects;

public class AddAvailDetailsDialogData {
    boolean ok_pressed;
    boolean was_exception;
    String productName;
    String packageType;
    double packageWidth, packageHeight, packageDepth;
    int section, stack;

    public AddAvailDetailsDialogData() {
        ok_pressed = false;
        was_exception = false;
    }

    public AddAvailDetailsDialogData(String productName) {
        setProductName(productName);
    }

    public boolean isOk() {
        return ok_pressed;
    }

    public void setOk(boolean flag) {
        ok_pressed = flag;
    }

    public void setWasException(boolean flag) {
        was_exception = true;
    }

    public boolean wasException() {
        return was_exception;
    }


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPackageType() {
        return packageType;
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }

    public double getPackageWidth() {
        return packageWidth;
    }

    public void setPackageWidth(double packageWidth) {
        this.packageWidth = packageWidth;
    }

    public double getPackageHeight() {
        return packageHeight;
    }

    public void setPackageHeight(double packageHeight) {
        this.packageHeight = packageHeight;
    }

    public double getPackageDepth() {
        return packageDepth;
    }

    public void setPackageDepth(double packageDepth) {
        this.packageDepth = packageDepth;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }

    public int getStack() {
        return stack;
    }

    public void setStack(int stack) {
        this.stack = stack;
    }

}
