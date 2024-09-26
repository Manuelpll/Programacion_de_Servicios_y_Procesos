package U_1;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class LanzaLeerNombres {
    public static void main(String[] args) throws IOException {
        // Usa barras simples en la ruta
        File directorio = new File("C:/Users/aludam2/IdeaProjects/Programacion_Servicios_y_procesos");

        // Asegúrate de que la ruta del proyecto y la clase sean correctas
        Process p = new ProcessBuilder("java", "-cp", "C:/Users/aludam2/IdeaProjects/Programacion_Servicios_y_procesos", "U_1.LeerNombre", "Agustin")
                .directory(directorio)
                .start();

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
            System.out.println("Valor de Salida: " + exitVal);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    }
