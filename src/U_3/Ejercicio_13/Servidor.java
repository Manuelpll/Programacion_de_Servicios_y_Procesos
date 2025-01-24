package U_3.Ejercicio_13;

import java.io.*;
import java.net.*;

public class Servidor {
    public static void main(String[] args) {
        int puerto = 11223; // Puerto para escuchar conexiones
        System.out.println("Servidor iniciado. Esperando clientes en el puerto " + puerto + "...");

        try (ServerSocket servidor = new ServerSocket(puerto)) {
            while (true) {
                // Aceptar conexiones de clientes
                Socket cliente = servidor.accept();
                System.out.println("Cliente conectado desde " + cliente.getInetAddress());

                // Crear un hilo para manejar al cliente
                Thread hiloCliente = new Thread(new ManejadorCliente(cliente));
                hiloCliente.start();
            }
        } catch (IOException e) {
            System.err.println("Error en el servidor: " + e.getMessage());
        }
    }
}

