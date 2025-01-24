package U_3.Ejercicios_Clase.Apartado_3_8.EjemploAdivinaNumero;

import java.io.*;
import java.net.*;

public class HiloServidorAdivina extends Thread {
	ObjectInputStream objectInputStream;
	ObjectOutputStream objectOutputStream;

	Socket socket = null;

	ObjetoCompartido objetoCompartido;
	int idJugador;
	int numIntentos = 0;

	public HiloServidorAdivina(Socket socket, ObjetoCompartido objetoCompartido, int idJugador) {
		this.socket = socket;
		this.objetoCompartido = objetoCompartido;
		this.idJugador = idJugador;
		try {
			 objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			 objectInputStream = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			System.out.println("ERROR DE E/S en HiloServidor");
			e.printStackTrace();
		}
	}// ..

	// ----------------------------------------------------------------
	public void run() {
				
		System.out.println("=>Cliente conectado: " + idJugador);

		//PREPARAR PRIMER ENVIO AL CLIENTE	
		Datos datos = new Datos("Adivina un NÚMERO ENTRE 1 Y 25", numIntentos, idJugador);
		
		if (objetoCompartido.getFinalizado()) {
			datos.setCadena("LO SENTIMOS, EL JUEGO HA TERMINADO, HAN ADIVINADO EL Nº");
			datos.setJuega(false); // ya no TIENE QUE JUGAR
		}

		try {
			objectOutputStream.reset();
			objectOutputStream.writeObject(datos);
		} catch (IOException e1) {
			System.out.println("Error en el Hilo al realizar " + 
		                  "el primer envio al jugador: " + idJugador);
			return;
		}
		
		// mientras no se haya acabado el juego
		while (!objetoCompartido.getFinalizado() || !(datos.getNumintentos() == 5)) { 
			int numeroDeCliente = 0; 
			try {
				// leer numero del cliente
				Datos datosCliente = (Datos) objectInputStream.readObject(); 
				numeroDeCliente = Integer.parseInt(datosCliente.getCadena());
			} catch (IOException e) {
				System.out.println("Error en el Hilo al leer del jugador: " + idJugador);
				break;
			} catch (NumberFormatException n) {
				System.out.println("El jugador:" + idJugador + " se ha desconectado");
				break;
			} catch (ClassNotFoundException e) {				
				e.printStackTrace();
				break;
			}
			
			// jugar el numero
			String cadena = objetoCompartido.nuevaJugada(idJugador, numeroDeCliente);
			numIntentos++;
			datos = new Datos(cadena, numIntentos, idJugador);

			if (objetoCompartido.getFinalizado()) {
				datos.setJuega(false); // ya no tiene que seguir jugando
				if (idJugador == objetoCompartido.getGanador())
					datos.setGana(true);				
			}

			try {
				objectOutputStream.reset();
				objectOutputStream.writeObject(datos);				
			} catch (IOException n1) {
				System.out.println(
						"Error escribiendo en flujo de salida del jugador: " 
										+ idJugador + " * " + n1.getMessage());
				break;
			} catch (NullPointerException n) {// se produce cuando el cliente				
				System.out.println("El jugador  " + idJugador + " ha desconectado ");
				break;
			}

		} // fin while

		if (objetoCompartido.getFinalizado()) {
			System.out.println("EL JUEGO SE HA ACABADO.....");
			System.out.println("\t==>Desconecta: " + idJugador);
		}
		try {
			objectOutputStream.close();
			objectInputStream.close();
			socket.close();
		} catch (IOException e) {
			System.out.println("Error en Hilo al cerrar cliente: " + idJugador);
			e.printStackTrace();
		}

	}// run

}// ..


