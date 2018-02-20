package appchat;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 5555);
        System.out.println("Connected.");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Scanner in = new Scanner(socket.getInputStream());
                    while (in.hasNextLine()) {
                        System.out.println(in.nextLine());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream());
        BufferedWriter bw = new BufferedWriter(osw);
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter nickname: ");
        String nickname = scanner.nextLine();

        while (true) {
            System.out.print("Send messages: ");
            String text = scanner.nextLine();
            if (nickname.equalsIgnoreCase("")) {
                bw.write(socket.getInetAddress() + ": " + text);
            } else {
                bw.write(nickname + ": " + text);
            }
            bw.newLine();
            bw.flush();
            if (text.equalsIgnoreCase("bye")) {
                break;
            }
        }
        bw.close();
        osw.close();
    }
}
