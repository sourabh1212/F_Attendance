package com.linear.f_attendance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper
{
    public static final String Database_NAME="attendanceDatabase";
    public static final String Database_Table="attendance";
    public static final String KEY_ID="id";
    public static final String KEY_SUBJECT="subject";
    public static final String KEY_ATTENDED="attended_classes";
    public static final String KEY_MISSED="attended_missed";
    public static final String KEY_PERCENTAGE = "percentage";
    public static final String KEY_REQUIRED = "required";
   // SQLiteDatabase sd;

    public DatabaseHelper(@Nullable Context context) {
        super(context, Database_NAME, null, 1);
        Log.i("a","super(context, Database_NAME, null, 1);");
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "+ Database_Table + " ( " + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_SUBJECT + " TEXT, " + KEY_ATTENDED + " INTEGER,"+ KEY_MISSED + " INTEGER," + KEY_PERCENTAGE + " DOUBLE, "+ KEY_REQUIRED + " INTEGER);" );
        Log.i("b","onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("DROP TABLE IF EXISTS "+Database_Table);
       // Log.i("c", "upgrade");
       // onCreate(db);
    }


    public boolean insertData(String sub, String attended , String missed)
    {
        try {
            int attended_int = Integer.parseInt(attended), missed_int = Integer.parseInt(missed);
            double percent = ((double) (attended_int) / (double) (attended_int + missed_int) * 100);
            double req = Math.ceil(((0.75 * (attended_int + missed_int)) - attended_int) / 0.25);
            int req_int;
            if (req <= 0) {
                req_int = 0;
            } else {
                req_int = (int) req;
            }
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(KEY_SUBJECT, sub);
            contentValues.put(KEY_ATTENDED, attended_int);
            contentValues.put(KEY_MISSED, missed_int);
            contentValues.put(KEY_PERCENTAGE, percent);
            contentValues.put(KEY_REQUIRED, req_int);

            long result = db.insert(Database_Table, null, contentValues);

            if (result == -1) {
                Log.i("tag", "false");
                return false;
            } else {
                Log.i("tag", "true");
                return true;
            }
        } catch (NumberFormatException e) {

            Log.i("enter","valid ");
            return false;
        }

        /*
        int attended_int = Integer.parseInt(attended),missed_int = Integer.parseInt(missed);
        double percent = ((double)(attended_int)/(double)(attended_int + missed_int)*100);
        double req = Math.ceil(((0.75 * (attended_int + missed_int))-attended_int)/0.25);
        int req_int;
        if(req<=0)
        {
            req_int=0;
        }
        else{
            req_int=(int)req;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_SUBJECT, sub);
        contentValues.put(KEY_ATTENDED, attended_int);
        contentValues.put(KEY_MISSED, missed_int);
        contentValues.put(KEY_PERCENTAGE, percent);
        contentValues.put(KEY_REQUIRED, req_int);

        long result = db.insert(Database_Table, null, contentValues);

        if(result == -1){
            Log.i("tag", "false");
            return false;
        }
        else{
            Log.i("tag", "true");
            return  true;
        }
*/
        }


    public Cursor allData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+Database_Table, null);
        Log.i("alldata","called hua he");
        return res;
    }

    public boolean updateData_att(String id,String miss,String att)
    {
        int at = 1+Integer.parseInt(att);
        int mis = Integer.parseInt(miss);
        double percent = (((Double.valueOf(at))/(Double.valueOf(at + mis)))*100);
        double req = Math.ceil(((0.75 * (at + mis))-at)/0.25);
        int req_int;
        if(req<=0)
        {
            req_int=0;
        }
        else{
            req_int=(int)req;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID, id);
        contentValues.put(KEY_ATTENDED, at);
        contentValues.put(KEY_PERCENTAGE, percent);
        contentValues.put(KEY_REQUIRED, req_int);
        int a = db.update(Database_Table, contentValues, "id = ?", new String[] { id });
        Log.i("rows effeted",String.valueOf(a));
        return true;


    }

    public boolean updateData_miss(String id,String miss,String att)
    {
        int at = Integer.parseInt(att);
        int mis = 1+Integer.parseInt(miss);
        double percent = (((Double.valueOf(at))/(Double.valueOf(at + mis)))*100);
        double req = Math.ceil(((0.75 * (at + mis))-at)/0.25);
        int req_int;
        if(req<=0)
        {
            req_int=0;
        }
        else{
            req_int=(int)req;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID, id);
        contentValues.put(KEY_MISSED, mis);
        contentValues.put(KEY_PERCENTAGE, percent);
        contentValues.put(KEY_REQUIRED, req_int);
        int a = db.update(Database_Table, contentValues, "id = ?", new String[] { id });
        Log.i("rows effeted",String.valueOf(a));
        return true;

    }

    public void deleteData(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Database_Table, "id = ?", new String[] { id });

    }


}
