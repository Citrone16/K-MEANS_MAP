package app;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Questa classe gestisce l'esecuzione della richiesta da parte del client.
 */
public class QueryClass {

    private Socket socket = null;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    /**
     * Metodo che inizializza la connessione con il server.
     *
     * @param address Indirizzo del server.
     * @param port    Porta del server.
     * @throws IOException Se si verifica un errore Input/Output.
     */
    void initializeConnection(String address, String port) throws IOException {
        if (socket == null) {
            InetAddress addr = null;

            try {
                addr = InetAddress.getByName(address);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            socket = new Socket(addr, Integer.parseInt(port));

            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            System.out.println("Apertura connessione");
        }
    }

    /**
     * Metodo che chiude la connessione con il server.
     */
    void closeConnection() {
        if (socket != null) {
            try {
                socket.close();
                System.out.println("Chiusura connessione");
                out.close();
                in.close();

                socket = null;
                out = null;
                in = null;
            } catch (IOException exception) {
                exception.printStackTrace();
            }

        }
    }

    /**
     * Raccoglie i valori compilati nell'interfaccia e li manda al server per effettuare la richiesta.
     * Legge i cluster da file.
     *
     * @return True se la richiesta è andata a buon fine, false altrimenti.
     * @throws IOException            Se si verifica un errore Input/Output.
     * @throws ClassNotFoundException Se non viene trovato l'oggetto durante la deserializzazione.
     */
    public boolean learningFromFile() throws IOException, ClassNotFoundException {

        int operazione = 3;

        String fileName = CaricaDaFile.getCaricaDaFile().getFileTextField().getText();

        System.out.println("Nome del file selezionato:" + fileName);

        boolean esito = true;
        try {
            out.writeObject(operazione);
            out.writeObject(fileName);

            String result = "";
            String clusters = "";

            try {
                result = (String) (in.readObject());
                clusters = (String) (in.readObject());
            } catch (EOFException e) {
                return false;
            }

            if (!result.equals("OK")) {
                esito = false;
            } else {
                CaricaDaFile.getCaricaDaFile().getClustersText().setText(clusters);
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return esito;
    }

    /**
     * Raccoglie i valori compilati nell'interfaccia e li manda al server per effettuare la richiesta.
     * Ricerca i cluster della tabella indicata nel Database.
     *
     * @return True se la richiesta è andata a buon fine, false altrimenti.
     * @throws IOException            Se si verifica un errore Input/Output.
     * @throws ClassNotFoundException Se non viene trovato l'oggetto durante la deserializzazione.
     */
    public boolean storeTableFromDb() throws IOException, ClassNotFoundException {
        int operazione = 1;

        String tableName = CaricaDati.getCaricaDati().getTableTextField().getText();
        int nClusters = Integer.parseInt(CaricaDati.getCaricaDati().getClustersTextField().getText());

        System.out.println("Nome della tabella selezionata:" + tableName);
        System.out.println("Numero di clusters selezionati:" + nClusters);

        boolean esito = true;
        try {
            out.writeObject(operazione);
            out.writeObject(tableName);
            out.writeObject(nClusters);

            String result = "";
            String clusters = "";

            try {
                result = (String) (in.readObject());
                clusters = (String) (in.readObject());
            } catch (EOFException e) {
                return false;
            }

            if (!result.equals("OK")) {
                esito = false;
            } else {
                CaricaDati.getCaricaDati().getClustersText().setText(clusters);
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        return esito;
    }

    /**
     * Raccoglie i valori compilati nell'interfaccia e li manda al server per effettuare la richiesta.
     * Salva i cluster precedentemente trovati in un file.
     *
     * @return True se la richiesta è andata a buon fine, false altrimenti.
     * @throws IOException            Se si verifica un errore Input/Output.
     * @throws ClassNotFoundException Se non viene trovato l'oggetto durante la deserializzazione.
     */
    public boolean storeClusterInFile() throws IOException, ClassNotFoundException {
        int operazione = 2;

        String fileName = CaricaDati.getCaricaDati().getFileTextField().getText();

        System.out.println("Nome del nuovo file:" + fileName);

        boolean esito = true;
        try {
            out.writeObject(operazione);
            out.writeObject(fileName);

            String result = "";

            try {
                result = (String) (in.readObject());
            } catch (EOFException e) {
                return false;
            }

            if (!result.equals("OK")) {
                esito = false;
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return esito;
    }
}
