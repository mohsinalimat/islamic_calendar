package com.aliindustries.islamiccalendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class events_yr extends AppCompatActivity {
    ListView resultsListView;
    BottomNavigationView btmnav_view;
    TextView title;
    String[] itemtitle = {"Shab e Meraj", "Shab e Barat", "Beginning of Ramadan", "Battle of Badr Day", "Victory at Makkah","Laylat al Qadr","Eid al Fitr","Battle of Uhud Day","First day of Hajj","Second day of Hajj","Third day of Hajj | Eid-ul-Adha","Fourth day of Hajj","Fifth day of Hajj","Sixth day of Hajj","Ashura Day 1","Ashura Day 2","Eid Milad-un-Nabi" };
    String[] itemsubtitle = {"27th Rajab 1441 | 22nd March 2020", "15th Sha'ban 1441 | 9th April 2020", "1st Ramadan 1441 | 24th April 2020", "17th Ramadan 1441 | 10th May 2020","20th Ramadan 1441 | 13th May 2020","27th Ramadan 1441 | 20th May 2020","1st Shawwal 1441 | 24th May 2020","6th Shawwal 1441 | 29th May 2020","8th Zul-hijjah 1441 | 29th July 2020","9th Zul-hijjah 1441 | 30th July 2020","10th Zul-hijjah 1441 | 31st July 2020","11th Zul-hijjah 1441 | 1st August 2020","12th Zul-hijjah 1441 | 2nd August 2020","13th Zul-hijjah 1441 | 3rd August 2020","9th Muharram 1442 | 28th August 2020","10th Muharram 1442 | 29th August 2020","12th Rabi al-Awwal 1442 | 29th October 2020"};
    CustomAdapter adapter;

    int[] drawableIds = {R.drawable.one, R.drawable.two, R.drawable.three, R.drawable.four,R.drawable.five,R.drawable.six,R.drawable.seven,R.drawable.eight,R.drawable.nine,R.drawable.ten,R.drawable.eleven,R.drawable.twelve,R.drawable.thirteen,R.drawable.fourteen,R.drawable.fifteen,R.drawable.sixteen,R.drawable.seventeen};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_yr);
        resultsListView = (ListView) findViewById(R.id.listview2);
        title = (TextView) findViewById(R.id.maintitle2);
        btmnav_view = (BottomNavigationView) findViewById(R.id.btmnavigationview2);


        btmnav_view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {

                    case R.id.share:
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://drive.google.com/file/d/1dGqGp9v3QZjXVWnC7dWxbXCJONXmAc8x/view"));
                        startActivity(browserIntent);
                        break;
                }
                return true;
            }
        });


        adapter = new CustomAdapter(this,  itemtitle,itemsubtitle, drawableIds);
        resultsListView = (ListView)findViewById(R.id.listview2);
        resultsListView.setAdapter(adapter);

    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);

    }

    public void backbtn(View view) {

        startActivity(new Intent(events_yr.this,Main2Activity.class));
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

    }

}
