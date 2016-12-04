package liquiddark.mousepad.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import liquiddark.mousepad.PcListActivity;
import liquiddark.mousepad.R;

public class InstructionActivity extends Activity {


    ImageView doneButton ;
    private Typeface CustomFontSegoePrint;
    TextView instruction;
    boolean fromPcListActivity = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_instruction);


        CustomFontSegoePrint = Typeface.createFromAsset(getAssets(), "fonts/Segoe_Print.ttf");


        doneButton = (ImageView) findViewById(R.id.imageViewDoneButton);

        instruction =(TextView) findViewById(R.id.textViewPortIns);
        instruction.setTypeface(CustomFontSegoePrint);

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mainIntent = new Intent(InstructionActivity.this,InstructionActivity2.class);
                startActivity(mainIntent);
                overridePendingTransition(R.xml.slide_in, R.xml.slide_out);

            }
        });

        fromPcListActivity = false;

        Bundle bundle = getIntent().getExtras();
        if(bundle!= null) {
            String name = bundle.getString("__FROM_ACTIVITY");
            if (name.equalsIgnoreCase("PC_LIST_ACTIVITY")) {
                fromPcListActivity = true;
            }
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
       // if(fromPcListActivity)
        fromPcListActivity = false;
            finish();
    }





    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if(fromPcListActivity){
                startActivity(new Intent(InstructionActivity.this, PcListActivity.class));
                overridePendingTransition(R.xml.slide_in2,R.xml.slide_out2);

                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {

        if(fromPcListActivity){
            startActivity(new Intent(InstructionActivity.this, PcListActivity.class));
            overridePendingTransition(R.xml.slide_in2,R.xml.slide_out2);

            return;
        }

        super.onBackPressed();


    }
}
