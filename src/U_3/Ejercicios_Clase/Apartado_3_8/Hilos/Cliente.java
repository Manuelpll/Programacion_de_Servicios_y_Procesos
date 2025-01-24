package U_3.Ejercicios_Clase.Apartado_3_8.Hilos;

import java.io.*;
import java.net.*;

public class Cliente {
  public static void main(String[] args) throws IOException {
	String host = "localhost";
	int puerto = 6000;// puerto remoto
	Socket socket = new Socket(host, puerto);
		
	// CREO FLUJO DE SALIDA AL SERVIDOR	
	PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
	// CREO FLUJO DE ENTRADA AL SERVIDOR	
	BufferedReader bufferedReaderServidor =  
			new BufferedReader(new InputStreamReader(socket.getInputStream()));
		 
	// FLUJO PARA ENTRADA ESTANDAR
	BufferedReader bufferedReaderStandard = 
			new BufferedReader(new InputStreamReader(System.in));
	String cadena, eco="";
		
	
	do{ 
		System.out.print("Introduce cadena: ");
		cadena = bufferedReaderStandard.readLine();
		printWriter.println(cadena);
		eco = bufferedReaderServidor.readLine();			
		System.out.println("  =>ECO: " + eco);	
	} while(!cadena.trim().equals("*"));
		
	printWriter.close();
	bufferedReaderServidor.close();
	System.out.println("Fin del envío... ");
	bufferedReaderStandard.close();
	socket.close();
	}//
}//



