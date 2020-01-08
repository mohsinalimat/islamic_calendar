package com.aliindustries.islamiccalendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import org.joda.time.DateTime;
import org.joda.time.chrono.IslamicChronology;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import android.text.format.DateFormat;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import static androidx.core.util.Preconditions.checkArgument;
import static com.aliindustries.islamiccalendar.Main2Activity.all_hijri_months;

public class reminder extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    ListView lv;


    String[] title = {"Start date", "End date", "Add alert"};
    String[] subitem;
    String[] subsubitem;
    CustomAdapter2 adapter;
    EditText editText;
    EditText editText2;

    private  int eventID = 0;

    String k_gregday = "";
    String k_gregmonth = "";
    String k_gregyr = "";
    int k_gregmonth2 = 0;
    BottomNavigationView bottomNavigationView;


    DatabaseHelper myDb;


    public static String itemoption = "";


    int tmp1;
    int tmp2;
    int tmp3;
    int tmp4;
    int tmp5;


    String fullgregoriandate = "";

    int counter = -1;

    private Calendar calendar1 = Calendar.getInstance();
    private Calendar calendar2 = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        Bundle extras = getIntent().getExtras();
        myDb = new DatabaseHelper(this);

        if (extras != null) {
            try {
                k_gregday = extras.getString("u_gregday");
                k_gregmonth = extras.getString("u_gregmonth");
                k_gregyr = extras.getString("u_gregyr");
                k_gregmonth2 = extras.getInt("u_gregmonth2");

            } catch (Exception e) {
            }
        }

        editText = (EditText) findViewById(R.id.editText1);
        editText2 = (EditText) findViewById(R.id.editText2);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.btmnavigationview60);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.save:
                        boolean isInserted = false;
                        if (itemoption.equals("") || editText.getText().toString().equals("") || myDb.CheckIsDataAlreadyInDBorNot(Integer.toString(calendar1.get(Calendar.DAY_OF_MONTH)),Integer.toString(calendar1.get(Calendar.MONTH)),Integer.toString(calendar1.get(Calendar.YEAR)),editText.getText().toString(),Integer.toString(calendar1.get(Calendar.HOUR_OF_DAY)),Integer.toString(calendar1.get(Calendar.MINUTE)),Integer.toString(calendar2.get(Calendar.DAY_OF_MONTH)),Integer.toString(calendar2.get(Calendar.MONTH)),Integer.toString(calendar2.get(Calendar.YEAR)),Integer.toString(calendar2.get(Calendar.HOUR_OF_DAY)),Integer.toString(calendar2.get(Calendar.MINUTE))) == true) {

                            if(myDb.CheckIsDataAlreadyInDBorNot(Integer.toString(calendar1.get(Calendar.DAY_OF_MONTH)),Integer.toString(calendar1.get(Calendar.MONTH)),Integer.toString(calendar1.get(Calendar.YEAR)),editText.getText().toString(),Integer.toString(calendar1.get(Calendar.HOUR_OF_DAY)),Integer.toString(calendar1.get(Calendar.MINUTE)),Integer.toString(calendar2.get(Calendar.DAY_OF_MONTH)),Integer.toString(calendar2.get(Calendar.MONTH)),Integer.toString(calendar2.get(Calendar.YEAR)),Integer.toString(calendar2.get(Calendar.HOUR_OF_DAY)),Integer.toString(calendar2.get(Calendar.MINUTE))) == true) {
                                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "This reminder already exists! Please create a new reminder", Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }
                            else {
                                if (itemoption.equals("") && editText.getText().toString().equals("")) {
                                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Please set a repeat option and a title", Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }
                                if (itemoption.equals("") && !editText.getText().toString().equals("")) {
                                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Please set a repeat option", Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }

                                if (editText.getText().toString().equals("") && !itemoption.equals("")) {
                                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Please set a title", Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }
                            }
                        }

                        else {
                            setAlarm();
                            SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("b_reminder", editText.getText().toString());
                            editor.apply();
                            long diffdays = 0;
                            Calendar tmpcalendar = (Calendar) calendar1.clone();
                            int u = 0;
                            int w = 0;
                            ArrayList<String> arrayList = new ArrayList<String>();

                            if (calendar1.get(Calendar.YEAR) != calendar2.get(Calendar.YEAR) || calendar1.get(Calendar.MONTH) != calendar2.get(Calendar.MONTH)) {

                                diffdays = daysBetween(calendar1, calendar2) + 1;


                                for (int i = 0; i <= diffdays; i++) {
                                    u = tmpcalendar.get(Calendar.DAY_OF_MONTH) + i;


                                    if (u > tmpcalendar.getActualMaximum(tmpcalendar.DAY_OF_MONTH)) {
                                        tmpcalendar.set(Calendar.DAY_OF_MONTH, 1);
                                        diffdays = diffdays - i;


                                        i = 0;
                                        tmpcalendar.set(Calendar.MONTH, tmpcalendar.get(Calendar.MONTH) + 1);
                                    }

                                    u = tmpcalendar.get(Calendar.DAY_OF_MONTH) + i;


                                    arrayList.add(Integer.toString(u));
                                    arrayList.add(Integer.toString(tmpcalendar.get(Calendar.MONTH)));
                                    arrayList.add(Integer.toString(tmpcalendar.get(Calendar.YEAR)));
                                    arrayList.add(editText.getText().toString());
                                    arrayList.add(Integer.toString(tmpcalendar.get(Calendar.HOUR_OF_DAY)));
                                    arrayList.add(Integer.toString(tmpcalendar.get(Calendar.MINUTE)));

                                    arrayList.add(Integer.toString(calendar2.get(Calendar.DAY_OF_MONTH)));
                                    arrayList.add(Integer.toString(calendar2.get(Calendar.MONTH)));
                                    arrayList.add(Integer.toString(calendar2.get(Calendar.YEAR)));
                                    arrayList.add(Integer.toString(calendar2.get(Calendar.HOUR_OF_DAY)));
                                    arrayList.add(Integer.toString(calendar2.get(Calendar.MINUTE)));

                                    arrayList.add(Integer.toString(eventID));
                                    arrayList.add(editText2.getText().toString());


                                    if (u == diffdays) {
                                        break;
                                    }

                                }

                                isInserted = myDb.quickinsertdata(arrayList);

                                if (isInserted == true) {
                                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Reminder saved", Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                } else {
                                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Reminder not saved! Please try again", Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }
                            } else {
                                diffdays = daysBetween(calendar1, calendar2);

                                for (int i = 0; i <= diffdays; i++) {

                                    isInserted = myDb.insertData(Integer.toString(calendar1.get(Calendar.DAY_OF_MONTH) + i), Integer.toString(calendar1.get(Calendar.MONTH)), Integer.toString(calendar1.get(Calendar.YEAR)), editText.getText().toString(), Integer.toString(calendar1.get(Calendar.HOUR_OF_DAY)), Integer.toString(calendar1.get(Calendar.MINUTE)), Integer.toString(calendar2.get(Calendar.DAY_OF_MONTH)), Integer.toString(calendar2.get(Calendar.MONTH)), Integer.toString(calendar2.get(Calendar.YEAR)), Integer.toString(calendar2.get(Calendar.HOUR_OF_DAY)), Integer.toString(calendar2.get(Calendar.MINUTE)),Integer.toString(eventID),editText2.getText().toString());
                                }
                                if (isInserted == true) {
                                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Reminder saved", Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                } else {
                                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Reminder not saved! Please try again", Snackbar.LENGTH_LONG);
                                    snackbar.show();
                                }
                            }

                        }



                        break;
                    case R.id.cancel:
                        startActivity(new Intent(reminder.this, Main2Activity.class));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        break;
                }
                return true;
            }
        });



        DateTime dtISO = new DateTime(Integer.parseInt(k_gregyr), k_gregmonth2, Integer.parseInt(k_gregday), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), 0); //NOTE: MUST GET DAY
        DateTime dtIslamic = dtISO.withChronology(IslamicChronology.getInstance());

        String islamicyr = Integer.toString(dtIslamic.getYear());
        String islamicmonth = Integer.toString(dtIslamic.getMonthOfYear());
        String islamic_month_name1 = all_hijri_months.get(Integer.parseInt(islamicmonth));
        String islamicday = Integer.toString(dtIslamic.getDayOfMonth());
        String islamichr = Integer.toString(dtIslamic.getHourOfDay());
        String islamichr2 = Integer.toString(dtIslamic.getHourOfDay() + 1);

        String islamicmin = Integer.toString(dtIslamic.getMinuteOfHour());

        subitem = new String[title.length];
        subsubitem = new String[2];

        fullgregoriandate = k_gregday + " " + k_gregmonth + " " + k_gregyr;

        calendar1.set(Calendar.YEAR, Integer.parseInt(k_gregyr));
        calendar1.set(Calendar.MONTH, k_gregmonth2 - 1);
        calendar1.set(Calendar.DAY_OF_MONTH, Integer.parseInt(k_gregday));
        calendar1.set(Calendar.HOUR_OF_DAY, calendar1.get(Calendar.HOUR_OF_DAY));
        calendar1.set(Calendar.MINUTE, 0);
        calendar1.set(Calendar.SECOND, 0);


        calendar2.set(Calendar.YEAR, Integer.parseInt(k_gregyr));
        calendar2.set(Calendar.MONTH, k_gregmonth2 - 1);
        calendar2.set(Calendar.DAY_OF_MONTH, Integer.parseInt(k_gregday));
        calendar2.set(Calendar.HOUR_OF_DAY, calendar2.get(Calendar.HOUR_OF_DAY) + 1);
        calendar2.set(Calendar.MINUTE, 0);
        calendar2.set(Calendar.SECOND, 0);


        if (checknumofdigits(dtIslamic.getMinuteOfHour()) == true) {
            islamicmin = "0" + islamicmin;
        }

        subitem[0] = calendar1.getTime().toString();
        subitem[1] = calendar2.getTime().toString();
        subitem[2] = "Choose";


        if (Integer.parseInt(islamichr) == 24) {
            islamichr = "0";
        }
        if (Integer.parseInt(islamichr2) == 24) {
            islamichr2 = "0";
        }

        subsubitem[0] = getDayOfMonthSuffix(Integer.parseInt(islamicday)) + " " + islamic_month_name1 + " " + islamicyr + " " + islamichr + ":" + islamicmin;
        subsubitem[1] = getDayOfMonthSuffix(Integer.parseInt(islamicday)) + " " + islamic_month_name1 + " " + islamicyr + " " + islamichr2 + ":" + islamicmin;

        lv = (ListView) findViewById(R.id.mlistview2);
        adapter = new CustomAdapter2(reminder.this, title, subitem, subsubitem);

        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Calendar c = Calendar.getInstance();
                int calday = c.get(Calendar.DAY_OF_MONTH);
                int calmonth = c.get(Calendar.MONTH);
                int calyr = c.get(Calendar.YEAR);

                switch (position) {
                    case 0:
                        counter = position;
                        DatePickerDialog datePickerDialog = new DatePickerDialog(reminder.this, reminder.this, calyr, calmonth, calday);
                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                        datePickerDialog.show();
                        break;

                    case 1:
                        counter = position;
                        DatePickerDialog datePickerDialog2 = new DatePickerDialog(reminder.this, reminder.this, calyr, calmonth, calday);
                        datePickerDialog2.getDatePicker().setMinDate(calendar1.getTimeInMillis());
                        datePickerDialog2.show();
                        break;
                    case 2:
                        final String[] items = {"1 min before", "5 mins before", "10 mins before", "15 mins before","30 mins before","1 hour before","1 day before"};
                        new AlertDialog.Builder(reminder.this)
                                .setSingleChoiceItems(items, 0, null)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {

                                        int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                                        itemoption = items[selectedPosition];
                                        subitem[2] = items[selectedPosition];
                                        adapter = new CustomAdapter2(reminder.this, title, subitem, subsubitem);
                                        lv.setAdapter(adapter);

                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                })
                                .show();
                        break;
                    case 3:
                        break;
                }
            }
        });

    }

    public int mincalculator(String item) {
        boolean hasDigit = item.matches(".*\\d+.*");
        int min = 0;

        if (hasDigit == true) {
            if(item.contains("min")) {

                min = Integer.parseInt(item.replaceAll("[^0-9]", ""));
                return min;
            }
            if(item.contains("hour")) {

                min = Integer.parseInt(item.replaceAll("[^0-9]", ""));
                min = min * 60;
                return min;
            }
            if(item.contains("day")) {
                min = Integer.parseInt(item.replaceAll("[^0-9]", ""));
                min = min * 1440;
                return min;

            }

        }

return min;
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

    private void setAlarm() {

        int min = mincalculator(itemoption);

        addEvent(reminder.this, editText.getText().toString(),editText2.getText().toString(), calendar1, calendar2,min);


    }

    private void addEvent(Context ctx, String title, String description, Calendar start, Calendar end,int minutes_notification) {

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
            if (checkSelfPermission(Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {

                finish();
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
        getContentResolver().insert(CalendarContract.Reminders.CONTENT_URI, reminder);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

        tmp1 = i;
        tmp2 = i1;
        tmp3 = i2;

        Calendar c = Calendar.getInstance();
        int hr = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(reminder.this,reminder.this,hr,min,DateFormat.is24HourFormat(reminder.this));
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {

        if(counter == 0) {
            tmp4 = i;
            tmp5 = i1;


            Date startDate = calendar1.getTime();
            Date endDate = calendar2.getTime();
            long startTime = startDate.getTime();
            long endTime = endDate.getTime();
            long diffTime = endTime - startTime;
            long diffDays = diffTime / (1000 * 60 * 60 * 24);
            int g = (int) (tmp3 + diffDays);
            int j =  tmp4;

            if(calendar2.get(Calendar.YEAR) == calendar1.get(Calendar.YEAR) && calendar2.get(Calendar.MONTH) == calendar1.get(Calendar.MONTH) && calendar2.get(Calendar.DAY_OF_MONTH) == calendar1.get(Calendar.DAY_OF_MONTH)) {
                j = hoursDifference(endDate,startDate) + tmp4;

            }


            System.out.println("the difference of days is: " + g);

            System.out.println("the hour difference is: " + j +" hours" +"\n" + "calendar1 time: " + calendar1.getTime().toString() + " calendar2 time: " + calendar2.getTime().toString());
            calendar2.set(Calendar.YEAR, tmp1);
            calendar2.set(Calendar.MONTH, tmp2);
            calendar2.set(Calendar.DAY_OF_MONTH, g);
            calendar2.set(Calendar.HOUR_OF_DAY, j);
            calendar2.set(Calendar.MINUTE, tmp5);
            calendar2.set(Calendar.SECOND, 0);


            calendar1.set(Calendar.YEAR, tmp1);
            calendar1.set(Calendar.MONTH, tmp2);
            calendar1.set(Calendar.DAY_OF_MONTH, tmp3);
            calendar1.set(Calendar.HOUR_OF_DAY, tmp4);
            calendar1.set(Calendar.MINUTE, tmp5);
            calendar1.set(Calendar.SECOND, 0);



            DateTime dtISO = new DateTime(tmp1, tmp2 + 1, tmp3, tmp4, tmp5); //NOTE: MUST GET DAY
            DateTime dtISO2 = new DateTime(calendar2.get(Calendar.YEAR), calendar2.get(Calendar.MONTH)+1, calendar2.get(Calendar.DAY_OF_MONTH), calendar2.get(Calendar.HOUR_OF_DAY), calendar2.get(Calendar.MINUTE)); //NOTE: MUST GET DAY





            DateTime dtIslamic = dtISO.withChronology(IslamicChronology.getInstance());
            DateTime dtIslamic2 = dtISO2.withChronology(IslamicChronology.getInstance());

            String islamicyr = Integer.toString(dtIslamic.getYear());
            String islamicmonth = Integer.toString(dtIslamic.getMonthOfYear());
            String islamic_month_name1 = all_hijri_months.get(Integer.parseInt(islamicmonth));
            String islamicday = Integer.toString(dtIslamic.getDayOfMonth());
            String islamichr = Integer.toString(dtIslamic.getHourOfDay());
            String islamicmin = Integer.toString(dtIslamic.getMinuteOfHour());

            if(checknumofdigits(dtIslamic.getMinuteOfHour()) == true) {

                islamicmin = "0" + islamicmin;
            }


            String islamicyr2 = Integer.toString(dtIslamic2.getYear());
            String islamicmonth2 = Integer.toString(dtIslamic2.getMonthOfYear());
            String islamic_month_name12 = all_hijri_months.get(Integer.parseInt(islamicmonth2));
            String islamicday2 = Integer.toString(dtIslamic2.getDayOfMonth());
            String islamichr2 = Integer.toString(dtIslamic2.getHourOfDay());
            String islamicmin2 = Integer.toString(dtIslamic2.getMinuteOfHour());

            if(checknumofdigits(dtIslamic2.getMinuteOfHour()) == true) {

                islamicmin2 = "0" + islamicmin2;
            }

            subitem[0] = calendar1.getTime().toString();
            subitem[1] = calendar2.getTime().toString();

            subsubitem[0] = getDayOfMonthSuffix(Integer.parseInt(islamicday)) + " " + islamic_month_name1 + " " + islamicyr + " " + islamichr + ":" + islamicmin;
            subsubitem[1] = getDayOfMonthSuffix(Integer.parseInt(islamicday2)) + " " + islamic_month_name12 + " " + islamicyr2 + " " + islamichr2 + ":" + islamicmin2;


            adapter = new CustomAdapter2(reminder.this, title, subitem, subsubitem);
            lv.setAdapter(adapter);
        }

        if(counter == 1) {
            tmp4 = i;
            tmp5 = i1;


            calendar2.set(Calendar.YEAR, tmp1);
            calendar2.set(Calendar.MONTH, tmp2);
            calendar2.set(Calendar.DAY_OF_MONTH, tmp3);
            calendar2.set(Calendar.HOUR_OF_DAY, tmp4);
            calendar2.set(Calendar.MINUTE, tmp5);
            calendar2.set(Calendar.SECOND, 0);

            DateTime dtISO = new DateTime(tmp1, tmp2 + 1, tmp3, tmp4, tmp5); //NOTE: MUST GET DAY
            DateTime dtIslamic = dtISO.withChronology(IslamicChronology.getInstance());
                if(calendar2.get(Calendar.YEAR) == calendar1.get(Calendar.YEAR) && calendar2.get(Calendar.MONTH) == calendar1.get(Calendar.MONTH) && calendar2.get(Calendar.DAY_OF_MONTH) == calendar1.get(Calendar.DAY_OF_MONTH) && calendar2.get(Calendar.HOUR_OF_DAY) < calendar1.get(Calendar.HOUR_OF_DAY)) {
                    calendar2.set(Calendar.HOUR_OF_DAY,calendar1.get(Calendar.HOUR_OF_DAY)+1);
                    int k = calendar1.get(Calendar.HOUR_OF_DAY) +1;
                    if(k >= 24) {
                        k = 0;
                    }
                    dtISO = new DateTime(tmp1, tmp2 + 1, tmp3, k, tmp5); //NOTE: MUST GET DAY
                    dtIslamic = dtISO.withChronology(IslamicChronology.getInstance());
                }

            if(calendar2.get(Calendar.YEAR) == calendar1.get(Calendar.YEAR) && calendar2.get(Calendar.MONTH) == calendar1.get(Calendar.MONTH) && calendar2.get(Calendar.DAY_OF_MONTH) == calendar1.get(Calendar.DAY_OF_MONTH) && calendar2.get(Calendar.HOUR_OF_DAY) == calendar1.get(Calendar.HOUR_OF_DAY) && calendar2.get(Calendar.MINUTE) < calendar1.get(Calendar.MINUTE)) {

                calendar2.set(Calendar.MINUTE,calendar1.get(Calendar.MINUTE)+1);

                dtISO = new DateTime(tmp1, tmp2 + 1, tmp3, tmp4, calendar2.get(Calendar.MINUTE)); //NOTE: MUST GET DAY
                dtIslamic = dtISO.withChronology(IslamicChronology.getInstance());

            }

                String islamicyr = Integer.toString(dtIslamic.getYear());
            String islamicmonth = Integer.toString(dtIslamic.getMonthOfYear());
            String islamic_month_name1 = all_hijri_months.get(Integer.parseInt(islamicmonth));
            String islamicday = Integer.toString(dtIslamic.getDayOfMonth());
            String islamichr = Integer.toString(dtIslamic.getHourOfDay());
            String islamicmin = Integer.toString(dtIslamic.getMinuteOfHour());

            if(checknumofdigits(dtIslamic.getMinuteOfHour()) == true) {

                islamicmin = "0" + islamicmin;
            }


            subitem[1] = calendar2.getTime().toString();
            subsubitem[1] = getDayOfMonthSuffix(Integer.parseInt(islamicday)) + " " + islamic_month_name1 + " " + islamicyr + " " + islamichr + ":" + islamicmin;

            adapter = new CustomAdapter2(reminder.this, title, subitem, subsubitem);
            lv.setAdapter(adapter);


        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    public void mbackbtn(View v) {
        startActivity(new Intent(reminder.this,Main2Activity.class));
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }


    private static int hoursDifference(Date date1, Date date2) {

        final int MILLI_TO_HOUR = 1000 * 60 * 60;
        return (int) (date1.getTime() - date2.getTime()) / MILLI_TO_HOUR;
    }


    private static boolean checknumofdigits(int s) {

        int length = String.valueOf(s).length();

        if(length == 1) {

            return true;

        }

        return false;


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

}
