package U_3.Ejercicios_Clase.Apartado_3_7;

import java.io.*;
import java.net.*;

public class TCPObjetoServidor1 {
	public static void main(String[] arg) throws IOException, ClassNotFoundException {
		int numeroPuerto = 6000;// Puerto
		ServerSocket serverSocket = new ServerSocket(numeroPuerto);
		System.out.println("Esperando al cliente.....");
		Socket socketServidor = serverSocket.accept();

		// Se prepara un flujo de salida para objetos
		ObjectOutputStream objectOutputStream = 
				new ObjectOutputStream(socketServidor.getOutputStream());
		// Se prepara un objeto y se envía
		Persona personaEnviar = new Persona("Juan", 20);
		objectOutputStream.writeObject(personaEnviar); // enviando objeto
		System.out.println("Envio: " 
						+ personaEnviar.getNombre() 
						+ "*" 
						+ personaEnviar.getEdad());

		// Se obtiene un stream para leer objetos
		ObjectInputStream objectInputStream = 
				new ObjectInputStream(socketServidor.getInputStream());
		Persona personaRecibir = (Persona) objectInputStream.readObject();
		System.out.println("Recibo: " 
						+ personaRecibir.getNombre() 
						+ "*" 
						+ personaRecibir.getEdad());

		// CERRAR STREAMS Y SOCKETS
		objectOutputStream.close();
		objectInputStream.close();
		socketServidor.close();
		serverSocket.close();
	}//Fin main
}//Fin TCPObjetoServidor1


