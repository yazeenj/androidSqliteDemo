package com.example.sqlitedemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
    private AutoCompleteTextView auto_comp_student;
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
        auto_comp_student = findViewById(R.id.auto_comp_student);

        //Set data to list view
        updateAutoCompView();
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
                updateAutoCompView();
            }
        });

        auto_comp_student.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
            }
        });


        registerForContextMenu(student_list);


    }

    private void updateViews() {
        adp = new ArrayAdapter<StudentModal>(this, android.R.layout.simple_list_item_1, myDbHelper.getAllStudents());
        student_list.setAdapter(adp);
    }

    private void updateAutoCompView (){
        ArrayAdapter<StudentModal> auto_adp = new ArrayAdapter<StudentModal>(this,android.R.layout.simple_list_item_1,myDbHelper.getAllStudents());
        auto_comp_student.setAdapter(auto_adp);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo ) item.getMenuInfo();

        switch (item.getItemId()){
            case R.id.menu_item_update:
                //Code Update
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.update_dialog,null);

                dialogBuilder.setView(dialogView);

                //Declare
                final EditText update_name, update_course;
                final Switch update_done;
                Button update_btn;

                //inti
                update_name = dialogView.findViewById(R.id.dialog_edit_name);
                update_course = dialogView.findViewById(R.id.dialog_edit_course);
                update_done = dialogView.findViewById(R.id.dialog_sw_done);
                update_btn = dialogView.findViewById(R.id.dialog_btn_update);


                final StudentModal tempStudent = (StudentModal) adp.getItem(info.position);

                //Setters
                update_name.setText(tempStudent.getName());
                update_course.setText(tempStudent.getCourse());
                update_done.setChecked(tempStudent.isDone());

                final AlertDialog updateDialog = dialogBuilder.create();
                updateDialog.show();

                update_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        tempStudent.setName(update_name.getText().toString());
                        tempStudent.setCourse(update_course.getText().toString());
                        tempStudent.setDone(update_done.isChecked());

                        myDbHelper.updateStudent(tempStudent);
                        Toast.makeText(MainActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                        updateViews();

                        updateDialog.hide();
                        updateAutoCompView();
                    }
                });

                break;
            case R.id.menu_item_delete:
                boolean status = myDbHelper.deleteStudent((StudentModal) adp.getItem(info.position));
                Toast.makeText(MainActivity.this, "Deleted: " + status, Toast.LENGTH_SHORT).show();
                updateViews();
                updateAutoCompView();
                break;

        }

        return super.onContextItemSelected(item);
    }

}
