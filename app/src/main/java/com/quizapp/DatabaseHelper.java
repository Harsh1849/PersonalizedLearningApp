package com.quizapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.google.gson.Gson;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "USER_DATA.db";
    private static final String TABLE_NAME = "user";
    public static final String COL_ID = "ID";
    public static final String COL_USERNAME = "USERNAME";
    public static final String COL_PASSWORD = "PASSWORD";
    public static final String COL_PHONE_NUMBER = "PHONENUMBER";
    public static final String COL_EMAIL = "EMAIL";
    public static final String COL_IMAGE_URL = "IMAGE_URL";

    private static final String TABLE_NAME_TASK = "task";
    private static final String COL_TASK_TITLE = "TASKTITLE";
    private static final String COL_TASK_DECS = "TASKDECS";
    private static final String COL_QUIZMODEL = "QUIZMODEL";
    private static final String COL_IS_TASK_COMPLETE = "isTaskComplete";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE user (ID INTEGER PRIMARY KEY AUTOINCREMENT , USERNAME TEXT, EMAIL TEXT, PASSWORD TEXT, PHONENUMBER TEXT, IMAGE_URL TEXT)");
        db.execSQL("CREATE TABLE task (ID INTEGER PRIMARY KEY AUTOINCREMENT , USERNAME TEXT, TASKTITLE TEXT, TASKDECS TEXT, QUIZMODEL TEXT, isTaskComplete INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME_TASK);
        onCreate(db);
    }

    public boolean insertUserData(ModelClass modelClass) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_USERNAME, modelClass.getUserName());
        contentValues.put(COL_EMAIL, modelClass.getEmail());
        contentValues.put(COL_PASSWORD, modelClass.getPassword());
        contentValues.put(COL_PHONE_NUMBER, modelClass.getPhoneNumber());
        contentValues.put(COL_IMAGE_URL, modelClass.getImageUrl());

        long result = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean checkUserName(String USERNAME) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_USERNAME + " = ?", new String[]{USERNAME});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkEmail(String EMAIL) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_EMAIL + " = ?", new String[]{EMAIL});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkPhoneNumber(String PHONENUMBER) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_PHONE_NUMBER + " = ?", new String[]{PHONENUMBER});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkLogIn(String USERNAME, String PASSWORD) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_USERNAME + "= ? AND " + COL_PASSWORD + " = ? ", new String[]{USERNAME, PASSWORD});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<ModelClass> getUserData(String USERNAME) {
        ArrayList<ModelClass> dataholder = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_USERNAME + "= ? ", new String[]{USERNAME});

        if (cursor.moveToNext()) {
            do {
                @SuppressLint("Range") ModelClass ModelClass = new ModelClass(cursor.getString(cursor.getColumnIndex(COL_USERNAME)), cursor.getString(cursor.getColumnIndex(COL_EMAIL)), cursor.getString(cursor.getColumnIndex(COL_PHONE_NUMBER)), cursor.getString(cursor.getColumnIndex(COL_PASSWORD)), cursor.getString(cursor.getColumnIndex(COL_IMAGE_URL)));
                dataholder.add(ModelClass);
            } while (cursor.moveToNext());
        }
        sqLiteDatabase.close();
        return dataholder;
    }

    public boolean insertTaskData(TaskModel taskModel) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_TASK_TITLE, taskModel.taskTitle);
        contentValues.put(COL_TASK_DECS, taskModel.taskDecs);
        contentValues.put(COL_QUIZMODEL, new Gson().toJson(taskModel.quizModel));
        contentValues.put(COL_USERNAME, taskModel.userName);
        contentValues.put(COL_IS_TASK_COMPLETE, taskModel.isTaskComplete);

        long result = sqLiteDatabase.insert(TABLE_NAME_TASK, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    @SuppressLint("Range")
    public ArrayList<TaskModel> getAllTasks(String USERNAME) {
        ArrayList<TaskModel> tasksList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME_TASK + " WHERE " + COL_USERNAME + "= ? ", new String[]{USERNAME});


        if (cursor.moveToFirst()) {
            do {
                TaskModel task = new TaskModel();
                task.id = cursor.getInt(cursor.getColumnIndex(COL_ID));
                task.quizModel = new Gson().fromJson(cursor.getString(cursor.getColumnIndex(COL_QUIZMODEL)), QuizModel.class);
                task.taskTitle = cursor.getString(cursor.getColumnIndex(COL_TASK_TITLE));
                task.taskDecs = cursor.getString(cursor.getColumnIndex(COL_TASK_DECS));
                task.userName = cursor.getString(cursor.getColumnIndex(COL_USERNAME));
                task.isTaskComplete = cursor.getInt(cursor.getColumnIndex(COL_IS_TASK_COMPLETE));
                tasksList.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return tasksList;
    }

    public boolean updateTaskData(Integer ID, Integer isTaskComplete, QuizModel quizModel) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_QUIZMODEL, new Gson().toJson(quizModel));
        values.put(COL_IS_TASK_COMPLETE, isTaskComplete);
        long result = sqLiteDatabase.update(TABLE_NAME_TASK, values, COL_ID + " = ?", new String[]{String.valueOf(ID)});
        sqLiteDatabase.close();

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

}
