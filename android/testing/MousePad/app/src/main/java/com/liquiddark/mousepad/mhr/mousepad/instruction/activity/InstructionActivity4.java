package com.liquiddark.mousepad.mhr.mousepad.instruction.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.liquiddark.mousepad.mhr.mousepad.PcListActivity;
import com.liquiddark.mousepad.mhr.mousepad.R;

public class InstructionActivity4 extends Activity {




    ImageView imageViewdoesntwork,imageViewBackButton4 ;
    private Typeface CustomFontSegoePrint;
    TextView textViewInstruction4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction4);

        textViewInstruction4 = (TextView) findViewById(R.id.textViewInstruction4);

        imageViewBackButton4 = (ImageView) findViewById(R.id.imageViewBackButton4);
        imageViewdoesntwork = (ImageView) findViewById(R.id.imageViewdoesntwork);


        CustomFontSegoePrint = Typeface.createFromAsset(getAssets(), "fonts/Segoe_Print.ttf");
        textViewInstruction4.setTypeface(CustomFontSegoePrint);


        imageViewdoesntwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mainIntent = new Intent(InstructionActivity4.this,InstructionActivity5.class);
                startActivity(mainIntent);
                overridePendingTransition(R.xml.slide_in, R.xml.slide_out);

            }
        });



        imageViewBackButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mainIntent = new Intent(InstructionActivity4.this,PcListActivity.class);
                startActivity(mainIntent);
                overridePendingTransition(R.xml.slide_in2,R.xml.slide_out2);

            }
        });

    }





}
