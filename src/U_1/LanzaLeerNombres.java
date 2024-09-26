package U_1;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class LanzaLeerNombres {
    public static void main(String[] args) throws IOException {
        File  directorio= new File("C:\\Users\\aludam2\\IdeaProjects\\Programacion_de_servicios_y_procesos");
        ProcessBuilder pb = new ProcessBuilder("java", "LeerNomnbre", "Agustin");
        Process p= pb.start();
        try {
          InputStream in = p.getInputStream();
          int c;
          while ((c = in.read()) != -1) {
              System.out.print((char) c);
          }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        int exitVal;
        try {
                exitVal = p.waitFor();
                System.out.println("Valor de Salida: "+exitVal);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
