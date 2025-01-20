package U_3.Ejercicios_Clase.Apartado_3_8.Hilos;

import java.io.*;
import java.net.*;

public class Servidor {
	public static void main(String args[]) throws IOException {
		ServerSocket serverSocket;
		serverSocket = new ServerSocket(6000);
		System.out.println("Servidor iniciado...");

		while (true) {
			Socket socket = new Socket();
			socket = serverSocket.accept();// esperando cliente
			HiloServidor hiloServidor = new HiloServidor(socket);
			hiloServidor.start();
		}
	}
}// ..




