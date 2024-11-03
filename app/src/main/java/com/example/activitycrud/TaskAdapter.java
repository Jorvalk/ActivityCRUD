package com.example.activitycrud;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class TaskAdapter extends CursorAdapter {
    private DatabaseHelper dbHelper;

    public TaskAdapter(Context context, Cursor cursor, DatabaseHelper dbHelper) {
        super(context, cursor, 0);
        this.dbHelper = dbHelper; // Asigna la instancia de dbHelper
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.list_item_task, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textViewTaskName = view.findViewById(R.id.textViewTaskName);
        Button buttonEditTask = view.findViewById(R.id.buttonEditTask);
        Button buttonDeleteTask = view.findViewById(R.id.buttonDeleteTask);

        // Obtiene los datos de la tarea
        String taskName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.getColumnTaskName()));
        int taskId = cursor.getInt(cursor.getColumnIndexOrThrow("_id")); // Cambiado a "_id"

        textViewTaskName.setText(taskName);

        // Configura el botón de eliminar
        buttonDeleteTask.setOnClickListener(v -> {
            int rowsAffected = dbHelper.deleteTask(taskId); // Obtiene el número de filas afectadas
            if (rowsAffected > 0) {
                // Actualiza el cursor
                changeCursor(dbHelper.getAllTasks());
                Toast.makeText(context, "Tarea eliminada", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Error al eliminar la tarea", Toast.LENGTH_SHORT).show();
            }
        });

        // Configura el botón de editar
        buttonEditTask.setOnClickListener(v -> {
            // Asegúrate de que showEditTaskDialog esté accesible desde aquí
            if (context instanceof MainActivity) {
                ((MainActivity) context).showEditTaskDialog(taskId, taskName);
            }
        });
    }
}
