package client.server.experi2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by mhr on 10/21/16.
 */
public class Server {
    public static String FLAG = "_SEARCH";
    public static String N_FLAG = "_MOUSE";

    public static int flag_block = -1;
    public static void main(String [] args) throws IOException{


        ServerSocket serverSocket = new ServerSocket(9000);


        while (true) {
            Socket clientSocket = null;
            clientSocket = serverSocket.accept();


            DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());
            String flag = dataInputStream.readUTF();


            System.out.println(flag);


            if (flag.equalsIgnoreCase(FLAG)) {
                flag_block = 1;

            System.out.println(flag_block);

            //if (flag_block == 1)
            {
                DataOutputStream outToClient = new DataOutputStream(clientSocket.getOutputStream());
                outToClient.writeUTF(getHostName());
            } }else
            //else if (flag_block == 2)
            {


                {
                   // clientSocket = serverSocket.accept();
                    dataInputStream = new DataInputStream(clientSocket.getInputStream());
                    String message = dataInputStream.readUTF();

                    System.out.println(message);


                }
            }


        }
    }

    private static void sendHostNameToClient(String ip) throws UnknownHostException {
        String name =  getHostName();

        Socket socket = null;
        try {
            socket = new Socket(ip, 9000);

            DataOutputStream DOS = null;
            DOS = new DataOutputStream(socket.getOutputStream());
            DOS.writeUTF("HELLO_WORLD");

            System.out.print("sendhost name ");
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(socket!=null)
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }



    }

    private static String getHostName() {
        String name = "LocalHost";
        try {
            InetAddress address = InetAddress.getLocalHost();

            name = address.getHostName();
        }
        catch (Exception ex){}
        return name;
    }

}
