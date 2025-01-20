package U_3;
import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class GuessNumberServer {

    private static final int PORT = 9999;
    private static final int MAX_CLIENTS = 10;
    private static final int SECRET_NUMBER = new Random().nextInt(100) + 1;
    private static volatile boolean numberGuessed = false; // Para compartir el estado entre los hilos

    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(MAX_CLIENTS);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("[INFO] Servidor de adivinanza de números iniciado en el puerto " + PORT);
            System.out.println("[INFO] Número secreto generado: " + SECRET_NUMBER);

            while (!numberGuessed) {
                Socket clientSocket = serverSocket.accept();
                pool.execute(new GameHandler(clientSocket, SECRET_NUMBER));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            pool.shutdown();
            System.out.println("[INFO] Servidor cerrado.");
        }
    }

    static class GameHandler implements Runnable {
        private final Socket clientSocket;
        private final int secretNumber;

        public GameHandler(Socket clientSocket, int secretNumber) {
            this.clientSocket = clientSocket;
            this.secretNumber = secretNumber;
        }

        @Override
        public void run() {
            try (
                    InputStream input = clientSocket.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    OutputStream output = clientSocket.getOutputStream();
                    PrintWriter writer = new PrintWriter(output, true)
            ) {
                writer.println("¡Bienvenido al juego de adivinanza de números! Adivina un número entre 1 y 100:");

                String message;
                while ((message = reader.readLine()) != null && !numberGuessed) {
                    int guess;
                    try {
                        guess = Integer.parseInt(message);
                    } catch (NumberFormatException e) {
                        writer.println("Por favor, introduce un número válido.");
                        continue;
                    }

                    if (guess < secretNumber) {
                        writer.println("El número es mayor. Intenta de nuevo.");
                    } else if (guess > secretNumber) {
                        writer.println("El número es menor. Intenta de nuevo.");
                    } else {
                        writer.println("¡Felicidades! Has adivinado el número.");
                        numberGuessed = true; // Cambiar el estado para cerrar el servidor
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("[INFO] Conexión cerrada");
            }
        }
    }
}
