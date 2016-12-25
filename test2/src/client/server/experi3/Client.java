package client.server.experi3;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Created by mhr on 10/22/16.
 */
public class Client {

    public static void main(String[] args) throws IOException {

        try {

            String sentence;
            String modifiedSentence;
            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));


//            Socket clientSocket = new Socket("192.168.1.101", 1234);
//
//            DataOutputStream DOS = new DataOutputStream(clientSocket.getOutputStream());
            //DOS.writeUTF(FLAG);

            Socket clientSocket;// = new Socket("192.168.1.101", 1234);



            {
                clientSocket = new Socket("192.168.1.103", 1239);
                DataOutputStream DOS = new DataOutputStream(clientSocket.getOutputStream());
                DOS.write(("18 -12 2").getBytes());
                byte [] test = new byte[100];
                DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());
                 dataInputStream.read(test);

                 System.out.println("FROM SERVER: " + new String(test, StandardCharsets.UTF_8));

                clientSocket.close();

            }

            //DOS.flush();

            System.out.println(" flashued ");





            // System.out.println("FROM SERVER: " + dataInputStream.readUTF());


          //  System.out.println(" message = "+modifiedSentence);


        }catch (Exception ex){
            System.out.print("error = "+ex.getMessage());
        }
    }
}
