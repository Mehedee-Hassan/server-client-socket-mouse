package client.server.app;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by mhr on 10/22/16.
 */
public class Client {

    public static void main(String[] args){
        Socket socket = null;
        try {
            socket = new Socket("192.168.1.101", 8888);


        DataOutputStream DOS = null;
            DOS = new DataOutputStream(socket.getOutputStream());
            DOS.writeUTF("HELLO_WORLD");

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
}
