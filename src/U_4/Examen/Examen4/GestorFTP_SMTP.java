package U_4.Examen.Examen4;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.smtp.SMTPClient;
import org.apache.commons.net.smtp.SMTPReply;

import java.io.*;
import java.util.Scanner;

public class GestorFTP_SMTP {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        FTPClient clienteFTP = new FTPClient();

        try {
            // Conexión al servidor FTP
            System.out.println("Conectando al servidor FTP...");
            String servidorFTP = "127.0.0.1";
            int puertoFTP = 21;
            String usuarioFTP = "usuario1";
            String contrasenaFTP = "1234";
            clienteFTP.connect(servidorFTP, puertoFTP);

            if (clienteFTP.login(usuarioFTP, contrasenaFTP)) {
                System.out.println("Conexión al servidor FTP exitosa.");
                clienteFTP.enterLocalPassiveMode();
                clienteFTP.setFileType(FTP.BINARY_FILE_TYPE);

                // Listar archivos en el servidor FTP
                System.out.println("\nArchivos disponibles en el servidor:");
                for (String archivo : clienteFTP.listNames()) {
                    System.out.println(" - " + archivo);
                }

                // Descargar archivo
                System.out.print("\nIntroduce el nombre del archivo a descargar: ");
                String archivoDescargar = scanner.nextLine();
                System.out.print("Introduce la ruta local para guardar el archivo: ");
                String rutaLocal = scanner.nextLine();

                try (FileOutputStream fos = new FileOutputStream(rutaLocal)) {
                    if (clienteFTP.retrieveFile(archivoDescargar, fos)) {
                        System.out.println("Archivo descargado correctamente.");
                    } else {
                        System.out.println("Error al descargar el archivo.");
                    }
                }

                clienteFTP.logout();
                clienteFTP.disconnect();
            } else {
                System.out.println("Error al iniciar sesión en el servidor FTP.");
            }
        } catch (IOException e) {
            System.err.println("Error con el servidor FTP: " + e.getMessage());
        }

        // Enviar correo electrónico con archivo adjunto
        try {
            System.out.println("\nConfigurando el correo electrónico...");
            SMTPClient clienteSMTP = new SMTPClient();
            clienteSMTP.connect("localhost");

            if (!SMTPReply.isPositiveCompletion(clienteSMTP.getReplyCode())) {
                clienteSMTP.disconnect();
                System.err.println("Error al conectarse al servidor SMTP.");
                return;
            }
            System.out.println("Conexión al servidor SMTP realizada.");

            // Autenticación y saludo
            clienteSMTP.sendCommand("ehlo localhost");

            // Datos del correo
            System.out.print("Introduce el remitente (e.g., postmaster@localhost): ");
            String remitente = scanner.nextLine();
            System.out.print("Introduce el destinatario: ");
            String destinatario = scanner.nextLine();
            System.out.print("Introduce el asunto: ");
            String asunto = scanner.nextLine();
            System.out.print("Introduce el mensaje: ");
            String mensaje = scanner.nextLine();

            System.out.print("Introduce la ruta del archivo a adjuntar: ");
            String rutaAdjunto = scanner.nextLine();

            if (!clienteSMTP.setSender(remitente)) {
                System.err.println("Error al configurar el remitente.");
                clienteSMTP.disconnect();
                return;
            }

            if (!clienteSMTP.addRecipient(destinatario)) {
                System.err.println("Error al añadir el destinatario.");
                clienteSMTP.disconnect();
                return;
            }

            Writer writer = clienteSMTP.sendMessageData();
            if (writer != null) {
                writer.write("Subject: " + asunto + "\n");
                writer.write("\n"); // Línea vacía entre asunto y cuerpo
                writer.write(mensaje);
                writer.write("\n\n--Adjunto:\n");
                try (BufferedReader br = new BufferedReader(new FileReader(rutaAdjunto))) {
                    String linea;
                    while ((linea = br.readLine()) != null) {
                        writer.write(linea + "\n");
                    }
                }
                writer.close();
                if (clienteSMTP.completePendingCommand()) {
                    System.out.println("Correo enviado correctamente.");
                } else {
                    System.err.println("Error al enviar el correo.");
                }
            }

            clienteSMTP.disconnect();
        } catch (IOException e) {
            System.err.println("Error con el servidor SMTP: " + e.getMessage());
        }
    }
}
