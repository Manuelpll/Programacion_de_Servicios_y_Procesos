package U_3.Cliente_Servidor_Ejemplos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Servidor {
    // Lista de PrintWriter para enviar mensajes a todos los clientes
    private static List<PrintWriter> clientes = new ArrayList<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            System.out.println("Servidor2 iniciado y esperando conexiones...");

            // Aceptar múltiples conexiones de clientes en un bucle
            while (true) {
                // Espera a que un cliente se conecte
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado.");

                // Crear un nuevo hilo para manejar la conexión del cliente
                ClienteHandler clienteHandler = new ClienteHandler(clientSocket);
                new Thread(clienteHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Clase interna para manejar la conexión de cada cliente
    private static class ClienteHandler implements Runnable {
        private Socket clientSocket;
        private PrintWriter output;

        public ClienteHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                // Crear streams de entrada y salida
                BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                output = new PrintWriter(clientSocket.getOutputStream(), true);

                // Agregar el output del cliente a la lista para retransmitir mensajes
                synchronized (clientes) {
                    clientes.add(output);
                }

                // Leer mensajes del cliente y retransmitirlos a todos los demás
                String mensaje;
                while ((mensaje = input.readLine()) != null) {
                    System.out.println("Mensaje recibido: " + mensaje);
                    enviarATodos("Cliente: " + mensaje);  // Envía el mensaje a todos los clientes
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // Cuando un cliente se desconecta, quitar su PrintWriter de la lista
                synchronized (clientes) {
                    clientes.remove(output);
                }
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("Cliente desconectado.");
            }
        }

        // Método para enviar un mensaje a todos los clientes conectados
        private void enviarATodos(String mensaje) {
            synchronized (clientes) {
                for (PrintWriter writer : clientes) {
                    writer.println(mensaje);
                }
            }
        }
    }
}
