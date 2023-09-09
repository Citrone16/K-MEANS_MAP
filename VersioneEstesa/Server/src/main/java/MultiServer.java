import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Questa classe rappresenta il server e gestisce le nuove connessioni con i client.
 */
public class MultiServer {
    private static int PORT = 8080;

    /**
     * Costruttore del MultiServer che inizializza la porta del server.
     *
     * @param port Porta del server.
     * @throws IOException Se si verifica un errore di input/output.
     */
    private MultiServer(int port) throws IOException {
        MultiServer.PORT = port;
        run();
    }

    /**
     * Metodo che gestisce le nuove connessioni al server creando un nuovo thread.
     *
     * @throws IOException Se si verifica un errore di input/output.
     */
    private void run() throws IOException, NullPointerException {
        try (ServerSocket s = new ServerSocket(PORT)) {
            System.out.println("Server avviato");
            while (true) {
                Socket socket = s.accept();
                System.out.println("Connessione di socket " + socket);
                System.out.println("Nuovo client connesso");
                try {
                    new ServerOneClient(socket);
                } catch (IOException e) {
                    socket.close();
                }
            }
        }
    }

    /**
     * Metodo di avvio del server.
     *
     * @param args Parametro non utilizzato.
     * @throws IOException Se si verifica un errore di input/output.
     */
    public static void main(String[] args) throws IOException {
        new MultiServer(PORT);
    }
}
