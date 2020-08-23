package com.example.MySQL;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
//https://blog.csdn.net/Afanbaby/article/details/79240620
public class MySQLiteHelper extends SQLiteOpenHelper {

    private Context context;

    public MySQLiteHelper(Context context, String name,
                          SQLiteDatabase.CursorFactory factory, int version) {

        super(context, "Blossom.db", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL("create table diary(id integer PRIMARY KEY AUTOINCREMENT,"
                +"title text,content text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {
        // TODO Auto-generated method stub
        db.execSQL("drop table if exists diary");
        onCreate(db);
    }

}
