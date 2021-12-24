public class ServerMain {
    public static void main(String... args){
        int PORT = 3001;
        Server server = new Server(PORT);
        server.start();
    }
}
