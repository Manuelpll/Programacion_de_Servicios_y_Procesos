package U_3.Ejercicio_13;
import java.io.*;
import java.net.Socket;

public class ManejadorCliente implements Runnable {
    private Socket socket;

    // Constructor que inicializa el socket
    public ManejadorCliente(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
                // Crear los flujos de entrada y salida
                DataInputStream entrada = new DataInputStream(socket.getInputStream());
                DataOutputStream salida = new DataOutputStream(socket.getOutputStream())
        ) {
            while (true) {
                // Leer la cadena enviada por el cliente
                String mensajeRecibido;
                try {
                    mensajeRecibido = entrada.readUTF(); // Leer mensaje del cliente
                } catch (EOFException e) {
                    // El cliente cerró la conexión de manera normal
                    System.out.println("Cliente desconectado.");
                    break;
                }

                // Detectar si el cliente desea finalizar la conexión
                if (mensajeRecibido.equals("*")) {
                    System.out.println("Cliente solicitó desconexión.");
                    break;
                }

                // Procesar la cadena eliminando las vocales
                String mensajeProcesado = eliminarVocales(mensajeRecibido);

                // Enviar la cadena resultante de vuelta al cliente
                salida.writeUTF(mensajeProcesado);
            }
        } catch (IOException e) {
            System.err.println("Error en el manejo del cliente: " + e.getMessage());
        } finally {
            // Cerrar el socket al finalizar el hilo
            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException e) {
                System.err.println("Error al cerrar el socket: " + e.getMessage());
            }
        }
    }

    // Método para eliminar las vocales de una cadena
    private String eliminarVocales(String texto) {
        return texto.replaceAll("[AEIOUaeiouáéíóúÁÉÍÓÚÜü]", "");
    }
}



