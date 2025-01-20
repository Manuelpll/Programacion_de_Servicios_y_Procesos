package U_3;

import java.io.InputStream;
import java.io.*;
import java.net.*;
import java.util.Scanner;

    public class GuessNumberClient {

        public static void main(String[] args) {
            String serverAddress = "localhost";
            int port = 9999;

            try (Socket socket = new Socket(serverAddress, port);
                 InputStream input = socket.getInputStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                 OutputStream output = socket.getOutputStream();
                 PrintWriter writer = new PrintWriter(output, true)) {

                Scanner scanner = new Scanner(System.in);
                String serverMessage;

                while ((serverMessage = reader.readLine()) != null) {
                    System.out.println(serverMessage);

                    if (serverMessage.contains("¡Felicidades!")) {
                        break;
                    }

                    System.out.print("Tu adivinanza: ");
                    String guess = scanner.nextLine();
                    writer.println(guess);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
