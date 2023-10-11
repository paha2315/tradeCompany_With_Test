package trade_company.logic.administrator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import trade_company.logic.sql_object.Containers.PersonContainer;
import trade_company.logic.sql_object.Person;

public class UserListLogic {
    protected ObservableList<Person> personData = FXCollections.observableArrayList();

    public UserListLogic() {
        PersonContainer.setAll();
    }

    protected void initTableData() {
        personData = FXCollections.observableArrayList(PersonContainer.getList());
    }

    protected void refreshTable(String filter) {
        filter = filter.toUpperCase();
        PersonContainer.setAll();
        personData.clear();
        if (filter.equals(""))
            personData.addAll(PersonContainer.getList());
        else
            for (var person : PersonContainer.getList())
                if (person.getFirstName().toUpperCase().contains(filter)
                    || person.getSecondName().toUpperCase().contains(filter)
                    || person.getThirdName().toUpperCase().contains((filter))) {
                    personData.add(person);
                }
    }
}
