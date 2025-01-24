package U_3.Ejercicios_Clase.Apartado_3_7;

import java.io.*;
import java.net.*;

public class TCPObjetoCliente1 {
	public static void main(String[] arg) throws IOException, ClassNotFoundException {
		String host = "localhost";
		int puerto = 6000;// puerto remoto

		System.out.println("PROGRAMA CLIENTE INICIADO....");
		Socket socketCliente = new Socket(host, puerto);

		// Flujo de entrada para objetos
		ObjectInputStream objectInputStream = 
				new ObjectInputStream(socketCliente.getInputStream());
		// Se recibe un objeto
		Persona persona = (Persona) objectInputStream.readObject();// recibo objeto
		System.out.println("Recibo: " 
						+ persona.getNombre() 
						+ "*" 
						+ persona.getEdad());

		// Modifico el objeto
		persona.setNombre("Juan Ramos");
		persona.setEdad(22);

		// FLUJO DE salida para objetos
		ObjectOutputStream objectOutputStream = 
				new ObjectOutputStream(socketCliente.getOutputStream());
		// Se envía el objeto
		objectOutputStream.writeObject(persona);
		System.out.println("Envio: " 
						+ persona.getNombre() 
						+ "*" 
						+ persona.getEdad());

		// CERRAR STREAMS Y SOCKETS
		objectInputStream.close();
		objectOutputStream.close();
		socketCliente.close();
	}
}// ..


