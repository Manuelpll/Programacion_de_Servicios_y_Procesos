package U_2;
 public class Hola_Mundo extends Thread {
    @Override
    public void run() {
        System.out.println("Hola mundo desde el hilo " + this.getName());
    }
}