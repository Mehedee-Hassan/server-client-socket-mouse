package com.liquiddark.mousepad.mhr.mousepad.keyboard;

import android.app.Activity;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.liquiddark.mousepad.mhr.mousepad.R;

public class KeyboardActivity extends Activity {


    EditText keyboardEventGetET;
    ImageView backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard);

        InputMethodManager imm = (InputMethodManager)   getSystemService(this.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        keyboardEventGetET = (EditText) findViewById(R.id.keyboardEventGetET);
        keyboardEventGetET.addTextChangedListener(textWatcher);

        keyboardEventGetET.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_DEL){

                    Log.d("_LL","delete ");
                }
                return false;
            }
        });

        backButton = (ImageView) findViewById(R.id.backButtonKeyboard);

    }






    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            CharSequence newString = s.subSequence(start,start+count);
            Log.d("_LL",""+newString+" "+count+" "+before+" "+start);
           // Log.d("_LL",""+s);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}

