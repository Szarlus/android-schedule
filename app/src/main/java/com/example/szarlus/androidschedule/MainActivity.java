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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.szarlus.androidschedule.db.TodoContract;
import com.example.szarlus.androidschedule.db.TodoDbHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TodoDbHelper todoDbHelper;
    private ListView todoListView;
    private ArrayAdapter<String> adapter;
    private Filter arrayFilter;
    private ArrayList<String> todosList;
    private TextView editText;

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
        this.editText = (TextView) findViewById(R.id.editText);
        this.editText.addTextChangedListener(new FilterTextWatcher(this.editText, this));
        logText("In MainActivity");
        this.todoDbHelper = new TodoDbHelper(this);
        updateUI();
    }

    public void logText(String text) {
        Log.d(TAG, text);
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

    public void filter(CharSequence sequence) {
        Filter f = getFilter();

        f.filter(sequence);
        logText("filtered");
    }

    public Filter getFilter() {
        if(arrayFilter == null) {
            arrayFilter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    FilterResults results = new FilterResults();

                    if(todosList == null) {
//                        updateUI();
                    }

                    if(charSequence == null || charSequence.length() == 0) {
                        ArrayList<String> list = new ArrayList(todosList);
                        results.values = list;
                        results.count = list.size();
                    } else {
                        final String soughtSequence = charSequence.toString().toLowerCase();

                        ArrayList<String> values = todosList;
                        int count = values.size();

                        ArrayList<String> newValues = new ArrayList<>(count);
                        for(int i = 0; i < count; i++) {
                            String item = values.get(i);

                            String[] words = item.toString().toLowerCase().split(" ");
                            int wordCount = words.length;

                            for (int k= 0; k<wordCount; k++) {
                                final String word = words[k];

                                if(word.contains(soughtSequence)) {
                                    newValues.add(item);
                                    break;
                                }
                            }
                        }

                        results.values = newValues;
                        results.count = newValues.size();
                    }

                    return results;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
//                    updateUI();
                    ArrayList<String> todosListPublish = (ArrayList<String>) filterResults.values;
                    logText(todosList.toString());
                    if(filterResults.count > 0) {
                        adapter.clear();
                        adapter.addAll(todosListPublish);
                        adapter.notifyDataSetChanged();
                    } else {
                        adapter.clear();
                        adapter.addAll(todosListPublish);
                        adapter.notifyDataSetInvalidated();
                    }
                }
            };
        }
        return arrayFilter;
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
//            case R.id.checkbox_meat:
//                if (checked)
//                // Put some meat on the sandwich
//                else
//                // Remove the meat
//                break;
            default:
                break;
        }
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

        todosList = new ArrayList<>();
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
            adapter = new ArrayAdapter<String>(this,
                    R.layout.todo_item,
                    R.id.todo_title,
                    (List<String>) todosList.clone());
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
