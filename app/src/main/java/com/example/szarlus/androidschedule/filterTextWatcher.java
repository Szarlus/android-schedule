package com.example.szarlus.androidschedule;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import static android.content.ContentValues.TAG;

/**
 * Created by karol on 09.02.2017.
 */

public class FilterTextWatcher implements TextWatcher {

    private Context context;
    View editTextView;

    public FilterTextWatcher(View editTextView, Context context) {
        super();
        this.editTextView = editTextView;
        this.context = context;
        Log.d(TAG, "watcher instantiated");
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        Log.d(TAG, "seq: "+charSequence);
        Log.d(TAG, "start: "+start);
        Log.d(TAG, "seq: "+before);
        Log.d(TAG, "seq: "+count);
//        this.context.getApplicationContext().
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
