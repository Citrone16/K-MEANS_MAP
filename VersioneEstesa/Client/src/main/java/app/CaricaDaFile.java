package app;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * Questa classe gestisce l'interfaccia utente per l'inserimento dei dati della richiesta da inviare al server.
 */
public class CaricaDaFile {
    private Stage stage;
    private TextField fileTextField;
    private TextArea clustersText;
    private static CaricaDaFile instance = null;
    private static QueryClass queryClass;

    /**
     * Restituisce il singleton della classe.
     *
     * @return Singleton della classe.
     */
    public static CaricaDaFile getCaricaDaFile() {
        if (instance == null) {
            instance = new CaricaDaFile();
            queryClass = new QueryClass();
        }
        return instance;
    }

    /**
     * Costruttore privato.
     */
    private CaricaDaFile() {
    }

    /**
     * Costruisce l'interfaccia per l'inserimento dei dati da inviare al server.
     */
    void buildCaricaDaFile() {
        stage = new Stage();

        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(10));

        //Label
        Label fileLabel = new Label("Nome file:");
        //TextField
        fileTextField = new TextField();

        fileTextField.setPrefWidth(200);

        //TextArea
        clustersText = new TextArea();
        clustersText.setEditable(false);

        //Buttons
        Button searchButton = new Button("Avvia ricerca");
        Button backButton = new Button("Indietro");

        searchButton.setOnAction(e -> {
            try {
                queryClass.initializeConnection(ConnectionScene.getConnectionScene().getAddress(), ConnectionScene.getConnectionScene().getPort());
                if (check()) {
                    if (!queryClass.learningFromFile()) {
                        clustersText.setText("");
                        Alerts.alertError("Errore nell'elaborazione della richiesta. Assicurarsi che il nome del file sia corretto.");
                    }
                }
            } catch (IOException | ClassNotFoundException e1) {
                Alerts.alertError("Connessione al server non riuscita.");
            }
        });

        backButton.setOnAction(e -> {
            queryClass.closeConnection();
            stage.close();
            ChoiceScene.getChoiceScene().buildChoiceScene();
        });

        //Creazione del layout con GridPane
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        gridPane.add(fileLabel, 0, 0);
        gridPane.add(fileTextField, 1, 0);
        gridPane.add(searchButton, 0, 1, 2, 1);

        clustersText.setPrefRowCount(20);
        gridPane.add(clustersText, 0, 2, 3, 1);

        GridPane.setHalignment(searchButton, HPos.CENTER);

        gridPane.setAlignment(Pos.CENTER);

        VBox vbox = new VBox(gridPane, backButton);
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER);

        borderPane.setTop(vbox);
        BorderPane.setAlignment(vbox, Pos.TOP_CENTER);

        StackPane backButtonContainer = new StackPane(backButton);
        backButtonContainer.setAlignment(Pos.BOTTOM_RIGHT);
        borderPane.setBottom(backButtonContainer);
        BorderPane.setAlignment(backButtonContainer, Pos.BOTTOM_RIGHT);
        BorderPane.setMargin(backButtonContainer, new Insets(10));

        Scene scene = new Scene(borderPane, 500, 600);
        String css = Objects.requireNonNull(this.getClass().getResource("style.css")).toExternalForm();
        scene.getStylesheets().add(css);

        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("K-MEANS");
        stage.show();
    }

    /**
     * Verifica che il campo fileTextField sia stato compilato.
     *
     * @return Booleano che inidica se il campo è stato compilato o meno.
     */
    private boolean check() {
        if (fileTextField.getText().isEmpty()) {
            Alerts.alertError("Attenzione, compilare il campo della tabella");
            return false;
        }
        return true;
    }

    /**
     * Restituisce il TextField del file.
     *
     * @return TextField del file.
     */
    public TextField getFileTextField() {
        return fileTextField;
    }

    /**
     * Restituisce la TextArea della tabella dei cluster.
     *
     * @return TextArea della tabella dei cluster.
     */
    public TextArea getClustersText() {
        return clustersText;
    }
}