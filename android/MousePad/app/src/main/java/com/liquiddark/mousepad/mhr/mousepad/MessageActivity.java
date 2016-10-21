package com.liquiddark.mousepad.mhr.mousepad;

import android.app.Activity;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class MessageActivity extends Activity
    {


    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        Log.d("==on click view ===" ,"created");



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    String ipAddress = getIp();


                    Client client = new Client(ipAddress);
                    client.execute();
                    Log.d("==onclick==","0");
                  //  Socket socket = new Socket(ipAddress, 8080);

                    Log.d("==onclick==","1");
                    //DataOutputStream DOS = new DataOutputStream(socket.getOutputStream());
                    Log.d("==onclick==","2");

                    //DOS.writeUTF("HELLO_WORLD");
                    Log.d("==onclick==","3");
                   // socket.close();




                }catch (Exception ex){
                    Log.e("==on click button ===" ,"error == "+ex);
                }
            }
        });
    }

        private String getIp() {
            WifiManager wifiMgr = (WifiManager) getSystemService(WIFI_SERVICE);
            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
            int ip = wifiInfo.getIpAddress();
            String ipAddress = Formatter.formatIpAddress(ip);

            Log.d("on click " ,"ip = "+ipAddress);
            return ipAddress;
        }


    }


 class Client extends AsyncTask<Void, Void, Void> {

    String dstAddress;
    int dstPort;
    String response = "";


    Client(String ip) {


        dstAddress = ip;
        dstPort = 8888;
    }

    @Override
    protected Void doInBackground(Void... arg0) {

        Socket socket = null;

        try {
            socket = new Socket("192.168.1.101", 8888);


            Log.d("==onclick==","1");
            DataOutputStream DOS = new DataOutputStream(socket.getOutputStream());
            Log.d("==onclick==","2");

            DOS.writeUTF("HELLO_WORLD");
            Log.d("==onclick==","3");

        } catch (UnknownHostException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {


                    e.printStackTrace();
                }
            }
        }
        return null;
    }



}