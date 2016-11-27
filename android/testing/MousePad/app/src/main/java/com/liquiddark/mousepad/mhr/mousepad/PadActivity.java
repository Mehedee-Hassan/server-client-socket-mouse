package com.liquiddark.mousepad.mhr.mousepad;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liquiddark.mousepad.mhr.mousepad.constant.Constant;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.regex.Pattern;

public class PadActivity extends Activity {

    private Context thisContext ;

    private static final int MAX_CLICK_DURATION = 200;
    private static String COMMAND_CLOSE_WINDOW = "7";
    private static final String COMMAND_TAB_WINDOW = "8";
    private static final String COMMAND_ENTER = "9";
    private static final String COMMAND_SCROLL_VERTICAL_DOWN = "10";
    private static final String COMMAND_SCROLL_VERTICAL_UP = "11";
    private static final String COMMAND_ESCAPE = "12";
    private static final String COMMAND_LEFT_CLICK = "13";
    private static final String COMMAND_RIGHT_CLICK = "14";
    private static final String     COMMAND_LONG_CLICK = "15";
    Vibrator vibration;

    int tempDistence = 0;
    private long startClickTime = Calendar.getInstance().getTimeInMillis();

    public static String ipTosend ="";
    public static byte[] IpToTheThread;

    public static double saveX = 0;
    public static double saveY = 0;


    public static int oldScrollDistance = 0;
    public static int mouseMidPositionBottom = 0;
    public static int mouseMidPositionTop = 0;

    Button closeWindowButton,enterButton, escapeButton;
    ImageButton mouseLeftButton,mouseRightButton;
    TextView letsPlayTv;
    View MousePad,mouseScroll;
    ImageView keyboardButtonImageView,openKeyboardIv;
    private RelativeLayout keyboardShowlayout,mousePadLayout;
    private ImageView clearButton;
    private ImageView backButtonKeyboard;
    private EditText keyboardEventGetET2;
    private ImageView copyButton,cutButton,pestButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_mouse_pad);

        initMouspad();
        thisContext  = this;

         vibration = (Vibrator) getApplication().getSystemService(Context.VIBRATOR_SERVICE);


    }


    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }


        InetAddress address = null;
        try {
//            address = InetAddress.getByAddress(this.IpToTheThread);
//
//            if (!ping(address.getHostAddress()))
//            {
//                Toast.makeText(this,"Disconnected from the pc",Toast.LENGTH_LONG).show();
//                startActivity(new Intent(this,PcListActivity.class));
//
//            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        //finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    private void initMouspad() {
        initActivity();
        //laterImplementation();

        closeWindowEvent();
        escapeButtonEvent();
        mouseScrollButtonEvent();
        enterButtonEvent();
        mousePadEvent();
        mouseLeftRightButtonEvent();




        keyboardEventGetET2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_DEL){

                    (new Thread(new IpTest2(Constant.Action.TYPE_DELETE,thisContext))).start();

                }


                return false;


            }



        });

        keyboardEventGetET2.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {

                    (new Thread(new IpTest2(COMMAND_ENTER,thisContext))).start();


                    handled = true;
                }
                return handledj;
            }
        });


        openKeyboardIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(PadActivity.this, KeyboardActivity.class));

                keyboardShowlayout.setVisibility(View.VISIBLE);
                mousePadLayout.setVisibility(View.GONE);

                keyboardEventGetET2.requestFocus();

        //        InputMethodManager imm = (InputMethodManager)   getSystemService(PadActivity.this.INPUT_METHOD_SERVICE);
        //        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                if(imm != null){
                    imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                }

            }
        });




        backButtonKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyboardShowlayout.setVisibility(View.GONE);
                mousePadLayout.setVisibility(View.VISIBLE);
          //      InputMethodManager imm = (InputMethodManager)   getSystemService(PadActivity.this.INPUT_METHOD_SERVICE);
          //      imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);


                if(imm != null){
                   imm.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
                }else {
                         imm.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);

                }

            }
        });




        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyboardEventGetET2.setText("");
            }
        });


        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(PadActivity.this,"copy",Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();

                (new Thread(new IpTest2(Constant.Action.COMMAND_COPY,thisContext))).start();
            }
        });

        pestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(PadActivity.this,"pest",Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();

                (new Thread(new IpTest2(Constant.Action.COMMAND_PEST,thisContext))).start();

            }
        });

        cutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast toast = Toast.makeText(PadActivity.this,"cut",Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();

                (new Thread(new IpTest2(Constant.Action.COMMAND_CUT,thisContext))).start();

            }
        });



        enableWifi();


    }

    private void enableWifi() {
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if(!wifiManager.isWifiEnabled()){
            Toast.makeText(this,"Please enable wifi .. ",Toast.LENGTH_SHORT).show();
            this.startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS),0);
            finish();
        }
    }




    private void mouseLeftRightButtonEvent() {
        mouseLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String val = COMMAND_LEFT_CLICK;

                (new Thread(new IpTest2(val,thisContext))).start();
            }
        });

        mouseLeftButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {


                vibration.vibrate(50);

                String val = COMMAND_LONG_CLICK;
                Log.d("mouse_button_click", "onLongClick: ");
                (new Thread(new IpTest2(val,thisContext))).start();


                return true;
            }
        });
        mouseRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String val = COMMAND_RIGHT_CLICK;


                (new Thread(new IpTest2(val,thisContext))).start();
            }
        });
    }

    private void mousePadEvent() {

        MousePad.setOnDragListener(new View.OnDragListener() {

            @Override
            public boolean onDrag(View v, DragEvent event) {
                return false;
            }

        });


        MousePad.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                        String val = COMMAND_LONG_CLICK;
                        Log.d("mouse_button_click", "onLongClick: ");
                        (new Thread(new IpTest2(val,thisContext))).start();

                return true;
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

                    (new Thread(new IpTest2("16",thisContext))).start();


                }else if(action == MotionEvent.ACTION_HOVER_MOVE){
                    coordinateTemp= 0 + " "+ 0;
                    saveX = event.getX();
                    saveY = event.getY();
                }
                else
                    coordinateTemp= String.format("%.2f %.2f" ,(event.getX()-saveX), (event.getY()-saveY));

                if(action == MotionEvent.ACTION_MOVE){
                    action =3;
                    saveX = event.getX();
                    saveY = event.getY();

                }


                String val = action+" "+ coordinateTemp;

                (new Thread(new IpTest2(val,thisContext))).start();

                return true;
            }


        });
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

    private void enterButtonEvent() {
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String val = COMMAND_ENTER;

                (new Thread(new IpTest2(val,thisContext))).start();

            }
        });
    }

    private void mouseScrollButtonEvent() {
        mouseMidPositionBottom = mouseScroll.getHeight();


        mouseScroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                if(event.getAction() == MotionEvent.ACTION_MOVE){
                    tempDistence = (int) (mouseMidPositionBottom - event.getRawY());


                    String val = "0 0 0";

                    if(tempDistence > oldScrollDistance)
                         val = COMMAND_SCROLL_VERTICAL_UP;
                    else
                    if(tempDistence < oldScrollDistance)
                         val = COMMAND_SCROLL_VERTICAL_DOWN;

                   // val = COMMAND_SCROLL_VERTICAL_DOWN;

                    (new Thread(new IpTest2(val,thisContext))).start();





                    oldScrollDistance = tempDistence;
                }else
                if(event.getAction() == MotionEvent.ACTION_UP){

                }

                return true;
            }


        });
    }

    private void escapeButtonEvent() {
        escapeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String val = COMMAND_ESCAPE;

                (new Thread(new IpTest2(val,thisContext))).start();
            }
        });
    }

    private void closeWindowEvent() {
        closeWindowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String val = COMMAND_CLOSE_WINDOW;

                (new Thread(new IpTest2(val,thisContext))).start();
            }
        });
    }

    private void laterImplementation() {



//       keyboardButtonImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                InputMethodManager imm = (InputMethodManager)   getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
//
//            }
//        });



    }

    private void initActivity() {
        final Intent receive = this.getIntent();
        ipTosend = receive.getStringExtra("_IP");

        initComponents();







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


        Log.d("mouse_button_click", "onLongClick: "+MousePad.isLongClickable()+" "+MousePad.isClickable());

        MousePad.setLongClickable(true);
        MousePad.setClickable(true);
        MousePad.setFocusable(true);
        

        Log.d("mouse_button_click", "onLongClick: "+MousePad.isLongClickable()+" "+MousePad.isClickable());



    }

    private void initComponents() {
        closeWindowButton = (Button) findViewById(R.id.closeWindowButton);
        enterButton = (Button) findViewById(R.id.enterButton);
        escapeButton  = (Button) findViewById(R.id.escapeButton);

        mouseRightButton = (ImageButton) findViewById(R.id.mouse_right);
        mouseLeftButton = (ImageButton) findViewById(R.id.mouse_left);

        //keyboardButtonImageView = (ImageView) findViewById(R.id.keyboardButtonImageView);

        mouseScroll = (View) findViewById(R.id.mouseScroll);
        MousePad =  (View) findViewById(R.id.mousePad);
        letsPlayTv = (TextView) findViewById(R.id.lets_play_tv);


        keyboardShowlayout = (RelativeLayout) findViewById(R.id.keyboardShowlayout);
        mousePadLayout = (RelativeLayout) findViewById(R.id.mousePadLayout);

        clearButton = (ImageView) findViewById(R.id.clearETbutton);
        backButtonKeyboard = (ImageView) findViewById(R.id.backButtonKeyboard);
        openKeyboardIv= (ImageView ) findViewById(R.id.openKeyboardIv);



        keyboardEventGetET2 = (EditText) findViewById(R.id.keyboardEventGetET2);
        keyboardEventGetET2.addTextChangedListener(textWatcher);





        copyButton = (ImageView) findViewById(R.id.copyButton);
        cutButton = (ImageView) findViewById(R.id.cutButton);
        pestButton = (ImageView) findViewById(R.id.pestButton);

    }

    int abs(int number){

        return (number < 0 ? (-1*number):number);
    }


    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            CharSequence newString = "";
            if(start+count > 0) {

                newString = s.subSequence(start + count - 1, start + count);

                if(count != before)
                (new Thread(new IpTest2("22 "+newString.toString(),thisContext))).start();
            }

            Log.d("_LL",""+newString+" "+count+" "+before+" "+start);

            // Log.d("_LL",""+s);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

}




class IpTest2 implements Runnable{


    public static String FLAG = "_SEARCH";
    public static String N_FLAG = "_MOUSE";
    String val;
    String TAG = "IpTest";
    byte[] ipAddress ;
    Socket passedSocket;

    private Context padActContext ;
    public IpTest2(){

    }
    public IpTest2(String val,Context context){
        this.val = val;
        ipAddress = PadActivity.IpToTheThread;
        padActContext = context;
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
                //if (address.isReachable(1500))
              //  if(ping(address.getHostAddress()))
                {



                    try {

                        socket = new Socket(address, 1239);

                        DataOutputStream DOS = new DataOutputStream(socket.getOutputStream());
                        DOS.write(val.getBytes());
                        DOS.flush();


                    } catch(Exception ex){

                        if(!ping(address.getHostAddress())){
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    padActContext.startActivity(new Intent(padActContext,PcListActivity.class));
                                }
                            });
                        }

                        Log.d(TAG ,"exception ");
                    }




                }
                //else if (!address.getHostAddress().equals(address.getHostName())) {


                }
            //else
                {

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

}