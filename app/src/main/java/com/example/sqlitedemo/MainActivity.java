package com.example.sqlitedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button add_student;
    private ListView student_list;
    private EditText input_name, input_course;
    private Switch course_done;
    private DBHelper myDbHelper;
    private ArrayAdapter adp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //intis
        add_student = findViewById(R.id.btn_add);
        student_list = findViewById(R.id.list_studentlist);
        input_name = findViewById(R.id.input_name);
        input_course = findViewById(R.id.input_course);
        course_done = findViewById(R.id.sw_done);
        myDbHelper = new DBHelper(this);

        //Set data to list view
        updateViews();

        //Add student
        add_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //info form views
                String name = input_name.getText().toString();
                String course = input_course.getText().toString();
                boolean isDone = course_done.isChecked();

                //student object
                StudentModal studentToAdd = new StudentModal(-1,name,course,isDone);

                //add to db
                boolean status = myDbHelper.addStudentToDb(studentToAdd);

                //control
                Toast.makeText(MainActivity.this, "Status: " + status, Toast.LENGTH_SHORT).show();
                updateViews();
            }
        });

        student_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boolean status = myDbHelper.deleteStudent((StudentModal) parent.getItemAtPosition(position));
                Toast.makeText(MainActivity.this, "Deleted: " + status, Toast.LENGTH_SHORT).show();
                updateViews();
            }
        });



    }

    private void updateViews() {
        adp = new ArrayAdapter<StudentModal>(this, android.R.layout.simple_list_item_1, myDbHelper.getAllStudents());
        student_list.setAdapter(adp);
    }
}
