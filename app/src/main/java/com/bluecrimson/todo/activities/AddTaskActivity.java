package com.bluecrimson.todo.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bluecrimson.todo.R;
import com.bluecrimson.todo.database.DbHelper;
import com.bluecrimson.todo.databinding.ActivityAddTaskBinding;
import com.bluecrimson.todo.utils.DateValidator;

import java.util.Calendar;

@SuppressWarnings("ConstantConditions")
public class AddTaskActivity extends AppCompatActivity {

    Context context;
    ActivityAddTaskBinding binding;
    DbHelper db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddTaskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        context = this;
        db = new DbHelper(context);

        binding.addTaskEdtStartDate.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            @SuppressLint("DefaultLocale") DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                    (DatePickerDialog.OnDateSetListener) (view, year, monthOfYear, dayOfMonth) -> {
                        binding.addTaskEdtStartDate.setText(String.format("%02d-%02d-%d", dayOfMonth, (monthOfYear + 1), year));
                    }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        binding.addTaskEdtEndDate.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            @SuppressLint("DefaultLocale") DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                    (DatePickerDialog.OnDateSetListener) (view, year, monthOfYear, dayOfMonth) -> {
                        binding.addTaskEdtEndDate.setText(String.format("%02d-%02d-%d", dayOfMonth, (monthOfYear + 1), year));
                    }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        binding.addTaskToolbar.setNavigationOnClickListener(v -> onBackPressed());

        binding.addTaskBtnSave.setOnClickListener(v -> addTask());
    }

    private void addTask() {
        String title, desc, startDate, endDate;
        int priority;

        title = binding.addTaskEdtTitle.getText().toString().trim();
        if(title.isEmpty()) {
            Toast.makeText(context, "Title is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        desc = binding.addTaskEdtDesc.getText().toString().trim();
        if(desc.isEmpty()) {
            Toast.makeText(context, "Description is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        startDate = binding.addTaskEdtStartDate.getText().toString().trim();
        if(!DateValidator.isValidDate(startDate)) {
            Toast.makeText(context, "Choose Start Date", Toast.LENGTH_SHORT).show();
            return;
        }

        endDate = binding.addTaskEdtEndDate.getText().toString().trim();
        if(!DateValidator.isValidDate(endDate)) {
            Toast.makeText(context, "Choose End Date", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!DateValidator.isStartDateLessThanEndDate(startDate, endDate)){
            Toast.makeText(context, "Start Date must be less than end date", Toast.LENGTH_SHORT).show();
            return;
        }


        int checkedId = binding.addTaskRadioGroup.getCheckedRadioButtonId();
        if(checkedId == R.id.addTaskRdBtnLow) {
            priority = 1;
        } else if(checkedId == R.id.addTaskRdBtnMedium) {
            priority = 2;
        } else if(checkedId == R.id.addTaskRdBtnHigh) {
            priority = 3;
        }
        else {
            priority = 1;
        }

        db.addTask(title, desc, startDate, endDate, priority);
        finish();
    }

}
