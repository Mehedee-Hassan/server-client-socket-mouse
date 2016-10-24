package com.liquiddark.mousepad.mhr.mousepad;

import android.app.Activity;
import android.net.wifi.ScanResult;
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
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.regex.Pattern;

public class MessageActivity extends Activity
    {
    static String TAG = "_MessageActivity";

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
                    Log.d(TAG,"==onclick=="+"0");
                  //  Socket socket = new Socket(ipAddress, 8080);

                    Log.d(TAG,"==onclick=="+"1");
                    //DataOutputStream DOS = new DataOutputStream(socket.getOutputStream());
                    Log.d(TAG,"==onclick=="+"2");

                    //DOS.writeUTF("HELLO_WORLD");
                    Log.d(TAG,"==onclick=="+"3");
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
            String ipAddress = //Formatter.formatIpAddress(wifiMgr.getDhcpInfo().serverAddress);
            Formatter.formatIpAddress(ip);


            List<ScanResult> results = wifiMgr.getScanResults();





            Log.d(TAG ,"ip = "+ Formatter.formatIpAddress(wifiMgr.getDhcpInfo().gateway)+"  "+Formatter.formatIpAddress(wifiMgr.getDhcpInfo().serverAddress));
            return ipAddress;
        }


    }


 class Client extends AsyncTask<Void, Void, Void> {

     String TAG = "_MessageActivity";
    String dstAddress;
    int dstPort;
    String response = "";
     


    Client(String ip) {

        Log.d(TAG ,"|"+ip+"|");
        dstAddress = ip;
        dstPort = 8888;
    }

    @Override
    protected Void doInBackground(Void... arg0) {

        Socket socket = null;



        //===
        try {

            Log.d(TAG,"__+++___"+dstAddress);


            String[] temp = dstAddress.split(Pattern.quote("."));


            // IPv4 usage
            byte[] ip = new byte[4];//= localhost.getAddress();


            ip[0] = (byte)(Integer.parseInt(temp[0]));
            ip[1] = (byte)(Integer.parseInt(temp[1]));
            ip[2] = (byte)(Integer.parseInt(temp[2]));
            ip[3] = (byte)(Integer.parseInt(temp[3]));

            ip[0] &=  0xFF;
            ip[1] &=  0xFF;
            ip[2] &=  0xFF;
            ip[3] &=  0xFF;

            Log.d(TAG,""+ip.length+" "+ip[0]+" "+ip[1]);


            boolean first = true;
            // looping
            for (int i = 1; i <= 254; i++) {
                ip[3] = (byte) i;
                InetAddress address = InetAddress.getByAddress(ip);

                //Log.d(TAG, "doInBackground: address =" +address);
                if (address.isReachable(500)) {


///// TODO: 10/23/16 make an array of ip for all diveice that has that specific port open

                    try {

                        Log.d(TAG, "doInBackground: address =" +address+" "+address.getHostName() +" "+address.getHostAddress()
+" "+address.getCanonicalHostName()
                        );

                        socket = new Socket(address.getHostAddress(), 9000);

                        Log.d(TAG," try catch ="+address.toString());

                        DataOutputStream DOS = new DataOutputStream(socket.getOutputStream());
                        DOS.writeUTF(address.getHostAddress());





                    } catch(Exception ex){
                        Log.d(TAG ,"exception ");
                        continue;

                    }
//                    catch (UnknownHostException e) {
//
//                        e.printStackTrace();
//
//                    } catch (ConnectException e) {
//
//                        Log.d(TAG ,"exception ");
//                        //continue;
//
//                    }

                    finally {
                        if (socket != null) {
                            try {
                                socket.close();
                            } catch (IOException e) {


                                e.printStackTrace();
                            }
                        }
                    }

                } else if (!address.getHostAddress().equals(address.getHostName())) {


                } else {
                    //System.out.println(address + " - the host address and the host name are same");
                }
            }
        }catch (Exception ex){

        }
        //===





        return null;
    }



}