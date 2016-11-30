package liquiddark.mousepad;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by User on 11/1/2016.
 */

public class TextViewCustom extends TextView {


    public TextViewCustom(Context context) {
        super(context);
        init(context);
    }

    public TextViewCustom(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TextViewCustom(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TextViewCustom(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {

            super(context, attrs, defStyleAttr, defStyleRes);
            init(context);


    }


    private void init(Context context){
        Typeface CustomFontSegoePrint = Typeface.createFromAsset(context.getAssets(), "fonts/Segoe_Print.ttf");
        setTypeface(CustomFontSegoePrint);
    }
}
