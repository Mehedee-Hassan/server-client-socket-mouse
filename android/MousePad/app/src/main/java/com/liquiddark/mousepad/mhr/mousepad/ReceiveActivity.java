package com.liquiddark.mousepad.mhr.mousepad;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PathDashPathEffect;
import android.os.AsyncTask;
import android.support.annotation.ColorRes;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.MovementMethod;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.TextView;

import com.liquiddark.mousepad.mhr.mousepad.constant.Constant;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

public class ReceiveActivity extends Activity {



    private static final int MAX_CLICK_DURATION = 200;
    private static String COMMAND_CLOSE_WINDOW = "7";

    private long startClickTime = Calendar.getInstance().getTimeInMillis();

    public static String ipTosend ="";
    public static byte[] IpToTheThread;

    public static double saveX = 0;
    public static double saveY = 0;


    Button closeWindowButton ;
    TextView coordinateTv,letsPlayTv;
    View MousePad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_receive);

        initMouspad();


    }

    private void initMouspad() {
        final Intent receive = this.getIntent();
        ipTosend = receive.getStringExtra("_IP");

        closeWindowButton = (Button) findViewById(R.id.closeWindowButton);




        MousePad =  (View) findViewById(R.id.mousePad);
        coordinateTv = (TextView) findViewById(R.id.coordinateTextView);
        letsPlayTv = (TextView) findViewById(R.id.lets_play_tv);

        Animation textAlpha = new AlphaAnimation(1.0f ,0.0f);

        textAlpha.setDuration(1000);
        textAlpha.setFillAfter(true);
        letsPlayTv.startAnimation(textAlpha);


        String[] temp = ipTosend.split(Pattern.quote("."));
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


        IpToTheThread = ip;


        closeWindowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String val = COMMAND_CLOSE_WINDOW;

                (new Thread(new IpTest2(val))).start();
            }
        });

        MousePad.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {



                int action = event.getAction();


                String coordinateTemp = "";

                if(action == MotionEvent.ACTION_DOWN){
                    action = 1;
                    coordinateTemp= 0 + " "+ 0;
                    saveX = event.getX();
                    saveY = event.getY();


                    startClickTime = Calendar.getInstance().getTimeInMillis();

                }
                else if(action == MotionEvent.ACTION_UP){

                    action = 2;
                    coordinateTemp= 0 + " "+ 0;
                    saveX = event.getX();
                    saveY = event.getY();


                    if( Calendar.getInstance().getTimeInMillis()- startClickTime < MAX_CLICK_DURATION ){
                        action = Constant.Action.SHORT_TOUCH;
                    }

                }else if(action == MotionEvent.ACTION_HOVER_MOVE){
                    coordinateTemp= 0 + " "+ 0;
                    saveX = event.getX();
                    saveY = event.getY();
                }
                else
                    coordinateTemp= (event.getX()-saveX) + " "+ (event.getY()-saveY);

                if(action == MotionEvent.ACTION_MOVE){
                    action =3;
                    saveX = event.getX();
                    saveY = event.getY();

                }



                String val = action+" "+ coordinateTemp;

                (new Thread(new IpTest2(val))).start();

                coordinateTv.setText(val);
                return true;
            }


        });
        MousePad.setOnDragListener(new View.OnDragListener() {

            @Override
            public boolean onDrag(View v, DragEvent event) {
                return false;
            }

        });

    }
}




class IpTest2 implements Runnable{


    public static String FLAG = "_SEARCH";
    public static String N_FLAG = "_MOUSE";
    String val;
    String TAG = "IpTest";
    byte[] ipAddress ;


    public IpTest2(){

    }
    public IpTest2(String val){
        this.val = val;
        ipAddress = ReceiveActivity.IpToTheThread;
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

                        socket = new Socket(address, 9000);

                        DataOutputStream DOS = new DataOutputStream(socket.getOutputStream());
                        DOS.writeUTF(val);

                    } catch(Exception ex){
                        Log.d(TAG ,"exception ");
                    }




                } else if (!address.getHostAddress().equals(address.getHostName())) {


                } else {

                }
            }
        }catch (Exception ex){

        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {


                    e.printStackTrace();
                }
            }
        }
    }


}