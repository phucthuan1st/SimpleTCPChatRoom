import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        String serverAddress = "localhost"; // Change to your server's address
        int serverPort = 8888; // Change to your server's port

        ChatClient client = new ChatClient(serverAddress, serverPort);

        // Create a simple GUI for sending messages
        JFrame frame = new JFrame("Chat Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLayout(new BorderLayout());

        JTextArea chatArea = new JTextArea();
        JTextField messageField = new JTextField();
        JButton sendButton = new JButton("Send");

        sendButton.addActionListener(e -> {
            String message = messageField.getText();
            client.sendMessage(message);
            chatArea.append("You: " + message + "\n");
            messageField.setText("");
        });

        frame.add(new JScrollPane(chatArea), BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(messageField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.setVisible(true);

        // Create a separate thread for receiving messages
        Thread receiveThread = new Thread(() -> {
            while (true) {
                String message = client.receiveMessage();
                if (message != null) {
                    chatArea.append("Server: " + message + "\n");
                }
            }
        });
        receiveThread.start();
    }

}