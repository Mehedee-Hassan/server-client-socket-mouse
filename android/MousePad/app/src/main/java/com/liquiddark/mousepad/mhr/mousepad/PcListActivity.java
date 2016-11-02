package com.liquiddark.mousepad.mhr.mousepad;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.Image;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.liquiddark.mousepad.mhr.mousepad.adapter.Item;
import com.liquiddark.mousepad.mhr.mousepad.adapter.PcListArrayAdapter;

import org.w3c.dom.Text;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.regex.Pattern;

public class PcListActivity extends Activity {

    static String TAG = "_MessageActivity";

    public  ArrayAdapter<String> clientListAdapter;


    public PcListArrayAdapter pcListArrayAdapter;

    public static boolean REFRESH_BUTTON_CLICKED = false ;

    ListView clientListView;
    public  ArrayList<String> _hostNameList;
    public  ArrayList<String> _ipList;
    ProgressBar progressBarClientList ;


    ImageView refreshImageView;
    TextView tv,listViewMessagetextView;
    Typeface CustomFontSegoePrint ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        initActivity();
        enableWifi();

    }



    private void enableWifi() {
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if(!wifiManager.isWifiEnabled()){
            Toast.makeText(this,"Please enable wifi .. ",Toast.LENGTH_LONG).show();
            this.startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS),0);
        }
    }



    private void initActivity() {
        CustomFontSegoePrint = Typeface.createFromAsset(getAssets(), "fonts/Segoe_Print.ttf");


        setContentView(R.layout.activity_pc_list);


        tv = (TextView) findViewById(R.id.listViewTitileTextView);
        refreshImageView = (ImageView) findViewById(R.id.refreshImageView);

        tv.setTypeface(CustomFontSegoePrint,Typeface.BOLD);
        listViewMessagetextView = (TextView) findViewById(R.id.listViewMessagetextView);
        listViewMessagetextView.setTypeface(CustomFontSegoePrint);
        listViewMessagetextView.setTypeface(CustomFontSegoePrint,Typeface.BOLD);

        initClient();




        _hostNameList = new ArrayList<>();
        _ipList = new ArrayList<>();


        //    clientListAdapter = new ArrayAdapter<String>(this, R.layout.list_view_item1, _hostNameList);
        pcListArrayAdapter = new PcListArrayAdapter(this,R.layout.list_view_custom_item,_hostNameList);


        progressBarClientList = (ProgressBar) findViewById(R.id.progressBarClientList);
        clientListView = (ListView) findViewById(R.id.clientListView);
        clientListView.setAdapter(pcListArrayAdapter);


        clientListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent openMousePad = new Intent(PcListActivity.this, PadActivity.class);
                openMousePad.putExtra("_IP", _ipList.get(position));

                startActivity(openMousePad);

            }
        });


        refreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                REFRESH_BUTTON_CLICKED = true;

                initClient();

            }
        });


        listViewMessagetextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://bokadanob.wordpress.com/mousepad/"));
                startActivity(browserIntent);
            }
        });


    }
    public static int tt  = 0;

    private void initClient() {


        try {

            Log.e(TAG, "on click button error == "+tt++  );

        String ipAddress = getIp();
        Client client = new Client(ipAddress);
        client.execute();


        } catch (Exception ex) {
            Log.e(TAG, "on click button error == " + ex +" "+(tt++));

            //Toast.makeText(this ,"Is your pc running mouse pad ?",Toast.LENGTH_LONG).show();

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


        initClient();
        enableWifi();


        new android.os.Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                pcListArrayAdapter.notifyDataSetChanged();


                new android.os.Handler().postDelayed(new Runnable(){
                    @Override
                    public void run() {
                        if(_hostNameList.size() == 0){
                            tv.setText("No device Found!!");
                            listViewMessagetextView.setVisibility(View.VISIBLE);

                        }else{
                            tv.setText("Select your pc form the list");
                            listViewMessagetextView.setVisibility(View.GONE);

                        }
                    }
                }, 1000);
            }
        }, 2000);






    }


    @SuppressLint("NewApi")
    @Override
    protected void onResume() {
        super.onResume();


        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }


        Log.d(TAG, "on pause ip list count = " + pcListArrayAdapter.getCount());

        initClient();
        enableWifi();

        new android.os.Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                pcListArrayAdapter.notifyDataSetChanged();



                        if(_hostNameList.size() == 0){
                            tv.setText("No device Found!!");
                            listViewMessagetextView.setVisibility(View.VISIBLE);

                        }else{
                            tv.setText("Select your pc form the list");
                            listViewMessagetextView.setVisibility(View.GONE);

                        }

            }
        }, 2000);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }




    class Client extends AsyncTask<Void, Void, Void> {



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBarClientList.setVisibility(View.VISIBLE);
        }

        String TAG = "_MessageActivandroid.util.DisplayMetricsity";
        String dstAddress;
        String response = "";


        Client(String ip) {
            Log.d(TAG, "|" + ip + "|");
            dstAddress = ip;
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            Socket socket = null;

            try {

                Log.d(TAG, "__+++___" + dstAddress);


                String[] temp = dstAddress.split(Pattern.quote("."));
                byte[] ip = new byte[4];


                ip[0] = (byte) (Integer.parseInt(temp[0]));
                ip[1] = (byte) (Integer.parseInt(temp[1]));
                ip[2] = (byte) (Integer.parseInt(temp[2]));
                ip[3] = (byte) (Integer.parseInt(temp[3]));
                int tempIp4thPart = (Integer.parseInt(temp[3]));

                ip[0] &= 0xFF;
                ip[1] &= 0xFF;
                ip[2] &= 0xFF;
                ip[3] &= 0xFF;

                Log.d(TAG, "" + ip.length + " " + ip[0] + " " + ip[1]);


                // clear_IpAndHostList();
                for (int i = 1; i <= 254; i++) {

                    ip[3] = (byte) i;
//                    test(ip);
                   (new Thread(new IpTest(ip))).start();

                }
            } catch (Exception ex) {

            }


            return null;
        }



        public  String FLAG = "_SEARCH";
        public  String N_FLAG = "_MOUSE";



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


        void test(byte[] ipAddress) {
            Socket socket = null;

            Log.d(TAG, "" +"test method");

            //===
            try {

                    InetAddress address = InetAddress.getByAddress(ipAddress);


                    if (address.isReachable(1500)) {


                        try {

                            Log.d(TAG, "doInBackground: address =" + address + " " + address.getHostName() + " " + address.getHostAddress()
                                    + " " + address.getCanonicalHostName()
                            );

                            socket = new Socket(address, 1239);


                            Log.d(TAG, " try catch =" + address.toString() + " " + address.getHostName());

                            _ipList.add(address.getHostAddress());

                            DataOutputStream DOS = new DataOutputStream(socket.getOutputStream());
                            DOS.writeUTF(FLAG);


                            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                            String serverHostName = "";

                            serverHostName
                                    = dataInputStream.readUTF();

                            Log.d(TAG, "server's host name = " + serverHostName);


                            _hostNameList.add(serverHostName);


                        } catch (Exception ex) {
                            Log.d(TAG, "exception ");


                        } finally {
                            if (socket != null) {
                                try {
                                    socket.close();
                                    // pcListArrayAdapter.notifyDataSetChanged();
                                    //pcListArrayAdapter.notifyDataSetChanged();

                                } catch (IOException e) {


                                    e.printStackTrace();
                                }
                            }
                        }

                    } else if (!address.getHostAddress().equals(address.getHostName())) {


                    } else {

                    }

            } catch (Exception ex) {

            }
           // pcListArrayAdapter.notifyDataSetChanged();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            Log.d(TAG, "ip list size = " + _hostNameList.size());


//            pcListArrayAdapter = new ArrayAdapter<String>(getApplicationContext() , R.layout.list_view_item1, _hostNameList);
//            clientListView = (ListView) findViewById(R.id.clientListView);
//            clientListView.setAdapter(pcListArrayAdapter);
//            pcListArrayAdapter.notifyDataSetChanged();


            for (String a : _hostNameList) {
                Log.d(TAG, " list element =" + a);

            }


            new android.os.Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    progressBarClientList.setVisibility(View.GONE);


                    new android.os.Handler().postDelayed(new Runnable(){
                        @Override
                        public void run() {
                            if(REFRESH_BUTTON_CLICKED == true) {
                                initClient();
                                REFRESH_BUTTON_CLICKED = false;
                            }
                        }
                    }, 1000);

                }
            }, 1000);

                if(_hostNameList.size() == 0){
                    tv.setText("No device Found!!");
                    listViewMessagetextView.setVisibility(View.VISIBLE);

                }else{
                    tv.setText("Select your pc form the list");
                    listViewMessagetextView.setVisibility(View.GONE);

                }

        }


    }


    class IpTest implements Runnable {


        public  String FLAG = "_SEARCH";
        public  String N_FLAG = "_MOUSE";

        String TAG = "IpTest";
        byte[] ipAddress;

        public IpTest() {

        }

        public IpTest(byte[] ipAddress) {
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


        synchronized void test(String dstAddress) {
            Socket socket = null;
            boolean hasIp = false;

            //===
            try {


                {
                    InetAddress address = InetAddress.getByAddress(ipAddress);


                    if (address.isReachable(1500)) {


                        try {

                            Log.d(TAG, "doInBackground: address =" + address + " " + address.getHostName() + " " + address.getHostAddress()
                                    + " " + address.getCanonicalHostName()
                            );

                            socket = new Socket(address, 1239);


                            Log.d(TAG, " try catch =" + address.toString() + " " + address.getHostName());


                            if(!_ipList.contains(address.getHostAddress())) {
                                _ipList.add(address.getHostAddress());

                            }else
                                hasIp = true;



                            DataOutputStream DOS = new DataOutputStream(socket.getOutputStream());
                            DOS.writeUTF(FLAG);


                            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                            String serverHostName = "";

                            serverHostName
                                    = dataInputStream.readUTF();

                            Log.d(TAG, "server's host name = " + serverHostName);

                            if(!hasIp)
                                _hostNameList.add(serverHostName);


                        } catch (Exception ex) {
                            Log.d(TAG, "exception ");


                        } finally {
                            if (socket != null) {
                                try {
                                    socket.close();
                                   // pcListArrayAdapter.notifyDataSetChanged();

                                } catch (IOException e) {


                                    e.printStackTrace();
                                }
                            }
                        }

                    } else if (!address.getHostAddress().equals(address.getHostName())) {


                    } else {

                    }
                }
            } catch (Exception ex) {

            }

        }


    }
}