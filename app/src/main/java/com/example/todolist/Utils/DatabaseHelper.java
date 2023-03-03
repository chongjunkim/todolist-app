package com.example.todolist.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.todolist.Model.ToDoListModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private SQLiteDatabase db;

    private static final String DATABASE_NAME = "TODO_DATABASE";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "TODO_TABLE";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "TASK";
    private static final String COL_3 = "STATUS";
    private static final String COL_4 = "DATE";
    private static final String COL_5 = "COMPLETED_DATE";
    private static final String COL_6 = "CATEGORY";
    private static final String COL_7 = "CATEGORY_INDEX";


    private static final String CATEGORY_TABLE_NAME = "CATEGORY_TABLE";
    private static final String CATEGORY_COL_1 = "ID";
    private static final String CATEGORY_COL_2 = "NAME";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, TASK TEXT, STATUS INTEGER, DATE TEXT, COMPLETED_DATE TEXT, CATEGORY TEXT, CATEGORY_INDEX INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertTask(ToDoListModel model) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_2, model.getTask());
        values.put(COL_3, 0);
        values.put(COL_4, model.getDate());
        values.put(COL_5, model.getCompletedDate());
        values.put(COL_6, model.getCategory());
        values.put(COL_7, model.getCategoryIndex());
        db.insert(TABLE_NAME, null, values);
    }

    public void updateTask(int id, String task, String date, String category, int categoryIndex) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_2, task);
        values.put(COL_4, date);
        values.put(COL_6, category);
        values.put(COL_7, categoryIndex);
        db.update(TABLE_NAME, values, "ID=?", new String[]{String.valueOf(id)});
    }

    public void updateStatus(int id, int status, String completedDate) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_3, status);
        values.put(COL_5, completedDate);
        db.update(TABLE_NAME, values, "ID=?", new String[]{String.valueOf(id)});
    }

    public void deleteTask(int id) {
        db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "ID=?", new String[]{String.valueOf(id)});
    }

    public List<ToDoListModel> getAllTasks() {
        db = this.getWritableDatabase();
        Cursor cursor = null;
        List<ToDoListModel> modelList = new ArrayList<>();

        db.beginTransaction();
        try {
            cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    ToDoListModel task = new ToDoListModel();
                    task.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COL_1)));
                    task.setTask(cursor.getString(cursor.getColumnIndexOrThrow(COL_2)));
                    task.setStatus(cursor.getInt(cursor.getColumnIndexOrThrow(COL_3)));
                    task.setDate(cursor.getString(cursor.getColumnIndexOrThrow(COL_4)));
                    task.setCompletedDate(cursor.getString(cursor.getColumnIndexOrThrow(COL_5)));
                    task.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(COL_6)));
                    task.setCategoryIndex(cursor.getInt(cursor.getColumnIndexOrThrow(COL_7)));
                    modelList.add(task);
                } while (cursor.moveToNext());
            }
        } finally {
            db.endTransaction();
            if (cursor != null) {
                cursor.close();
            }
        }
        return modelList;
    }
}
