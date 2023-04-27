package com.bluecrimson.todo.interfaces;

import com.bluecrimson.todo.models.Task;

public interface TaskMarkedListener {
    void onTaskMarked(Task task);

    void onTaskDeleted(Task task);
}
