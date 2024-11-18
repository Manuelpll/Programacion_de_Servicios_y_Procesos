package U_3.Cliente_Servidor_Ejemplos;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
public class Servidor_Final {
    private static List<ClienteHandler> clientes = new ArrayList<>();
    private static final int MAX_LONGITUD_MENSAJE = 200;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            System.out.println("Servidor iniciado y esperando conexiones...");

            // Hilo que lee comandos desde la consola para el servidor
            new Thread(() -> {
                try (BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in))) {
                    String comando;
                    while ((comando = consoleInput.readLine()) != null) {
                        if (comando.startsWith("/ex ")) {
                            String nombre = comando.substring(4).trim();
                            expulsarCliente(nombre);
                        } else if (comando.length() <= MAX_LONGITUD_MENSAJE) {
                            enviarATodos("Servidor: " + comando);
                        } else {
                            System.out.println("Error: El mensaje del servidor supera los 200 caracteres.");
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            // Aceptar conexiones de clientes
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado.");
                ClienteHandler clienteHandler = new ClienteHandler(clientSocket);
                new Thread(clienteHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void enviarATodos(String mensaje) {
        synchronized (clientes) {
            for (ClienteHandler cliente : clientes) {
                cliente.enviarMensaje(mensaje);
            }
        }
    }

    private static void expulsarCliente(String nombre) {
        synchronized (clientes) {
            boolean expulsado = false;
            for (ClienteHandler cliente : clientes) {
                if (cliente.getNombreCliente().equalsIgnoreCase(nombre)) {
                    cliente.enviarMensaje("Has sido expulsado del servidor.");
                    cliente.cerrarConexion();
                    System.out.println("Cliente " + nombre + " ha sido expulsado.");
                    enviarATodos("El usuario " + nombre + " ha sido expulsado del servidor.");
                    expulsado = true;
                    break;
                }
            }
            if (!expulsado) {
                System.out.println("Cliente " + nombre + " no encontrado.");
            }
        }
    }

    private static void mostrarComandos(ClienteHandler cliente) {
        cliente.enviarMensaje("Comandos disponibles:");
        cliente.enviarMensaje("/help - Mostrar los comandos disponibles.");
        cliente.enviarMensaje("/msg <nombre> <mensaje> - Enviar un mensaje privado a un cliente.");
        cliente.enviarMensaje("/list - Ver la lista de clientes conectados.");
    }

    private static void enviarMensajePrivado(String nombre, String mensaje) {
        synchronized (clientes) {
            for (ClienteHandler cliente : clientes) {
                if (cliente.getNombreCliente().equalsIgnoreCase(nombre)) {
                    cliente.enviarMensaje("Mensaje privado: " + mensaje);
                    System.out.println("Mensaje privado a " + nombre + ": " + mensaje);
                    return;
                }
            }
            System.out.println("Cliente " + nombre + " no encontrado.");
        }
    }

    private static void listarClientes(ClienteHandler cliente) {
        synchronized (clientes) {
            if (clientes.isEmpty()) {
                cliente.enviarMensaje("No hay clientes conectados.");
            } else {
                cliente.enviarMensaje("Clientes conectados:");
                for (ClienteHandler c : clientes) {
                    cliente.enviarMensaje(c.getNombreCliente());
                }
            }
        }
    }

    private static class ClienteHandler implements Runnable {
        private Socket clientSocket;
        private PrintWriter output;
        private String nombreCliente;
        private String color;

        private static final String ANSI_RESET = "\u001B[0m";
        private static final String[] COLORES = {
                "\u001B[31m", // Rojo
                "\u001B[32m", // Verde
                "\u001B[33m", // Amarillo
                "\u001B[34m", // Azul
                "\u001B[35m", // Magenta
                "\u001B[36m", // Cian
                "\u001B[37m", // Blanco
                "\u001B[90m", // Gris oscuro
                "\u001B[91m", // Rojo claro
                "\u001B[92m", // Verde claro
                "\u001B[93m", // Amarillo claro
                "\u001B[94m", // Azul claro
                "\u001B[95m", // Magenta claro
                "\u001B[96m", // Cian claro
                "\u001B[97m", // Blanco claro
                "\u001B[41m", // Fondo rojo
                "\u001B[42m", // Fondo verde
                "\u001B[43m", // Fondo amarillo
                "\u001B[44m", // Fondo azul
                "\u001B[45m", // Fondo magenta
                "\u001B[48;5;130m", // Naranja (usando código 130 en 256 colores)
                "\u001B[38;5;129m", // Morado (usando código 129 en 256 colores)
                "\u001B[38;5;208m", // Naranja claro
                "\u001B[38;5;55m",  // Aqua (color similar al cyan)
                "\u001B[38;5;153m"  // Pink (color rosa)
        };

        public ClienteHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                output = new PrintWriter(clientSocket.getOutputStream(), true);

                output.println("Por favor, ingresa tu nombre:");
                nombreCliente = input.readLine();

                // Mostrar todos los colores disponibles en el mensaje inicial
                output.println("Elige un color (1: Rojo, 2: Verde, 3: Amarillo, 4: Azul, 5: Magenta, 6: Cian, 7: Blanco, 8: Gris oscuro, 9: Rojo claro, 10: Verde claro, 11: Amarillo claro, 12: Azul claro, 13: Magenta claro, 14: Cian claro, 15: Blanco claro, 16: Fondo Rojo, 17: Fondo Verde, 18: Fondo Amarillo, 19: Fondo Azul, 20: Fondo Magenta, 21: Naranja, 22: Morado, 23: Naranja claro, 24: Aqua, 25: Rosa):");

                int colorIndex;
                try {
                    colorIndex = Integer.parseInt(input.readLine()) - 1;
                    if (colorIndex < 0 || colorIndex >= COLORES.length) {
                        colorIndex = 0;
                    }
                } catch (NumberFormatException e) {
                    colorIndex = 0;
                }
                color = COLORES[colorIndex];

                System.out.println(color + nombreCliente + ANSI_RESET + " se ha conectado.");

                synchronized (clientes) {
                    clientes.add(this);
                }

                enviarATodos(color + nombreCliente + ANSI_RESET + " se ha unido al chat.");

                String mensaje;
                while ((mensaje = input.readLine()) != null) {
                    if (mensaje.startsWith("/")) {  // Verifica si es un comando
                        procesarComando(mensaje);
                    } else if (mensaje.length() <= MAX_LONGITUD_MENSAJE) {
                        System.out.println(color + nombreCliente + ANSI_RESET + ": " + mensaje);
                        enviarATodos(color + nombreCliente + ANSI_RESET + ": " + mensaje);
                    } else {
                        enviarMensaje("Error: El mensaje supera los 200 caracteres. Inténtalo de nuevo.");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                synchronized (clientes) {
                    clientes.remove(this);
                }
                enviarATodos(color + nombreCliente + ANSI_RESET + " se ha desconectado.");
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(color + nombreCliente + ANSI_RESET + " se ha desconectado.");
            }
        }

        private void procesarComando(String comando) {
            if (comando.startsWith("/help")) {
                mostrarComandos(this);
            } else if (comando.startsWith("/msg ")) {
                String[] partes = comando.substring(5).split(" ", 2);
                if (partes.length == 2) {
                    String nombre = partes[0].trim();
                    String mensaje = partes[1].trim();
                    enviarMensajePrivado(nombre, mensaje);
                } else {
                    enviarMensaje("Error: El comando /msg debe tener el formato '/msg <nombre> <mensaje>'.");
                }
            } else if (comando.equals("/list")) {
                listarClientes(this);
            } else {
                enviarMensaje("Comando no reconocido.");
            }
        }

        public void enviarMensaje(String mensaje) {
            if (output != null) {
                output.println(mensaje);
            }
        }

        public void cerrarConexion() {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public String getNombreCliente() {
            return nombreCliente;
        }
    }
}



