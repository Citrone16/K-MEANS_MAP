

import java.net.Socket;
import java.io.*;
import mining.KMeansMiner;
import data.Data;
import data.OutOfRangeSampleSize;

public class ServerOneClient extends Thread {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private KMeansMiner kmeans;

    public ServerOneClient(Socket socket) throws IOException {
        try {
            this.socket = socket;
            this.in = new ObjectInputStream(socket.getInputStream());
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.start();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        Data data = null;
        try {
            while (true) {
                int choice = (Integer) in.readObject();
                switch (choice) {
                    case 0:
                        try {
                            String table = (String) in.readObject();
                            data = new Data(table);
                            out.writeObject("OK");
                        } catch (Exception e) {
                            out.writeObject("Table error: " + e.getMessage());
                            e.printStackTrace();
                        }
                        break;
                    case 1:
                        try {
                            int n = (Integer) in.readObject();
                            kmeans = new KMeansMiner(n);
                            int numIter = kmeans.kmeans(data);
                            String clusters = kmeans.getC().toString(data);
                            out.writeObject("OK");
                            out.writeObject("Numero di iterazioni: " + numIter + "\n\n" + clusters);
                            out.writeObject(clusters);
                        } catch (OutOfRangeSampleSize e) {
                            out.writeObject("Cluster error: " + e.getMessage());
                        }
                        break;
                    case 2:
                        try {
                            String fileName = (String) in.readObject();
                            kmeans.salva(fileName);
                            out.writeObject("OK");
                        } catch (IOException e) {
                            out.writeObject("File error: " + e.getMessage());
                        }
                        break;
                    case 3:
                        try {
                            String tableName = (String) in.readObject();
                            kmeans = new KMeansMiner(tableName);
                            out.writeObject("OK");
                            out.writeObject(kmeans.getC().toString());
                        } catch (FileNotFoundException e) {
                            out.writeObject("File not found.");
                        } catch (Exception e) {
                            out.writeObject("Error: " + e.getMessage());
                            e.printStackTrace();
                        }
                        break;
                    default:
                        break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
