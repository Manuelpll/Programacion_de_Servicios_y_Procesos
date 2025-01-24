package U_3.Ejercicio_13;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        String direccionServidor = "localhost";
        int puerto = 11223;

        try (Socket socket = new Socket(direccionServidor, puerto);
             DataInputStream entrada = new DataInputStream(socket.getInputStream());
             DataOutputStream salida = new DataOutputStream(socket.getOutputStream());
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Conectado al servidor en " + direccionServidor + ":" + puerto);
            System.out.println("Introduce cadenas de texto para enviarlas al servidor (introduce * para salir):");

            while (true) {
                System.out.print("> ");
                String mensaje = scanner.nextLine();

                if (mensaje.equals("*")) {
                    System.out.println("Cerrando conexión...");
                    break;
                }

                // Enviar el mensaje al servidor
                salida.writeUTF(mensaje);

                // Leer la respuesta del servidor
                String respuesta = entrada.readUTF();
                System.out.println("Respuesta del servidor: " + respuesta);
            }
        } catch (IOException e) {
            System.err.println("Error en el cliente: " + e.getMessage());
        }
    }
}

