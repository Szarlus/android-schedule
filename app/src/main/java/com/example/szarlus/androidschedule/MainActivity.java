package com.example.szarlus.androidschedule;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.szarlus.androidschedule.db.TodoContract;
import com.example.szarlus.androidschedule.db.TodoDbHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TodoDbHelper todoDbHelper;
    private ListView todoListView;
    private ArrayAdapter<String> adapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_todo:
                onAddTodoButtonClicked();
                Log.d(TAG, "Add a new task");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.todoDbHelper = new TodoDbHelper(this);
        updateUI();
    }

    public void onAddTodoButtonClicked() {
        final EditText todoEditText = new EditText(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Add new Todo")
                .setMessage("What is there to do?")
                .setView(todoEditText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        storeTodo(todoEditText);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    public void storeTodo(EditText todoEditText) {
        String todoText = String.valueOf(todoEditText.getText());
        SQLiteDatabase sqliteDatabase = this.todoDbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TodoContract.TodoEntry.COL_TODO_TITLE, todoText);
        sqliteDatabase.insertWithOnConflict(TodoContract.TodoEntry.TABLE,
                null,
                contentValues,
                sqliteDatabase.CONFLICT_REPLACE);
        sqliteDatabase.close();

        updateUI();
    }

    public void updateUI() {
        this.todoListView = (ListView) findViewById(R.id.todos_list);

        ArrayList<String> todosList = new ArrayList<>();
        SQLiteDatabase sqliteDatabase = this.todoDbHelper.getReadableDatabase();
        Cursor cursor = sqliteDatabase.query(TodoContract.TodoEntry.TABLE,
                new String[]{TodoContract.TodoEntry._ID, TodoContract.TodoEntry.COL_TODO_TITLE},
                null,null,null,null,null);
        while (cursor.moveToNext()) {
            int index = cursor.getColumnIndex(TodoContract.TodoEntry.COL_TODO_TITLE);
            todosList.add(cursor.getString(index));
            Log.d(TAG, "Todo entry: " + cursor.getString(index));
        }

        if(adapter == null) {
            adapter = new ArrayAdapter<>(this,
                    R.layout.todo_item,
                    R.id.todo_title,
                    todosList);
            this.todoListView.setAdapter(this.adapter);
        } else {
            adapter.clear();
            adapter.addAll(todosList);
            adapter.notifyDataSetChanged();
        }

        cursor.close();
        sqliteDatabase.close();
    }

    public void deleteTodo(View view) {
        View parent = (View) view.getParent();
        TextView todoTextView = (TextView) parent.findViewById(R.id.todo_title);
        String todo = String.valueOf(todoTextView.getText());
        SQLiteDatabase sqliteDatabase = this.todoDbHelper.getWritableDatabase();
        sqliteDatabase.delete(TodoContract.TodoEntry.TABLE,
                TodoContract.TodoEntry.COL_TODO_TITLE + " = ?",
                new String[]{todo});
        sqliteDatabase.close();
        updateUI();
    }
}
