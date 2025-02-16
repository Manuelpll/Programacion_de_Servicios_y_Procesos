package U_4.Examen.Examen4;

import java.io.IOException;
import java.io.Writer;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.spec.InvalidKeySpecException;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import org.apache.commons.net.smtp.*;

public class ClienteSMTP_EnviarCorreo_Relay {
    public static void main(String[] args) throws NoSuchAlgorithmException, UnrecoverableKeyException, KeyStoreException, InvalidKeyException, InvalidKeySpecException {

        // Crear cliente SMTP
        AuthenticatingSMTPClient clienteSMTP = new AuthenticatingSMTPClient();

        // Configuración del servidor Mercury (localhost) con relay a Gmail
        String servidorSMTP = "localhost"; // Mercury Mail en localhost
        int puertoSMTP = 25; // Puerto estándar para SMTP
        String remitente = "postmaster@localhost"; // Usuario configurado en Mercury Mail
        String destinatario = "mparrallanson@gmail.com"; // Correo real de destino
        String asunto = "Correo enviado a través de Mercury y relay con Gmail";
        String mensajeTexto = "Hola,\nEste correo fue enviado desde Mercury Mail utilizando un retransmisor SMTP configurado con Gmail.\nSaludos.";

        try {
            int respuesta;

            // Crear clave para comunicación segura
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(null, null);
            KeyManager keyManager = keyManagerFactory.getKeyManagers()[0];

            // Conectar al servidor SMTP de Mercury
            clienteSMTP.connect(servidorSMTP, puertoSMTP);
            System.out.println("Conectado al servidor SMTP: " + clienteSMTP.getReplyString());
            clienteSMTP.setKeyManager(keyManager);

            // Verificar conexión
            respuesta = clienteSMTP.getReplyCode();
            if (!SMTPReply.isPositiveCompletion(respuesta)) {
                clienteSMTP.disconnect();
                System.err.println("Conexión rechazada.");
                return;
            }

            // Saludo EHLO
            clienteSMTP.ehlo("localhost");
            System.out.println("Respuesta EHLO: " + clienteSMTP.getReplyString());


            // Configuración del mensaje
            SimpleSMTPHeader cabecera = new SimpleSMTPHeader(remitente, destinatario, asunto);

            clienteSMTP.setSender(remitente);
            clienteSMTP.addRecipient(destinatario);
            System.out.println("Destinatario añadido: " + clienteSMTP.getReplyString());

            // Enviar mensaje DATA
            Writer writer = clienteSMTP.sendMessageData();
            if (writer == null) {
                System.err.println("Fallo al enviar DATA.");
                clienteSMTP.disconnect();
                return;
            }

            // Escribir cabecera y mensaje
            writer.write(cabecera.toString());
            writer.write(mensajeTexto);
            writer.close();
            System.out.println("Mensaje enviado: " + clienteSMTP.getReplyString());

            // Completar la transacción
            if (!clienteSMTP.completePendingCommand()) {
                System.err.println("Fallo al finalizar la transacción.");
            } else {
                System.out.println("Correo enviado con éxito.");
            }

            // Cerrar conexión
            clienteSMTP.logout();
            clienteSMTP.disconnect();
        } catch (IOException | NoSuchAlgorithmException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
