package U_3.Ejercicios_Clase.Apartado_3_8.EjemploAdivinaNumero;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Datos implements Serializable {
	String cadena; //cadena que se intercambia con el servidor
	int numIntentos;  //intentos que lleva el jugador
	int idJugador; //id del jugador
	boolean gana; //true si el jugador adivina el numero
	boolean juega; //true si el jugador juega, false juego finalizado

	
	public Datos(String cadena, int numIntentos, int idJugador) {		
		this.cadena = cadena;
		this.numIntentos = numIntentos;
		this.idJugador=idJugador;
		this.gana=false;
		this.juega=true;		
	}	
	public Datos() {
		super();		
	}
	public boolean getJuega() {
		return juega;
	}
	public void setJuega(boolean juega) {
		this.juega = juega;
	}
	public boolean getGana() {
		return gana;
	}
	public void setGana(boolean gana) {
		this.gana = gana;
	}
	public int getIdjugador() {
		return idJugador;
	}
	public void setIdjugador(int idJugador) {
		this.idJugador = idJugador;
	}
	public String getCadena() {
		return cadena;
	}
	public void setCadena(String cadena) {
		this.cadena = cadena;
	}
	public int getNumintentos() {
		return numIntentos;
	}
	public void setNumintentos(int numIntentos) {
		this.numIntentos = numIntentos;
	}
}


