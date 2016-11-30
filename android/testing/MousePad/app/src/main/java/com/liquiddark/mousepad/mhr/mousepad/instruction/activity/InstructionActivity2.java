package com.liquiddark.mousepad.mhr.mousepad.instruction.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.liquiddark.mousepad.mhr.mousepad.R;

public class InstructionActivity2 extends Activity {



    ImageView doneButton ;
    private Typeface CustomFontSegoePrint;
    TextView instruction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction2);


        CustomFontSegoePrint = Typeface.createFromAsset(getAssets(), "fonts/Segoe_Print.ttf");




        instruction = (TextView) findViewById(R.id.textViewInstruction2);
        instruction.setTypeface(CustomFontSegoePrint);

        doneButton = (ImageView) findViewById(R.id.imageViewDoneButton2);

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mainIntent = new Intent(InstructionActivity2.this,InstructionActivity3.class);
                startActivity(mainIntent);
                overridePendingTransition(R.xml.slide_in, R.xml.slide_out);

            }
        });

        ImageView imageViewBackButton2 =(ImageView) findViewById(R.id.imageViewBackButton2);

        imageViewBackButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mainIntent = new Intent(InstructionActivity2.this,InstructionActivity.class);
                startActivity(mainIntent);
                overridePendingTransition(R.xml.slide_in2,R.xml.slide_out2);

            }
        });


    }


    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
