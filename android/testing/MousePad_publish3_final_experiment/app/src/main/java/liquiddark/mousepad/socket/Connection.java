package liquiddark.mousepad.socket;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import liquiddark.mousepad.PcListActivity;
import liquiddark.mousepad.constant.Constant;

/**
 * Created by mhr on 03-Dec-16.
 */

public class Connection implements Runnable{

    private Context context;
    private static Socket socket;
    static DataOutputStream  DOS;
    private InetAddress address;
    private byte[] ip;

    public Connection(){

    }


    public void initialize(byte [] ip, Activity context){
        this.ip = ip;
        address=null;
        this.context = context;
    }


    public boolean sendData(String val){

        Log.d("_LL connection ",""+val);

        if(socket!=null){
            Log.d("_LL connection ","socket not null"+val);

            try {
                DOS = new DataOutputStream(socket.getOutputStream());

                DOS.flush();
                DOS.write(val.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public Socket getSocket(){

        return socket;
    }


    public boolean  ping(String url) {

        try {

            Process process = Runtime.getRuntime().exec(
                    "/system/bin/ping -c 1 " + url);

            int val = process.waitFor();


            //Log.d(TAG+" check", "check&&&& =" + url+"| "+process.getInputStream().toString() );


            if(val == 0)
            {
                process.destroy();

                return true;
            }
            else{
                return false;
            }




        } catch (IOException e) {
            // body.append("Error\n");
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void run() {
        try {
            address = InetAddress.getByAddress(ip);

            socket = new Socket(address, Constant.GLOBAL_PORT_NUMBER);
            Log.d("_LL connection ","init socket");

        } catch (IOException e) {
            e.printStackTrace();


            if(address == null || !ping(address.getHostAddress())){
                context.startActivity(new Intent(context,PcListActivity.class));
            }
        }

    }
}
