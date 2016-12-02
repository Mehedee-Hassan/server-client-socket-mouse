package liquiddark.mousepad.socket;

import java.io.IOException;
import java.net.Socket;

import liquiddark.mousepad.constant.Constant;

/**
 * Created by mhr on 03-Dec-16.
 */

public class Connection {


    private Socket socket;
    public Connection(){

    }


    public boolean createNewSocket(String address){

        try {
            socket = new Socket(address, Constant.GLOBAL_PORT_NUMBER);


            return true;

        } catch (IOException e) {


            e.printStackTrace();
            return false;
        }


    }


    public Socket getSocket(){

        return socket;
    }

}
