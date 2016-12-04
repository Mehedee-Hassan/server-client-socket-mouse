package client.server.experi3;

import java.io.*;
import java.net.Socket;

/**
 * Created by mhr on 10/22/16.
 */
public class Client {

    public static void main(String[] args) throws IOException {

        try {

            String sentence;
            String modifiedSentence;
            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));


            Socket clientSocket = new Socket("192.168.1.103", 1239);

            DataOutputStream DOS = new DataOutputStream(clientSocket.getOutputStream());
            //DOS.writeUTF(FLAG);


            for(int i = 10 ; i <15 ; i++)
            DOS.write((i +
                    " asdfsadf|  ").getBytes());



            DOS.flush();

            System.out.println(" flashued ");



          //  DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());
           // modifiedSentence = dataInputStream.readLine();


            // System.out.println("FROM SERVER: " + dataInputStream.readUTF());


          //  System.out.println(" message = "+modifiedSentence);

            clientSocket.close();
        }catch (Exception ex){
            System.out.print("error = "+ex.getMessage());
        }
    }
}
