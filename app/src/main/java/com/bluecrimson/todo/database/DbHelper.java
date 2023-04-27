package com.bluecrimson.todo.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.bluecrimson.todo.models.Task;

import java.util.ArrayList;

public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "tasks_db";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SqlQueries.CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS tasks");
        onCreate(db);
    }

    public void populateDummyData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS tasks");
        onCreate(db);
        addTask("Going to Bangalore", "Purchasing Books and Stationary", "01-05-2023", "10-05-2023", 3);
        addTask("Going to Manglore", "Purchasing Fishes and Sea Crabs", "01-05-2023", "20-05-2023", 2);
        addTask("Going to Assam", "Purchasing Tea", "01-05-2023", "30-05-2023", 1);
        addTask("Going to Andra Pradesh", "Purchasing Laddu", "01-05-2023", "09-05-2023", 3);
        addTask("Going to Jammu Kashmir", "Purchasing Kesari", "01-05-2023", "19-05-2023", 2);
        addTask("Going to Madhya Pradesh", "Visiting Kanno National Park", "01-05-2023", "29-05-2023", 1);
        addTask("Going to Rajastan", "Travelling Karai Camels", "01-05-2023", "08-05-2023", 3);
        addTask("Going to Gujarat", "Visiting Dani", "01-05-2023", "18-05-2023", 2);
        addTask("Going to Goa", "Purchasing Hot Drinks", "01-05-2023", "28-05-2023", 1);

        addTask("Going to Odissa", "Watching Hockey Rourkela Stadium", "01-06-2023", "10-06-2023", 3);
        addTask("Going to West Bengal", "Visiting Sunder-ban National Park", "01-06-2023", "20-06-2023", 2);
        addTask("Going to Bihar", "Visiting Patna", "01-06-2023", "30-06-2023", 1);
        addTask("Going to Maharashtra", "Visiting Nagpur", "01-06-2023", "30-06-2023", 1);
    }

    public void addTask(String title, String desc, String startDate, String endDate, int priority) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("desc", desc);
        values.put("start_date", startDate);
        values.put("end_date", endDate);
        values.put("priority", priority);
        db.insert("tasks", null, values);
        db.close();
    }

    public void updateTask(int id, String title, String desc, String startDate, String endDate, int priority) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("desc", desc);
        values.put("start_date", startDate);
        values.put("end_date", endDate);
        values.put("priority", priority);
        db.update("tasks", values, "id=?", new String[]{String.valueOf(id)});
        db.close();
    }


    public void markComplete(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("is_completed", true);
        db.update("tasks", values, "id=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void markAsPending(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("is_completed", false);
        db.update("tasks", values, "id=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteTask(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("tasks", "id=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> taskList = new ArrayList<Task>();
        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle")
        Cursor cursor = db.rawQuery(SqlQueries.MAIN_SELECT_QUERY, null);
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(Integer.parseInt(cursor.getString(0)));
                task.setTitle(cursor.getString(1));
                task.setDesc(cursor.getString(2));
                task.setStartDate(cursor.getString(3));
                task.setEndDate(cursor.getString(4));
                task.setPriority(cursor.getInt(5));
                task.setCompleted((cursor.getInt(7) == 1));
                taskList.add(task);
            } while (cursor.moveToNext());
        }
        return taskList;
    }

    public Task getTask(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        @SuppressLint("Recycle")
        Cursor cursor = db.rawQuery("select * from tasks where id=" + id + ";", null);
        Log.d("Raghu", "getTask: " + cursor.getCount());
        cursor.moveToFirst();
        Task task = new Task();
        task.setId(Integer.parseInt(cursor.getString(0)));
        task.setTitle(cursor.getString(1));
        task.setDesc(cursor.getString(2));
        task.setStartDate(cursor.getString(3));
        task.setEndDate(cursor.getString(4));
        task.setPriority(cursor.getInt(5));
        task.setCompleted((cursor.getInt(7) == 1));
        return task;
    }

}
