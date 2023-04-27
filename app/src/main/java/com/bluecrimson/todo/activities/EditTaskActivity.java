package com.bluecrimson.todo.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bluecrimson.todo.R;
import com.bluecrimson.todo.database.DbHelper;
import com.bluecrimson.todo.databinding.ActivityEditTaskBinding;
import com.bluecrimson.todo.models.Task;
import com.bluecrimson.todo.utils.DateValidator;

import java.util.Calendar;

@SuppressWarnings("ConstantConditions")
public class EditTaskActivity extends AppCompatActivity {

    Context context;
    ActivityEditTaskBinding binding;
    DbHelper db;

    int taskId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditTaskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        context = this;

        db = new DbHelper(context);

        Intent intent = getIntent();
        taskId = intent.getIntExtra("task_id", 0);
        if (taskId == 0) {
            Toast.makeText(context, "Invalid Task Id", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Task task = db.getTask(taskId);
            if (task != null) {
                binding.editTaskEdtTitle.setText(task.getTitle());
                binding.editTaskEdtDesc.setText(task.getDesc());
                binding.editTaskEdtStartDate.setText(task.getStartDate());
                binding.editTaskEdtEndDate.setText(task.getEndDate());
                switch (task.getPriority()) {
                    case 1:
                        binding.editTaskRdBtnLow.setChecked(true);
                        break;
                    case 2:
                        binding.editTaskRdBtnMedium.setChecked(true);
                        break;
                    case 3:
                        binding.editTaskRdBtnHigh.setChecked(true);
                        break;
                }
            }
        }


        binding.editTaskEdtStartDate.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            @SuppressLint("DefaultLocale") DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                    (DatePickerDialog.OnDateSetListener) (view, year, monthOfYear, dayOfMonth) -> {
                        binding.editTaskEdtStartDate.setText(String.format("%02d-%02d-%d", dayOfMonth, (monthOfYear + 1), year));
                    }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        binding.editTaskEdtEndDate.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            @SuppressLint("DefaultLocale") DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                    (DatePickerDialog.OnDateSetListener) (view, year, monthOfYear, dayOfMonth) -> {
                        binding.editTaskEdtEndDate.setText(String.format("%02d-%02d-%d", dayOfMonth, (monthOfYear + 1), year));
                    }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        binding.editTaskBtnSave.setOnClickListener(v -> editTask(taskId));
    }

    private void editTask(int id) {
        String title, desc, startDate, endDate;
        int priority;

        title = binding.editTaskEdtTitle.getText().toString().trim();
        if (title.isEmpty()) {
            Toast.makeText(context, "Title is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        desc = binding.editTaskEdtDesc.getText().toString().trim();
        if (desc.isEmpty()) {
            Toast.makeText(context, "Description is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        startDate = binding.editTaskEdtStartDate.getText().toString().trim();
        if (!DateValidator.isValidDate(startDate)) {
            Toast.makeText(context, "Choose Start Date", Toast.LENGTH_SHORT).show();
            return;
        }

        endDate = binding.editTaskEdtEndDate.getText().toString().trim();
        if (!DateValidator.isValidDate(endDate)) {
            Toast.makeText(context, "Choose End Date", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!DateValidator.isStartDateLessThanEndDate(startDate, endDate)) {
            Toast.makeText(context, "Start Date must be less than end date", Toast.LENGTH_SHORT).show();
            return;
        }


        int checkedId = binding.editTaskRadioGroup.getCheckedRadioButtonId();
        if (checkedId == R.id.editTaskRdBtnLow) {
            priority = 1;
        } else if (checkedId == R.id.editTaskRdBtnMedium) {
            priority = 2;
        } else if (checkedId == R.id.editTaskRdBtnHigh) {
            priority = 3;
        } else {
            priority = 1;
        }

        db.updateTask(id, title, desc, startDate, endDate, priority);
        finish();
    }

}
