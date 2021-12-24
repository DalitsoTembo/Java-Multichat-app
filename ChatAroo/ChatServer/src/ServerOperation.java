import java.io.*;
import java.net.Socket;
import java.util.List;


public class ServerOperation extends Thread {
    private Server server;
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private String login;

    public ServerOperation(Server server, Socket socket){
        try{
            this.server = server;
            this.socket = socket;
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        }catch(IOException exception){
            exception.printStackTrace();
        }
    }


    @Override
    public void run(){
        handleClient();
    }

//    HELPER FUNCTIONS START HERE
    public void handleClient(){
        String line;
        try{
            while((line = reader.readLine()) != null){
                String[] tokens = line.split(" ");
                if(tokens != null && tokens.length > 0){
                    String command = tokens[0];

                    if(command.equalsIgnoreCase("quit")){
                        closeAllConnections(socket, reader, writer);
                        break;

                    }else if(command.equalsIgnoreCase("login")){
                        handleLogin(tokens);
                    }

                    else{
                        String message = "unknown command "+command;
                        sendMessage(message, writer);
                        closeAllConnections(socket, reader, writer);
                    }
                }
            }
        }catch(IOException exception){
            exception.printStackTrace();
        }
    }

    public String getLogin() {
        return login;
    }

    private void handleLogin(String[] tokens) {
        if(tokens.length == 3){
            String username = tokens[1];
            String password = tokens[2];

            if((username.equals("guest") && password.equals("guest")) || (username.equals("root") && password.equals("root"))){
                sendMessage("ok login", writer);
                this.login = username;
                System.out.println("New user "+login+" has logged in...");

                List<ServerOperation> list = server.getAllServers();
                for(ServerOperation serverOperation : list){
                    if(serverOperation.login.equals(this.login)){
                        serverOperation.sendMessage(login+" is now online", serverOperation.writer);
                    }
                }

            }else{
                sendMessage("error login", writer);
            }
        }
    }

    private void closeAllConnections(Socket socket, BufferedReader reader, BufferedWriter writer){
        try{
            if(socket != null){
                socket.close();
            }
            if(reader != null){
                reader.close();
            }
            if (writer != null){
                writer.close();
            }

        }catch(IOException exception){
            exception.printStackTrace();
        }
    }

    private void sendMessage(String message, BufferedWriter writer){
        try {
            writer.write(message);
            writer.newLine();
            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
