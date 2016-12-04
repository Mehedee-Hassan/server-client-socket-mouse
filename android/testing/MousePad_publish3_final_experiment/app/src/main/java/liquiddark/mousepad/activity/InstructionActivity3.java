package liquiddark.mousepad.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import liquiddark.mousepad.PcListActivity;
import liquiddark.mousepad.R;


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

        ImageView imageViewBackButton3 =(ImageView) findViewById(R.id.imageViewBackButton3);

        imageViewBackButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mainIntent = new Intent(InstructionActivity3.this,InstructionActivity2.class);
                startActivity(mainIntent);
                overridePendingTransition(R.xml.slide_out2, R.xml.slide_in2);

            }
        });

    }


    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
