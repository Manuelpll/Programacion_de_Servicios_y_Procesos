package U_3.Ejercicios_Clase.Apartado_3_6;

import java.io.*;
import java.net.*;

public class MCservidor {
	public static void main(String args[]) throws Exception {
		// FLUJO PARA ENTRADA ESTANDAR
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

		// Se crea el socket multicast.
		MulticastSocket multicastSocket = new MulticastSocket();

		int puerto = 1234;// Puerto multicast
		InetAddress inetAddress = InetAddress.getByName("225.0.0.2");// Grupo

		String mensaje = "";

		while (!mensaje.trim().equals("*")) {
			System.out.print("Datos a enviar al grupo: ");
			mensaje = in.readLine();
			// ENVIANDO AL GRUPO
			DatagramPacket datagramPacket = new DatagramPacket(
					mensaje.getBytes(), 
					mensaje.length(), 
					inetAddress, 
					puerto);
			multicastSocket.send(datagramPacket);
		}
		multicastSocket.close();// cierro socket
		System.out.println("Socket cerrado...");
	}
}
