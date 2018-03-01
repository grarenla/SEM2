import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ServerChat {
    private static String message = "";

    public static void main(String[] args) {
        try {
            // open server with port 5555
            final ServerSocket serverSocket = new ServerSocket(5555);
            System.out.println("Server started . . . ");
            final ArrayList<Socket> listSocket = new ArrayList<>();

            while (true) {
                // Wait for connection from client
                final Socket socket = serverSocket.accept();
                listSocket.add(socket);
                System.out.println(socket.getInetAddress() + " connected.");

                // thread handles receiving messages from client and returns them to others
                new Thread(() -> {
                    try {
                        Scanner scan = new Scanner(socket.getInputStream());
                        while (scan.hasNextLine()) {
                            message = scan.nextLine();
                            for (Socket client : listSocket) {
                                // Do not return messages sent by that customer
                                if (!(client.equals(socket))) {
                                    PrintWriter printWriter = new PrintWriter(client.getOutputStream(), true);
                                    printWriter.println(message);
                                }
                            }
                        }
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }).start();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
