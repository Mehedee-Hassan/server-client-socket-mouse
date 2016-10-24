package com.liquiddark.mousepad.mhr.mousepad;

import android.app.Activity;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class MessageActivity extends Activity
     {

        static String TAG = "_MessageActivity";
        public static ArrayAdapter<String> ClientList ;
        ListView clientListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        {

            try {
                String ipAddress = getIp();


                Client client = new Client(ipAddress);
                client.execute();


            } catch (Exception ex) {
                Log.e(TAG, "on click button error == " + ex);
            }

            ClientList = new ArrayAdapter<String>(this, R.layout.list_view_item1, Client._hostNameList);





            clientListView = (ListView) findViewById(R.id.clientListView);
            clientListView.setAdapter(ClientList);

            clientListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent openMousePad = new Intent(MessageActivity.this, ReceiveActivity.class);
                    openMousePad.putExtra("_IP", Client._ipList.get(position));

                    startActivity(openMousePad);

                }
            });
        }
    }



        private String getIp() {
            WifiManager wifiMgr = (WifiManager) getSystemService(WIFI_SERVICE);
            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
            int ip = wifiInfo.getIpAddress();
            String ipAddress = Formatter.formatIpAddress(ip);                 //Formatter.formatIpAddress(wifiMgr.getDhcpInfo().serverAddress);

            return ipAddress;
        }


        @Override
        protected void onRestart() {
            super.onRestart();

        }

        @Override
        protected void onResume() {
            super.onResume();

        }

        @Override
        protected void onPause() {
            super.onPause();
            onStop();
        }



}


 class Client extends AsyncTask<Void, Void, Void> {


     public static List<String> _hostNameList;
     public static List<String> _ipList;



    String TAG = "_MessageActivity";
    String dstAddress;

    String response = "";




    Client(String ip) {

        Log.d(TAG ,"|"+ip+"|");
        dstAddress = ip;

        _hostNameList = new ArrayList<>();
        _ipList = new ArrayList<>();


    }

    @Override
    protected Void doInBackground(Void... arg0) {

        Socket socket = null;



        //===
        try {

            Log.d(TAG,"__+++___"+dstAddress);


            String[] temp = dstAddress.split(Pattern.quote("."));
            byte[] ip = new byte[4];


            ip[0] = (byte)(Integer.parseInt(temp[0]));
            ip[1] = (byte)(Integer.parseInt(temp[1]));
            ip[2] = (byte)(Integer.parseInt(temp[2]));
            ip[3] = (byte)(Integer.parseInt(temp[3]));
            int tempIp4thPart = (Integer.parseInt(temp[3]));

            ip[0] &=  0xFF;
            ip[1] &=  0xFF;
            ip[2] &=  0xFF;
            ip[3] &=  0xFF;

            Log.d(TAG,""+ip.length+" "+ip[0]+" "+ip[1]);


            boolean first = true;


            clear_IpAndHostList();
            for (int i = 1; i <= 254 ; i++) {

                ip[3] = (byte) i;

                (new Thread(new IpTest(ip))).start();

            }
        }catch (Exception ex){

        }
        //===





        return null;
    }


     @Override
     protected void onPostExecute(Void aVoid) {
         super.onPostExecute(aVoid);


         Log.d(TAG,"ip list size = "+ _hostNameList.size());


         MessageActivity.ClientList.notifyDataSetChanged();


         for(String a : _hostNameList){
             Log.d(TAG, " list element ="+a);

         }
     }

     public static void clear_IpAndHostList(){
        _hostNameList.clear();
         _ipList.clear();
    }
}




class IpTest implements Runnable{


    public static String FLAG = "_SEARCH";
    public static String N_FLAG = "_MOUSE";

    String TAG = "IpTest";
    byte[] ipAddress ;

    public IpTest(){

    }

    public IpTest(byte[] ipAddress){
        this.ipAddress = ipAddress;
    }





    @Override
    public void run() {
        test(getHostAddress());
    }

    private String getHostAddress() {
        InetAddress addr = null;
        try {
            addr = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        String ip = addr.getHostAddress();


        return ip;
    }


    void test(String dstAddress){
        Socket socket = null;



        //===
        try {


            {
                InetAddress address = InetAddress.getByAddress(ipAddress);


                if (address.isReachable(1500)) {



                    try {

                        Log.d(TAG ,"doInBackground: address =" +address+" "+address.getHostName() +" "+address.getHostAddress()
                                +" "+address.getCanonicalHostName()
                        );

                        socket = new Socket(address, 9000);


                        Log.d(TAG ," try catch ="+address.toString()+" "+address.getHostName());

                        Client._ipList.add(address.getHostAddress());

                        DataOutputStream DOS = new DataOutputStream(socket.getOutputStream());
                        DOS.writeUTF(FLAG);



                        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                        String serverHostName = "" ;

                         serverHostName
                                = dataInputStream.readUTF();

                        Log.d(TAG ,"server's host name = "+serverHostName);


                        Client._hostNameList.add(serverHostName);


                    } catch(Exception ex){
                        Log.d(TAG ,"exception ");


                    }


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

                }
            }
        }catch (Exception ex){

        }
    }


}