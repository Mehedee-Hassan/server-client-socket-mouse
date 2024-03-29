package com.liquiddark.mousepad.mhr.mousepad;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.liquiddark.mousepad.mhr.mousepad.instruction.activity.InstructionActivity;

public class SplashActivity extends Activity {

    private static final long SPLASH_DISPLAY_LENGTH = 700;
    SharedPreferences sharedPref ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);


        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        final boolean applicationOpened = sharedPref.getBoolean("__APP_FIRST_TIME_STARTED",true);




        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
//                Intent mainIntent = new Intent(SplashActivity.this,PcListActivity.class);


                Intent mainIntent;

                if(applicationOpened){
                    mainIntent= new Intent(SplashActivity.this,InstructionActivity.class);

                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean("__APP_FIRST_TIME_STARTED",false);
                    editor.apply();
                    ShortcutIcon();

                }else {
                    mainIntent= new Intent(SplashActivity.this,PcListActivity.class);

                }


                startActivity(mainIntent);
                overridePendingTransition(R.xml.slide_in, R.xml.slide_out);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);




    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

    }


    private void ShortcutIcon(){

        Intent shortcutIntent = new Intent(getApplicationContext(), SplashActivity.class);
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Mousepad");
        addIntent.putExtra("duplicate", false);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.mipmap.mousepad_icon4));
        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        getApplicationContext().sendBroadcast(addIntent);
    }
}
