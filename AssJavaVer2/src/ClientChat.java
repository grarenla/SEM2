
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientChat {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter InetAddress: ");
        String iAddress = scan.nextLine();
        System.out.println("Enter port: ");
        int port = scan.nextInt();
        Socket socket = handleConnection(iAddress, port);
        receiveMessages(socket);
        sendMessages(socket);
    }
//  connect to server
    private static Socket handleConnection(String iAddress, int port) {
        Socket socket = null;
        try {
            socket = new Socket(iAddress, port);
            System.out.println("Connection success.");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return socket;
    }

//  Receive message handling
    private static void receiveMessages(Socket socket) {
        new Thread(() -> {
            try {
                InputStreamReader isr = new InputStreamReader(socket.getInputStream());
                BufferedReader br = new BufferedReader(isr);
                while (true) {
                    System.out.println(br.readLine());
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }).start();
    }

//    handle the sending of messages
    private static void sendMessages(Socket socket) {
        new Thread(() -> {
            PrintWriter printWriter = null;
            try {
                printWriter = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter nickname: ");
            String nickname = scanner.nextLine();

            while (true) {
                String text = scanner.nextLine();
                if (nickname.equalsIgnoreCase("")) {
                    printWriter.println(socket.getInetAddress() + ": " + text);
                } else {
                    printWriter.println(nickname + ": " + text);
                }
                if (text.equalsIgnoreCase("quit")) {
                    break;
                }
            }
        }).start();
    }

}
