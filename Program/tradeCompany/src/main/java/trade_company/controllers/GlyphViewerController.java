package trade_company.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class GlyphViewerController {
    @FXML
    private VBox vbox;
    @FXML
    private Label glyph_name;
    @FXML
    private FontAwesomeIconView glyph;

    @FXML
    public void initialize() {
        vbox.setSpacing(10);

        final int GLYPHS_PER_ROW = 18;
        int column = GLYPHS_PER_ROW;

        HBox hbox = null;
        for (var icon : FontAwesomeIcon.values()) {
            if (column == GLYPHS_PER_ROW) {
                column = 0;
                if (hbox != null)
                    vbox.getChildren().add(hbox);
                hbox = new HBox();
                hbox.setSpacing(5);
            }

            FontAwesomeIconView icon_view = new FontAwesomeIconView(icon, "4em");
            icon_view.setOnMouseClicked(mouseEvent -> {
                glyph.setText(icon_view.getText());
                glyph_name.setText(icon_view.getGlyphName());
            });
            StackPane pane = new StackPane();
            pane.setPrefSize(64.0, 64.0);
            pane.getChildren().add(icon_view);

            StackPane.setAlignment(icon_view, Pos.CENTER);

            hbox.getChildren().add(pane);

            column++;
        }
        vbox.getChildren().add(hbox);
    }
}
