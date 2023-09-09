package app;

import javafx.scene.control.Alert;

/**
 * Questa classe gestisce i messaggi di errore e informativi.
 */
public class Alerts {

    /**
     * Mostra una finestra di errore.
     *
     * @param text Messaggio di errore.
     */
    public static void alertError(String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore");
        alert.setContentText(text);
        alert.showAndWait();
    }

    /**
     * Mostra una finestra informativa.
     *
     * @param text Messaggio informativo.
     */
    public static void alertInformation(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informazione");
        alert.setContentText(text);
        alert.showAndWait();
    }
}
