package com.liquiddark.mousepad.mhr.mousepad;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.liquiddark.mousepad.mhr.mousepad.adapter.PcListArrayAdapter;
import com.liquiddark.mousepad.mhr.mousepad.instruction.activity.InstructionActivity;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class PcListActivity extends Activity {

    static String TAG = "_PcListActivity";

    public  ArrayAdapter<String> clientListAdapter;

    MediaPlayer mpFound;
    public PcListArrayAdapter pcListArrayAdapter;
    public static boolean REFRESH_BUTTON_CLICKED = false ;

    ListView clientListView;
    public  ArrayList<String> _hostNameList;
    public  ArrayList<String> _ipList;
    ProgressBar progressBarClientList ;


    ImageView refreshImageView,imageViewPcMobile;
    TextView tvConnectionStatus,listViewMessagetextView;
    Typeface CustomFontSegoePrint ;


    EditText edtitTextForDialog;

    private static boolean refreshed_wait = true;
    private static int numberOfTimePingCalled = 0;
    private static boolean addManualIpFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        initActivity();
        enableWifi();
        mpFound = MediaPlayer.create(getApplicationContext(), R.raw.found_mess);


    }



    private void enableWifi() {
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if(!wifiManager.isWifiEnabled()){
            Toast.makeText(this,"Please enable wifi .. ",Toast.LENGTH_LONG).show();
            this.startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS),0);
        }
    }


    AnimationDrawable  connectiongAnimation;

    private void initActivity() {
        CustomFontSegoePrint = Typeface.createFromAsset(getAssets(), "fonts/Segoe_Print.ttf");
        refreshed_wait = true;

        setContentView(R.layout.activity_pc_list);


        tvConnectionStatus = (TextView) findViewById(R.id.listViewTitileTextView);
        //tvConnectionStatus.setPaintFlags(tvConnectionStatus.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        refreshImageView = (ImageView) findViewById(R.id.refreshImageView);
        imageViewPcMobile = (ImageView) findViewById(R.id.imageViewPcMobile);

        imageViewPcMobile.setBackgroundResource(R.drawable.connecting_to_pc);
        connectiongAnimation = (AnimationDrawable) imageViewPcMobile.getBackground();
        connectiongAnimation.start();

        ImageView imageViewInstruction = (ImageView) findViewById(R.id.imageViewInstruction);






            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                if(pcListArrayAdapter.getCount() ==0)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        //connectiongAnimation.addFrame(getResources().getDrawable(R.drawable.connecting_to_pc5,getApplicationContext().getTheme()), 300);
                        tvConnectionStatus.setText("Unable to find pc..");

                    } else {
                        //connectiongAnimation.addFrame(getResources().getDrawable(R.drawable.connecting_to_pc5), 300);
                        tvConnectionStatus.setText("Unable to find pc..");

                    }


                }
            }, 20000);



        tvConnectionStatus.setTypeface(CustomFontSegoePrint,Typeface.BOLD);
        listViewMessagetextView = (TextView) findViewById(R.id.listViewMessagetextView);
        listViewMessagetextView.setTypeface(CustomFontSegoePrint);
        listViewMessagetextView.setTypeface(CustomFontSegoePrint,Typeface.BOLD);





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


        refreshButton();


        listViewMessagetextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://bokadanob.wordpress.com/mousepad/"));
                startActivity(browserIntent);
            }
        });


        ImageView imageViewAddButton = (ImageView) findViewById(R.id.imageViewAddButton);


        addManualIpButton(imageViewAddButton);
        showInstructionButton(imageViewInstruction);


    }



    private void refreshButton() {
        refreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                REFRESH_BUTTON_CLICKED = true;
               // _hostNameList.clear();
               // _ipList.clear();

                MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.refresh);
//                mp.setVolume(0.2f,0.2f);
                mp.start();


                if(refreshed_wait == false) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            refreshed_wait = true;
                            _hostNameList.clear();
                            _ipList.clear();
                        }
                    }, 1000);
                }


                if(refreshed_wait == true) {
                    initClient();
                    refreshed_wait=false;
                }else{
                    Toast.makeText(getApplicationContext(),"Searching...",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void showInstructionButton(ImageView imageViewInstruction) {
        imageViewInstruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PcListActivity.this, InstructionActivity.class);
                intent.putExtra("__FROM_ACTIVITY","PC_LIST_ACTIVITY");
                startActivity(intent);
            }
        });
    }
    private void addManualIpButton(ImageView imageViewAddButton) {
        imageViewAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edtitTextForDialog = new EditText(PcListActivity.this);
                edtitTextForDialog.setTypeface(CustomFontSegoePrint);
                edtitTextForDialog.setHint("192.168.1.101");

                edtitTextForDialog.setFocusable(true);
                edtitTextForDialog.setEnabled(true);
                edtitTextForDialog.setGravity(Gravity.CENTER_VERTICAL);
                edtitTextForDialog.setText("192.168.1.101");

            AlertDialog al =
                new AlertDialog.Builder(PcListActivity.this)
                        .setView(edtitTextForDialog)
                        .setMessage("Set your pc ip manually\n")
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {


                                try{
                                    byte[] ip = getIpBytes(edtitTextForDialog.getText().toString());
                                  // _ipList.add(edtitTextForDialog.getText().toString());
                                    startThreadForIpTest(ip);
                                    addManualIpFlag = true;
                                    progressBarClientList.setVisibility(View.VISIBLE);
                                }catch (Exception ex){
                                 Toast.makeText(PcListActivity.this,"Sorry not a valid ip",Toast.LENGTH_SHORT).show();
                                }


                               // startThreadForIpTest();

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {


                            }
                        })
                        .show();

            }
        });
    }


    private void startThreadForIpTest(byte[] ip) {
        (new Thread(new IpTest(ip))).start();
    }

    private byte[] getIpBytes(String dstAddress) {
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
        return ip;
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
//                            tvConnectionStatus.setText("couldn't find pc!!");
                            tvConnectionStatus.setText("Searching ...");
                            listViewMessagetextView.setVisibility(View.VISIBLE);
                            imageViewPcMobile.setVisibility(View.VISIBLE);
                        }else{
                            tvConnectionStatus.setText("Choose your pc");
                            listViewMessagetextView.setVisibility(View.GONE);
                            imageViewPcMobile.setVisibility(View.GONE);


                        }
                    }
                }, 100);
            }
        }, 10);



        Log.d("_i8i "+TAG, "on restart== ip list count = " + pcListArrayAdapter.getCount());



    }

    private static boolean onStartPerformed = false;

    @Override
    protected void onStart() {
        super.onStart();


        onStartPerformed = true;
        initClient();


        Log.d("_i8i "+TAG, "on start ip list count = " + pcListArrayAdapter.getCount());

    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("_i8i "+TAG, "on resume ip list count = " + pcListArrayAdapter.getCount());

        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }



        pcListArrayAdapter.notifyDataSetChanged();

//        if(!onStartPerformed)
        {

            initClient();

        }
        onStartPerformed = false;


      //  initClient();

        new android.os.Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
              //  initClient();
                pcListArrayAdapter.notifyDataSetChanged();
                new android.os.Handler().postDelayed(new Runnable(){
                    @Override
                    public void run() {
                        if(_hostNameList.size() == 0){
//                            tvConnectionStatus.setText("couldn't find pc!!");
                            tvConnectionStatus.setText("Searching ...");
                            listViewMessagetextView.setVisibility(View.VISIBLE);
                            imageViewPcMobile.setVisibility(View.VISIBLE);
                        }else{
                            tvConnectionStatus.setText("Choose your pc");
                            listViewMessagetextView.setVisibility(View.GONE);
                            imageViewPcMobile.setVisibility(View.GONE);


                        }
                    }
                }, 100);
            }
        }, 1000);

        enableWifi();

        Log.d("_i8i "+TAG, "on pause ip list count = " + pcListArrayAdapter.getCount());


    }

    @Override
    protected void onPause() {
        Log.d("_i8i "+TAG, "on pause ip list count = " + pcListArrayAdapter.getCount());


        super.onPause();
        super.finish();


    }




    class Client extends AsyncTask<Void, Void, Void> {



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBarClientList.setVisibility(View.VISIBLE);
        }

        String TAG = "_MessageActivandroid";
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


                byte[] ip = getIpBytes(dstAddress);


                // clear_IpAndHostList();
                for (int i = 1; i <= 254; i++) {

                    ip[3] = (byte) i;
//                    runThreadDetails(ip);
                    startThreadForIpTest(ip);

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

            Log.d(TAG, "" +"runThreadDetails method");

            //===
            try {

                    InetAddress address = InetAddress.getByAddress(ipAddress);


                    if (address.isReachable(1500)) {


                        try {

                            Log.d(TAG, "doInBackground: address =" + address + " " + address.getHostName() + " " + address.getHostAddress()
                                    + " " + address.getCanonicalHostName());

                            socket = new Socket(address, 1239);


                            Log.d(TAG, " try catch =" + address.toString() + " " + address.getHostName());

                            _ipList.add(address.getHostAddress());

                            DataOutputStream DOS = new DataOutputStream(socket.getOutputStream());
//                            DOS.writeUTF(FLAG);
                            DOS.write("_SEARCH".getBytes());
                            DOS.flush();

                            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                            String serverHostName = "";

                            serverHostName
                                    = dataInputStream.readUTF();

                            Log.d(TAG, "server's host name = " + serverHostName);


                            _hostNameList.add(serverHostName);


                        } catch (Exception ex) {
                            Log.d(TAG, "exception ");


                        } finally {

                            Log.d("_i8i 2"+TAG, "on pause ip list count = " + pcListArrayAdapter.getCount());

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
                               // initClient();
                                REFRESH_BUTTON_CLICKED = false;
                            }
                        }
                    }, 1000);

                }
            }, 1000);

                if(_hostNameList.size() == 0){
                   // tvConnectionStatus.setText("couldn't find pc!!");
                    tvConnectionStatus.setText("Searching ...");
                    listViewMessagetextView.setVisibility(View.VISIBLE);
                    imageViewPcMobile.setVisibility(View.VISIBLE);


                }else{
                    tvConnectionStatus.setText("Choose your pc");
                    listViewMessagetextView.setVisibility(View.GONE);
                    imageViewPcMobile.setVisibility(View.GONE);


                }

        }


    }


    class IpTest implements Runnable {


        public  String FLAG = "_SEARCH";
        public  String N_FLAG = "_MOUSE";

        String TAG = "_H_IpTest";
        byte[] ipAddress;

        public IpTest() {

        }

        public IpTest(byte[] ipAddress) {
            this.ipAddress = ipAddress;
        }


        @Override
        public void run() {
            runThreadDetails();


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


        public boolean pingHost(String host,Socket socket) {
            try  {

                socket = new Socket(host,1239);
                //socket.connect(new InetSocketAddress(host, 1239), 2000);
              //  socket.close();

                Log.d("ip","connected");
                return true;
            } catch (IOException e) {

                return false; // Either timeout or unreachable or failed DNS lookup.
            }
        }


        public  boolean isReachableByTcp(String host, int port, int timeout) {
            try {
                Socket socket = new Socket();
                SocketAddress socketAddress = new InetSocketAddress(host, port);
                socket.connect(socketAddress, 0);
                socket.close();
                return true;
            } catch (IOException e) {

                //Log.d("__EXCEPTION","__");

                if(addManualIpFlag){
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            progressBarClientList.setVisibility(View.GONE);
                            Log.d("__EXCEPTION","_222_");
                            Toast.makeText(getApplicationContext(),"   Sorry!! Make sure   \n" +
                                                                   " entered ip is correct",Toast.LENGTH_LONG).show();

                        }
                    });
                    addManualIpFlag = false;


                }

                return false;
            }
        }

        boolean  ping(String url) {
            String str = "";
            try {



                Process process = Runtime.getRuntime().exec(
                        "/system/bin/bintlas -c 1 " + url);

                int val = process.waitFor();


                Log.d(TAG+" check", "check&&&& =" + url+"| "+process.getInputStream().toString() );


                if(val == 0)
                {
//                    process.destroy();

                    numberOfTimePingCalled ++;
                    return true;
                }
                else{

                    numberOfTimePingCalled ++;

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


        public boolean isReachable(String ip){


            try(Socket socket = new Socket()){
                socket.connect(new InetSocketAddress(ip, 1239), 2000);
                return true;
            }catch(Exception ex){

                return false;
            }

        }

        synchronized void runThreadDetails() {
            Socket socket = null;
            boolean hasIp = false;

            //===
            try {


                {


                    InetAddress address = InetAddress.getByAddress(ipAddress);

                    Log.d(TAG, "__address =" + address + " " + address.getHostName() + " |" + address.getHostAddress()
                            + "| "
                    );

                    //if (pingHost(address.getHostAddress(),socket))

//                    if(address.isReachable(1500) || ping(address.getHostAddress()))
///                    if(address.isReachable(1500))

                    if(isReachableByTcp(address.getHostAddress(),1239,1500))
                    {

                        try {

                            Log.d(TAG, "doInBackground: address =" + address + " " + address.getHostName() + " |" + address.getHostAddress()
                                    + "| " + address.getCanonicalHostName()
                            );

                            if(socket==null)
                            socket = new Socket(address, 1239);


                            Log.d("_LL"+TAG, "___try catch =" + address.toString() + " " + address.getHostName());





                            DataOutputStream DOS = new DataOutputStream(socket.getOutputStream());
                            //DOS.writeUTF(FLAG);

                            DOS.write("18 _SEARCH".getBytes());

                            Log.d("_LL"+TAG, " ____send done  =" +FLAG);

                            DOS.flush();

                            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

                            String serverHostName = "";


                            Log.d("_LL"+TAG, "server's host name 2_e = " + serverHostName);

                            serverHostName
                                    = dataInputStream.readLine();


                            Log.d("_LL"+TAG, "server's host name 2 = " + serverHostName);




                            if(!_ipList.contains(address.getHostAddress())) {
                                _ipList.add(address.getHostAddress());
                                if(!_hostNameList.contains(serverHostName)){
                                    _hostNameList.add(serverHostName);
                                }
                            }




                            new Handler(Looper.getMainLooper()).post(new Runnable() { // Tried new Handler(Looper.myLopper()) also
                                @Override
                                public void run() {
                                    pcListArrayAdapter.notifyDataSetChanged();
                                    progressBarClientList.setVisibility(View.GONE);


                                    if(!_ipList.isEmpty()){
                                        imageViewPcMobile.setVisibility(View.GONE);
                                        tvConnectionStatus.setText("Choose your pc");

                                        mpFound.setVolume(0.1f,0.1f);
                                        mpFound.start();


                                    }else {
                                        tvConnectionStatus.setText("couldn't find your pc");
                                    }

                                    //if(numberOfTimePingCalled > 230 || addManualIpFlag)
                                    {
                                      //  addManualIpFlag = false;
                                    }
                                }
                            });


                        } catch (Exception ex) {


                            Log.d("_LL"+TAG, " exception "+ ex.getMessage());


                        } finally {


                            if (socket != null) {
                                try {

                                   socket.close();

                                    new Handler(Looper.getMainLooper()).post(new Runnable() { // Tried new Handler(Looper.myLopper()) also
                                        @Override
                                        public void run() {
                                            pcListArrayAdapter.notifyDataSetChanged();
                                            progressBarClientList.setVisibility(View.GONE);


                                            if(pcListArrayAdapter.getCount()!=0){
                                                imageViewPcMobile.setVisibility(View.GONE);
                                            }
                                        }
                                    });

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