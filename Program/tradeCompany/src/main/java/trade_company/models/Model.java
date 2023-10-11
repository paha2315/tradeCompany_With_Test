package trade_company.models;

import trade_company.views.DialogFactory;
import trade_company.views.ViewFactory;
import trade_company.views.WindowFactory;

public class Model {
    private static volatile Model modelInstance;

    private final WindowFactory windowFactory;
    private final ViewFactory viewFactory;
    private final DialogFactory dialogFactory;

    private Model() {
        this.windowFactory = new WindowFactory();
        this.viewFactory = new ViewFactory();
        this.dialogFactory = new DialogFactory();
    }

    public static Model getInstance() {
        Model localInstance = modelInstance;
        if (localInstance == null) {
            synchronized (Model.class) {
                localInstance = modelInstance;
                if (localInstance == null) {
                    modelInstance = localInstance = new Model();
                }
            }
        }
        return localInstance;
    }

    public WindowFactory getWindowFactory() {
        return windowFactory;
    }

    public ViewFactory getViewFactory() {
        return viewFactory;
    }

    public DialogFactory getDialogFactory() {
        return dialogFactory;
    }
}
