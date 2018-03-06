package viewclient;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientChat extends JFrame {

    private String nickname = "";
    private String msg;
    private JLabel nicknameLabel;
    private JButton btnNickname;
    private JTextField textNickname;
    private JTextArea msgBox;
    private JTextField textFieldMsg;
    private JButton buttonSend;
    private Socket socket;

    public ClientChat() throws IOException {
        setSize(650, 650);
        setTitle("Chat Client");
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        nicknameLabel = new JLabel();
        nicknameLabel.setText("Nickname");
        nicknameLabel.setBounds(20, 30, 100, 30);
        add(nicknameLabel);

        textNickname = new JTextField();
        textNickname.setBounds(130, 30, 350, 30);
        add(textNickname);

        btnNickname = new JButton();
        btnNickname.setBounds(500, 30, 100, 30);
        btnNickname.setText("Ok");
        btnNickname.addActionListener(actionEvent -> {
            nickname = textNickname.getText();
            textNickname.setEditable(false);
            btnNickname.setEnabled(false);
        });
        add(btnNickname);

        msgBox = new JTextArea();
        msgBox.setBounds(20, 110, 580, 380);
        msgBox.setEditable(false);
        add(msgBox);

        textFieldMsg = new JTextField();
        textFieldMsg.setBounds(20, 550, 450, 30);
        textFieldMsg.addActionListener(actionEvent -> {
            String msgsend = textFieldMsg.getText().trim();
            try {
                OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream());
                BufferedWriter bw = new BufferedWriter(osw);
                if (nickname.equalsIgnoreCase("")) {
                    bw.write(socket.getInetAddress() + ": " + msgsend);
                } else {
                    bw.write(nickname + ": " + msgsend);
                }
                if (msgsend.equalsIgnoreCase("stop")) {
                    socket.close();
                }
                bw.newLine();
                bw.flush();
                textFieldMsg.setText("");
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        });
        add(textFieldMsg);

        buttonSend = new JButton();
        buttonSend.setText("Send");
        buttonSend.setBounds(500, 550, 100, 30);
        add(buttonSend);

        setVisible(true);
        socket = new Socket("localhost", 5555);

        Scanner scanner = new Scanner(socket.getInputStream());
        while (scanner.hasNextLine()) {
            msg = scanner.nextLine();
            msgBox.setText(msgBox.getText().trim() + "\n" + msg);
        }
    }

    public static void main(String[] args) throws IOException {
        ClientChat clientChat = new ClientChat();
    }


}
