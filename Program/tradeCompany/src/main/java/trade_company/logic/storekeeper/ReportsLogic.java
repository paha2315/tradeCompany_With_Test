package trade_company.logic.storekeeper;

import SQL.SSQLController;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import trade_company.logic.sql_object.Availability;
import trade_company.logic.sql_object.Containers.AvailabilityContainer;
import trade_company.logic.sql_object.Document;
import trade_company.models.Model;
import trade_company.models.UserDataModel;
import trade_company.report.reportClass;
import trade_company.views.diagram.ReportDiagramTypes;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

import static trade_company.report.reportClass.requestSaveFolderFromUser;

public class ReportsLogic {
    protected ArrayList<String> diagramTypesText;
    protected LocalDate dateStart;
    protected LocalDate dateEnd;
    protected double money;

    public ReportsLogic() {
        diagramTypesText = new ArrayList<>();
        for (var item : ReportDiagramTypes.values())
            diagramTypesText.add(item.label);
        dateStart = null;
        dateEnd = null;
    }

    protected javafx.scene.chart.XYChart.Series<String, Double> getBarchartData() {
        XYChart.Series<String, Double> set1 = new XYChart.Series<>();

        try {
            var result = SSQLController.Query("SELECT p.Name, sum(-al.Count) sold FROM availability_link al\n" +
                    "JOIN document d ON d.ID_Document=al.ID_Document\n" +
                    "\tAND d.Date BETWEEN '" + Document.getFormattedDate(dateStart) + "' AND '" + Document.getFormattedDate(dateEnd) + "'\n" +
                    "    AND al.Count<0\n" +
                    "JOIN availability a ON al.ID_Availability=a.ID_Availability\n" +
                    "JOIN product p ON p.ID_Product=a.ID_Product\n" +
                    "GROUP BY p.Name;");
            while (result.next()) {
                set1.getData().add(new XYChart.Data<>(
                        result.getString("Name"),
                        result.getDouble("Sold")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return set1;
    }

    protected ArrayList<PieChart.Data> getPiechartData(boolean got) {
        var list = new ArrayList<PieChart.Data>();

        money = 0.;

        String sign = got ? "-" : "";
        String signCheck = got ? "<0" : ">0";

        try {
            var result = SSQLController.Query("SELECT p.Name, sum(" + sign + "al.Count * a.Price) money FROM availability_link al\n" +
                    "JOIN document d ON d.ID_Document=al.ID_Document\n" +
                    "\tAND d.Date BETWEEN '" + Document.getFormattedDate(dateStart) + "' AND '" + Document.getFormattedDate(dateEnd) + "'\n" +
                    "    AND al.Count" + signCheck + "\n" +
                    "JOIN availability a ON al.ID_Availability=a.ID_Availability\n" +
                    "JOIN product p ON p.ID_Product=a.ID_Product\n" +
                    "GROUP BY p.Name;");
            while (result.next()) {
                list.add(new PieChart.Data(
                        result.getString("Name"),
                        result.getDouble("money")));
                money += result.getDouble("money");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    protected void makeMH20() throws IOException {
        if (dateStart == null || dateEnd == null || dateStart.isAfter(dateEnd)) {
            Model.getInstance().getWindowFactory().showAlert(Alert.AlertType.WARNING, "Выбор даты", "", "Начальная дата должна быть меньше конечной.");
            return;
        }

        String formName = "МХ-20";
        var formWriter = new reportClass();

        String folder = requestSaveFolderFromUser(formName);
        if (folder == null) {
            Model.getInstance().getWindowFactory().showAlert(Alert.AlertType.WARNING, "Сохранение отчёта о движении ТМЦ", "", "Пользователь отказался от сохранения файлов.");
            return;
        }

        AvailabilityContainer.setAll("WHERE ID_Warehouse = " + UserDataModel.getInstance().getWarehouse().getId());
        ArrayList<Availability> availabilities = AvailabilityContainer.getList();
        ArrayList<Double> beginning = new ArrayList<>(), coming = new ArrayList<>(), expenditure = new ArrayList<>();
        for (var avail : availabilities) {
            try {
                ResultSet result = SSQLController.Query("SELECT SUM(al.Count) sm FROM availability_link al\n" +
                        "JOIN document d ON al.ID_Document = d.ID_Document\n" +
                        "WHERE al.ID_Availability = " + avail.getId() + " AND d.Date between '" + Document.getFormattedDate(dateStart) + "' AND '" + Document.getFormattedDate(LocalDate.now()) + "';");
                result.next();
                beginning.add(avail.getCount() - result.getDouble("sm"));
                result = SSQLController.Query("SELECT SUM(al.Count) sm FROM availability_link al\n" +
                        "JOIN document d ON al.ID_Document = d.ID_Document\n" +
                        "WHERE al.ID_Availability = " + avail.getId() + " AND al.Count>0 AND d.Date between '" + Document.getFormattedDate(dateStart) + "' AND '" + Document.getFormattedDate(dateEnd) + "';");
                result.next();
                coming.add(result.getDouble("sm"));
                result = SSQLController.Query("SELECT SUM(al.Count) sm FROM availability_link al\n" +
                        "JOIN document d ON al.ID_Document = d.ID_Document\n" +
                        "WHERE al.ID_Availability = " + avail.getId() + " AND al.Count<0 AND d.Date between '" + Document.getFormattedDate(dateStart) + "' AND '" + Document.getFormattedDate(dateEnd) + "';");
                result.next();
                expenditure.add(-result.getDouble("sm"));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        int orderNumber = 1;

        String filePath = folder + "\\" + formName + "_" + orderNumber + "_" + Document.getFormattedDate(LocalDate.now());

        formWriter.makeMH20(orderNumber, UserDataModel.getInstance().getWarehouse(),
                UserDataModel.getInstance().getPerson(),
                availabilities, beginning, coming, expenditure,
                Date.from(dateStart.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                Date.from(dateEnd.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                filePath);
    }
}
