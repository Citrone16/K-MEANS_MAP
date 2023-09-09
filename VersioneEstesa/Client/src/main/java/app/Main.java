package app;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Questa classe avvia l'interfaccia grafica dell'utente per l'inserimento dei parametri del server.
 */
public class Main extends Application {

    /**
     * Avvia l'interfaccia grafica.
     *
     * @param primaryStage Stage principale del programma.
     */
    @Override
    public void start(Stage primaryStage) {

        ConnectionScene.getConnectionScene().buildConnectionScene(primaryStage);
    }

    /**
     * Avvia il programma client.
     *
     * @param args Stringa che contiene l'indirizzo e porta del server, se esso viene avviato tramite paramteri.
     */
    public static void main(String[] args) {
        launch(args);
    }
}






