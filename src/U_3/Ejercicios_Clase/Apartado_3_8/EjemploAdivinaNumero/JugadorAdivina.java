package U_3.Ejercicios_Clase.Apartado_3_8.EjemploAdivinaNumero;

import java.io.*;
import java.net.*;
import java.util.Scanner;


public class JugadorAdivina {
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		String host = "localhost";
		int puerto = 6001;// puerto remoto
		Socket socketCliente = new Socket(host, puerto);

		ObjectOutputStream objetcOutputStream = new ObjectOutputStream(socketCliente.getOutputStream());
		ObjectInputStream objectInputStream = new ObjectInputStream(socketCliente.getInputStream());

		// FLUJO PARA ENTRADA ESTANDAR
		Scanner sc = new Scanner(System.in);
		String cadena= "";
		
		//OBTENER PRIMER OBJETO ENVIADO POR EL SERVIDOR
		Datos datos = (Datos) objectInputStream.readObject();	
		
		int idJugador = datos.getIdjugador();
		System.out.println("Id jugador: " + idJugador);		
		System.out.println(datos.getCadena());
		
		cadena = "";
		
		if (!datos.getJuega()) 		
			cadena="*";
						
		while(datos.getJuega() && !cadena.trim().equals("*")) {						
				System.out.print("Intento: "+(datos.getNumintentos() +1)+" => Introduce número: ");
				cadena = sc.nextLine();
				Datos datosJugador = new Datos();
				if(!validarCadena(cadena))
					continue;
				
				datosJugador.setCadena(cadena);
				
				objetcOutputStream.reset();
				objetcOutputStream.writeObject(datosJugador);	
				
				datos = (Datos) objectInputStream.readObject();
				System.out.println("\t"+datos.getCadena());
			
				if (datos.getNumintentos()>=5){
					System.out.println("\t<<JUEGO FINALIZADO, NO HAY MÁS INTENTOS>>");
					cadena="*";					
				}
				if (datos.getGana()) {
					System.out.println("<<HAS GANADO!! EL JUEGO HA TERMINADO>>");	
					cadena="*";						
	             } else 
				if (!(datos.getJuega()) ){
					System.out.println("<<EL JUEGO HA TERMINADO, HAN ADIVINADO EL NUMERO>>");
					cadena="*";							
				}			

		}//fin while

		objetcOutputStream.close();
		objectInputStream.close();
		System.out.println("Fin de proceso... ");
		sc.close();
		socketCliente.close();
	}//main

	private static boolean validarCadena(String cadena) {
		//comprueba si la cadena es numerica
		boolean valor = false;
		try{
			Integer.parseInt(cadena);
			valor=true;
		} catch (NumberFormatException e){
		
		}
		
		return valor;
	}//validarCadena
	
}//JugadorAdivina


