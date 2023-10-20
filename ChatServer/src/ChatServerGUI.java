import javax.swing.*;
import java.awt.*;

public class ChatServerGUI extends JFrame {
    private final ChatServer chatServer;
    private final JTextArea logTextArea;

    private final JList<String> clientList;
    private final JScrollPane clientScrollPane;

    private final DefaultListModel<String> clientListModel;

    public ChatServerGUI() {
        this.setSize(1000, 800);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        // Create a panel for buttons
        JPanel buttonPanel = new JPanel();
        JButton startButton = new JButton("Start Server");
        JButton stopButton = new JButton("Stop Server");
        buttonPanel.add(startButton);
        startButton.addActionListener(e -> startServer());
        buttonPanel.add(stopButton);
        stopButton.addActionListener(e -> stopServer());

        // Create a text area with word wrap and vertical scrolling (left)
        this.logTextArea = new JTextArea(20, 60); // Adjusted size to be larger
        logTextArea.setLineWrap(true);
        logTextArea.setWrapStyleWord(true);
        JScrollPane scrollPane1 = new JScrollPane(logTextArea);

        // Create another scrollable list area to the right
        this.clientListModel = new DefaultListModel<>();
        clientList = new JList<>(clientListModel);
        clientScrollPane = new JScrollPane(clientList);

        // Create a split pane to arrange the text areas side by side
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPane1, clientScrollPane);
        splitPane.setResizeWeight(0.7); // Adjusted for preferred size

        // Add components to the frame
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(buttonPanel, BorderLayout.NORTH);
        this.getContentPane().add(splitPane, BorderLayout.CENTER);

        chatServer = new ChatServer();

        // Create a Timer to periodically update the logTextArea
        Timer timer = new Timer(500, e -> updateLogTextArea());
        timer.start();
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
        var clients = chatServer.getConnectedClients();
        clientList.setListData(clients);
        this.clientScrollPane.repaint();
    }

    private void updateLogTextArea() {
        // Fetch data from chatServer.log() and update the logTextArea
        String logData = chatServer.log();
        logTextArea.append(logData);
        logTextArea.repaint();
    }
}
