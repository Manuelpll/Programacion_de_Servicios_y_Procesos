package U_1;

import java.io.IOException;

public class LeerNombre {
    public static void main(String[] args) throws IOException {
        if(args.length >0) {
            String nombre = args[0];
            if (nombre != null) {
                System.out.println(nombre);

            }
            System.exit(1);
        }else {

            System.exit(-1);
        }//Fin if-else
    }
}
