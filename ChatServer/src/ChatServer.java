import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
    private final int portNumber;
    private ServerSocket socket;

    private final StringBuilder logging; // Use StringBuilder for efficient string concatenation

    private final ArrayList<Socket> connectedClients;

    public ChatServer() {
        this.portNumber = 8888;
        connectedClients = new ArrayList<>();
        logging = new StringBuilder(); // Initialize StringBuilder
    }

    public ChatServer(int customPortNumber) {
        this.portNumber = customPortNumber;
        connectedClients = new ArrayList<>();
        logging = new StringBuilder(); // Initialize StringBuilder
    }

    public void start() throws Exception {
        this.socket = new ServerSocket(portNumber);
    }

    public void listening() throws Exception {
        while (true) {
            Socket clientSocket = this.socket.accept();

            // A client is connected successfully
            this.connectedClients.add(clientSocket);
            ClientHandlerThread clientThread = new ClientHandlerThread(clientSocket);
            clientThread.start();
        }
    }

    public void stop() throws IOException {
        for (Socket clientSocket : connectedClients) {
            clientSocket.close();
        }

        this.socket.close();
    }

    class ClientHandlerThread extends Thread {
        private final Socket clientSocket;
        private final ChatServer server = ChatServer.this;

        public ClientHandlerThread(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                // Create input stream for client communication
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String message;

                while ((message = reader.readLine()) != null) {
                    if (message.equals(":signal:disconnect")) {
                        break;
                    }
                    // If the incoming message is not a disconnect signal, broadcast it to every connected client
                    server.broadcast(message);
                    server.logging.append(message).append("\n");
                }

                server.connectedClients.remove(clientSocket);
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public int getPortNumber() {
        return this.portNumber;
    }

    public String[] getConnectedClients() {
        List<String> clients = new ArrayList<>();
        for (Socket client : this.connectedClients) {
            clients.add(client.getInetAddress().toString());
        }

        return clients.toArray(new String[0]);
    }

    public synchronized void broadcast(String msg) throws IOException {
        msg += "\n"; // Add newline character
        for (Socket client : this.connectedClients) {
            PrintWriter outToClient = new PrintWriter(client.getOutputStream(), true);
            outToClient.println(msg);
        }
    }

    public void killClient(Socket client) throws IOException {
        this.connectedClients.remove(client);
        client.close();
    }

    public String log() {
        return this.logging.toString(); // Convert StringBuilder to String
    }
}
