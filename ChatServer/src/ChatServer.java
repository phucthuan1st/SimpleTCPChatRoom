import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer {
    private final int portNumber;
    private ServerSocket socket;

    private final ArrayList<Socket> connectedClients;

    public ChatServer() {
        this.portNumber = 8888;
        connectedClients = new ArrayList<>();
    }

    public ChatServer(int customPortNumber) {
        this.portNumber = customPortNumber;
        connectedClients = new ArrayList<>();
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
            connectedClients.remove(clientSocket);
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
                InputStream in = clientSocket.getInputStream();

                // Reader to read buffer of client input stream
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String message;

                while (!(message = reader.readLine()).equals(":signal:disconnect")) {

                    // If the incoming message is not disconnect signal, broadcast it to every connected client
                    for (Socket client : server.connectedClients) {
                        DataOutputStream outToClient = new DataOutputStream(client.getOutputStream());
                        outToClient.writeBytes(message);
                        outToClient.close();
                    }
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

    public ArrayList<Socket> getConnectedClients() {
        return this.connectedClients;
    }
}
