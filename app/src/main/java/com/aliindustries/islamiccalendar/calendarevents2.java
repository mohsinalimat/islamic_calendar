package com.aliindustries.islamiccalendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.INotificationSideChannel;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.joda.time.DateTime;
import org.joda.time.chrono.IslamicChronology;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


import static androidx.core.util.Preconditions.checkArgument;
import static com.aliindustries.islamiccalendar.Main2Activity.all_hijri_months;

public class calendarevents2 extends AppCompatActivity  {
    ListView resultsListView;
    String islamicmonth_val = "";
    String islamicyr_val = "";
    String islamicday_val = "";
    BottomNavigationView btmnav_view;
    TextView title;

    String gregday_val = "";
    String gregmonth_val = "";
    String gregmonth_numberval = "";
    public static String itemoption = "";

    String gregyr_val = "";
    public  String eventval = "";
    DatabaseHelper myDb;
    Calendar c_calendar = Calendar.getInstance();

    int checkcounter = 0;

    Calendar start_calendar = Calendar.getInstance();
    String db_event = "";
    Calendar end_calendar = Calendar.getInstance();


    Calendar startdate = Calendar.getInstance();
    Calendar enddate = Calendar.getInstance();

    private String[] title2;
   private String[] subitem ;
    private String[] subsubitem;
    private  String[] subsubsubitem ;
    private String[] subsubsubsubitem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendarevents2);

        resultsListView = (ListView) findViewById(R.id.listview0002);
        title = (TextView) findViewById(R.id.maintitle005);
        btmnav_view = (BottomNavigationView) findViewById(R.id.btmnavigationview3000);
        myDb = new DatabaseHelper(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            islamicmonth_val = extras.getString("islamic_month");
            islamicyr_val = extras.getString("islamic_year");
            islamicday_val = extras.getString("islamic_day");

            gregday_val = extras.getString("greg_day");
            gregmonth_val = extras.getString("greg_month");
            gregmonth_numberval = extras.getString("greg_monthnumber");
            gregyr_val = extras.getString("greg_yr");

            eventval = extras.getString("m_event");

        }

        c_calendar.set(Calendar.DAY_OF_MONTH,Integer.parseInt(gregday_val));
        c_calendar.set(Calendar.MONTH,Integer.parseInt(gregmonth_numberval));
        c_calendar.set(Calendar.YEAR,Integer.parseInt(gregyr_val));
        c_calendar.set(Calendar.HOUR_OF_DAY,c_calendar.get(Calendar.HOUR_OF_DAY));
        c_calendar.set(Calendar.MINUTE,c_calendar.get(Calendar.MINUTE)+10);


        btmnav_view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.delete:
                        if(checkcounter <= 0) {
                            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Please select a reminder", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                        else {
                            Cursor cursor50 = myDb.searchEventID(Integer.toString(start_calendar.get(Calendar.DAY_OF_MONTH)), Integer.toString(start_calendar.get(Calendar.MONTH)), Integer.toString(start_calendar.get(Calendar.YEAR)), db_event, Integer.toString(start_calendar.get(Calendar.HOUR_OF_DAY)), Integer.toString(start_calendar.get(Calendar.MINUTE)), Integer.toString(end_calendar.get(Calendar.DAY_OF_MONTH)), Integer.toString(end_calendar.get(Calendar.MONTH)), Integer.toString(end_calendar.get(Calendar.YEAR)), Integer.toString(end_calendar.get(Calendar.HOUR_OF_DAY)), Integer.toString(end_calendar.get(Calendar.MINUTE)));

                            int id = 0;
                            ArrayList<String> idArraylist = new ArrayList<String>();
                            if (cursor50.moveToFirst()) {
                                do {
                                    String data = cursor50.getString(cursor50.getColumnIndex("EVENTID"));
                                    id = Integer.parseInt(data);

                                    // do what ever you want here
                                } while (cursor50.moveToNext());
                            }
                            cursor50.close();
                            DeleteCalendarEntry(id);

                            System.out.println("the id is: " + id);

                            Cursor cursor51 = myDb.getID_EventID(Integer.toString(id));
                            if (cursor51.moveToFirst()) {
                                do {
                                    String data = cursor51.getString(cursor51.getColumnIndex("ID"));
                                    idArraylist.add(data);

                                    // do what ever you want here
                                } while (cursor51.moveToNext());
                            }
                            cursor51.close();


                            for (int i = 0; i < idArraylist.size(); i++) {

                                System.out.println("the id array is: " + idArraylist.get(i));

                                myDb.deleteData(idArraylist.get(i));

                            }

                            startActivity(new Intent(calendarevents2.this, Main2Activity.class));
                        }

                        break;

                    case R.id.share:
                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        String shareSub = "IMPORTANT ANNOUNCEMENT!";
                        String shareBody = "Salam brother or sister, \n\n " + islamicday_val + " " + islamicmonth_val + " " + islamicyr_val + " | " + gregday_val + " " + gregmonth_val + " " + gregyr_val + "\n\nToday is " + eventval + ". Share this purposeful news with your family and friends. \n\n Jazakallahu Khairan";
                        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                        startActivity(Intent.createChooser(sharingIntent, "Share using"));
                        break;
                }
                return true;
            }
        });

        title.setText(getDayOfMonthSuffix(Integer.parseInt(islamicday_val)) + " " +islamicmonth_val + " " + islamicyr_val);

        Cursor cursor1 = myDb.searchReminder(Integer.toString(c_calendar.get(Calendar.DAY_OF_MONTH)),Integer.toString(c_calendar.get(Calendar.MONTH)),Integer.toString(c_calendar.get(Calendar.YEAR)));
        Cursor cursor2 = myDb.searchStartDate(Integer.toString(c_calendar.get(Calendar.DAY_OF_MONTH)),Integer.toString(c_calendar.get(Calendar.MONTH)),Integer.toString(c_calendar.get(Calendar.YEAR)));
        Cursor cursor3 = myDb.searchEndDate(Integer.toString(c_calendar.get(Calendar.DAY_OF_MONTH)),Integer.toString(c_calendar.get(Calendar.MONTH)),Integer.toString(c_calendar.get(Calendar.YEAR)));

        ArrayList<String> reminderarraylist = new ArrayList<String>();
        ArrayList<String> startdayarraylist = new ArrayList<String>();
        ArrayList<String> startmontharraylist = new ArrayList<String>();
        ArrayList<String> startyrarraylist = new ArrayList<String>();
        ArrayList<String> starthrarraylist = new ArrayList<String>();
        ArrayList<String> startminarraylist = new ArrayList<String>();

        ArrayList<String> enddayarraylist = new ArrayList<String>();
        ArrayList<String> endmontharraylist = new ArrayList<String>();
        ArrayList<String> endyrarraylist = new ArrayList<String>();
        ArrayList<String> endhrarraylist = new ArrayList<String>();
        ArrayList<String> endminarraylist = new ArrayList<String>();

        if (cursor1.moveToFirst()){
            do{
                String data = cursor1.getString(cursor1.getColumnIndex("EVENT"));
                reminderarraylist.add(data);

                // do what ever you want here
            }while(cursor1.moveToNext());
        }
        cursor1.close();


        if (cursor2.moveToFirst()){
            do{
                String data = cursor2.getString(cursor2.getColumnIndex("DAY"));
                String data2 = cursor2.getString(cursor2.getColumnIndex("MONTH"));
                String data3 = cursor2.getString(cursor2.getColumnIndex("YEAR"));
                String data4 = cursor2.getString(cursor2.getColumnIndex("HOUR"));
                String data5 = cursor2.getString(cursor2.getColumnIndex("MINUTE"));

                startdayarraylist.add(data);
                startmontharraylist.add(data2);
                startyrarraylist.add(data3);
                starthrarraylist.add(data4);
                startminarraylist.add(data5);

                // do what ever you want here
            }while(cursor2.moveToNext());
        }
        cursor2.close();


        if (cursor3.moveToFirst()){
            do{
                String data = cursor3.getString(cursor3.getColumnIndex("ENDDAY"));
                String data2 = cursor3.getString(cursor3.getColumnIndex("ENDMONTH"));
                String data3 = cursor3.getString(cursor3.getColumnIndex("ENDYEAR"));
                String data4 = cursor3.getString(cursor3.getColumnIndex("ENDHOUR"));
                String data5 = cursor3.getString(cursor3.getColumnIndex("ENDMINUTE"));

                enddayarraylist.add(data);
                endmontharraylist.add(data2);
                endyrarraylist.add(data3);
                endhrarraylist.add(data4);
                endminarraylist.add(data5);

                // do what ever you want here
            }while(cursor3.moveToNext());
        }
        cursor3.close();

        title2 = new String[reminderarraylist.size()];
        subitem = new String[title2.length];
        subsubitem = new String[title2.length];
        subsubsubitem = new String[title2.length];
        subsubsubsubitem = new String[title2.length];
        int counter = 0;

        startdate.set(Calendar.SECOND,0);

        enddate.set(Calendar.SECOND,0);



        for(int i = 0; i <reminderarraylist.size();i++) {
            startdate.set(Calendar.DAY_OF_MONTH,Integer.parseInt(startdayarraylist.get(i)));
            startdate.set(Calendar.MONTH,Integer.parseInt(startmontharraylist.get(i)));
            startdate.set(Calendar.YEAR,Integer.parseInt(startyrarraylist.get(i)));
            startdate.set(Calendar.HOUR_OF_DAY,Integer.parseInt(starthrarraylist.get(i)));
            startdate.set(Calendar.MINUTE,Integer.parseInt(startminarraylist.get(i)));


            enddate.set(Calendar.DAY_OF_MONTH,Integer.parseInt(enddayarraylist.get(i)));
            enddate.set(Calendar.MONTH,Integer.parseInt(endmontharraylist.get(i)));
            enddate.set(Calendar.YEAR,Integer.parseInt(endyrarraylist.get(i)));
            enddate.set(Calendar.HOUR_OF_DAY,Integer.parseInt(endhrarraylist.get(i)));
            enddate.set(Calendar.MINUTE,Integer.parseInt(endminarraylist.get(i)));

            title2[i] = reminderarraylist.get(counter);
            counter++;

            DateTime dtISO1 = new DateTime(startdate.get(Calendar.YEAR), startdate.get(Calendar.MONTH) + 1, startdate.get(Calendar.DAY_OF_MONTH), startdate.get(Calendar.HOUR_OF_DAY), startdate.get(Calendar.MINUTE)); //NOTE: MUST GET DAY
            DateTime dtIslamic1 = dtISO1.withChronology(IslamicChronology.getInstance());

            DateTime dtISO2 = new DateTime(enddate.get(Calendar.YEAR), enddate.get(Calendar.MONTH) + 1, enddate.get(Calendar.DAY_OF_MONTH), enddate.get(Calendar.HOUR_OF_DAY), enddate.get(Calendar.MINUTE)); //NOTE: MUST GET DAY
            DateTime dtIslamic2 = dtISO2.withChronology(IslamicChronology.getInstance());

            String islamicday = Integer.toString(dtIslamic1.getDayOfMonth());
            String islamicmonth = Integer.toString(dtIslamic1.getMonthOfYear());
            String islamicyr = Integer.toString(dtIslamic1.getYear());
            String islamichr = Integer.toString(dtIslamic1.getHourOfDay());
            String islamicmin = Integer.toString(dtIslamic1.getMinuteOfHour());

            String islamicday2 = Integer.toString(dtIslamic2.getDayOfMonth());
            String islamicmonth2 = Integer.toString(dtIslamic2.getMonthOfYear());
            String islamicyr2 = Integer.toString(dtIslamic2.getYear());
            String islamichr2 = Integer.toString(dtIslamic2.getHourOfDay());
            String islamicmin2 = Integer.toString(dtIslamic2.getMinuteOfHour());

            String startislamicdate = "From: " + getDayOfMonthSuffix(Integer.parseInt(islamicday)) + " " + all_hijri_months.get(Integer.parseInt(islamicmonth)) + " " + islamicyr + " " + islamichr + ":" +islamicmin;

            if(checknumofdigits(Integer.parseInt(islamicmin)) == true) {
                startislamicdate = "From: " + getDayOfMonthSuffix(Integer.parseInt(islamicday)) + " " + all_hijri_months.get(Integer.parseInt(islamicmonth)) + " " + islamicyr + " " + islamichr + ":" + "0" +islamicmin;
            }

            String endislamicdate = "To: " + getDayOfMonthSuffix(Integer.parseInt(islamicday2)) + " " + all_hijri_months.get(Integer.parseInt(islamicmonth2)) + " " + islamicyr2 + " " + islamichr2 + ":" +islamicmin2;

            if(checknumofdigits(Integer.parseInt(islamicmin2)) == true) {
                endislamicdate = "To: " + getDayOfMonthSuffix(Integer.parseInt(islamicday2)) + " " + all_hijri_months.get(Integer.parseInt(islamicmonth2)) + " " + islamicyr2 + " " + islamichr2 + ":" + "0" +islamicmin2;
            }


            subitem[i] = "From: " + startdate.getTime().toString();
            subsubitem[i] = "To: " + enddate.getTime().toString();
            subsubsubitem[i] = startislamicdate;
            subsubsubsubitem[i] = endislamicdate;
        }


        final CustomAdapter3 adapter = new CustomAdapter3(calendarevents2.this,  title2,subitem, subsubitem,subsubsubitem,subsubsubsubitem,calendarevents2.this);

        resultsListView.setAdapter(adapter);
        resultsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                checkcounter = 1;
                String end_date_selectedFromList = adapter.getItem3(position);
                end_date_selectedFromList = end_date_selectedFromList.replace("End date: ","");
                String start_date_selectedFromList2 = adapter.getItem2(position);
                start_date_selectedFromList2 = start_date_selectedFromList2.replace("Start date: ","");

                String event_selectedFromList3 = adapter.getItem1(position);

                db_event = event_selectedFromList3;

                SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.getDefault());
                SimpleDateFormat sdf2 = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.getDefault());

                try {
                    Date date = sdf.parse(end_date_selectedFromList);// all done
                    Date date2 = sdf2.parse(start_date_selectedFromList2);// all done


                } catch (ParseException e) {
                    e.printStackTrace();
                }
                end_calendar = sdf.getCalendar();
                start_calendar = sdf2.getCalendar();



            }});




    }

    public static int daysBetween(Calendar day1, Calendar day2) {
        Calendar dayOne = (Calendar) day1.clone(),
                dayTwo = (Calendar) day2.clone();

        if (dayOne.get(Calendar.YEAR) == dayTwo.get(Calendar.YEAR)) {
            return Math.abs(dayOne.get(Calendar.DAY_OF_YEAR) - dayTwo.get(Calendar.DAY_OF_YEAR));
        } else {
            if (dayTwo.get(Calendar.YEAR) > dayOne.get(Calendar.YEAR)) {
                //swap them
                Calendar temp = dayOne;
                dayOne = dayTwo;
                dayTwo = temp;
            }
            int extraDays = 0;

            int dayOneOriginalYearDays = dayOne.get(Calendar.DAY_OF_YEAR);

            while (dayOne.get(Calendar.YEAR) > dayTwo.get(Calendar.YEAR)) {
                dayOne.add(Calendar.YEAR, -1);
                // getActualMaximum() important for leap years
                extraDays += dayOne.getActualMaximum(Calendar.DAY_OF_YEAR);
            }

            return extraDays - dayTwo.get(Calendar.DAY_OF_YEAR) + dayOneOriginalYearDays;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        resultsListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

    }

    private int DeleteCalendarEntry(int entryID) {
        int iNumRowsDeleted = 0;

        Uri eventUri = ContentUris
                .withAppendedId(getCalendarUriBase(), entryID);
        iNumRowsDeleted = getContentResolver().delete(eventUri, null, null);

        return iNumRowsDeleted;
    }

    private Uri getCalendarUriBase() {
        Uri eventUri;
        if (android.os.Build.VERSION.SDK_INT <= 7) {
            // the old way

            eventUri = Uri.parse("content://calendar/events");
        } else {
            // the new way

            eventUri = Uri.parse("content://com.android.calendar/events");
        }

        return eventUri;
    }
    public void backbtn900(View view) {

        startActivity(new Intent(calendarevents2.this,Main2Activity.class));
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);

    }

    @SuppressLint("RestrictedApi")
    String getDayOfMonthSuffix(final int n) {
        checkArgument(n >= 1 && n <= 31, "illegal day of month: " + n);
        if (n >= 11 && n <= 13) {
            return n+"th";
        }
        switch (n % 10) {
            case 1:  return n+"st";
            case 2:  return n+"nd";
            case 3:  return n+"rd";
            default: return n+"th";
        }
    }
    private static boolean checknumofdigits(int s) {

        int length = String.valueOf(s).length();

        if(length == 1) {

            return true;

        }

        return false;


    }
}