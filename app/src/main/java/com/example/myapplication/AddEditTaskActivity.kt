package com.example.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.databinding.ActivityAddEditTaskBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AddEditTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditTaskBinding
    private val taskDao by lazy {
        TaskDatabase.getDatabase(this).taskDao()
    }

    private var existingTask: Task? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        existingTask = intent.getParcelableExtra("task")

        if (existingTask != null) {
            binding.editTextTitle.setText(existingTask!!.title)
            binding.editTextDescription.setText(existingTask!!.description)
            binding.editTextDueDate.setText(
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    .format(Date(existingTask!!.dueDate))
            )
            binding.buttonSave.text = "Update Task"
        }

        binding.buttonSave.setOnClickListener {
            saveTask()
        }
    }

    private fun saveTask() {
        val title = binding.editTextTitle.text.toString().trim()
        val description = binding.editTextDescription.text.toString().trim()
        val dueDateString = binding.editTextDueDate.text.toString().trim()

        if (title.isEmpty() || description.isEmpty() || dueDateString.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val dueDate = try {
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dueDateString)?.time
        } catch (e: Exception) {
            null
        }

        if (dueDate == null) {
            Toast.makeText(this, "Invalid date format. Use yyyy-MM-dd", Toast.LENGTH_SHORT).show()
            return
        }

        val task = Task(
            id = existingTask?.id ?: 0,
            title = title,
            description = description,
            dueDate = dueDate
        )

        lifecycleScope.launch {
            if (existingTask == null) {
                taskDao.insert(task)
            } else {
                taskDao.update(task)
            }
            finish()
        }
    }
}
