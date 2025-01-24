package U_3.Ejercicios_Clase.Apartado_3_8.EjemploAdivinaNumero;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorAdivina {
  public static void main(String args[]) throws IOException  {

	ServerSocket serverSocket = new ServerSocket(6001);
	System.out.println("Servidor iniciado...");		
	
	// Numero a adivinar entre 1 y 25
	int numero = (int) (1 + 25*Math.random()); 
	System.out.println("NUMERO A ADIVINAR=> " + numero);

	ObjetoCompartido objetoCompartido = new ObjetoCompartido(numero);
	int id = 0;
	while (true) {	
		Socket socket = new Socket();
		socket = serverSocket.accept(); //esperando cliente	
		id++;			
		HiloServidorAdivina hilo = new 
                           HiloServidorAdivina(socket, objetoCompartido, id);
		hilo.start();		
	}	
  }
}//..

