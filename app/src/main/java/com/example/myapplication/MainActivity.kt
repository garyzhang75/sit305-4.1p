package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.AddEditTaskActivity
import com.example.myapplication.Task
import com.example.myapplication.TaskDatabase
import com.example.myapplication.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), TaskAdapter.OnTaskClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: TaskAdapter
    private val taskDao by lazy {
        TaskDatabase.getDatabase(this).taskDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = TaskAdapter(this)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        taskDao.getAllTasks().observe(this) { tasks ->
            adapter.submitList(tasks)
        }

        binding.fabAddTask.setOnClickListener {
            startActivity(Intent(this, AddEditTaskActivity::class.java))
        }
    }

    override fun onEditClick(task: Task) {
        val intent = Intent(this, AddEditTaskActivity::class.java)
        intent.putExtra("task", task)
        startActivity(intent)
    }

    override fun onDeleteClick(task: Task) {
        lifecycleScope.launch {
            taskDao.delete(task)
        }
    }
}
