package com.example.activitycrud;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "taskDatabase.db";
    private static final String TABLE_TASKS = "tasks";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TASK_NAME = "taskName";
    private static final String COLUMN_TASK_STATUS = "taskStatus";

    // Sentencia SQL para crear la tabla
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_TASKS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TASK_NAME + " TEXT, " +
                    COLUMN_TASK_STATUS + " INTEGER);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE); // Crea la tabla al iniciar la base de datos
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        onCreate(db);
    }

    // Getter para COLUMN_TASK_NAME
    public static String getColumnTaskName() {
        return COLUMN_TASK_NAME;
    }

    // Métodos CRUD

    // Método para insertar una nueva tarea
    public long addTask(String taskName, int taskStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_NAME, taskName);
        values.put(COLUMN_TASK_STATUS, taskStatus);
        return db.insert(TABLE_TASKS, null, values);
    }

    // Método para leer todas las tareas
    public Cursor getAllTasks() {
        SQLiteDatabase db = this.getReadableDatabase();
        // Usar un alias _id para la columna id
        return db.rawQuery("SELECT id AS _id, taskName, taskStatus FROM " + TABLE_TASKS, null);
    }
    

    // Método para actualizar el nombre de una tarea
    public int updateTaskName(int id, String newTaskName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_NAME, newTaskName);
        return db.update(TABLE_TASKS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    // Método para actualizar el estado de una tarea
    public int updateTaskStatus(int id, int newStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_STATUS, newStatus);
        return db.update(TABLE_TASKS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    // Método para eliminar una tarea
    public int deleteTask(int taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_TASKS, COLUMN_ID + " = ?", new String[]{String.valueOf(taskId)});
    }

}
