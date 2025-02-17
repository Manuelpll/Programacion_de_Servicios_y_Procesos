package U_4.Examen.Examen1;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.smtp.AuthenticatingSMTPClient;
import org.apache.commons.net.smtp.SMTPReply;
import org.apache.commons.net.smtp.SimpleSMTPHeader;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Scanner;

public class ExamenFTP_SMTP {
    private static final String SERVIDOR_FTP = "127.0.0.1";
    private static final int PUERTO_FTP = 21;
    private static final String USUARIO_FTP = "usuario1";
    private static final String CONTRASENA_FTP = "1234";

    public static void main(String[] args) {
        FTPClient clienteFTP = new FTPClient();
        String archivoSeleccionado = null;

        try {
            // Conexión al servidor FTP
            clienteFTP.connect(SERVIDOR_FTP, PUERTO_FTP);
            if (clienteFTP.login(USUARIO_FTP, CONTRASENA_FTP)) {
                System.out.println("Conexión al servidor FTP exitosa.");
                clienteFTP.enterLocalPassiveMode();
                clienteFTP.setFileType(FTP.BINARY_FILE_TYPE);

                Scanner scanner = new Scanner(System.in);
                while (true) {
                    System.out.println("\nOpciones:");
                    System.out.println("1. Listar archivos del servidor FTP");
                    System.out.println("2. Descargar archivo del servidor FTP");
                    System.out.println("3. Seleccionar archivo para enviar como adjunto");
                    System.out.println("4. Enviar correo electrónico");
                    System.out.println("5. Salir");
                    System.out.print("Selecciona una opción: ");
                    int opcion = scanner.nextInt();
                    scanner.nextLine(); // Consumir salto de línea

                    switch (opcion) {
                        case 1:
                            listarArchivos(clienteFTP);
                            break;
                        case 2:
                            System.out.print("Introduce el nombre del archivo a descargar: ");
                            String archivoDescargar = scanner.nextLine();
                            System.out.print("Introduce la ruta local para guardar el archivo: ");
                            String rutaDescargar = scanner.nextLine();
                            descargarArchivo(clienteFTP, archivoDescargar, rutaDescargar);
                            break;
                        case 3:
                            System.out.print("Introduce la ruta del archivo local a seleccionar: ");
                            archivoSeleccionado = scanner.nextLine();
                            System.out.println("Archivo seleccionado: " + archivoSeleccionado);
                            break;
                        case 4:
                            enviarCorreo(archivoSeleccionado);
                            break;
                        case 5:
                            System.out.println("Cerrando conexión...");
                            clienteFTP.logout();
                            clienteFTP.disconnect();
                            return;
                        default:
                            System.out.println("Opción no válida.");
                    }
                }
            } else {
                System.out.println("Error al iniciar sesión en el servidor FTP.");
            }
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (clienteFTP.isConnected()) {
                    clienteFTP.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static void listarArchivos(FTPClient clienteFTP) throws IOException {
        System.out.println("Archivos disponibles en el servidor FTP:");
        for (String nombre : clienteFTP.listNames()) {
            System.out.println(" - " + nombre);
        }
    }

    private static void descargarArchivo(FTPClient clienteFTP, String archivoRemoto, String rutaLocal) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(rutaLocal)) {
            boolean exito = clienteFTP.retrieveFile(archivoRemoto, fos);
            if (exito) {
                System.out.println("Archivo descargado correctamente.");
            } else {
                System.out.println("Error al descargar el archivo.");
            }
        }
    }

    private static void enviarCorreo(String archivoAdjunto) {
        Scanner scanner = new Scanner(System.in);

        // Solicitar datos del correo
        System.out.print("Introduce la dirección de correo del destinatario: ");
        String destinatario = scanner.nextLine();
        System.out.print("Introduce el asunto del correo: ");
        String asunto = scanner.nextLine();
        System.out.print("Introduce el mensaje del correo: ");
        String mensajeTexto = scanner.nextLine();

        String servidorSMTP = "smtp.gmail.com";
        int puertoSMTP = 587;
        String usuarioSMTP = "mparrallanson@gmail.com";
        String contrasenaSMTP = "xymqjpdjdvqyrgpp";

        AuthenticatingSMTPClient clienteSMTP = new AuthenticatingSMTPClient("TLS");
        try {
            // Conectar al servidor SMTP
            clienteSMTP.connect(servidorSMTP, puertoSMTP);
            if (!SMTPReply.isPositiveCompletion(clienteSMTP.getReplyCode())) {
                System.err.println("Conexión rechazada.");
                clienteSMTP.disconnect();
                return;
            }

            // Saludo EHLO
            clienteSMTP.ehlo("localhost");
            System.out.println("Respuesta EHLO: " + clienteSMTP.getReplyString());

            // Inicia STARTTLS
            if (!clienteSMTP.execTLS()) {
                System.err.println("Error al iniciar STARTTLS: " + clienteSMTP.getReplyString());
                clienteSMTP.disconnect();
                return;
            }
            System.out.println("STARTTLS iniciado correctamente.");

            // Autenticación
            if (!clienteSMTP.auth(AuthenticatingSMTPClient.AUTH_METHOD.LOGIN, usuarioSMTP, contrasenaSMTP)) {
                System.err.println("Error de autenticación: " + clienteSMTP.getReplyString());
                clienteSMTP.disconnect();
                return;
            }

            // Configuración del mensaje
            SimpleSMTPHeader cabecera = new SimpleSMTPHeader(usuarioSMTP, destinatario, asunto);

            clienteSMTP.setSender(usuarioSMTP);
            clienteSMTP.addRecipient(destinatario);

            var writer = clienteSMTP.sendMessageData();
            if (writer != null) {
                writer.write(cabecera.toString());
                writer.write("\n");
                writer.write(mensajeTexto);

                // Adjuntar archivo si se seleccionó
                if (archivoAdjunto != null) {
                    writer.write("\n\nArchivo adjunto: " + archivoAdjunto);
                    System.out.println("Archivo adjunto incluido: " + archivoAdjunto);
                }

                writer.close();
                if (clienteSMTP.completePendingCommand()) {
                    System.out.println("Correo enviado correctamente.");
                } else {
                    System.err.println("Error al enviar el correo.");
                }
            } else {
                System.err.println("Error al preparar el mensaje.");
            }

            // Cerrar sesión
            clienteSMTP.logout();
            clienteSMTP.disconnect();
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException | InvalidKeySpecException ex) {
            System.err.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
