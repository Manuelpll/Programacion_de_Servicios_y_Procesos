package U_3.Ejercicios_Clase.Apartado_3_8.EjemploAdivinaNumero;

public class ObjetoCompartido {
	private int numero; // número a adivinar
	private boolean finalizado; // true cuando se haya terminado el juego
	private int jugadorGanador; // jugador ganador

	public ObjetoCompartido(int numero) {
		this.numero = numero;// numero A ADIVINAR
		this.finalizado = false;
	}
	public boolean getFinalizado() {
		return finalizado;
	}
	public int getGanador() {
		return jugadorGanador;
	}
	public synchronized String nuevaJugada(int jugador, int suNumero) {
		String mensajeSalida = "";
		if (!getFinalizado()) { // si el juego ha terminado no se comprueba nada
			if (suNumero > numero) { // demasiado grande
				mensajeSalida = "Numero demasiado grande";
			}
			if (suNumero < numero) { // demasiado pequeño
				mensajeSalida = "Numero demasiado bajo";
			}
			if (suNumero == numero) { // ha acertado
				mensajeSalida = "Jugador " + jugador + " gana, adivinó el número: " + numero;
				finalizado = true;
				jugadorGanador = jugador;
			}
		} else
			mensajeSalida = "Jugador " + jugadorGanador + " adivinó el número: " + numero;
		return mensajeSalida;
	}// nuevaJugada
}// ObjetoCompartido..


