package com.liquiddark.mousepad.mhr.mousepad;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PathDashPathEffect;
import android.support.annotation.ColorRes;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.TextView;

public class ReceiveActivity extends Activity {


    TextView coordinateTv,letsPlayTv;
    View MousePad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_receive);


        MousePad =  (View) findViewById(R.id.mousePad);



        coordinateTv = (TextView) findViewById(R.id.coordinateTextView);


        letsPlayTv = (TextView) findViewById(R.id.lets_play_tv);

        Animation textAlpha = new AlphaAnimation(1.0f ,0.0f);

        textAlpha.setDuration(1000);
        textAlpha.setFillAfter(true);
        letsPlayTv.startAnimation(textAlpha);




        MousePad.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                String val = ""+event.getRawX()+ " "+event.getRawY();


                coordinateTv.setText(val);
                return true;
            }
        });


    }
}
