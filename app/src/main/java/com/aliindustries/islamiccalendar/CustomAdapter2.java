package com.aliindustries.islamiccalendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter2 extends BaseAdapter {

    private Context mContext;
    private String[]  Title;
    private String[]  subTitle1;
    private String[]  subTitle2;
    private View view2;
    LayoutInflater inflater;
    public CustomAdapter2(Context context, String[] text1,String[] subtext1,String[] subtext2) {
        mContext = context;
        Title = text1;
        subTitle1 = subtext1;
        subTitle2 = subtext2;

    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return Title.length;
    }

    @Override

    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView title;
        TextView subtitle1;

        TextView subtitle2;

        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            view2 = new View(mContext);

        } else {
            view2 = (View) convertView;
            //re-using if already here
        }

        view2 = inflater.inflate(R.layout.list_items3, parent, false);
        title = (TextView) view2.findViewById(R.id.item5);
        subtitle1 = (TextView) view2.findViewById(R.id.subitem5);
        subtitle2 = (TextView) view2.findViewById(R.id.subsubitem5);

        title.setText(Title[position]);
         subtitle1.setText(subTitle1[position]);
        if(position < 2) {
            subtitle2.setText(subTitle2[position]);
        }
        if(subtitle2.getText().toString().equals("")) {

            subtitle2.setHeight(15);
            subtitle2.setMinimumHeight(15);
        }
        return view2;
    }


}