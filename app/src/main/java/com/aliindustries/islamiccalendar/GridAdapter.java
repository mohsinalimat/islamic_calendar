package com.aliindustries.islamiccalendar;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.chrono.IslamicChronology;


import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;


import static android.content.Context.MODE_PRIVATE;
import static com.aliindustries.islamiccalendar.Main2Activity.all_hijri_months;

public class GridAdapter extends BaseAdapter {
    private Context context;
    private String[] days;
    private String[] days2;
    private Activity activity;
    DatabaseHelper myDb;

    private int eventID = 0;

    private View view2;
    private LayoutInflater layoutInflater;


    public GridAdapter(Context context, String[] days, String[] days2, Activity activity) {
        this.context = context;
        this.days = days;
        this.days2 = days2;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return days.length;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final TextView textView1;
        TextView textView2;
        GridView gridView;

        final Spinner sp01 = (Spinner) activity.findViewById(R.id.spinner);
        final Spinner sp02 = (Spinner) activity.findViewById(R.id.spinner2);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null) {
            view2 = new View(context);

        } else {
            view2 = (View) view;
            //re-using if already here
        }


        view2 = layoutInflater.inflate(R.layout.single_item, null);
        textView1 = (TextView) view2.findViewById(R.id.txtviw1);
        textView2 = (TextView) view2.findViewById(R.id.txtviw2);
        gridView = (GridView) activity.findViewById(R.id.gridview);
        myDb = new DatabaseHelper(context);

        Cursor cursor1 = myDb.getDayData();
        Cursor cursor2 = myDb.getMonthData();
        Cursor cursor3 = myDb.getYearData();
        Cursor cursor4 = myDb.getEventData();

        ArrayList<String> dayarraylist = new ArrayList<String>();
        ArrayList<String> montharraylist = new ArrayList<String>();
        ArrayList<String> yrarraylist = new ArrayList<String>();
        ArrayList<String> eventarraylist = new ArrayList<String>();


        if (cursor1.moveToFirst()){
            do{
                String data = cursor1.getString(cursor1.getColumnIndex("DAY"));
                dayarraylist.add(data);

                // do what ever you want here
            }while(cursor1.moveToNext());
        }
        cursor1.close();

        if (cursor2.moveToFirst()){
            do{
                String data = cursor2.getString(cursor2.getColumnIndex("MONTH"));
                int tmp66 = Integer.parseInt(data) + 1;
                montharraylist.add(Integer.toString(tmp66));

                // do what ever you want here
            }while(cursor2.moveToNext());
        }
        cursor2.close();


        if (cursor3.moveToFirst()){
            do{
                String data = cursor3.getString(cursor3.getColumnIndex("YEAR"));
                yrarraylist.add(data);

                // do what ever you want here
            }while(cursor3.moveToNext());
        }
        cursor3.close();

        if (cursor4.moveToFirst()){
            do{
                String data = cursor4.getString(cursor4.getColumnIndex("EVENT"));
                eventarraylist.add(data);

                // do what ever you want here
            }while(cursor4.moveToNext());
        }
        cursor4.close();


        view2.setMinimumHeight(Main2Activity.height / 11);

        textView1.setText(days[i]);
        textView2.setText(days2[i]);
        view2.setBackgroundResource(R.drawable.gridborder);


        if (textView1.getText().toString().equals("M") || textView1.getText().toString().equals("T") || textView1.getText().toString().equals("W") || textView1.getText().toString().equals("F") || textView1.getText().toString().equals("S")) {

            view2.setBackgroundResource(R.drawable.whitebackground);

        }
        if (textView1.getText().toString().equals("")) {

            view2.setBackgroundResource(R.drawable.whitebackground);

        }


        Calendar now = Calendar.getInstance();
        int year2 = now.get(Calendar.YEAR);
        int month2 = now.get(Calendar.MONTH) + 1;
        int day2 = now.get(Calendar.DAY_OF_MONTH);

        String val1 = Integer.toString(day2);
        String month2string = Integer.toString(month2);
        String year2string = Integer.toString(year2);

        String mytxt = sp01.getSelectedItem().toString();
        String mytxt2 = sp02.getSelectedItem().toString();


        if (textView1.getText().toString().equals(val1) && month2string.equals(mytxt) && year2string.equals(mytxt2)) {
            view2.setBackgroundResource(R.drawable.border_color);
            Calendar mcalendar = Calendar.getInstance();
            DateTime dtISO1 = new DateTime(mcalendar.get(Calendar.YEAR), mcalendar.get(Calendar.MONTH) + 1, Integer.parseInt(textView1.getText().toString()), 0, 0); //NOTE: MUST GET DAY
            DateTime dtIslamic1 = dtISO1.withChronology(IslamicChronology.getInstance());
            String islamicyr2 = Integer.toString(dtIslamic1.getYear());
            String islamicmonth2 = Integer.toString(dtIslamic1.getMonthOfYear());
            String islamicday2 = Integer.toString(dtIslamic1.getDayOfMonth());

            String islamic_month_name3 = all_hijri_months.get(Integer.parseInt(islamicmonth2));
            SharedPreferences prefs2 = context.getSharedPreferences("UserData", MODE_PRIVATE);
            SharedPreferences.Editor editor2 = prefs2.edit();
            editor2.putString("islammonth1", islamic_month_name3);
            editor2.putString("islamday1", islamicday2);
            editor2.putString("islamyr1", islamicyr2);
            editor2.apply();

            if (textView2.getText().toString().equals("1")) {
                if (textView1.getText().toString().equals(val1) && month2string.equals(mytxt) && year2string.equals(mytxt2)) {


                    String txt1 = sp01.getSelectedItem().toString();
                    String txt2 = sp02.getSelectedItem().toString();
                    int month = Integer.parseInt(txt1);
                    int yr = Integer.parseInt(txt2);
                    DateTime dtISO = new DateTime(yr, month, Integer.parseInt(textView1.getText().toString()), 0, 0); //NOTE: MUST GET DAY
                    DateTime dtIslamic = dtISO.withChronology(IslamicChronology.getInstance());
                    String islamicyr = Integer.toString(dtIslamic.getYear());
                    String islamicmonth = Integer.toString(dtIslamic.getMonthOfYear());
                    String islamic_month_name1 = all_hijri_months.get(Integer.parseInt(islamicmonth));


                    SharedPreferences prefs = context.getSharedPreferences("UserData", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("islamicmonth", islamic_month_name1);
                    editor.putString("islamicday", textView2.getText().toString());
                    editor.putString("islamicyr", islamicyr);
                    editor.putString("event", "1st day of " + islamic_month_name1);
                    editor.apply();

                }
            }
            else {

                String txt1 = sp01.getSelectedItem().toString();
                String txt2 = sp02.getSelectedItem().toString();
                int month = Integer.parseInt(txt1);
                int yr = Integer.parseInt(txt2);
                DateTime dtISO = new DateTime(yr, month, Integer.parseInt(textView1.getText().toString()), 0, 0); //NOTE: MUST GET DAY
                DateTime dtIslamic = dtISO.withChronology(IslamicChronology.getInstance());


                String islamicyr = Integer.toString(dtIslamic.getYear());
                String islamicmonth = Integer.toString(dtIslamic.getMonthOfYear());
                String islamic_month_name1 = all_hijri_months.get(Integer.parseInt(islamicmonth));


                SharedPreferences prefs = context.getSharedPreferences("UserData", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("islamicmonth", islamic_month_name1);
                editor.putString("islamicday", textView2.getText().toString());
                editor.putString("islamicyr", islamicyr);
                editor.putString("event", "No significant event today");
                editor.apply();
            }
        }


        if (textView2.getText().toString().equals("1")) {

            String txt1 = sp01.getSelectedItem().toString();
            String txt2 = sp02.getSelectedItem().toString();
            int month = Integer.parseInt(txt1);
            int yr = Integer.parseInt(txt2);
            DateTime dtISO = new DateTime(yr, month, Integer.parseInt(textView1.getText().toString()), 0, 0); //NOTE: MUST GET DAY
            DateTime dtIslamic = dtISO.withChronology(IslamicChronology.getInstance());

            String islamicmonth = Integer.toString(dtIslamic.getMonthOfYear());
            String islamic_month_name1 = all_hijri_months.get(Integer.parseInt(islamicmonth));



            Calendar mcalendar1 = Calendar.getInstance();
            Calendar mcalendar2 = Calendar.getInstance();
            Calendar mnow = Calendar.getInstance();

            String events = "1st day of " + islamic_month_name1;

            mcalendar1.set(Calendar.DAY_OF_MONTH,Integer.parseInt(textView1.getText().toString()));
            mcalendar1.set(Calendar.MONTH,month-1);
            mcalendar1.set(Calendar.YEAR,yr);
            mcalendar1.set(Calendar.HOUR_OF_DAY,0);
            mcalendar1.set(Calendar.MINUTE,0);
            mcalendar1.set(Calendar.SECOND,1);


            mcalendar2.set(Calendar.DAY_OF_MONTH,Integer.parseInt(textView1.getText().toString()));
            mcalendar2.set(Calendar.MONTH,month-1);
            mcalendar2.set(Calendar.YEAR,yr);
            mcalendar2.set(Calendar.HOUR_OF_DAY,23);
            mcalendar2.set(Calendar.MINUTE,59);
            mcalendar1.set(Calendar.SECOND,59);


            if(mcalendar1.before(mnow)) {

            }
            else {
                if (myDb.CheckIsDataAlreadyInDBorNot(Integer.toString(mcalendar1.get(Calendar.DAY_OF_MONTH)), Integer.toString(mcalendar1.get(Calendar.MONTH)), Integer.toString(mcalendar1.get(Calendar.YEAR)), events, Integer.toString(mcalendar1.get(Calendar.HOUR_OF_DAY)), Integer.toString(mcalendar1.get(Calendar.MINUTE)), Integer.toString(mcalendar2.get(Calendar.DAY_OF_MONTH)), Integer.toString(mcalendar2.get(Calendar.MONTH)), Integer.toString(mcalendar2.get(Calendar.YEAR)), Integer.toString(mcalendar2.get(Calendar.HOUR_OF_DAY)), Integer.toString(mcalendar2.get(Calendar.MINUTE))) == false){
                    addEvent(context, events, "", mcalendar1, mcalendar2, 1);
                    myDb.insertData(Integer.toString(mcalendar1.get(Calendar.DAY_OF_MONTH)), Integer.toString(mcalendar1.get(Calendar.MONTH)), Integer.toString(mcalendar1.get(Calendar.YEAR)), events, Integer.toString(mcalendar1.get(Calendar.HOUR_OF_DAY)), Integer.toString(mcalendar1.get(Calendar.MINUTE)), Integer.toString(mcalendar2.get(Calendar.DAY_OF_MONTH)), Integer.toString(mcalendar2.get(Calendar.MONTH)), Integer.toString(mcalendar2.get(Calendar.YEAR)), Integer.toString(mcalendar2.get(Calendar.HOUR_OF_DAY)), Integer.toString(mcalendar2.get(Calendar.MINUTE)), Integer.toString(eventID), events);

                }

            }


            if (textView1.getText().toString().equals(val1) && month2string.equals(mytxt) && year2string.equals(mytxt2)) {
                view2.setBackgroundResource(R.drawable.border_color2);
            } else {
                int color = R.color.lightblue; // Transparent
                view2.setBackgroundColor(activity.getResources().getColor(color));
            }






            view2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String txt1 = sp01.getSelectedItem().toString();
                    String txt2 = sp02.getSelectedItem().toString();
                    int month = Integer.parseInt(txt1);
                    int yr = Integer.parseInt(txt2);
                    DateTime dtISO = new DateTime(yr, month, Integer.parseInt(textView1.getText().toString()), 0, 0); //NOTE: MUST GET DAY
                    DateTime dtIslamic = dtISO.withChronology(IslamicChronology.getInstance());

                    String islamicyr = Integer.toString(dtIslamic.getYear());
                    String islamicmonth = Integer.toString(dtIslamic.getMonthOfYear());
                    String islamicday = Integer.toString(dtIslamic.getDayOfMonth());
                    String islamic_month_name1 = all_hijri_months.get(Integer.parseInt(islamicmonth));


                    String gregorian_day = textView1.getText().toString();
                    String gregorian_month = sp01.getSelectedItem().toString();
                    String gregorian_yr = sp02.getSelectedItem().toString();
                    String gregorian_monthname = getMonthForInt(Integer.parseInt(gregorian_month) - 1);
                    String events = "1st day of " + islamic_month_name1;
                    String tmp = Integer.toString(month-1);


                    Intent i1 = new Intent(context, calendarevents.class);
                    i1.putExtra("islamic_month", islamic_month_name1);
                    i1.putExtra("islamic_year", islamicyr);
                    i1.putExtra("islamic_day", islamicday);
                    i1.putExtra("m_event", events);
                    i1.putExtra("greg_monthnumber", tmp);

                    i1.putExtra("greg_day", gregorian_day);
                    i1.putExtra("greg_month", gregorian_monthname);

                    i1.putExtra("greg_yr", gregorian_yr);
                    context.startActivity(i1);
                    activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


                }
            });
        }


        String txt1 = sp01.getSelectedItem().toString();
        String txt2 = sp02.getSelectedItem().toString();
        int month = Integer.parseInt(txt1);
        int yr = Integer.parseInt(txt2);
        int day = 0;

        datechoser("Ashura Day", yr, month, day, "9", "1");
        datechoser("Ashura Day", yr, month, day, "10", "1");

        datechoser("Eid Milad un Nabi", yr, month, day, "12", "3");

        datechoser("Shab e Meraj", yr, month, day, "27", "7");

        datechoser("Shab e Barat", yr, month, day, "15", "8");

        datechoser("Battle of Badr Day", yr, month, day, "17", "9");
        datechoser("Battle of Badr Day", yr, month, day, "17", "9");
        datechoser("Victory at Makkah", yr, month, day, "20", "9");
        datechoser("Laylat al Qadr", yr, month, day, "27", "9");

        datechoser("Eid al Fitr", yr, month, day, "1", "10");
        datechoser("Battle of Uhud Day", yr, month, day, "6", "10");

        datechoser("First day of Hajj: Tawaf and Saee", yr, month, day, "8", "12");
        datechoser("Second day of Hajj: Arafat and Muzdalifah", yr, month, day, "9", "12");
        datechoser("Third day of Hajj: Eid ul Adha, Ramy al Jamarat, Animal sacrifice and Tawaf Ziyarat", yr, month, day, "10", "12");
        datechoser("Fourth day of Hajj: Day of Tashriq", yr, month, day, "11", "12");
        datechoser("Fifth day of Hajj: Day of Tashriq", yr, month, day, "12", "12");
        datechoser("Sixth day of Hajj: Day of Tashriq", yr, month, day, "13", "12");


        for(int k = 0; k <dayarraylist.size();k++) {

            datechoser_reminder(eventarraylist.get(k),yrarraylist.get(k),montharraylist.get(k),dayarraylist.get(k));
        }

        //make new function for reminder


        return view2;
    }

    private String getMonthForInt(int m) {
        String month = "invalid";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (m >= 0 && m <= 11) {
            month = months[m];
        }
        return month;
    }

    private void datechoser(final String events, final int yr, final int month, int day, String islamicday, String islam_month_int) {
        final TextView textView1 = (TextView) view2.findViewById(R.id.txtviw1);
        TextView textView2 = (TextView) view2.findViewById(R.id.txtviw2);

        Calendar now = Calendar.getInstance();
        int year2 = now.get(Calendar.YEAR);
        int month2 = now.get(Calendar.MONTH) + 1;
        int day2 = now.get(Calendar.DAY_OF_MONTH);

        String val1 = Integer.toString(day2);
        String month2string = Integer.toString(month2);
        String year2string = Integer.toString(year2);

        String mytxt = Integer.toString(month);
        String mytxt2 = Integer.toString(yr);


        if (textView2.getText().toString().equals(islamicday)) {

            day = Integer.parseInt(textView1.getText().toString());
        }


        if (day != 0) {
            DateTime dtISO = new DateTime(yr, month, day, 0, 0); //NOTE: MUST GET DAY
            DateTime dtIslamic = dtISO.withChronology(IslamicChronology.getInstance());
            String islamicmonth = Integer.toString(dtIslamic.getMonthOfYear());
            String islamicyr = Integer.toString(dtIslamic.getYear());

            if (textView2.getText().toString().equals(islamicday) && islamicmonth.equals(islam_month_int)) {
                if (textView1.getText().toString().equals(val1) && month2string.equals(mytxt) && year2string.equals(mytxt2)) {
                    view2.setBackgroundResource(R.drawable.border_color2);
                    SharedPreferences prefs = context.getSharedPreferences("UserData", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("islamicmonth", all_hijri_months.get(Integer.parseInt(islamicmonth)));
                    editor.putString("islamicday", textView2.getText().toString());
                    editor.putString("islamicyr", islamicyr);
                    editor.putString("event", events);
                    editor.apply();


                } else {
                    int color = R.color.lightblue; // Transparent
                    view2.setBackgroundColor(activity.getResources().getColor(color));
                }


                Calendar mcalendar1 = Calendar.getInstance();
                Calendar mcalendar2 = Calendar.getInstance();
                Calendar mnow = Calendar.getInstance();


                mcalendar1.set(Calendar.DAY_OF_MONTH,day);
                mcalendar1.set(Calendar.MONTH,month-1);
                mcalendar1.set(Calendar.YEAR,yr);
                mcalendar1.set(Calendar.HOUR_OF_DAY,1);
                mcalendar1.set(Calendar.MINUTE,0);

                mcalendar2.set(Calendar.DAY_OF_MONTH,day);
                mcalendar2.set(Calendar.MONTH,month-1);
                mcalendar2.set(Calendar.YEAR,yr);
                mcalendar2.set(Calendar.HOUR_OF_DAY,23);
                mcalendar2.set(Calendar.MINUTE,56);

                if(mcalendar1.before(mnow)) {

                }
                else {
                    if (myDb.CheckIsDataAlreadyInDBorNot(Integer.toString(mcalendar1.get(Calendar.DAY_OF_MONTH)), Integer.toString(mcalendar1.get(Calendar.MONTH)), Integer.toString(mcalendar1.get(Calendar.YEAR)), events, Integer.toString(mcalendar1.get(Calendar.HOUR_OF_DAY)), Integer.toString(mcalendar1.get(Calendar.MINUTE)), Integer.toString(mcalendar2.get(Calendar.DAY_OF_MONTH)), Integer.toString(mcalendar2.get(Calendar.MONTH)), Integer.toString(mcalendar2.get(Calendar.YEAR)), Integer.toString(mcalendar2.get(Calendar.HOUR_OF_DAY)), Integer.toString(mcalendar2.get(Calendar.MINUTE))) == false){
                        addEvent(context, events, "", mcalendar1, mcalendar2, 1);
                    myDb.insertData(Integer.toString(mcalendar1.get(Calendar.DAY_OF_MONTH)), Integer.toString(mcalendar1.get(Calendar.MONTH)), Integer.toString(mcalendar1.get(Calendar.YEAR)), events, Integer.toString(mcalendar1.get(Calendar.HOUR_OF_DAY)), Integer.toString(mcalendar1.get(Calendar.MINUTE)), Integer.toString(mcalendar2.get(Calendar.DAY_OF_MONTH)), Integer.toString(mcalendar2.get(Calendar.MONTH)), Integer.toString(mcalendar2.get(Calendar.YEAR)), Integer.toString(mcalendar2.get(Calendar.HOUR_OF_DAY)), Integer.toString(mcalendar2.get(Calendar.MINUTE)), Integer.toString(eventID), events);

                }

                }



                view2.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        DateTime dtISO = new DateTime(yr, month, Integer.parseInt(textView1.getText().toString()), 0, 0); //NOTE: MUST GET DAY
                        DateTime dtIslamic = dtISO.withChronology(IslamicChronology.getInstance());
                        String islamicyr = Integer.toString(dtIslamic.getYear());
                        String islamicmonth = Integer.toString(dtIslamic.getMonthOfYear());
                        String islamicday = Integer.toString(dtIslamic.getDayOfMonth());
                        String islamic_month_name1 = all_hijri_months.get(Integer.parseInt(islamicmonth));

                        String gregorian_day = textView1.getText().toString();
                        String greg_monthname = getMonthForInt(month - 1);
                        String tmp = Integer.toString(month-1);


                        Intent i1 = new Intent(context, calendarevents.class);
                        i1.putExtra("islamic_month", islamic_month_name1);
                        i1.putExtra("islamic_year", islamicyr);
                        i1.putExtra("islamic_day", islamicday);
                        i1.putExtra("greg_monthnumber", tmp);

                        i1.putExtra("greg_day", gregorian_day);
                        i1.putExtra("greg_month", greg_monthname);
                        i1.putExtra("greg_yr", Integer.toString(yr));

                        i1.putExtra("m_event", events);


                        context.startActivity(i1);


                    }

                });
            }

        }

    }


    public void addEvent(Context ctx, String title, String description, Calendar start, Calendar end,int minutes_notification) {

        ContentResolver contentResolver = ctx.getContentResolver();
        TimeZone tz = TimeZone.getDefault();

        ContentValues calEvent = new ContentValues();
        calEvent.put(CalendarContract.Events.CALENDAR_ID, 1); // XXX pick)
        calEvent.put(CalendarContract.Events.TITLE, title);
        calEvent.put(CalendarContract.Events.DESCRIPTION, description);

        calEvent.put(CalendarContract.Events.DTSTART, start.getTimeInMillis());
        calEvent.put(CalendarContract.Events.DTEND, end.getTimeInMillis());
        calEvent.put(CalendarContract.Events.EVENT_TIMEZONE, tz.getID());
        calEvent.put(CalendarContract.Events.HAS_ALARM, 1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ctx.checkSelfPermission(Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
        }
        Uri uri = contentResolver.insert(CalendarContract.Events.CONTENT_URI, calEvent);

        // The returned Uri contains the content-retriever URI for
        // the newly-inserted event, including its id
        int id = Integer.parseInt(uri.getLastPathSegment());
        eventID = id;
        Toast.makeText(ctx, "Created Calendar Event " + id,
                Toast.LENGTH_SHORT).show();

        ContentValues reminder = new ContentValues();
        reminder.put(CalendarContract.Reminders.EVENT_ID, id);
        reminder.put(CalendarContract.Reminders.MINUTES, minutes_notification);
        reminder.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
        ctx.getContentResolver().insert(CalendarContract.Reminders.CONTENT_URI, reminder);
    }



    private void datechoser_reminder(final String events, final String yr, final String month, String day) {
        final TextView textView1 = (TextView) view2.findViewById(R.id.txtviw1);
        TextView textView2 = (TextView) view2.findViewById(R.id.txtviw2);
        final Spinner sp01 = (Spinner) activity.findViewById(R.id.spinner);
        final Spinner sp02 = (Spinner) activity.findViewById(R.id.spinner2);
        Calendar now = Calendar.getInstance();
        int year2 = now.get(Calendar.YEAR);
        int month2 = now.get(Calendar.MONTH) + 1;
        int day2 = now.get(Calendar.DAY_OF_MONTH);

        String val1 = Integer.toString(day2);
        String month2string = Integer.toString(month2);
        String year2string = Integer.toString(year2);

        String mytxt = month;
        String mytxt2 = yr;

        final DateTime dtISO = new DateTime(Integer.parseInt(yr), Integer.parseInt(month), Integer.parseInt(day), 0, 0); //NOTE: MUST GET DAY
        final DateTime dtIslamic = dtISO.withChronology(IslamicChronology.getInstance());
        String islamicmonth = Integer.toString(dtIslamic.getMonthOfYear());
        String islamicyr = Integer.toString(dtIslamic.getYear());

        if (textView1.getText().toString().equals(day) && sp01.getSelectedItem().toString().equals(month) && sp02.getSelectedItem().toString().equals(yr)) {
            if (textView1.getText().toString().equals(val1) && month2string.equals(mytxt) && year2string.equals(mytxt2)) {
                view2.setBackgroundResource(R.drawable.border_color2);
                SharedPreferences prefs = context.getSharedPreferences("UserData", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("islamicmonth", all_hijri_months.get(Integer.parseInt(islamicmonth)));
                editor.putString("islamicday", textView2.getText().toString());
                editor.putString("islamicyr", islamicyr);
                editor.putString("event", events);
                editor.apply();


            } else {
                int color = R.color.lightblue;
                view2.setBackgroundColor(activity.getResources().getColor(color));
            }


            view2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    String islamicyr = Integer.toString(dtIslamic.getYear());
                    String islamicmonth = Integer.toString(dtIslamic.getMonthOfYear());
                    String islamicday = Integer.toString(dtIslamic.getDayOfMonth());
                    String islamic_month_name1 = all_hijri_months.get(Integer.parseInt(islamicmonth));

                    String gregorian_day = textView1.getText().toString();
                    String greg_monthname = getMonthForInt(Integer.parseInt(month) - 1);

                    String tmp = String.valueOf(Integer.parseInt(month) - 1);

                    Intent i1 = new Intent(context, calendarevents2.class);
                    i1.putExtra("islamic_month", islamic_month_name1);
                    i1.putExtra("islamic_year", islamicyr);
                    i1.putExtra("islamic_day", islamicday);
                    i1.putExtra("greg_day", gregorian_day);
                    i1.putExtra("greg_month", greg_monthname);
                    i1.putExtra("greg_monthnumber", tmp);

                    i1.putExtra("greg_yr", yr);
                    i1.putExtra("m_event", events);

                    context.startActivity(i1);


                }

            });


        }

    }

}