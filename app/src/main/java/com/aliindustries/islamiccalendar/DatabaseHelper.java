package com.aliindustries.islamiccalendar;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;



public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "calendar.db";
    public static final String TABLE_NAME = "reminder_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "day";
    public static final String COL_3 = "month";
    public static final String COL_4 = "year";
    public static final String COL_5 = "event";
    public static final String COL_6 = "hour";
    public static final String COL_7 = "minute";

    public static final String COL_8 = "endday";
    public static final String COL_9 = "endmonth";
    public static final String COL_10 = "endyear";
    public static final String COL_11 = "endhour";
    public static final String COL_12 = "endminute";
    public static final String COL_13 = "eventid";
    public static final String COL_14 = "description";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,DAY TEXT,MONTH TEXT,YEAR TEXT,EVENT TEXT,HOUR TEXT,MINUTE TEXT,ENDDAY TEXT,ENDMONTH TEXT,ENDYEAR TEXT,ENDHOUR TEXT,ENDMINUTE TEXT,EVENTID TEXT, DESCRIPTION TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String d,String m,String y,String event,String hr,String min,String end_d,String end_m,String end_y, String end_hr, String end_min,String event_id,String desc) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,d);
        contentValues.put(COL_3,m);
        contentValues.put(COL_4,y);
        contentValues.put(COL_5,event);
        contentValues.put(COL_6,hr);
        contentValues.put(COL_7,min);

        contentValues.put(COL_8,end_d);
        contentValues.put(COL_9,end_m);
        contentValues.put(COL_10,end_y);
        contentValues.put(COL_11,end_hr);
        contentValues.put(COL_12,end_min);
        contentValues.put(COL_13,event_id);
        contentValues.put(COL_14,desc);


        long result = db.insert(TABLE_NAME,null ,contentValues);

        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean quickinsertdata(List<String> list) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            for (int i = 0; i < list.size();i++) {
                if (i*13 >= list.size()) {
                    break;
                }
                else {
                    contentValues.put(COL_2, list.get(i * 13));
                    contentValues.put(COL_3, list.get((i * 13) + 1));
                    contentValues.put(COL_4, list.get((i * 13) + 2));
                    contentValues.put(COL_5, list.get((i * 13) + 3));
                    contentValues.put(COL_6, list.get((i * 13) + 4));
                    contentValues.put(COL_7, list.get((i * 13) + 5));

                    contentValues.put(COL_8,list.get((i * 13) + 6));
                    contentValues.put(COL_9,list.get((i * 13) + 7));
                    contentValues.put(COL_10,list.get((i * 13) + 8));
                    contentValues.put(COL_11,list.get((i * 13) + 9));
                    contentValues.put(COL_12,list.get((i * 13) + 10));
                    contentValues.put(COL_13,list.get((i * 13) + 11));
                    contentValues.put(COL_14,list.get((i * 13) + 12));

                }

                db.insert(TABLE_NAME, null, contentValues);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        return true;
    }



    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }

    public Cursor searchReminder(String d, String m, String y) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select EVENT from "+TABLE_NAME + " where DAY = '" + d +"' AND MONTH = '" + m +"' AND YEAR = '" + y +"'",null);
        return res;
    }


    public Cursor searchStartDate(String d, String m, String y) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select DAY,MONTH,YEAR,HOUR,MINUTE from "+TABLE_NAME + " where DAY = '" + d +"' AND MONTH = '" + m +"' AND YEAR = '" + y+"'",null);
        return res;
    }


    public Cursor getID_EventID(String event_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select ID from "+TABLE_NAME + " where EVENTID = '" + event_id +"'",null);
        return res;
    }


    public Cursor searchEndDate(String d, String m, String y) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select ENDDAY,ENDMONTH,ENDYEAR,ENDHOUR,ENDMINUTE from "+TABLE_NAME + " where DAY = '" + d +"' AND MONTH = '" + m +"' AND YEAR = '" + y+"'",null);
        return res;
    }

    public Cursor searchEventID(String d,String m,String y,String event,String hr,String min,String end_d,String end_m,String end_y, String end_hr, String end_min) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select EVENTID from "+TABLE_NAME + " where DAY = '" + d +"' AND MONTH = '" + m +"' AND YEAR = '" + y+"' AND EVENT = '" + event +"' AND HOUR = '" + hr+"' AND MINUTE = '" + min+"' AND ENDDAY = '" +end_d+"' AND ENDMONTH = '" +end_m+"' AND ENDYEAR = '" + end_y+"' AND ENDHOUR = '" + end_hr+"' AND ENDMINUTE = '" + end_min+"'",null);
        return res;
    }

    public  boolean CheckIsDataAlreadyInDBorNot(String d,String m,String y,String event,String hr,String min,String end_d,String end_m,String end_y, String end_hr, String end_min) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select EVENTID from "+TABLE_NAME + " where DAY = '" + d +"' AND MONTH = '" + m +"' AND YEAR = '" + y+"' AND EVENT = '" + event +"' AND HOUR = '" + hr+"' AND MINUTE = '" + min+"' AND ENDDAY = '" +end_d+"' AND ENDMONTH = '" +end_m+"' AND ENDYEAR = '" + end_y+"' AND ENDHOUR = '" + end_hr+"' AND ENDMINUTE = '" + end_min+"'",null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }





    public Cursor searchID(String d,String m,String y,String event,String hr,String min,String end_d,String end_m,String end_y, String end_hr, String end_min) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select ID from "+TABLE_NAME + " where DAY = '" + d +"' AND MONTH = '" + m +"' AND YEAR = '" + y+"' AND EVENT = '" + event +"' AND HOUR = '" + hr+"' AND MINUTE = '" + min+"' AND ENDDAY = '" +end_d+"' AND ENDMONTH = '" +end_m+"' AND ENDYEAR = '" + end_y+"' AND ENDHOUR = '" + end_hr+"' AND ENDMINUTE = '" + end_min+"'",null);
        return res;
    }


    public Cursor getDayData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select DAY from "+TABLE_NAME,null);
        return res;
    }

    public Cursor getMonthData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select MONTH from "+TABLE_NAME,null);
        return res;
    }

    public Cursor getYearData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select YEAR from "+TABLE_NAME,null);
        return res;
    }

    public Cursor getEventData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select EVENT from "+TABLE_NAME,null);
        return res;
    }

    public boolean updateData(String id,String d,String m,String y,String event,String hr,String min,String end_d,String end_m,String end_y, String end_hr, String end_min,String event_id,String desc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_2,d);
        contentValues.put(COL_3,m);
        contentValues.put(COL_4,y);
        contentValues.put(COL_5,event);
        contentValues.put(COL_6,hr);
        contentValues.put(COL_7,min);
        contentValues.put(COL_8,end_d);
        contentValues.put(COL_9,end_m);
        contentValues.put(COL_10,end_y);
        contentValues.put(COL_11,end_hr);
        contentValues.put(COL_12,end_min);
        contentValues.put(COL_13,event_id);
        contentValues.put(COL_14,desc);


        db.update(TABLE_NAME, contentValues, "ID = ?",new String[] { id });
        return true;
    }

    public Integer deleteData (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?",new String[] {id});
    }

    public Integer deleteData2 (String id, String day, String month,String year) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor =db.rawQuery("select ID from reminder_table where DAY = '" + day +"' AND MONTH = '" + month +"' AND YEAR = '" + year+"'",null);

        if (cursor.moveToFirst()) { //<<<< Move to the first row (should only be 1)
            id = cursor.getString(cursor.getColumnIndex("ID")); //<< get the data from the column
            return db.delete(TABLE_NAME, "ID = ?",new String[] {id});
        }

        return db.delete(TABLE_NAME, "ID = ?",new String[] {id});
    }





}
