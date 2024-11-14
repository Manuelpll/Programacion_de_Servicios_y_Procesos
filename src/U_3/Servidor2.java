package U_3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Servidor2 {
    // Lista de clientes conectados
    private static List<ClienteHandler> clientes = new ArrayList<>();
    private static final int MAX_LONGITUD_MENSAJE = 70; // Límite de caracteres

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            System.out.println("Servidor iniciado y esperando conexiones...");

            // Crear un hilo separado para leer y enviar mensajes desde la consola del servidor
            new Thread(() -> {
                try (BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in))) {
                    String comando;
                    while ((comando = consoleInput.readLine()) != null) {
                        if (comando.startsWith("/expulsar ")) {
                            String nombre = comando.substring(10).trim();
                            expulsarCliente(nombre);
                        } else if (comando.length() <= MAX_LONGITUD_MENSAJE) {
                            enviarATodos("Servidor: " + comando);
                        } else {
                            System.out.println("Error: El mensaje del servidor supera los 70 caracteres.");
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            // Aceptar múltiples conexiones de clientes en un bucle
            while (true) {
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

    // Método para expulsar a un cliente por nombre
    private static void expulsarCliente(String nombre) {
        synchronized (clientes) {
            for (ClienteHandler cliente : clientes) {
                if (cliente.getNombreCliente().equalsIgnoreCase(nombre)) {
                    cliente.enviarMensaje("Has sido expulsado del servidor.");
                    cliente.cerrarConexion();
                    System.out.println("Cliente " + nombre + " ha sido expulsado.");
                    enviarATodos("El usuario " + nombre + " ha sido expulsado del servidor.");
                    return;
                }
            }
            System.out.println("Cliente " + nombre + " no encontrado.");
        }
    }

    // Método para enviar un mensaje a todos los clientes conectados
    private static void enviarATodos(String mensaje) {
        synchronized (clientes) {
            for (ClienteHandler cliente : clientes) {
                cliente.enviarMensaje(mensaje);
            }
        }
    }

    // Clase interna para manejar la conexión de cada cliente
    private static class ClienteHandler implements Runnable {
        private Socket clientSocket;
        private PrintWriter output;
        private String nombreCliente;

        public ClienteHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                output = new PrintWriter(clientSocket.getOutputStream(), true);

                // Solicitar nombre al cliente
                output.println("Por favor, ingresa tu nombre:");
                nombreCliente = input.readLine();
                System.out.println(nombreCliente + " se ha conectado.");

                // Añadir el cliente a la lista de clientes conectados
                synchronized (clientes) {
                    clientes.add(this);
                }

                enviarATodos(nombreCliente + " se ha unido al chat.");

                String mensaje;
                while ((mensaje = input.readLine()) != null) {
                    if (mensaje.length() <= MAX_LONGITUD_MENSAJE) {
                        System.out.println(nombreCliente + ": " + mensaje);
                        enviarATodos(nombreCliente + ": " + mensaje);
                    } else {
                        enviarMensaje("Error: El mensaje supera los 70 caracteres. Inténtalo de nuevo.");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                synchronized (clientes) {
                    clientes.remove(this);
                }
                enviarATodos(nombreCliente + " se ha desconectado.");
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(nombreCliente + " se ha desconectado.");
            }
        }

        // Método para enviar un mensaje al cliente
        public void enviarMensaje(String mensaje) {
            if (output != null) {
                output.println(mensaje);
            }
        }

        // Método para cerrar la conexión del cliente
        public void cerrarConexion() {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Método para obtener el nombre del cliente
        public String getNombreCliente() {
            return nombreCliente;
        }
    }
}