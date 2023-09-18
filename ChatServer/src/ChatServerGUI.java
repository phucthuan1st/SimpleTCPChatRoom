import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.net.Socket;

public class ChatServerGUI extends JFrame {
    private final ChatServer chatServer;
    private final DefaultListModel<String> clientListModel;

    private final JButton startButton;
    private final JButton stopButton;
    private final JTextArea logTextArea;

    public ChatServerGUI() {
        chatServer = new ChatServer();
        clientListModel = new DefaultListModel<>();
        JList<String> clientList = new JList<>(clientListModel);

        setTitle("Chat Server");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLayout(new BorderLayout());

        startButton = new JButton("Start Server");
        stopButton = new JButton("Stop Server");
        logTextArea = new JTextArea();
        logTextArea.setEditable(false);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);

        add(buttonPanel, BorderLayout.NORTH);
        add(new JScrollPane(logTextArea), BorderLayout.CENTER);
        add(new JScrollPane(clientList), BorderLayout.SOUTH);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startServer();
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopServer();
            }
        });
    }

    private void startServer() {
        try {
            chatServer.start();
            logTextArea.append("Server started on port " + chatServer.getPortNumber() + "\n");
            updateClientList();
        } catch (Exception e) {
            logTextArea.append("Error starting server: " + e.getMessage() + "\n");
        }
    }

    private void stopServer() {
        try {
            chatServer.stop();
            logTextArea.append("Server stopped\n");
            updateClientList();
        } catch (Exception e) {
            logTextArea.append("Error stopping server: " + e.getMessage() + "\n");
        }
    }

    private void updateClientList() {
        ArrayList<Socket> clients = chatServer.getConnectedClients();
        clientListModel.clear();
        for (Socket client : clients) {
            clientListModel.addElement(client.getInetAddress().toString());
        }
    }
}
