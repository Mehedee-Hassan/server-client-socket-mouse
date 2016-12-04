package liquiddark.mousepad.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import liquiddark.mousepad.PadActivity;
import liquiddark.mousepad.PcListActivity;
import liquiddark.mousepad.R;

public class InstructionActivity7 extends Activity {




    ImageView imageViewdoneKey, imageViewBackButton;
    private Typeface CustomFontSegoePrint;
    TextView textViewInstruction4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction7);

        textViewInstruction4 = (TextView) findViewById(R.id.textViewInstruction7);

        imageViewBackButton = (ImageView) findViewById(R.id.imageViewBackButton7);
        imageViewdoneKey = (ImageView) findViewById(R.id.imageViewDoneButton7);



        CustomFontSegoePrint = Typeface.createFromAsset(getAssets(), "fonts/Segoe_Print.ttf");
        textViewInstruction4.setTypeface(CustomFontSegoePrint);

        imageViewdoneKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mainIntent = new Intent(InstructionActivity7.this,PcListActivity.class);
                startActivity(mainIntent);
                overridePendingTransition(R.xml.slide_in, R.xml.slide_out);

            }
        });



        imageViewBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mainIntent = new Intent(InstructionActivity7.this,InstructionActivity6.class);
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


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            startActivity(new Intent(InstructionActivity7.this, InstructionActivity6.class));
            overridePendingTransition(R.xml.slide_in2, R.xml.slide_out2);
            finish();
            return true;
//            return super.onKeyDown(keyCode, event);

        }
        return false;
    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(InstructionActivity7.this, InstructionActivity6.class));
        overridePendingTransition(R.xml.slide_in2,R.xml.slide_out2);


           finish();
        //    super.onBackPressed();


    }


}
