package app;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * Questa classe gestisce l'interfaccia utente per l'inserimento dei parametri del server.
 */
public class ConnectionScene {
    private String address;
    private String port;
    private static ConnectionScene instance = null;
    private Stage stage;
    private TextField addressText, portText;

    /**
     * Restituisce il singleton della classe.
     *
     * @return Singleton della classe.
     */
    public static ConnectionScene getConnectionScene() {
        if (instance == null)
            instance = new ConnectionScene();
        return instance;
    }

    /**
     * Costruttore privato.
     */
    private ConnectionScene() {
    }

    /**
     * Costruisce l'interfaccia per l'inserimento dei parametri.
     *
     * @param primaryStage Stage su cui inserire la scena.
     */
    public void buildConnectionScene(Stage primaryStage) {
        stage = primaryStage;

        AnchorPane anchorPane = new AnchorPane();

        // TextField
        addressText = new TextField();
        portText = new TextField();

        addressText.setPromptText("Inserisci l'indirizzo");
        portText.setPromptText("Inserisci il numero di porta");

        addressText.setAlignment(Pos.CENTER);
        portText.setAlignment(Pos.CENTER);

        addressText.setFocusTraversable(false);
        portText.setFocusTraversable(false);

        addressText.setPrefWidth(180);
        portText.setPrefWidth(180);

        AnchorPane.setTopAnchor(addressText, (double) 100);
        AnchorPane.setLeftAnchor(addressText, (double) 105);
        AnchorPane.setTopAnchor(portText, (double) 150);
        AnchorPane.setLeftAnchor(portText, (double) 105);

        // Buttons
        Button submitButton = new Button("Connetti");
        submitButton.setAlignment(Pos.CENTER);
        submitButton.setFocusTraversable(false);

        AnchorPane.setTopAnchor(submitButton, (double) 250);
        AnchorPane.setLeftAnchor(submitButton, (double) 165);

        submitButton.setOnAction(e -> {
            address = addressText.getText();
            port = portText.getText();

            if (check()) {
                ChoiceScene.getChoiceScene().buildChoiceScene();
                stage.close();
            }
        });

        Button defaultButton = new Button("Usa credenziali di default");
        defaultButton.setAlignment(Pos.CENTER);
        defaultButton.setFocusTraversable(false);

        AnchorPane.setTopAnchor(defaultButton, (double) 200);
        AnchorPane.setLeftAnchor(defaultButton, (double) 120);

        defaultButton.setOnAction(e -> {
            addressText.setText("localhost");
            portText.setText("8080");
        });

        anchorPane.getChildren().addAll(addressText, portText, submitButton, defaultButton);

        Scene scene = new Scene(anchorPane, 400, 400);
        String css = Objects.requireNonNull(this.getClass().getResource("style.css")).toExternalForm();
        scene.getStylesheets().add(css);

        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("K-MEANS");
        stage.show();
    }

    /**
     * Restituisce l'indirizzo inserito nel TextField addressText.
     *
     * @return Indirizzo presente nel TextField addressText.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Restituisce la porta inserita nel TextField portText.
     *
     * @return Porta presente nel TextField portText.
     */
    public String getPort() {
        return port;
    }

    /**
     * Controlla se entrambi i campi siano stati compilati.
     *
     * @return Booleano che indica se entrambi i campi sono compilati o meno.
     */
    private boolean check() {
        if (address.isEmpty() || port.isEmpty()) {
            Alerts.alertError("Attenzione, è necessario compilare entrambi i campi");
            return false;
        }
        try {
            int portNumber = Integer.parseInt(port);
            if (portNumber <= 0 || portNumber > 65535) {
                Alerts.alertError("Numero di porta non valido. Inserire un valore tra 1 e 65535.");
                return false;
            }
        } catch (NumberFormatException e) {
            Alerts.alertError("Attenzione, assicurati che il valore inserito sia un numero intero");
            return false;
        }
        return true;
    }
}
