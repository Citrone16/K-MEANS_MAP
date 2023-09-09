import java.net.Socket;
import java.io.*;
import java.sql.SQLException;

import database.DatabaseConnectionException;
import database.EmptySetException;
import database.NoValueException;
import mining.KMeansMiner;
import data.Data;
import data.OutOfRangeSampleSize;

/**
 * Questa classe gestisce la connessione con un nuovo client.
 */
public class ServerOneClient extends Thread {
    private final Socket socket;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    private KMeansMiner kmeans;

    /**
     * Costruttore che inizializza la socket e gli stream di input e output.
     *
     * @param socket Descrittore della socket.
     * @throws IOException Se si verifica un errore di input/output.
     */
    public ServerOneClient(Socket socket) throws IOException {
        this.socket = socket;
        in = new ObjectInputStream(socket.getInputStream());
        out = new ObjectOutputStream(socket.getOutputStream());
        start();
    }

    /**
     * Esegue le richieste del client.
     */
    @Override
    public void run() {
        Data data;

        String directoryName = "savings";
        File directory = new File(directoryName);
        if (!directory.exists()) {
            directory.mkdir();
        }
        String archivePath = directory.getAbsolutePath() + "\\";
        try {
            while (true) {
                int choice = (Integer) in.readObject();
				switch (choice) {
					case 1 -> {
						try {
							String table = (String) in.readObject();
							int n = (Integer) in.readObject();
							data = new Data(table);
							kmeans = new KMeansMiner(n);
							int numIter = kmeans.kmeans(data);
							String clusters = kmeans.getC().toString(data);
							out.writeObject("OK");
							out.writeObject("Numero di iterazioni: " + numIter + "\n\n" + clusters);
						} catch (IOException | ClassNotFoundException | NoValueException | DatabaseConnectionException |
								 SQLException | EmptySetException e) {
							out.writeObject("Errore");
							out.writeObject("Errore");
						}
					}
					case 2 -> {
						try {
							String fileName = (String) in.readObject();
							kmeans.salva(archivePath + fileName + ".dat");
							out.writeObject("OK");
						} catch (IOException e) {
							out.writeObject("File error: " + e.getMessage());
						}
					}
					case 3 -> {
						try {
							String fileName = (String) in.readObject();
							try {
								kmeans = new KMeansMiner(archivePath + fileName + ".dat");
								out.writeObject("OK");
								out.writeObject(kmeans.getC().toString());
							} catch (FileNotFoundException e) {
								out.writeObject("File not found.");
								out.writeObject(("File not found."));
							} catch (Exception e) {
								out.writeObject("Error: " + e.getMessage());
								out.writeObject("Error: " + e.getMessage());
							}
						} catch (IOException | ClassNotFoundException e) {
							System.out.println(e.getMessage());
						}
					}
					default -> {
					}
				}
            }
        } catch (IOException | ClassNotFoundException | OutOfRangeSampleSize e) {
            System.out.println(e.getMessage());
        }
    }
}
