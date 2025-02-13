package U_4;
import java.io.BufferedReader;
import java.io.IOException;

import org.apache.commons.net.pop3.POP3SClient;
import org.apache.commons.net.pop3.POP3MessageInfo;

public class Ejemplo4POP3 {
	public static void main(String[] args) {
		String server = "localhost", username = "usu1", password = "usu1";
		int puerto = 110;
		
		POP3SClient pop3SClient = new POP3SClient();
		try {
			//conectamos al servidor
			pop3SClient.connect(server, puerto);
			System.out.println("Conexión realizada al servidor POP3 " + server);
			
			//inicio de sesión
			if (!pop3SClient.login(username,password))
				System.err.println("Error al hacer login");
			else {
				//obtenemos todos los mensajes en un array
				POP3MessageInfo[] pop3MessageInfo = pop3SClient.listMessages();
				
				if (pop3MessageInfo == null)
					System.out.println("Imposible recuperar mensajes.");
				else
					System.out.println("Nº de mensajes: "+ pop3MessageInfo.length);
				
				recuperarTodo(pop3MessageInfo, pop3SClient);
				
				//finalizar sesión
				pop3SClient.logout();
			}//else
		
			//desconectamos
			pop3SClient.disconnect();
			
		} catch (IOException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}//try-catch
		System.exit(0);
	}//main
	
	
	private static void recuperarTodo (POP3MessageInfo[] pop3MessageInfo, 
							POP3SClient pop3SClient) throws IOException {
		for (int i=0; i< pop3MessageInfo.length; i++) {
			System.out.println("Mensaje: " + (i+1));
			POP3MessageInfo msgInfo = pop3MessageInfo[i]; //lista de mensajes
			
			//recupera todo el mensaje
			System.out.println("Mensaje:");
			BufferedReader bufferedReader =
					(BufferedReader) pop3SClient.retrieveMessage(msgInfo.number);
			String linea;
			while ((linea = bufferedReader.readLine()) !=null)
				System.out.println(linea.toString());
			bufferedReader.close();
		}//for
	}//recuperarTodo
	
}//Ejemplo2POP3