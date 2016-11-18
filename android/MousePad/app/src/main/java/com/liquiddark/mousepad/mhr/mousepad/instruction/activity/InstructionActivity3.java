package com.liquiddark.mousepad.mhr.mousepad.instruction.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.liquiddark.mousepad.mhr.mousepad.PcListActivity;
import com.liquiddark.mousepad.mhr.mousepad.R;

public class InstructionActivity3 extends Activity {


    ImageView doneButton ;
    private Typeface CustomFontSegoePrint;
    TextView instruction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction3);


        CustomFontSegoePrint = Typeface.createFromAsset(getAssets(), "fonts/Segoe_Print.ttf");


        doneButton = (ImageView) findViewById(R.id.imageViewDoneButton3);
        instruction = (TextView) findViewById(R.id.textViewInstruction3);

        instruction.setTypeface(CustomFontSegoePrint);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mainIntent = new Intent(InstructionActivity3.this,PcListActivity.class);
                startActivity(mainIntent);
                overridePendingTransition(R.xml.slide_in, R.xml.slide_out);

            }
        });


    }
}
