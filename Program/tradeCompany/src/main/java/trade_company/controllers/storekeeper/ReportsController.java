package trade_company.controllers.storekeeper;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.text.Text;
import trade_company.logic.storekeeper.ReportsLogic;
import trade_company.models.Model;
import trade_company.views.diagram.ReportDiagramTypes;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class ReportsController extends ReportsLogic {
    @FXML
    private ComboBox<String> combobox_diagramSelector;
    @FXML
    private DatePicker datepicker_end;
    @FXML
    private DatePicker datepicker_start;
    @FXML
    private BarChart<String, Double> barchart;
    @FXML
    private PieChart piechart;
    @FXML
    private Text text_money;
    @FXML
    private Button button_makeMH20;

    @FXML
    public void initialize() {
        initSelector();
        initGui();
        setupListeners();
    }

    protected void initSelector() {
        combobox_diagramSelector.getItems().setAll(diagramTypesText);
        combobox_diagramSelector.getSelectionModel().select(0);
    }

    protected void initGui() {
        switchDiagrams(true);

        barchart.setAnimated(false);
        piechart.setAnimated(false);
    }

    protected void setupListeners() {
        combobox_diagramSelector.getSelectionModel().selectedIndexProperty().addListener((observable, oldVal, newVal) -> {
            var label = combobox_diagramSelector.getItems().get((Integer) newVal);
            switchDiagrams(ReportDiagramTypes.valueOfLabel(label) == ReportDiagramTypes.COUNT_SALES);
            diagramUpdate();
        });

        datepicker_start.setOnAction(event -> {
            dateStart = datepicker_start.getValue();
            diagramUpdate();
        });
        datepicker_end.setOnAction(event -> {
            dateEnd = datepicker_end.getValue();
            diagramUpdate();
        });

        button_makeMH20.setOnAction(actionEvent -> {
            try {
                makeMH20();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void switchDiagrams(boolean barchart_) {
        barchart.setVisible(barchart_);
        piechart.setVisible(!barchart_);
        if (barchart_) {
            barchart.toFront();
            piechart.toBack();
            text_money.setVisible(false);
        } else {
            barchart.toBack();
            piechart.toFront();
            text_money.setVisible(true);
        }
    }

    protected void diagramUpdate() {
        if (dateStart == null || dateEnd == null)
            return;
        if (dateStart.isAfter(dateEnd)) {
            Model.getInstance().getWindowFactory().showAlert(Alert.AlertType.WARNING, "Выбор даты", "", "Начальная дата должна быть меньше конечной.");
            return;
        }
        barchart.getData().clear();
        barchart.getData().add(getBarchartData());

        piechart.getData().clear();
        boolean got = ReportDiagramTypes.valueOfLabel(combobox_diagramSelector.getSelectionModel().getSelectedItem()) == ReportDiagramTypes.MONEY_MASS_GET;
        piechart.getData().addAll(getPiechartData(got));

        NumberFormat formatter = new DecimalFormat("###,###.##");
        text_money.setText("Всего " + (got ? "получено" : "потрачено") + " денег за отчётный период: "
                           + formatter.format(money) + "руб");
    }
}
