package com.bluecrimson.todo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bluecrimson.todo.activities.EditTaskActivity;
import com.bluecrimson.todo.database.DbHelper;
import com.bluecrimson.todo.databinding.ListItemTaskBinding;
import com.bluecrimson.todo.interfaces.TaskMarkedListener;
import com.bluecrimson.todo.models.Task;
import com.bluecrimson.todo.utils.DateValidator;

import java.util.ArrayList;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.TodoViewHolder> {

    public ArrayList<Task> tasks;
    DbHelper db;
    Context context;
    LayoutInflater layoutInflater;

    Drawable drawablePending;
    Drawable drawableSuccess;

    String[] priorities = {"None", "Low", "Medium", "High"};
    int[] priorityColors = {Color.parseColor("#FFFFFF"), Color.parseColor("#FFF44336"), Color.parseColor("#FFFF9800"), Color.parseColor("#009688")};

    TaskMarkedListener taskMarkedListener;

    public TodoListAdapter(Context context, ArrayList<Task> tasks) {
        this.context = context;
        this.tasks = tasks;
        layoutInflater = LayoutInflater.from(context);
        drawablePending = ContextCompat.getDrawable(context, R.drawable.ic_status_pending_indicator);
        drawableSuccess = ContextCompat.getDrawable(context, R.drawable.ic_status_done_indicator);
        db = new DbHelper(context);
        taskMarkedListener = (TaskMarkedListener) context;
    }

    @NonNull
    @Override
    public TodoListAdapter.TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TodoViewHolder(ListItemTaskBinding.inflate(layoutInflater, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TodoListAdapter.TodoViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.binding.liTaskTitle.setText(task.getTitle());
        holder.binding.liTaskDesc.setText(task.getDesc());
        Log.d("Raghu", "onBindViewHolder: " + task.isCompleted());
        if (task.isCompleted()) {
            holder.binding.liTaskIsStatusText.setText("Completed");
            holder.binding.liTaskStatusIcon.setImageDrawable(drawableSuccess);
            holder.binding.liTaskBtnMarkComplete.setText("Mark as Pending");
        } else {
            holder.binding.liTaskIsStatusText.setText("Pending");
            holder.binding.liTaskStatusIcon.setImageDrawable(drawablePending);
            holder.binding.liTaskBtnMarkComplete.setText("Mark as Completed");
        }
        holder.binding.liTaskPriority.setTextColor(priorityColors[task.getPriority()]);
        holder.binding.liTaskPriority.setText(priorities[task.getPriority()]);
        holder.binding.liTaskDate.setText(DateValidator.convertDateString(task.getStartDate()) + " • " + DateValidator.convertDateString(task.getEndDate()));
        holder.binding.liTaskBtnMarkComplete.setOnClickListener(v -> {
            if (task.isCompleted()) {
                db.markAsPending(task.getId());
            } else {
                db.markComplete(task.getId());
            }
            taskMarkedListener.onTaskMarked(task);
        });
        holder.binding.liTaskBtnEdit.setOnClickListener(v -> context.startActivity(new Intent(context, EditTaskActivity.class).putExtra("task_id", task.getId())));
        holder.binding.liTaskBtnDelete.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Are you sure to delete this task");
            builder.setMessage(task.getTitle());
            builder.setPositiveButton("Delete", (dialog, which) -> {
                db.deleteTask(task.getId());
                taskMarkedListener.onTaskDeleted(task);
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> {
                dialog.dismiss();
            });
            builder.create().show();
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public static class TodoViewHolder extends RecyclerView.ViewHolder {
        ListItemTaskBinding binding;

        public TodoViewHolder(ListItemTaskBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
