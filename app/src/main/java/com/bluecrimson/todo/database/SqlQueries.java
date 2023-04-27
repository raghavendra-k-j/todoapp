package com.bluecrimson.todo.database;

public class SqlQueries {
    public static final String CREATE_TABLE_QUERY = "CREATE TABLE tasks (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, title VARCHAR(255), desc TEXT, start_date DATE, end_date DATE, priority INTEGER, created_at DATETIME DEFAULT CURRENT_TIMESTAMP, is_completed BOOLEAN DEFAULT false);";
    public static final String MAIN_SELECT_QUERY = "SELECT * FROM tasks ORDER BY is_completed DESC, start_date ASC, priority DESC;";

}
