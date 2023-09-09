package app;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * Questa classe gestisce l'interfaccia utente per l'inserimento dei dati della richiesta da inviare al server.
 */
public class CaricaDati {
    private Stage stage;
    private TextField tableTextField, nClusterTextField, fileTextField;
    private TextArea clustersText;
    private static CaricaDati instance = null;
    private static QueryClass queryClass;

    /**
     * Restituisce il singleton della classe.
     *
     * @return Singleton della classe.
     */
    public static CaricaDati getCaricaDati() {
        if (instance == null) {
            instance = new CaricaDati();
            queryClass = new QueryClass();
        }
        return instance;
    }

    /**
     * Costruttore privato.
     */
    private CaricaDati() {
    }

    /**
     * Costruisce l'interfaccia per l'inserimento dei dati da inviare al server.
     */
    void buildCaricaDati() {
        stage = new Stage();

        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(10));

        //Labels
        Label tableLabel = new Label("Nome tabella:");
        Label nClusterLabel = new Label("Numero di cluster:");
        Label fileLabel = new Label("Nome del file in cui salvare i cluster: ");

        //TextField
        tableTextField = new TextField();
        nClusterTextField = new TextField();
        fileTextField = new TextField();

        //TextArea
        clustersText = new TextArea();
        clustersText.setEditable(false);

        //Buttons
        Button searchButton = new Button("Avvia ricerca");
        Button saveButton = new Button("Salva");
        Button backButton = new Button("Indietro");

        searchButton.setOnAction(e -> {
            try {
                queryClass.initializeConnection(ConnectionScene.getConnectionScene().getAddress(), ConnectionScene.getConnectionScene().getPort());
                if(check_1()) {
                    if (!queryClass.storeTableFromDb()) {
                        clustersText.setText("");
                        Alerts.alertError("Errore nell'elaborazione della richiesta.");
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

        saveButton.setOnAction(e -> {
            try {
                queryClass.initializeConnection(ConnectionScene.getConnectionScene().getAddress(), ConnectionScene.getConnectionScene().getPort());
                if(check_2()) {
                    if (!queryClass.storeClusterInFile()) {
                        Alerts.alertError("Errore nell'elaborazione della richiesta.");
                    } else {
                        Alerts.alertInformation("File salvato.");
                    }
                }
            } catch (IOException | ClassNotFoundException e1) {
                Alerts.alertError("Connessione al server non riuscita.");
            }
        });

        //Creazione del layout con GridPane
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        fileTextField.setPrefWidth(200);

        gridPane.add(tableLabel, 0, 0);
        gridPane.add(tableTextField, 1, 0);
        gridPane.add(nClusterLabel, 0, 1);
        gridPane.add(nClusterTextField, 1, 1);
        gridPane.add(searchButton, 0, 2, 2, 1); // Colspan 2 per far occupare due colonne

        clustersText.setPrefRowCount(20); // Sostituisci 20 con il numero di righe desiderato
        gridPane.add(clustersText, 0, 3, 3, 1); // Colspan 3 per far occupare tre colonne

        gridPane.add(fileLabel, 0, 5);
        gridPane.add(fileTextField, 1, 5);
        gridPane.add(saveButton, 0, 6, 2, 1);

        GridPane.setHalignment(searchButton, HPos.CENTER);
        GridPane.setHalignment(saveButton, HPos.CENTER);

        gridPane.setAlignment(Pos.CENTER);

        VBox vbox = new VBox(gridPane, backButton);
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER);

        borderPane.setTop(vbox);
        BorderPane.setAlignment(vbox, Pos.TOP_CENTER);

        BorderPane.setAlignment(backButton, Pos.BOTTOM_RIGHT);
        BorderPane.setMargin(backButton, new Insets(0, 20, 20, 0));
        borderPane.setBottom(backButton);

        Scene scene = new Scene(borderPane, 500, 600);
        String css = Objects.requireNonNull(this.getClass().getResource("style.css")).toExternalForm();
        scene.getStylesheets().add(css);

        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("K-MEANS");
        stage.show();
    }

    /**
     * Verifica che i campi per la ricerca non siano vuoti che abbiano il giusto formato.
     *
     * @return Booleano che indica se tutti i campi sono stati correttamente compilati o meno.
     */
    private boolean check_1() {
        int n;

        if (tableTextField.getText().isEmpty() || nClusterTextField.getText().isEmpty()) {
            Alerts.alertError("Attenzione, compilare entrambi i campi");
            return false;
        }

        try {
            n = Integer.parseInt(nClusterTextField.getText());
            if (n <= 1) {
                Alerts.alertError("Attenzione, il numero di clusters deve essere maggiore di 1");
                return false;
            }
        } catch (NumberFormatException e) {
            Alerts.alertError("Attenzione, assicurati che il valore inserito sia un numero intero");
            return false;
        }

        return true;
    }

    /**
     * Verifica che il campo fileTextField sia stato compilato.
     *
     * @return Booleano che inidica se il campo è stato compilato o meno.
     */
    private boolean check_2() {
        if(fileTextField.getText().isEmpty()) {
            Alerts.alertError("Attenzione, compilare il campo");
            return false;
        }
        return true;
    }

    /**
     * Restituisce il TextField della tabella.
     *
     * @return TextField della tabella.
     */
    public TextField getTableTextField() {
        return tableTextField;
    }

    /**
     * Restituisce il TextField del numero di cluster.
     *
     * @return TextField del numero di cluster.
     */
    public TextField getClustersTextField() {
        return nClusterTextField;
    }

    /**
     * Restituisce il TextField del file.
     *
     * @return TextField del file.
     */
    public TextField getFileTextField(){
        return fileTextField;
    }

    /**
     * Restituisce la TextArea della tabella dei cluster.
     *
     * @return TextArea della tabella dei cluster.
     */
    public TextArea getClustersText(){
        return clustersText;
    }
}