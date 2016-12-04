package liquiddark.mousepad.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import liquiddark.mousepad.R;

/**
 * Created by User on 10/31/2016.
 */

public class PcListArrayAdapter extends ArrayAdapter<String> {


    Typeface  CustomFontSegoePrint = Typeface.createFromAsset(getContext().getAssets(), "fonts/Segoe_Print.ttf");

    ArrayList<String> items;


    public PcListArrayAdapter(Context context, int resource, ArrayList<String> items) {
        super(context, resource, items);
        this.items = items;
    }




    @Override
    public int getCount() {
        return super.getCount();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.list_view_custom_item, null);

        TextView pcNameTextView = (TextView) v.findViewById(R.id.pcNameTextView);
        pcNameTextView.setTypeface(CustomFontSegoePrint,Typeface.BOLD);
        pcNameTextView.setText(items.get(position));

        ImageView pcImage = (ImageView) v.findViewById(R.id.pcIconImageView);
        pcImage.setImageResource(R.drawable.pc_icon_orange);


        return v;
    }

    
}

