package com.example.szarlus.androidschedule.db;

import android.provider.BaseColumns;

/**
 * Created by karol on 28.01.2017.
 */

public class TodoContract {
    public static final String DB_NAME = "com.example.szarlus.androidschedule.db";
    public static final int DB_VERSION = 1;

    public class TodoEntry implements BaseColumns {
        public static final String TABLE = "todos";

        public static final String COL_TODO_TITLE = "title";
    }
}
