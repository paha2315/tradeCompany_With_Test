package trade_company.controllers.administrator;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import trade_company.logic.administrator.UserListLogic;
import trade_company.logic.sql_object.Person;

public class UserListController extends UserListLogic {
    @FXML
    private TableColumn<Person, String> column_userFirstName;
    @FXML
    private TableColumn<Person, String> column_userLogin;
    @FXML
    private TableColumn<Person, String> column_userPost;
    @FXML
    private TableColumn<Person, String> column_userSecondName;
    @FXML
    private TableColumn<Person, String> column_userThirdName;
    @FXML
    private TableView<Person> table_users;
    @FXML
    private TextField textfield_filterFIO;

    @FXML
    public void initialize() {
        initTable();
        setupListeners();
    }

    protected void initTable() {
        initTableData();
        column_userPost.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPost().getName()));
        column_userFirstName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFirstName()));
        column_userSecondName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSecondName()));
        column_userThirdName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getThirdName()));
        column_userLogin.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLogin()));
        table_users.setItems(personData);
    }

    protected void setupListeners() {
        textfield_filterFIO.textProperty().addListener((observable, oldVal, newVal) -> {
            if (!newVal.equals(oldVal)) {
                refreshTable(newVal);
            }
        });
    }
}
