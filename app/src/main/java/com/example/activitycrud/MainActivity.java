package com.example.activitycrud;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private EditText editTextTaskName;
    private Button buttonAddTask;
    private ListView listViewTasks;
    private TaskAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        editTextTaskName = findViewById(R.id.editTextTaskName);
        buttonAddTask = findViewById(R.id.buttonAddTask);
        listViewTasks = findViewById(R.id.listViewTasks);

        loadTasks();

        buttonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskName = editTextTaskName.getText().toString().trim();
                if (!taskName.isEmpty()) {
                    long id = dbHelper.addTask(taskName, 0);
                    if (id != -1) {
                        loadTasks(); // Refresca la lista
                        editTextTaskName.setText(""); // Limpia el campo
                    } else {
                        Toast.makeText(MainActivity.this, "Error al agregar la tarea", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Ingrese un nombre para la tarea", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadTasks() {
        Cursor cursor = dbHelper.getAllTasks();
        if (cursor != null) {
            adapter = new TaskAdapter(this, cursor, dbHelper);
            listViewTasks.setAdapter(adapter);
        } else {
            Toast.makeText(this, "No se encontraron tareas", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adapter != null && adapter.getCursor() != null) {
            adapter.getCursor().close(); // Cierra el cursor asociado con el adaptador
        }
        dbHelper.close(); // Cierra la base de datos
    }

    public void showEditTaskDialog(int taskId, String currentTaskName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Editar tarea");

        final EditText input = new EditText(this);
        input.setText(currentTaskName);
        builder.setView(input);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String newTaskName = input.getText().toString().trim();
            if (!newTaskName.isEmpty()) {
                dbHelper.updateTaskName(taskId, newTaskName); // Asegúrate de tener un método para actualizar solo el nombre
                loadTasks(); // Refresca la lista
            } else {
                Toast.makeText(MainActivity.this, "Ingrese un nombre para la tarea", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
        builder.show();
    }
}
