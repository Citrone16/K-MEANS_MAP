package app;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * Questa classe gestisce l'interfaccia utente per la scelta dell'operazione da far eseguire al server.
 */
public class ChoiceScene {
    private RadioButton option_1, option_2;
    private Stage stage;
    private static ChoiceScene instance = null;
    private static QueryClass queryClass;
    private ToggleGroup toggleGroup;

    /**
     * Restituisce il singleton della classe.
     *
     * @return Singleton della classe.
     */
    public static ChoiceScene getChoiceScene() {
        if (instance == null) {
            instance = new ChoiceScene();
            queryClass = new QueryClass();
        }
        return instance;
    }

    /**
     * Costruttore privato.
     */
    private ChoiceScene() {
    }

    /**
     * Metodo che costruisce l'interfaccia per l'utente.
     */
    void buildChoiceScene() {
        stage = new Stage();

        BorderPane borderPane = new BorderPane();
        VBox vbox = new VBox(20);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));

        //Label
        Label choiceLabel = new Label("Scegli un'opzione");
        choiceLabel.setId("choiceLabel");

        //RadioButtons
        option_1 = new RadioButton("Carica cluster da file");
        option_2 = new RadioButton("Carica dati");

        toggleGroup = new ToggleGroup();
        option_1.setToggleGroup(toggleGroup);
        option_2.setToggleGroup(toggleGroup);

        //Buttons
        Button backButton = new Button("Indietro");
        Button nextButton = new Button("Avanti");

        nextButton.setOnAction(e -> {
            RadioButton selectedRadioButton = (RadioButton) toggleGroup.getSelectedToggle();
            if (selectedRadioButton != null) {
                if (selectedRadioButton == option_1) {
                    System.out.println("Scelta: Carica cluster da file.");
                    CaricaDaFile.getCaricaDaFile().buildCaricaDaFile();
                } else if (selectedRadioButton == option_2) {
                    System.out.println("Scelta: Carica dati");
                    CaricaDati.getCaricaDati().buildCaricaDati();
                }
                stage.close();
            }
        });

        backButton.setOnAction(e -> {
            queryClass.closeConnection();
            stage.close();
            ConnectionScene.getConnectionScene().buildConnectionScene(new Stage());
        });

        vbox.getChildren().addAll(choiceLabel, option_1, option_2, nextButton);
        vbox.setPadding(new Insets(20));

        borderPane.setCenter(vbox);
        BorderPane.setAlignment(backButton, Pos.BOTTOM_RIGHT);
        BorderPane.setMargin(backButton, new Insets(0, 20, 20, 0));
        borderPane.setBottom(backButton);

        Scene scene = new Scene(borderPane, 400, 300);
        String css = Objects.requireNonNull(this.getClass().getResource("style.css")).toExternalForm();
        scene.getStylesheets().add(css);

        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("K-MEANS");
        stage.show();
    }
}