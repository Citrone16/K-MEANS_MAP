import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiServer {
	int PORT = 8080;

	public MultiServer(int port) {
		this.PORT = port;
		run();
	}

	void run() {
		ServerSocket s = null;
        try{
            s = new ServerSocket(PORT);
            System.out.println("Server is listening on port " + PORT);
            try{
                while(true) {
                    Socket socket = s.accept();
                    System.out.println("New client connected");
                    try{
                        new ServerOneClient(socket);
                    }catch(IOException e){
                        socket.close();
                    }
                }
            }catch(IOException e){
                System.out.println(e.getMessage());
            }
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

	public static void main(String[] args) {
		MultiServer server = new MultiServer(8080);
	}
}
