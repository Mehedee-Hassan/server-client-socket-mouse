package liquiddark.mousepad;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
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

import liquiddark.mousepad.constant.Constant;
import liquiddark.mousepad.socket.Connection;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Pattern;

public class PadActivity extends Activity {

    private Context thisContext ;

    public static boolean threadRun = true;

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

    private static boolean _isKeyBoardShown;

    int tempDistence = 0;
    private long startClickTime = Calendar.getInstance().getTimeInMillis();

    public static String ipTosend ="";
    public static byte[] IpToTheThread;

    public  int saveX = 0;
    public  int saveY = 0;
    public  int tmpX = 0;
    public  int tmpY = 0;


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
    private RelativeLayout rootPcListLayoutVIew;
    private static boolean _isBackPressed;

    private static int __numberOfTimeBaackPressed = 0;
    private static boolean __backspaceIsNotPressed =false;
    private static int __oldCount = -1;


    IpTest2 ipTest2test;

    private Connection connection;
    private Socket socket;
    private static Queue<String> eventQueue;
    private int dumpDelete;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_mouse_pad);

        initMouspad();
        thisContext  = this;

        vibration = (Vibrator) getApplication().getSystemService(Context.VIBRATOR_SERVICE);


        //ipTest2test = new IpTest2("",this);
        rootPcListLayoutVIewEvent();


        connection = new Connection();


    }





    private void rootPcListLayoutVIewEvent() {

        rootPcListLayoutVIew.getViewTreeObserver().addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
            @Override
            public void onGlobalFocusChanged(View oldFocus, View newFocus) {

                int difference = rootPcListLayoutVIew.getRootView().getHeight() - mousePadLayout.getHeight() ;
                //Log.d("_LL"," clicked here "+difference);


                if(difference > 30){
                    _isKeyBoardShown = true;
                }
                else{
                    _isKeyBoardShown = false;

                }

            }
        });
    }




    @Override
    protected void onResume() {

        pauseResumeInit();

        super.onResume();
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        threadRun = true;

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
        pauseResumeInit();

        super.onPause();
        finish();
    }

    private void pauseResumeInit() {
        __numberOfTimeBaackPressed = 0;
        __oldCount = -1;
        saveX = 0;
        saveY = 0;
        eventQueue = new LinkedList<String>();

        ipTest2test = new IpTest2(eventQueue,thisContext);

        (new Thread(ipTest2test)).start();

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
        pauseResumeInit();
        keyboardEventGetET2Event();
        openKeyboardIvEvent();

        clearCpyPstCt();



        enableWifi();




    }

    private void clearCpyPstCt() {
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
                toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 250);
                toast.show();

                //(new Thread(new IpTest2(Constant.Action.COMMAND_COPY,thisContext))).start();
                eventQAdeed(Constant.Action.COMMAND_COPY);


            }
        });

        pestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(PadActivity.this,"paste",Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL,0, 250);
                toast.show();
                eventQAdeed(Constant.Action.COMMAND_PEST);


                //(new Thread(new IpTest2(Constant.Action.COMMAND_PEST,thisContext))).start();

            }
        });


        cutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast toast = Toast.makeText(PadActivity.this,"cut",Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, 250);
                toast.show();
                eventQAdeed(Constant.Action.COMMAND_CUT);


                //(new Thread(new IpTest2(Constant.Action.COMMAND_CUT,thisContext))).start();

            }
        });
    }

    private void openKeyboardIvEvent() {
        openKeyboardIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(PadActivity.this, KeyboardActivity.class));

                keyboardShowlayout.setVisibility(View.VISIBLE);
                mousePadLayout.setVisibility(View.GONE);

                keyboardEventGetET2.requestFocus();


                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);


            }
        });
    }

    private void keyboardEventGetET2Event() {
        keyboardEventGetET2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                //Log.d("_LL"," = "+keyCode);



                if(keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_UP){
                    __backspaceIsNotPressed = false;
                }


                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {

                    if(keyCode == KeyEvent.KEYCODE_DEL){


                        if( event.getAction() == KeyEvent.ACTION_DOWN){
                            //  __backspaceIsNotPressed = true;

                            if(keyboardEventGetET2.getText().toString().isEmpty())
                                eventQAdeed(Constant.Action.TYPE_DELETE);


                            //(new Thread(new IpTest2(Constant.Action.TYPE_DELETE,thisContext))).start();

                        }
                        if( event.getAction() == KeyEvent.ACTION_UP){
                            //   __backspaceIsNotPressed = false;
                        }

                    }else {
                        //   __backspaceIsNotPressed = false;

                    }


                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            eventQAdeed(COMMAND_ENTER);


                            //(new Thread(new IpTest2(COMMAND_ENTER,thisContext))).start();

                            return true;

                        default:
                            break;
                    }
                }




                //  //Log.d("_LL"," back space = "+__backspaceIsNotPressed);

                return false;




            }



        });

        keyboardEventGetET2.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {

                    //(new Thread(new IpTest2(COMMAND_ENTER,thisContext))).start();
                    eventQAdeed(COMMAND_ENTER);



                    handled = true;

                }

                return handled;
            }
        });
    }

    private void eventQAdeed(String commandEnter) {
        eventQueue.add(commandEnter);
        ipTest2test.added();
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
//                String val = COMMAND_LEFT_CLICK;
                eventQAdeed(COMMAND_LEFT_CLICK);



                //(new Thread(new IpTest2(val,thisContext))).start();
            }
        });

        mouseLeftButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {


                vibration.vibrate(50);

//                String val = COMMAND_LONG_CLICK;
                //Log.d("mouse_button_click", "onLongClick: ");

                eventQAdeed(COMMAND_LONG_CLICK);


                //(new Thread(new IpTest2(val,thisContext))).start();


                return true;
            }
        });
        mouseRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String val = COMMAND_RIGHT_CLICK;
                eventQAdeed(COMMAND_RIGHT_CLICK);



                //(new Thread(new IpTest2(val,thisContext))).start();
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

//                String val = COMMAND_LONG_CLICK;
                //Log.d("mouse_button_click", "onLongClick: ");
                //(new Thread(new IpTest2(val,thisContext))).start();

                eventQAdeed(COMMAND_LONG_CLICK);


                return true;
            }
        });

        MousePad.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {



                int action = event.getAction();
                int tempAction = 3;

                String coordinateTemp = "";



                if(action == MotionEvent.ACTION_DOWN){
                    tempAction = 1;
                    coordinateTemp= 0 + " "+ 0;
                   // saveX = event.getX();
                   // saveY = event.getY();


                    startClickTime = Calendar.getInstance().getTimeInMillis();

                }
                else if(action == MotionEvent.ACTION_UP){

                    tempAction = 16;
                    coordinateTemp= 0 + " "+ 0;
                   // saveX = event.getX();
                   // saveY = event.getY();


                    if( Calendar.getInstance().getTimeInMillis()- startClickTime < MAX_CLICK_DURATION ){
                        tempAction = Constant.Action.SHORT_TOUCH;
                    }

                  //  (new Thread(new IpTest2("16",thisContext))).start();


                }else if(action == MotionEvent.ACTION_HOVER_MOVE){
                    coordinateTemp= 0 + " "+ 0;
                    //saveX = event.getX();
                    //saveY = event.getY();
                }
                else
                if(action == MotionEvent.ACTION_MOVE){
                    tempAction =3;
                    //saveX = event.getX();
                    //saveY = event.getY();
                    //Log.d("_LL","move--");




                }



                tmpX = (int)event.getX();
                tmpY = (int)event.getY();

                String val = tempAction+" "+ (tmpX-saveX)+" "+ (tmpY-saveY);

                //Log.d("_LL","move--"+val);

                eventQAdeed(val);



//                Thread t = new Thread(new IpTest2(val,thisContext));
//                t.start();

//                try {
//                    t.join();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }

//                coordinateTemp= String.format("%.0f %.0f" ,(tmpX-saveX), (tmpY-saveY));




                //after calculation save for later use
                //saving current
                saveX = tmpX;
                saveY = tmpY;



               // //Log.d("__LL",""+val);




                return true;
            }


        });
    }




    private void enterButtonEvent() {
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String val = COMMAND_ENTER;

                //(new Thread(new IpTest2(val,thisContext))).start();
                eventQAdeed(COMMAND_ENTER);


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

                    //(new Thread(new IpTest2(val,thisContext))).start();

                        eventQAdeed(val);




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

                //(new Thread(new IpTest2(val,thisContext))).start();
                eventQAdeed(val);

            }
        });
    }

    private void closeWindowEvent() {
        closeWindowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String val = COMMAND_CLOSE_WINDOW;

                //(new Thread(new IpTest2(val,thisContext))).start();
                eventQAdeed(val);

            }
        });
    }



    private void initActivity() {
        final Intent receive = this.getIntent();
        ipTosend = receive.getStringExtra("_IP");

        initComponents();

        Animation textAlpha = new AlphaAnimation(1.0f ,0.0f);

        textAlpha.setDuration(1000);
        textAlpha.setFillAfter(true);
        letsPlayTv.startAnimation(textAlpha);


        ipToSednExtraction();


        //Log.d("mouse_button_click", "onLongClick: "+MousePad.isLongClickable()+" "+MousePad.isClickable());

        MousePad.setLongClickable(true);
        MousePad.setClickable(true);
        MousePad.setFocusable(true);


        //Log.d("mouse_button_click", "onLongClick: "+MousePad.isLongClickable()+" "+MousePad.isClickable());




//        (new Thread(connection)).start();

//        boolean testFlag = connection.initialize(IpToTheThread,this);

//        if(testFlag){}


    }

    private void ipToSednExtraction() {
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
    }

    private void initComponents() {

        rootPcListLayoutVIew = (RelativeLayout) findViewById(R.id.rootPcListLayoutVIew);

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
        //  backButtonKeyboard = (ImageView) findViewById(R.id.backButtonKeyboard);
        openKeyboardIv= (ImageView ) findViewById(R.id.openKeyboardIv);



        keyboardEventGetET2 = (EditText) findViewById(R.id.keyboardEventGetET2);
        keyboardEventGetET2.addTextChangedListener(textWatcher);

      //  keyboardEventGetET2.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);



        copyButton = (ImageView) findViewById(R.id.copyButton);
        cutButton = (ImageView) findViewById(R.id.cutButton);
        pestButton = (ImageView) findViewById(R.id.pestButton);

    }

    int abs(int number){

        return (number < 0 ? (-1*number):number);
    }

    static boolean passedAll = false;
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            CharSequence newString = "";
            int pos = start + count - 1;
            boolean passMeThrough = false;
            char temp = 0 ;

            //Log.d("_LL","got u = "+temp);

            if(before!=0 && count<__oldCount)
            {
                __oldCount = count;
                if(!keyboardEventGetET2.getText().toString().isEmpty())
                    //(new Thread(new IpTest2(Constant.Action.TYPE_DELETE,thisContext))).start();
                eventQAdeed(Constant.Action.TYPE_DELETE);


            }else
            if(count!=0){
                if(pos>=0) {
                    __oldCount = count;

                    temp = s.charAt(pos);
                    //Log.d("_LL","got u2 = "+temp+" "+start+" "+before+" "+count);

                    if(count != before) {
                        eventQAdeed(Constant.Action.TYPE_ALPHA + " " + temp);

                    }
                    //(new Thread(new IpTest2("22 " + temp, thisContext))).start();

                }
            }else {
                __oldCount = count;

                if(!keyboardEventGetET2.getText().toString().isEmpty()) {
                    eventQAdeed(Constant.Action.TYPE_DELETE);


                }

                //(new Thread(new IpTest2(Constant.Action.TYPE_DELETE,thisContext))).start();
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };




    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            hideFrontKeyboardView();

            __numberOfTimeBaackPressed++;

            if(__numberOfTimeBaackPressed >2) {
                startActivity(new Intent(PadActivity.this, PcListActivity.class));
                overridePendingTransition(R.xml.slide_in2, R.xml.slide_out2);
                finish();


                return super.onKeyDown(keyCode, event);

            }if(__numberOfTimeBaackPressed >1) {
                Toast.makeText(this, "press again to exit", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run() {
                        __numberOfTimeBaackPressed = 0;

                    }
                }, 10000);

            }

            else
            Toast.makeText(this,"exit pad?!!",Toast.LENGTH_SHORT).show();


            return true;

        }
        return false;
    }

    @Override
    public void onBackPressed() {
        //
        hideFrontKeyboardView();
        startActivity(new Intent(PadActivity.this ,PcListActivity.class));
        overridePendingTransition(R.xml.slide_in2,R.xml.slide_out2);

        __numberOfTimeBaackPressed++;

        if(__numberOfTimeBaackPressed >2) {
            startActivity(new Intent(PadActivity.this, PcListActivity.class));
            overridePendingTransition(R.xml.slide_in2, R.xml.slide_out2);
            finish();
            super.onBackPressed();
        }if(__numberOfTimeBaackPressed >1){

            Toast.makeText(this,"press again to exit",Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    __numberOfTimeBaackPressed = 0;

                }
            }, 10000);

        }

        else
            Toast.makeText(this,"exit pad?!!",Toast.LENGTH_SHORT).show();


    }


    private void hideFrontKeyboardView(){
        keyboardShowlayout.setVisibility(View.GONE);
        mousePadLayout.setVisibility(View.VISIBLE);
    }

    private void goBackToPad() {
        hideFrontKeyboardView();

        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);


        //Log.d("_LL"," value = "+_isBackPressed);

        if(!_isBackPressed && imm!=null){
            imm.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }


    private void writeToPort(String val){

        DataOutputStream DOS = null;
        try {
            DOS = new DataOutputStream(socket.getOutputStream());
            DOS.writeUTF(val);
            DOS.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void onDestroy() {
        threadRun = false;

        super.onDestroy();
    }



class IpTest2 implements Runnable,AddedToQueue{


    String val;
    String TAG = "IpTest";
    byte[] ipAddress ;

     Queue<String> eventQueue;

    private Context padActContext ;
    private int addedItemToQueue;

    public IpTest2(){



    }

    public IpTest2(Queue<String> eventQueue,Context context){
        this.eventQueue = eventQueue;
        this.padActContext = context;
        ipAddress = PadActivity.IpToTheThread;
        addedItemToQueue=0;
    }


    private IpTest2(String val,Context context){
        this.val = val;
        ipAddress = PadActivity.IpToTheThread;
        padActContext = context;
    }





    @Override
    public void run() {

        if(threadRun)
            test();
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
     String testString;


    synchronized void test(){
        Socket socket = null;


        //Log.d("__LL"," in thread : ");

        //===
        try {


            {
                InetAddress address = InetAddress.getByAddress(ipAddress);
                //if (address.isReachable(1500))
                //  if(ping(address.getHostAddress()))
                {



                    try {

                        socket = new Socket(address, Constant.GLOBAL_PORT_NUMBER);

                        DataOutputStream DOS = new DataOutputStream(socket.getOutputStream());


//                        java
//                          DOS.writeUTF("4"); //connect to pad

//                        c++
//                        DOS.writeBytes(Constant.FLAGS.PAD_START); //connect to pad

//                        qt c++
                        //                        c++
                        DOS.writeBytes(Constant.FLAGS.PAD_START); //connect to pad




                        int ii=10,j =0;

                        DataOutputStream testBuffered ;
                        testBuffered = new DataOutputStream(new BufferedOutputStream( socket.getOutputStream()));
                        while (true) {


                            testString="";
                           // if(addedItemToQueue>0)
//                            if(j++>2)
                                {
//                                    j = 0;

                                if(!eventQueue.isEmpty())
                                {
                                    testString = eventQueue.poll();
                                    addedItemToQueue--;

                                     Thread.sleep(2);


                                 //   //Log.d("__LL", " in thread : "+testString );


//                                    java
//                                      DOS.writeUTF(testString);
//                                    c++

                                    testBuffered.writeBytes("\n"+testString+"\n");
                                 //   //Log.d("___LL", " in thread2 : "+testString );
                                    testBuffered.flush();



                                }
                                else {

                                    Thread.sleep(2);
                                }

                            }

                        }

                        //DOS.flush();


                    } catch(Exception ex){

                        if(!socket.isClosed())
                            socket.close();




                        //Log.d(TAG ,"exception ");
                    }
                    if(!socket.isClosed())
                        socket.close();




                }
                //else if (!address.getHostAddress().equals(address.getHostName())) {


            }
            //else
            {

            }

        }catch (Exception ex){
            //Log.d("__LL", " exception " );

        } finally {

        }
        //Log.d("__LL", " out thread : " );

    }





    public boolean  ping(String url) {

        try {

            Process process = Runtime.getRuntime().exec(
                    "/system/bin/ping -c 1 " + url);

            int val = process.waitFor();


            ////Log.d(TAG+" check", "check&&&& =" + url+"| "+process.getInputStream().toString() );


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
    public void added() {

        addedItemToQueue ++;
    }
}
}