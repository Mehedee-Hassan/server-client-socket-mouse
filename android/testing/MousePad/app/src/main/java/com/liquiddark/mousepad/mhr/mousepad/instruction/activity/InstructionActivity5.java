package com.liquiddark.mousepad.mhr.mousepad.instruction.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.liquiddark.mousepad.mhr.mousepad.R;

public class InstructionActivity5 extends Activity {




    ImageView imageViewOkButton, imageViewBackButton;
    private Typeface CustomFontSegoePrint;
    TextView textViewInstruction4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction5);

        textViewInstruction4 = (TextView) findViewById(R.id.textViewInstruction5);

        imageViewBackButton = (ImageView) findViewById(R.id.imageViewBackButton5);
        imageViewOkButton = (ImageView) findViewById(R.id.imageViewDoneButton5);


        CustomFontSegoePrint = Typeface.createFromAsset(getAssets(), "fonts/Segoe_Print.ttf");
        textViewInstruction4.setTypeface(CustomFontSegoePrint);

        imageViewOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mainIntent = new Intent(InstructionActivity5.this,InstructionActivity6.class);
                startActivity(mainIntent);
                overridePendingTransition(R.xml.slide_in, R.xml.slide_out);

            }
        });



        imageViewBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mainIntent = new Intent(InstructionActivity5.this,InstructionActivity4.class);
                startActivity(mainIntent);
                overridePendingTransition(R.xml.slide_in2,R.xml.slide_out2);

            }
        });

    }





}
