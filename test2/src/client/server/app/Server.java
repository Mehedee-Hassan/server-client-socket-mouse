package client.server.app;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by mhr on 10/21/16.
 */
public class Server {


    public static void main(String [] args) throws IOException{


        ServerSocket serverSocket = new ServerSocket(8888);
Socket clientSocket = null;
int i = 1;   
while(i++ < 10){
	 clientSocket = serverSocket.accept();
		
        DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());
        String message = dataInputStream.readUTF();

        System.out.print(message);

}

	if(clientSocket!= null)
        clientSocket.close();
        serverSocket.close();


    }

}
