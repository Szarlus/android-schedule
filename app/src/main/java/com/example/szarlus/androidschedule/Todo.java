package com.example.szarlus.androidschedule;

import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by karol on 10.02.2017.
 */

public class Todo {

    public int id;
    public String title;
    public boolean done;

    public Todo(int id, String title, boolean done) {
        this.id = id;
        this.title = title;
        this.done = done;
        Log.d(TAG, this.toString());
    }

    @Override
    public String toString() {
        return "New Todo instantiated: " + this.id + " " + this.title + " " + this.done;
    }
}
