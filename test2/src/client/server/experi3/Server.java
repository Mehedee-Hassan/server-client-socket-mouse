package client.server.experi3;


import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by mhr on 10/21/16.
 */
public class Server {


    public static void main(String [] args) throws IOException {

        String clientSentence;
        String capitalizedSentence;
        ServerSocket welcomeSocket = new ServerSocket(1239);

        while(true)
        {
            Socket connectionSocket = welcomeSocket.accept();


            BufferedReader inFromClient =
                    new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));


            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            clientSentence = inFromClient.readLine();


            System.out.println("Received: " + clientSentence+"\n");

//            capitalizedSentence = clientSentence.toUpperCase() + '\n';

//            outToClient.writeBytes(capitalizedSentence);
        }
    }


}
