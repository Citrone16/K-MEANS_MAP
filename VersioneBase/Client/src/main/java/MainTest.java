import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import exception.ServerException;

import keyboardinput.Keyboard;

public class MainTest {

	/**
	 * @param args
	 */
	private final ObjectOutputStream out;
	private final ObjectInputStream in; // stream con richieste del client

	public MainTest(String ip, int port) throws IOException {
		InetAddress addr = InetAddress.getByName(ip); // ip
		System.out.println("addr = " + addr);
		Socket socket = new Socket(addr, port); // Port
		System.out.println(socket);

		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());
		; // stream con richieste del client
	}

	private int menu() {
		int answer;
		System.out.println("Scegli una opzione");
		do {
			System.out.println("(1) Carica Cluster da File");
			System.out.println("(2) Carica Dati");
			System.out.print("Risposta:");
			answer = Keyboard.readInt();
		} while (answer <= 0 || answer > 2);
		return answer;

	}

	private String learningFromFile() throws SocketException, ServerException, IOException, ClassNotFoundException {
		out.writeObject(3);

		System.out.print("Nome file:");
		String tabName = Keyboard.readString();
		out.writeObject(tabName);
		String result = (String) in.readObject();
		if (result.equals("OK"))
			return (String) in.readObject();
		else
			throw new ServerException(result);

	}

	private void storeTableFromDb() throws ServerException, IOException, ClassNotFoundException {
		out.writeObject(0);
		System.out.print("Nome tabella:");
		String tabName = Keyboard.readString();
		out.writeObject(tabName);
		String result = (String) in.readObject();
		if (!result.equals("OK"))
			throw new ServerException(result);

	}

	private String learningFromDbTable() throws ServerException, IOException, ClassNotFoundException {
		out.writeObject(1);
		System.out.print("Numero di cluster:");
		int k = Keyboard.readInt();
		out.writeObject(k);
		String result = (String) in.readObject();
		if (result.equals("OK")) {
			System.out.println("Clustering output:" + in.readObject());
			return (String) in.readObject();
		} else
			throw new ServerException(result);

	}

	private void storeClusterInFile() throws ServerException, IOException, ClassNotFoundException {
		out.writeObject(2);

		System.out.println("Inserire il nome del file in cui salvare i cluster trovati:");
		String fileName = Keyboard.readString();
		out.writeObject(fileName);

		String result = (String) in.readObject();
		if (!result.equals("OK")) {
			throw new ServerException(result);
		}

	}

	public static void main(String[] args) {
		String ip = args[0];
		int port = Integer.parseInt(args[1]);
		MainTest main = null;
		try {
			main = new MainTest(ip, port);
		} catch (IOException e) {
			System.out.println(e);
			return;
		}

		do {
			int menuAnswer = main.menu();
			switch (menuAnswer) {
				case 1:
					try {
						String kmeans = main.learningFromFile();
						System.out.println(kmeans);
					} catch (IOException | ClassNotFoundException e) {
						System.out.println(e);
						return;
					} catch (ServerException e) {
						System.out.println(e.getMessage());
					}
					break;
				case 2: // learning from db

					while (true) {
						try {
							main.storeTableFromDb();
							break; // esce fuori dal while
						}

						catch (ClassNotFoundException | IOException e) {
							System.out.println(e);
							return;
						} catch (ServerException e) {
							System.out.println(e.getMessage());
						}
					} // end while [viene fuori dal while con un db (in alternativa il programma
					// termina)

					char answer = 'y';// itera per learning al variare di k
					do {
						try {
							main.learningFromDbTable();

							main.storeClusterInFile();
							System.out.println("File salvato.");
						} catch (ClassNotFoundException | IOException e) {
							System.out.println(e);
							return;
						} catch (ServerException e) {
							System.out.println(e.getMessage());
						}
						System.out.print("Vuoi ripetere l'esecuzione?(y/n)");
						answer = Keyboard.readChar();
					} while (answer == 'y');
					break; // fine case 2
				default:
					System.out.println("Opzione non valida!");
			}

			System.out.print("Vuoi scegliere una nuova operazione da menu?(y/n)");
			if (Keyboard.readChar() != 'y')
				break;
		} while (true);
	}
}
