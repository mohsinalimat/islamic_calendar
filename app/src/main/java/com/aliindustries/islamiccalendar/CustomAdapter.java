package com.aliindustries.islamiccalendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {

    private Context mContext;
    private String[]  Title;
    private String[]  subTitle;
    private int[] imge;
    private View view2;
    LayoutInflater inflater;
    public CustomAdapter(Context context, String[] text1,String[] subtext1,int[] imageIds) {
        mContext = context;
        Title = text1;
        imge = imageIds;
        subTitle = subtext1;

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

        ImageView i1;

        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            view2 = new View(mContext);

        } else {
            view2 = (View) convertView;
            //re-using if already here
        }

        view2 = inflater.inflate(R.layout.list_items2, parent, false);
        i1 = (ImageView) view2.findViewById(R.id.imgIcon);
        title = (TextView) view2.findViewById(R.id.txtTitle);
        subtitle1 = (TextView) view2.findViewById(R.id.txtTitle2);

        title.setText(Title[position]);
        subtitle1.setText(subTitle[position]);
        i1.setImageResource(imge[position]);

        return view2;
    }


}