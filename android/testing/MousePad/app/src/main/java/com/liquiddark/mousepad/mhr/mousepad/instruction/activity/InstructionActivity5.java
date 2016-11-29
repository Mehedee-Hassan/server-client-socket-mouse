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

public class InstructionActivity5 extends Activity {




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


        imageViewdoesntwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mainIntent = new Intent(InstructionActivity5.this,InstructionActivity3.class);
                startActivity(mainIntent);
                overridePendingTransition(R.xml.slide_in, R.xml.slide_out);

            }
        });



        imageViewBackButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mainIntent = new Intent(InstructionActivity5.this,PcListActivity.class);
                startActivity(mainIntent);
                overridePendingTransition(R.xml.slide_in2,R.xml.slide_out2);

            }
        });

    }





}
