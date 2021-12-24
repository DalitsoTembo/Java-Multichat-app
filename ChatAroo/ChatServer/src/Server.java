import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread {

    private int serverPort;
    private ArrayList<ServerOperation> serverOperations = new ArrayList<>();

    public Server(int serverPort) {
        this.serverPort = serverPort;
    }

    public List<ServerOperation> getAllServers(){
        return this.serverOperations;
    }

    @Override
    public void run(){
        try {
            ServerSocket serverSocket = new ServerSocket(serverPort);
            while(true){
                Socket socket = serverSocket.accept();
                System.out.println("New client has connected");
                ServerOperation serverOperation = new ServerOperation(this, socket);
                serverOperations.add(serverOperation);
                serverOperation.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
