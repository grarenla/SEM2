/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientchat;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

/**
 *
 * @author s2rai
 */
public class FXMLClientController implements Initializable {

    @FXML
    private JFXTextField txtNickname;
    @FXML
    private JFXButton btnNickname;
    @FXML
    private JFXTextField txtMsg;
    @FXML
    private TextArea msgArea;
    @FXML
    Socket socket;
    @FXML
    String msg = "";
    @FXML
    String nickname = "";

    @FXML
    private void handleBtnSend(ActionEvent event) throws IOException {
        handleSendMsg(socket);
    }

    @FXML
    private void handleBtnSave(ActionEvent event) {
        nickname = txtNickname.getText();
        txtNickname.setDisable(true);
        btnNickname.setDisable(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            socket = new Socket("localhost", 5555);
            msgArea.setText("Connection success.");
            handleReceiveMsg(socket);
        } catch (IOException ex) {
            Logger.getLogger(FXMLClientController.class.getName()).log(Level.SEVERE, null, ex);
        }
        // TODO
    }

    @FXML
    private void handleSendMsg(Socket socket) throws IOException {
        new Thread(() -> {
            OutputStreamWriter osw = null;
            try {
                msg = txtMsg.getText().trim();
                osw = new OutputStreamWriter(socket.getOutputStream());
                BufferedWriter bw = new BufferedWriter(osw);
                if (nickname.equalsIgnoreCase("")) {
                    bw.write(socket.getInetAddress() + ": " + msg);
                } else {
                    bw.write(nickname + ": " + msg);
                }
                if (msg.equalsIgnoreCase("stop")) {
                    socket.close();
                }
                bw.newLine();
                bw.flush();
                txtMsg.setText("");
            } catch (IOException ex) {
                Logger.getLogger(FXMLClientController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();
    }

    @FXML
    private void handleReceiveMsg(Socket socket) throws IOException {
        new Thread(() -> {
            try {
                Scanner scanner = new Scanner(socket.getInputStream());
                while (scanner.hasNextLine()) {
                    msg = scanner.nextLine();
                    msgArea.setText(msgArea.getText().trim() + "\n" + msg);
                }
            } catch (IOException ex) {
                Logger.getLogger(FXMLClientController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();
    }
}
