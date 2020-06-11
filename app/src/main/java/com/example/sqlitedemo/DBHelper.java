package com.example.sqlitedemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public static final String STUDENT_TABLE = "STUDENT_TABLE";
    public static final String COL_ID = "ID";
    public static final String COL_STUDENT_NAME = "STUDENT_NAME";
    public static final String COL_STUDENT_COURSE = "STUDENT_COURSE";
    public static final String COL_COURSE_DONE = "COURSE_DONE";

    public DBHelper(@Nullable Context context) {
        super(context, "student.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_table_query = "CREATE TABLE " + STUDENT_TABLE + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_STUDENT_NAME + " TEXT, " + COL_STUDENT_COURSE + " TEXT, " + COL_COURSE_DONE + " BOOL)";
        db.execSQL(create_table_query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addStudentToDb (StudentModal studentToAdd){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COL_STUDENT_NAME,studentToAdd.getName());
        cv.put(COL_STUDENT_COURSE, studentToAdd.getCourse());
        cv.put(COL_COURSE_DONE, studentToAdd.isDone());

        long insertStatus = db.insert(STUDENT_TABLE,null,cv);

        if(insertStatus == -1){
            db.close();
            return false;
        }else{
            db.close();
            return true;
        }

    }

    public List<StudentModal> getAllStudents(){

        List<StudentModal> result = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String get_all_student_query = "SELECT * FROM " + STUDENT_TABLE;

        Cursor cursor = db.rawQuery(get_all_student_query,null);

        if(cursor.moveToFirst()){
            do {
                int student_id = cursor.getInt(0);
                String student_name = cursor.getString(1);
                String student_course = cursor.getString(2);
                boolean course_done = cursor.getInt(3) == 1 ? true:false;

                StudentModal tempStudent = new StudentModal(student_id,student_name,student_course,course_done);

                result.add(tempStudent);

            } while(cursor.moveToNext());
        }else {
            //manage failure
        }

        cursor.close();
        return result;
    }

    public boolean deleteStudent(StudentModal studentToRemove){
        SQLiteDatabase db = this.getWritableDatabase();
        String delete_student_query = "DELETE FROM " + STUDENT_TABLE + " WHERE " + COL_ID + " = " + studentToRemove.getId();
        Cursor cursor = db.rawQuery(delete_student_query, null);

        if(cursor.moveToFirst()){
            db.close();
            cursor.close();
            return false;
        }else{
            db.close();
            cursor.close();
            return true;
        }

    }

    public void updateStudent(StudentModal studentToUpdate){
        SQLiteDatabase db = this.getWritableDatabase();

        int done = studentToUpdate.isDone() == true ? 1:0;

        String update_student_query = "UPDATE " + STUDENT_TABLE + " SET " + COL_STUDENT_NAME + " = " + "'" + studentToUpdate.getName() + "'"
                + " , " + COL_STUDENT_COURSE + " = " + "'" + studentToUpdate.getCourse() + "'"
                + " , " + COL_COURSE_DONE + " = " + "'" + done  + "'"
                + " WHERE " + COL_ID + " = " + "'" + studentToUpdate.getId() + "'";

        db.execSQL(update_student_query);
    }



}
