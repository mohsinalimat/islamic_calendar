package com.aliindustries.islamiccalendar;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GestureDetectorCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.chrono.IslamicChronology;
import org.w3c.dom.Text;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import static androidx.core.util.Preconditions.checkArgument;


public class Main2Activity extends AppCompatActivity  {

    private AppBarConfiguration mAppBarConfiguration;
    GridView m_gridview;
    Spinner sp1;
    Spinner sp2;
    TextView greg_month;
    TextView islam_month;
    ArrayAdapter adapter;
    ArrayAdapter adapter2;
    RelativeLayout relativeLayout;
    DatePickerDialog datePickerDialog;
    NavigationView navigationView;
    TextView title_nav;
    TextView subtitle_nav;
    DrawerLayout drawer;
    private GestureDetectorCompat detector;
    com.getbase.floatingactionbutton.FloatingActionButton floatingActionButton;
    FloatingActionsMenu floatingActionsMenu;
    View bckgroundDimmer;

    ImageView imageView;
    ImageView imageView2;

    public static DisplayMetrics metrics;
    public static int width;
    public static int height;

    TextView grid_greg_textview1;
    TextView grid_hijri_textview2;
    DatabaseHelper myDb;


    public String reminder_gregday;
    public String reminder_gregmonth;
    public int reminder_gregmonth2;

    public String reminder_gregyr;

    public static HashMap<Integer,String> all_hijri_months;

    private final int REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE=1;
    private final int REQUEST_PERMISSION_READ_EXTERNAL_STORAGE=2;
    private final int REQUEST_PERMISSION_READ_CALENDAR=3;
    private final int REQUEST_PERMISSION_WRITE_CALENDAR=4;



    String[] d1 = {"M", "T", "W", "T", "F", "S", "S", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};
    String[] d2 = {"", "", "", "", "", "", "", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        writesettingPermission();
        ActivityCompat.requestPermissions(Main2Activity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.READ_CALENDAR,Manifest.permission.WRITE_CALENDAR}, 1);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(getResources().getColor(R.color.black));


        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2019);
        cal.set(Calendar.MONTH, 5);
        cal.set(Calendar.DAY_OF_MONTH, 20);
        cal.set(Calendar.HOUR, 10);
        cal.set(Calendar.MINUTE, 4320);
        cal.set(Calendar.SECOND, 0);

        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println("this is the time: " + cal.getTime());


        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels;
        height = metrics.heightPixels;

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        m_gridview = (GridView) findViewById(R.id.gridview);
        sp1 = (Spinner) findViewById(R.id.spinner);
        sp2 = (Spinner) findViewById(R.id.spinner2);
        greg_month = (TextView) findViewById(R.id.gregorian_month);
        islam_month = (TextView) findViewById(R.id.islamic_month);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativelayout1);
        imageView = (ImageView) findViewById(R.id.imgview5);
        imageView2 = (ImageView) findViewById(R.id.imgview6);
        floatingActionButton = findViewById(R.id.minifab1) ;
        floatingActionsMenu = findViewById(R.id.fab1);
        bckgroundDimmer = findViewById(R.id.background_dimmer);
        myDb = new DatabaseHelper(Main2Activity.this);

        final LayoutInflater factory = getLayoutInflater();

        final View textEntryView = factory.inflate(R.layout.single_item, null);

        grid_greg_textview1 = (TextView) textEntryView.findViewById(R.id.txtviw1);
        grid_hijri_textview2 = (TextView) textEntryView.findViewById(R.id.txtviw2);




        all_hijri_months = new HashMap<Integer, String>();

        all_hijri_months.put(1,"Muharram");
        all_hijri_months.put(2,"Safar");
        all_hijri_months.put(3,"Rabi al Awwal");
        all_hijri_months.put(4,"Rabi al Thani");
        all_hijri_months.put(5,"Jumada al Ula");
        all_hijri_months.put(6,"Jumada al Akhirah");
        all_hijri_months.put(7,"Rajab");
        all_hijri_months.put(8,"Shaban");
        all_hijri_months.put(9,"Ramadan");
        all_hijri_months.put(10,"Shawwal");
        all_hijri_months.put(11,"Zul Qiddah");
        all_hijri_months.put(12,"Zul Hijjah");
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.currentdate, R.id.upcoming, R.id.exitapp, R.id.nav_share)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        getSupportActionBar().setTitle("Calendar");
        navigationView.getMenu().getItem(0).setChecked(false);
        navigationView.bringToFront();


        Calendar timenow = Calendar.getInstance();

        DateTime dtISO1 = new DateTime(timenow.get(Calendar.YEAR), timenow.get(Calendar.MONTH) + 1, timenow.get(Calendar.DAY_OF_MONTH), 0, 0); //NOTE: MUST GET DAY
        DateTime dtIslamic1 = dtISO1.withChronology(IslamicChronology.getInstance());
        String islamicyr2 = Integer.toString(dtIslamic1.getYear());
        String islamicmonth2 = Integer.toString(dtIslamic1.getMonthOfYear());
        String islamicday2 = Integer.toString(dtIslamic1.getDayOfMonth());

        String islamic_month_name3 = all_hijri_months.get(Integer.parseInt(islamicmonth2));

        View header = navigationView.getHeaderView(0);
        title_nav = (TextView) header.findViewById(R.id.titlenav);
        subtitle_nav = (TextView) header.findViewById(R.id.subtitlenav);
        title_nav.setText(getDayOfMonthSuffix(Integer.parseInt(islamicday2)) + " " + islamic_month_name3 + " " + islamicyr2);
        subtitle_nav.setText(timenow.getTime().toString());

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                // Handle navigation view item clicks here.
                switch (menuItem.getItemId()) {

                    case R.id.currentdate: {
                        drawer.closeDrawer(Gravity.LEFT);

                        Calendar getcurrenttime = Calendar.getInstance();
                        int currentmonth = getcurrenttime.get(Calendar.MONTH);
                        int currentyr = getcurrenttime.get(Calendar.YEAR);
                        sp1.setSelection(getIndex(sp1, Integer.toString(currentmonth+1)));
                        sp2.setSelection(getIndex(sp2, Integer.toString(currentyr)));
                        m_gridview.startAnimation(AnimationUtils.loadAnimation(Main2Activity.this, R.anim.slide_in_right));
                        islam_month.startAnimation(AnimationUtils.loadAnimation(Main2Activity.this, R.anim.slide_in_right));
                        imageView.startAnimation(AnimationUtils.loadAnimation(Main2Activity.this, R.anim.slide_in_right));
                        imageView2.startAnimation(AnimationUtils.loadAnimation(Main2Activity.this, R.anim.slide_in_right));

                        break;
                    }
                    case R.id.upcoming: {
                        startActivity(new Intent(Main2Activity.this,events_yr.class));
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        break;
                    }
                    case R.id.exitapp: {
                        finish();
                        System.exit(0);
                        break;
                    }
                    case R.id.nav_share: {
                        try {
                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("text/plain");
                            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Hijri Calendar");
                            String shareMessage= "\nLet me recommend you this application\n\n";
                            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                            startActivity(Intent.createChooser(shareIntent, "choose one"));
                        } catch(Exception e) {
                            //e.toString();
                        }
                        break;
                    }
                }
                return true;
            }
        });

        android.graphics.Typeface custom_font2 = android.graphics.Typeface.createFromAsset(getAssets(),  "fonts/gothic.ttf");
        greg_month.setTypeface(custom_font2);
        islam_month.setTypeface(custom_font2);

        adapter = ArrayAdapter.createFromResource(Main2Activity.this,R.array.months,R.layout.color_spinner_layput);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);

        List<String> mlist = Arrays.asList(getResources().getStringArray(R.array.months));
        List<String> mlist2 = Arrays.asList(getResources().getStringArray(R.array.year));


        sp1.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, mlist) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView1 = (TextView) super.getView(position, convertView, parent);
                textView1.setTextColor(Color.WHITE);
                textView1.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        getResources().getDimension(R.dimen.navtextsize1));
                return textView1; }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                TextView textView1 = (TextView) super.getDropDownView(position, convertView, parent); textView1.setTextColor(Color.BLACK);
                textView1.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        getResources().getDimension(R.dimen.navtextsize1));
                return textView1; }
        });


        sp1.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        adapter2 = ArrayAdapter.createFromResource(Main2Activity.this,R.array.year,R.layout.color_spinner_layput);
        adapter2.setDropDownViewResource(R.layout.spinner_dropdown_layout);

        sp2.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, mlist2) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView1 = (TextView) super.getView(position, convertView, parent);
                textView1.setTextColor(Color.WHITE);
                textView1.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        getResources().getDimension(R.dimen.navtextsize1));
                return textView1; }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                TextView textView1 = (TextView) super.getDropDownView(position, convertView, parent); textView1.setTextColor(Color.BLACK);
                textView1.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        getResources().getDimension(R.dimen.navtextsize1));
                return textView1; }
        });

        sp2.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);



        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        final int month = now.get(Calendar.MONTH) +1;
        int day = now.get(Calendar.DAY_OF_MONTH);

        String val2 = Integer.toString(month);
        String val3 = Integer.toString(year);

        for (int i = 0; i < sp1.getCount(); i++) {
            if (sp1.getItemAtPosition(i).equals(val2)) {
                sp1.setSelection(i);
                break;
            }
        }
        for (int i = 0; i < sp2.getCount(); i++) {
            if (sp2.getItemAtPosition(i).equals(val3)) {
                sp2.setSelection(i);
                break;
            }
        }

        islam_month.setOnTouchListener(new OnSwipeTouchListener(Main2Activity.this) {

            public void onSwipeRight() {


                int i = sp1.getSelectedItemPosition();
                int i2 = sp2.getSelectedItemPosition();

                if(i != 0) {
                    i--;
                }

                else if(i == 0 && i2 > 0) {
                    i = 11;
                    i2--;
                    sp2.setSelection(i2);
                }

                sp1.setSelection(i);
                m_gridview.startAnimation(AnimationUtils.loadAnimation(Main2Activity.this, R.anim.slide_in_left));
                islam_month.startAnimation(AnimationUtils.loadAnimation(Main2Activity.this, R.anim.slide_in_left));
                imageView.startAnimation(AnimationUtils.loadAnimation(Main2Activity.this, R.anim.slide_in_left));
                imageView2.startAnimation(AnimationUtils.loadAnimation(Main2Activity.this, R.anim.slide_in_left));



            }
            public void onSwipeLeft() {

                int i = sp1.getSelectedItemPosition();
                int i2 = sp2.getSelectedItemPosition();

                int sp1_count = sp1.getAdapter().getCount()-1 ;
                int sp2_count = sp2.getAdapter().getCount()-1;

                if(i != sp1_count) {
                    i++;
                }

                else if(i == sp1_count && i2 < sp2_count) {
                    i = 0;
                    i2++;
                    sp2.setSelection(i2);
                }


                sp1.setSelection(i);
                m_gridview.startAnimation(AnimationUtils.loadAnimation(Main2Activity.this, R.anim.slide_in_right));
                islam_month.startAnimation(AnimationUtils.loadAnimation(Main2Activity.this, R.anim.slide_in_right));
                imageView.startAnimation(AnimationUtils.loadAnimation(Main2Activity.this, R.anim.slide_in_right));
                imageView2.startAnimation(AnimationUtils.loadAnimation(Main2Activity.this, R.anim.slide_in_right));
            }
        });


        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                String txt1 = sp1.getSelectedItem().toString();
                String txt2 = sp2.getSelectedItem().toString();
                int month = Integer.parseInt(txt1);
                int yr = Integer.parseInt(txt2);

                String monthname = new DateFormatSymbols().getMonths()[month-1];
                String string_yr = Integer.toString(yr);


                greg_month.setText(monthname + " " + string_yr);

                DateTime dtISO = new DateTime(yr, month, 1, 0,0);

                DateTime dtIslamic = dtISO.withChronology(IslamicChronology.getInstance());
                String getmonthyr1 = Integer.toString(dtIslamic.getMonthOfYear());
                String getyr1 = Integer.toString(dtIslamic.getYear());


                GregorianCalendar gc = new GregorianCalendar(yr, month - 1, 1);
                int final_day_of_month = gc.getActualMaximum(Calendar.DATE);
                DateTime dtISO2 = new DateTime(yr, month, final_day_of_month, 0,0);
                DateTime dtIslamic2 = dtISO2.withChronology(IslamicChronology.getInstance());
                String getyr2 = Integer.toString(dtIslamic2.getYear());


                String islamic_month_name1 = all_hijri_months.get(Integer.parseInt(getmonthyr1));

                String[] hijriday_arr = hijrah(month,yr);
                List<String> list = new ArrayList<String>(Arrays.asList(hijriday_arr));
                list.removeAll(Arrays.asList(""));
                int mcount = 0;

                for(int i = 1; i < list.size();i++) {
                    if(list.get(i).equals("1")) {
                        mcount++;
                    }
                }
                int tmp = dtIslamic.getMonthOfYear()+1;
                if(tmp > 12) {
                    tmp =1;
                }
                String getmonthyr2 = Integer.toString(tmp);
                String islamic_month_name2 = all_hijri_months.get(Integer.parseInt(getmonthyr2));
                if(mcount > 0) {
                    islam_month.setText(islamic_month_name1 + " " + getyr1 + " - " + islamic_month_name2 + " " + getyr2);
                }
                else  {
                    islam_month.setText(islamic_month_name1 + " " + getyr2);

                }


                GridAdapter gridAdapter = new GridAdapter(Main2Activity.this, gregorian_date(month, yr), hijrah(month,yr),Main2Activity.this);
                m_gridview.setAdapter(gridAdapter);

            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                String txt1 = sp1.getSelectedItem().toString();
                String txt2 = sp2.getSelectedItem().toString();
                int month = Integer.parseInt(txt1);
                int yr = Integer.parseInt(txt2);

                String monthname = new DateFormatSymbols().getMonths()[month-1];
                String string_yr = Integer.toString(yr);

                greg_month.setText(monthname + " " + string_yr);

                DateTime dtISO = new DateTime(yr, month, 1, 0,0);

                DateTime dtIslamic = dtISO.withChronology(IslamicChronology.getInstance());
                String getmonthyr1 = Integer.toString(dtIslamic.getMonthOfYear());
                String getyr1 = Integer.toString(dtIslamic.getYear());


                GregorianCalendar gc = new GregorianCalendar(yr, month - 1, 1);
                int final_day_of_month = gc.getActualMaximum(Calendar.DATE);
                DateTime dtISO2 = new DateTime(yr, month, final_day_of_month, 0,0);
                DateTime dtIslamic2 = dtISO2.withChronology(IslamicChronology.getInstance());
                String getyr2 = Integer.toString(dtIslamic2.getYear());


                String islamic_month_name1 = all_hijri_months.get(Integer.parseInt(getmonthyr1));

                String[] hijriday_arr = hijrah(month,yr);
                List<String> list = new ArrayList<String>(Arrays.asList(hijriday_arr));
                list.removeAll(Arrays.asList(""));
                int mcount = 0;

                for(int i = 1; i < list.size();i++) {
                    if(list.get(i).equals("1")) {
                        mcount++;
                    }
                }
                int tmp = dtIslamic.getMonthOfYear()+1;
                if(tmp > 12) {
                    tmp =1;
                }
                String getmonthyr2 = Integer.toString(tmp);
                String islamic_month_name2 = all_hijri_months.get(Integer.parseInt(getmonthyr2));
                if(mcount > 0) {
                    islam_month.setText(islamic_month_name1 + " " + getyr1 + " - " + islamic_month_name2 + " " + getyr2);
                }
                else  {
                    islam_month.setText(islamic_month_name1 + " " + getyr2);

                }



                GridAdapter gridAdapter = new GridAdapter(Main2Activity.this, gregorian_date(month, yr), hijrah(month,yr),Main2Activity.this);
                m_gridview.setAdapter(gridAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        setRepeatedNotification(0,1,0,0);

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                greg_month.setText(getMonthForInt(monthOfYear) + " " + year);

                sp1.setSelection(getIndex(sp1, Integer.toString(monthOfYear+1)));
                sp2.setSelection(getIndex(sp2, Integer.toString(year)));

            }
        };

        Calendar newDate = Calendar.getInstance();
        int year2 = newDate.get(Calendar.YEAR);
        int month2 = newDate.get(Calendar.MONTH);
        int day2 = newDate.get(Calendar.DAY_OF_MONTH);


        datePickerDialog =   new DatePickerDialog(Main2Activity.this, date, year2, month2, day2);
        greg_month.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });


        floatingActionsMenu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                bckgroundDimmer.setVisibility(View.VISIBLE);
            }

            @Override
            public void onMenuCollapsed() {
                bckgroundDimmer.setVisibility(View.GONE);
            }
        });


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Calendar checkedday = Calendar.getInstance();
                    checkedday.set(Calendar.DAY_OF_MONTH, Integer.parseInt(reminder_gregday));
                    checkedday.set(Calendar.MONTH, reminder_gregmonth2 - 1);
                    checkedday.set(Calendar.YEAR, Integer.parseInt(reminder_gregyr));

                    Calendar today = Calendar.getInstance();

                    if (checkedday.before(today)) {

                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "To set a reminder, choose a current or future date", Snackbar.LENGTH_LONG);
                        snackbar.show();

                    } else {
                        Intent i = new Intent(Main2Activity.this, reminder.class);
                        i.putExtra("u_gregday", reminder_gregday);
                        i.putExtra("u_gregmonth", reminder_gregmonth);
                        i.putExtra("u_gregyr", reminder_gregyr);
                        i.putExtra("u_gregmonth2", reminder_gregmonth2);

                        startActivity(i);

                    }
                }
                catch (Exception e ) {

                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), e.toString(), Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });
        reminder_gregday = Integer.toString(day);
        reminder_gregmonth = getMonthForInt(month-1);
        reminder_gregmonth2 = month;
        reminder_gregyr = Integer.toString(year);

m_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        try {
            for (int i = 0; i < parent.getCount(); i++) {
                View view1 = parent.getChildAt(i);
                TextView v = view1.findViewById(R.id.txtviw1);
                if (v.getCurrentTextColor() == getResources().getColor(R.color.white)) {

                    v.setBackgroundResource(R.drawable.whitebackground);
                    v.setTextColor(getResources().getColorStateList(R.color.black));
                    break;
                }
            }
        }
        catch (Exception e) {

            Toast.makeText(Main2Activity.this,e.toString(),Toast.LENGTH_SHORT).show();

        }

        TextView temp_textView1 = (TextView) view.findViewById(R.id.txtviw1);

        if(isNumeric(temp_textView1.getText().toString()) == true) {
            temp_textView1.setBackgroundResource(R.drawable.textviewcircle);
            temp_textView1.setTextColor(getResources().getColorStateList(R.color.white));
        }



        reminder_gregday = temp_textView1.getText().toString();
        reminder_gregmonth = getMonthForInt(Integer.parseInt(sp1.getSelectedItem().toString())-1);
        reminder_gregmonth2 = Integer.parseInt(sp1.getSelectedItem().toString());
        reminder_gregyr = sp2.getSelectedItem().toString();

    }
});

Calendar calendar1 = Calendar.getInstance();
Calendar calendar2 = Calendar.getInstance();

        calendar1.set(Calendar.YEAR, 2019);
        calendar1.set(Calendar.MONTH, 11);
        calendar1.set(Calendar.DAY_OF_MONTH, 20);
        calendar1.set(Calendar.HOUR_OF_DAY, 10);
        calendar1.set(Calendar.MINUTE, 2);
        calendar1.set(Calendar.SECOND, 0);


        calendar2.set(Calendar.YEAR, 2019);
        calendar2.set(Calendar.MONTH, 11);
        calendar2.set(Calendar.DAY_OF_MONTH, 20+20);
        calendar2.set(Calendar.HOUR_OF_DAY, 10);
        calendar2.set(Calendar.MINUTE, 2);
        calendar2.set(Calendar.SECOND, 0);

    }
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    private String getMonthForInt(int m) {
        String month = "invalid";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (m >= 0 && m <= 11 ) {
            month = months[m];
        }
        return month;
    }





    private void setRepeatedNotification(int ID, int hh, int mm, int ss) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent alarmIntent = new Intent(Main2Activity.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(Main2Activity.this, ID, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmIntent.setData((Uri.parse("custom://"+System.currentTimeMillis())));
        alarmManager.cancel(pendingIntent);

        Calendar calendar = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hh);
        calendar.set(Calendar.MINUTE, mm);
        calendar.set(Calendar.SECOND, ss);

        //check whether the time is earlier than current time. If so, set it to tomorrow. Otherwise, all alarms for earlier time will fire

        if(calendar.before(now)){
            calendar.add(Calendar.DATE, 1);
        }

        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }


    }
    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return -1;
    }


    public String[] gregorian_date(int month, int year) {

        GregorianCalendar gc = new GregorianCalendar(year, month - 1, 1);
        java.util.Date monthEndDate = new java.util.Date(gc.getTime().getTime());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(monthEndDate);

        int g = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (g <= 0) {
            g = 7;
        }

        String[] tmp = d1;
        String[] mynewarray = new String[d1.length + g];
        mynewarray[0] = "M";mynewarray[1] = "T";mynewarray[2] = "W";mynewarray[3] = "T";mynewarray[4] = "F";mynewarray[5] = "S";mynewarray[6] = "S";

        for (int i = 7; i < 7 + g - 1; i++) {
            mynewarray[i] = "";
        }

        int res = gc.getActualMaximum(Calendar.DATE);

        int counter = 7;
        for (int i = 7 + g - 1; i < mynewarray.length; i++) {
            mynewarray[i] = tmp[counter];
            counter++;

            int h = Integer.parseInt(mynewarray[i]);
            if (h == res) {
                break;
            }

        }

        List<String> values = new ArrayList<String>();
        for(String data: mynewarray) {
            if(data != null) {
                values.add(data);
            }
        }
        String[] target = values.toArray(new String[values.size()]);

        System.out.println("target for gregorian is: " + Arrays.toString(target));
        System.out.println("target size for gregorian is: " + target.length);


        return target;


    }

    public String[] hijrah(int month, int year) {
        GregorianCalendar gc = new GregorianCalendar(year, month - 1, 1);
        java.util.Date monthEndDate = new java.util.Date(gc.getTime().getTime());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(monthEndDate);

        int g = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (g <= 0) {
            g = 7;
        }

        String[] tmp = d2;
        String[] mynewarray = new String[d2.length + g];
        mynewarray[0] = "";mynewarray[1] = "";mynewarray[2] = "";mynewarray[3] = "";mynewarray[4] = "";mynewarray[5] = "";mynewarray[6] = "";

        for (int i = 7; i < 7 + g - 1; i++) {
            mynewarray[i] = "";
        }

        int res = gc.getActualMaximum(Calendar.DATE);

        int counter = 1;
        for (int i = 7 + g - 1; i < mynewarray.length; i++) {

            DateTime dtISO = new DateTime(year, month, counter, 0,0);

            DateTime dtIslamic = dtISO.withChronology(IslamicChronology.getInstance());
            String f = Integer.toString(dtIslamic.getDayOfMonth());

            mynewarray[i] = f;
            counter++;

            if (counter == res + 1) {
                break;
            }

        }

        List<String> values = new ArrayList<String>();
        for(String data: mynewarray) {
            if(data != null) {
                values.add(data);
            }
        }
        String[] target = values.toArray(new String[values.size()]);



        System.out.println("target for hijrah is: " + Arrays.toString(target));

        System.out.println("target size for hijrah is: " + target.length);


        return target;


    }
    public void backbtn2(View view) {
        int i = sp1.getSelectedItemPosition();
        int i2 = sp2.getSelectedItemPosition();

        if(i != 0) {
            i--;
        }

        else if(i == 0 && i2 > 0) {
            i = 11;
            i2--;
            sp2.setSelection(i2);
        }

        sp1.setSelection(i);
        m_gridview.startAnimation(AnimationUtils.loadAnimation(Main2Activity.this, R.anim.slide_in_left));
        islam_month.startAnimation(AnimationUtils.loadAnimation(Main2Activity.this, R.anim.slide_in_left));
        imageView.startAnimation(AnimationUtils.loadAnimation(Main2Activity.this, R.anim.slide_in_left));
        imageView2.startAnimation(AnimationUtils.loadAnimation(Main2Activity.this, R.anim.slide_in_left));
    }
    public void forwardbtn(View view) {
        int i = sp1.getSelectedItemPosition();
        int i2 = sp2.getSelectedItemPosition();

        int sp1_count = sp1.getAdapter().getCount()-1 ;
        int sp2_count = sp2.getAdapter().getCount()-1;

        if(i != sp1_count) {
            i++;
        }

        else if(i == sp1_count && i2 < sp2_count) {
            i = 0;
            i2++;
            sp2.setSelection(i2);
        }


        sp1.setSelection(i);
        m_gridview.startAnimation(AnimationUtils.loadAnimation(Main2Activity.this, R.anim.slide_in_right));
        islam_month.startAnimation(AnimationUtils.loadAnimation(Main2Activity.this, R.anim.slide_in_right));
        imageView.startAnimation(AnimationUtils.loadAnimation(Main2Activity.this, R.anim.slide_in_right));
        imageView2.startAnimation(AnimationUtils.loadAnimation(Main2Activity.this, R.anim.slide_in_right));
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);

    }
    public void writesettingPermission() {
        boolean settingsCanWrite = false;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            settingsCanWrite = Settings.System.canWrite(getApplicationContext());
        }
        if(!settingsCanWrite) {

           finish();
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 200);
            }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED &&  grantResults[2] == PackageManager.PERMISSION_GRANTED &&  grantResults[3] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    finish();
                    System.exit(0);
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
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
    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }
}
