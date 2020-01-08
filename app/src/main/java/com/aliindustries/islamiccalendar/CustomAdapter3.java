package com.aliindustries.islamiccalendar;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter3 extends BaseAdapter {

    private Context mContext;
    private String[]  Title;
    private String[]  subTitle1;
    private String[]  subTitle2;
    private String[]  subTitle3;
    private String[]  subTitle4;
    private Activity activity;


    private View view2;
    LayoutInflater inflater;
    public CustomAdapter3(Context context, String[] text1, String[] subtext1, String[] subtext2, String[] subtext3, String[] subtext4, Activity activity) {
        mContext = context;
        Title = text1;
        subTitle1 = subtext1;
        subTitle2 = subtext2;
        subTitle3 = subtext3;
        subTitle4 = subtext4;
        this.activity = activity;


    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return Title.length;
    }

    public String getItem1(int position) {
        return Title[position];
    }
    public String getItem2(int position) {
        return subTitle1[position];
    }
    public String getItem3(int position) {
        return subTitle2[position];
    }
    public String getItem4(int position) {
        return subTitle3[position];
    }
    public String getItem5(int position) {
        return subTitle4[position];
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

        TextView subtitle3;
        TextView subtitle4;


        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            view2 = new View(mContext);

        } else {
            view2 = (View) convertView;
            //re-using if already here
        }

        view2 = inflater.inflate(R.layout.list_items4, parent, false);
        title = (TextView) view2.findViewById(R.id.item5000);
        subtitle1 = (TextView) view2.findViewById(R.id.subitem5000);
        subtitle2 = (TextView) view2.findViewById(R.id.subsubitem5000);
        subtitle3 = (TextView) view2.findViewById(R.id.subsubsubitem5000);
        subtitle4 = (TextView) view2.findViewById(R.id.subsubsubsubitem5000);



        title.setText(Title[position]);
        subtitle1.setText(subTitle1[position]);
        subtitle2.setText(subTitle2[position]);

        subtitle3.setText(subTitle3[position]);
        subtitle4.setText(subTitle4[position]);

        return view2;
    }


}