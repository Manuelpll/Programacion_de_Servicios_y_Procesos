package U_4.Examen.Examen4;

import org.apache.commons.net.smtp.SMTPClient;
import org.apache.commons.net.smtp.SMTPReply;

import java.io.IOException;
import java.io.Writer;

public class ClienteSMTP_EnviarCorreo {
	public static void main(String[] args) {
		SMTPClient clienteSMTP = new SMTPClient();
		try {
			int respuesta;
			// NOS CONECTAMOS AL PUERTO 25
			clienteSMTP.connect("localhost");
			System.out.print(clienteSMTP.getReplyString());
			respuesta = clienteSMTP.getReplyCode();

			if (!SMTPReply.isPositiveCompletion(respuesta)) {
				clienteSMTP.disconnect();
				System.err.println("CONEXION RECHAZADA.");
				System.exit(1);
			} else{
				System.out.println("CONEXION REALIZADA.");
			}

			// REALIZAR ACCION: enviar un correo
			// Saludo EHLO
			clienteSMTP.sendCommand("ehlo localhost");
			System.out.println(clienteSMTP.getReplyString());

			// Configuración del mensaje
			String remitente = "postmaster@localhost";
			String destinatario = "mparrallanson@gmail.com";
			String asunto = "Prueba de correo";
			String mensaje = "Hola,\nEste es un mensaje de prueba enviado desde Java2.";

			// Enviar MAIL FROM
			if (!clienteSMTP.setSender(remitente)) {
				System.err.println("Error al configurar el remitente.");
				clienteSMTP.disconnect();
				return;
			}
			System.out.println("Remitente configurado correctamente.");

			// Enviar RCPT TO
			if (!clienteSMTP.addRecipient(destinatario)) {
				System.err.println("Error al añadir el destinatario.");
				clienteSMTP.disconnect();
				return;
			}
			System.out.println("Destinatario añadido correctamente.");

			// Enviar DATA
			Writer writer = clienteSMTP.sendMessageData();
			if (writer != null) {
				writer.write("Subject: " + asunto + "\n");
				writer.write("\n"); // Línea vacía entre el asunto y el cuerpo
				writer.write(mensaje);
				writer.close();
				if (clienteSMTP.completePendingCommand()) {
					System.out.println("Mensaje enviado correctamente.");
				} else {
					System.err.println("Error al enviar el mensaje.");
				}
			} else {
				System.err.println("Error al enviar DATA.");
			}

			// Nos desconectamos
			clienteSMTP.disconnect();
			System.out.println("Desconexión realizada.");

		} catch (IOException e) {
			System.err.println("NO SE PUEDE CONECTAR AL SERVIDOR.");
			e.printStackTrace();
			System.exit(2);
		}

	}// main
}// ..
