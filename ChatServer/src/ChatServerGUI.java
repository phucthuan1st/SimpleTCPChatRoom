import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatServerGUI extends JFrame {
    private final ChatServer chatServer;

    private JButton startButton;
    private JButton stopButton;
    private final JTextArea logTextArea;

    public ChatServerGUI() {
        chatServer = new ChatServer();

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
        } catch (Exception e) {
            logTextArea.append("Error starting server: " + e.getMessage() + "\n");
        }
    }

    private void stopServer() {
        try {
            chatServer.stop();
            logTextArea.append("Server stopped\n");
        } catch (Exception e) {
            logTextArea.append("Error stopping server: " + e.getMessage() + "\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ChatServerGUI serverGUI = new ChatServerGUI();
                serverGUI.setVisible(true);
            }
        });
    }
}
