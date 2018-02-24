package appchat;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Server {

    private static String message = "";

    public static void main(String[] args) {

        try {
            final ServerSocket serverSocket = new ServerSocket(5555);
            System.out.println("Server started. . . ");
            final ArrayList<Socket> listSocket = new ArrayList<>();
            while (true) {
                final Socket socket = serverSocket.accept();
                listSocket.add(socket);
                System.out.println(socket.getInetAddress() + " connected.");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Scanner scan = new Scanner(socket.getInputStream());
                            while (scan.hasNextLine()) {
                                message = scan.nextLine();
                                for (Socket client : listSocket) {
                                    if (!(client.equals(socket))) {
                                        PrintWriter printWriter = new PrintWriter(client.getOutputStream(), true);
                                        printWriter.println(message);
                                    }
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
