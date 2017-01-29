package com.example.szarlus.androidschedule.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by karol on 28.01.2017.
 */

public class TodoDbHelper extends SQLiteOpenHelper {

    public TodoDbHelper(Context context) {
        super(context, TodoContract.DB_NAME, null, TodoContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableSql = "CREATE TABLE " + TodoContract.TodoEntry.TABLE +
                " ( " + TodoContract.TodoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TodoContract.TodoEntry.COL_TODO_TITLE + " TEXT NOT NULL); ";

        sqLiteDatabase.execSQL(createTableSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String dropTableSql = "DROP TABLE IF EXISTS " + TodoContract.TodoEntry.TABLE + ";";

        sqLiteDatabase.execSQL(dropTableSql);
        onCreate(sqLiteDatabase);
    }
}
