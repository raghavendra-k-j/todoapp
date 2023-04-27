package com.bluecrimson.todo.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bluecrimson.todo.R;
import com.bluecrimson.todo.TodoListAdapter;
import com.bluecrimson.todo.database.DbHelper;
import com.bluecrimson.todo.databinding.ActivityMainBinding;
import com.bluecrimson.todo.interfaces.TaskMarkedListener;
import com.bluecrimson.todo.models.Task;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements TaskMarkedListener {

    public static final String TAG = "Raghu-MainActivity";
    ActivityMainBinding binding;
    Context context;

    TodoListAdapter todoListAdapter;

    DbHelper db;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        context = this;

        db = new DbHelper(context);

        todoListAdapter = new TodoListAdapter(context, db.getAllTasks());

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        binding.mainRecylerView.setLayoutManager(layoutManager);
        binding.mainRecylerView.setAdapter(todoListAdapter);
        binding.mainToolbar.inflateMenu(R.menu.main_menu);
        binding.mainToolbar.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_item_exit) {
                finish();
            } else if (itemId == R.id.menu_item_search) {
                Snackbar.make(binding.getRoot(), "Dummy Search Option", Snackbar.LENGTH_SHORT).show();
            }
            else if (itemId == R.id.menu_item_dummy_data) {
                db.populateDummyData();
                Snackbar.make(binding.getRoot(), "Dummy Data was Populated", Snackbar.LENGTH_SHORT).show();
                reloadTasksFromDatabase();
            }
            return false;
        });
        binding.mainToolbar.setOverflowIcon(ContextCompat.getDrawable(context, R.drawable.ic_more_vertical));
        binding.mainToolbar.setNavigationOnClickListener(v -> Snackbar.make(binding.getRoot(), "Dummy Navigation Button", Snackbar.LENGTH_SHORT).show());

        binding.mainBtnAddTask.setOnClickListener(v -> startActivity(new Intent(context, AddTaskActivity.class)));


    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onResume() {
        super.onResume();
        todoListAdapter.tasks = db.getAllTasks();
        todoListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTaskMarked(Task task) {
        reloadTasksFromDatabase();
    }

    @Override
    public void onTaskDeleted(Task task) {
        reloadTasksFromDatabase();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void reloadTasksFromDatabase() {
        todoListAdapter.tasks = db.getAllTasks();
        todoListAdapter.notifyDataSetChanged();
    }

    private void populateDummyData() {
        db.addTask("Going to Bangalore", "Purchasing Books and Stationary", "12-04-2023", "28-08-2023", 1);
        db.addTask("Going to Manglore", "Purchasing Fishes and Sea Crabs", "12-04-2023", "29-04-2023", 2);
        db.addTask("Going to Goa", "Purchasing Hot Drinks", "12-04-2023", "30-04-2023", 3);
        db.addTask("Going to Assam", "Purchasing Tea", "12-04-2023", "01-04-2023", 1);
        db.addTask("Going to Andra Pradesh", "Purchasing Laddu", "12-04-2023", "02-04-2023", 1);
        db.addTask("Going to Jammu Kashmir", "Purchasing Kesari", "12-04-2023", "02-04-2023", 1);
        db.addTask("Going to Madhya Pradesh", "Visiting Kanno National Park", "27-04-2023", "03-05-2023", 1);
        db.addTask("Going to Rajastan", "Travelling Karai Camels", "27-04-2023", "03-05-2023", 2);
        db.addTask("Going to Gujarat", "Visiting Dani", "12-04-2023", "27-04-2023", 3);
        db.addTask("Going to Odissa", "Watching Hockey Rorkel Stadium", "12-04-2023", "13-04-2023", 1);
        db.addTask("Going to West Bengal", "Visiting Sunderban National Park", "12-04-2023", "13-04-2023", 1);
        db.addTask("Going to Bihar", "Visiting Patna", "12-04-2023", "13-04-2023", 1);
        db.addTask("Going to Maharastra", "Visiting Nagpur", "12-04-2023", "13-04-2023", 1);
    }

}