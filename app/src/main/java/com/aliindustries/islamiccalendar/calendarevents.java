package com.aliindustries.islamiccalendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.joda.time.DateTime;
import org.joda.time.chrono.IslamicChronology;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static androidx.core.util.Preconditions.checkArgument;
import static com.aliindustries.islamiccalendar.Main2Activity.all_hijri_months;

public class calendarevents extends AppCompatActivity {
    ListView resultsListView;
    String islamicmonth_val = "";
    String islamicyr_val = "";
    String islamicday_val = "";
    BottomNavigationView btmnav_view;
    TextView title;
    Calendar c_calendar = Calendar.getInstance();
    Calendar c_calendar_end = Calendar.getInstance();

    String gregmonth_numberval = "";

    String gregday_val = "";
    String gregmonth_val = "";
    String gregyr_val = "";
    public  String eventval = "";

    private String[] title2;
    private String[] subitem ;
    private String[] subsubitem;
    private  String[] subsubsubitem ;
    private String[] subsubsubsubitem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendarevents);

        resultsListView = (ListView) findViewById(R.id.listview);
        title = (TextView) findViewById(R.id.maintitle);
        btmnav_view = (BottomNavigationView) findViewById(R.id.btmnavigationview);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            islamicmonth_val = extras.getString("islamic_month");
            islamicyr_val = extras.getString("islamic_year");
            islamicday_val = extras.getString("islamic_day");
            gregmonth_numberval = extras.getString("greg_monthnumber");

            gregday_val = extras.getString("greg_day");
            gregmonth_val = extras.getString("greg_month");
            gregyr_val = extras.getString("greg_yr");

            eventval = extras.getString("m_event");
        }


        c_calendar.set(Calendar.DAY_OF_MONTH,Integer.parseInt(gregday_val));
        c_calendar.set(Calendar.MONTH,Integer.parseInt(gregmonth_numberval));
        c_calendar.set(Calendar.YEAR,Integer.parseInt(gregyr_val));
        c_calendar.set(Calendar.HOUR_OF_DAY,0);
        c_calendar.set(Calendar.MINUTE,0);
        c_calendar.set(Calendar.SECOND,1);

        c_calendar_end.set(Calendar.DAY_OF_MONTH,Integer.parseInt(gregday_val));
        c_calendar_end.set(Calendar.MONTH,Integer.parseInt(gregmonth_numberval));
        c_calendar_end.set(Calendar.YEAR,Integer.parseInt(gregyr_val));
        c_calendar_end.set(Calendar.HOUR_OF_DAY,23);
        c_calendar_end.set(Calendar.MINUTE,59);
        c_calendar_end.set(Calendar.SECOND,59);

        btmnav_view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {

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

        title.setText(getDayOfMonthSuffix(Integer.parseInt(islamicday_val)) + " " + islamicmonth_val + " " + islamicyr_val);


        title2 = new String[1];
        subitem = new String[title2.length];
        subsubitem = new String[title2.length];
        subsubsubitem = new String[title2.length];
        subsubsubsubitem = new String[title2.length];


        DateTime dtISO = new DateTime(c_calendar.get(Calendar.YEAR), c_calendar.get(Calendar.MONTH)+1, c_calendar.get(Calendar.DAY_OF_MONTH), c_calendar.get(Calendar.HOUR_OF_DAY), c_calendar.get(Calendar.MINUTE)); //NOTE: MUST GET DAY
        DateTime dtIslamic = dtISO.withChronology(IslamicChronology.getInstance());

        String islamicstartdate = "From: " + getDayOfMonthSuffix(dtIslamic.getDayOfMonth()) + " " + all_hijri_months.get(dtIslamic.getMonthOfYear()) + " " + dtIslamic.getYear() + " " + dtIslamic.getHourOfDay() + ":" + dtIslamic.getMinuteOfHour();;

        if(checknumofdigits(dtIslamic.getMinuteOfHour()) == true) {
            islamicstartdate = "From: " + getDayOfMonthSuffix(dtIslamic.getDayOfMonth()) + " " + all_hijri_months.get(dtIslamic.getMonthOfYear()) + " " + dtIslamic.getYear() + " " + dtIslamic.getHourOfDay() + ":" + "0" + dtIslamic.getMinuteOfHour();
        }




        dtISO = new DateTime(c_calendar_end.get(Calendar.YEAR), c_calendar_end.get(Calendar.MONTH)+1, c_calendar_end.get(Calendar.DAY_OF_MONTH), c_calendar_end.get(Calendar.HOUR_OF_DAY), c_calendar_end.get(Calendar.MINUTE));
        dtIslamic = dtISO.withChronology(IslamicChronology.getInstance());

        String islamicenddate = "To: " +getDayOfMonthSuffix(dtIslamic.getDayOfMonth()) + " " + all_hijri_months.get(dtIslamic.getMonthOfYear()) + " " + dtIslamic.getYear() + " " + dtIslamic.getHourOfDay() + ":" + dtIslamic.getMinuteOfHour();


        if(checknumofdigits(dtIslamic.getMinuteOfHour()) == true) {
            islamicenddate = "To: " +getDayOfMonthSuffix(dtIslamic.getDayOfMonth()) + " " + all_hijri_months.get(dtIslamic.getMonthOfYear()) + " " + dtIslamic.getYear() + " " + dtIslamic.getHourOfDay() + ":" + "0" + dtIslamic.getMinuteOfHour();
        }



        title2[0] = eventval;
        subitem[0] = "From: " + c_calendar.getTime().toString();
        subsubitem[0] = "To: " + c_calendar_end.getTime().toString();
        subsubsubitem[0] = islamicstartdate;
        subsubsubsubitem[0] = islamicenddate;


        final CustomAdapter3 adapter = new CustomAdapter3(calendarevents.this,  title2,subitem, subsubitem,subsubsubitem,subsubsubsubitem,calendarevents.this);

        resultsListView.setAdapter(adapter);

    }
    public void backbtn(View view) {

        startActivity(new Intent(calendarevents.this,Main2Activity.class));
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