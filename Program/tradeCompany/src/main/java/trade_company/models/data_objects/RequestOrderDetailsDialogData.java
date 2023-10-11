package trade_company.models.data_objects;

public class RequestOrderDetailsDialogData {

    boolean ok_pressed;
    boolean was_exception;
    String productName;
    double count;
    double price;

    public RequestOrderDetailsDialogData() {
        this.ok_pressed = false;
        this.was_exception = false;
        this.productName = "none";
        this.count = 0.;
        this.price = 0.;
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

    public double getCount() {
        return count;
    }

    public void setCount(double count) {
        this.count = count;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
