package com.example.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Task
import com.example.myapplication.databinding.ItemTaskBinding
import com.example.myapplication.DateUtils

class TaskAdapter(private val listener: OnTaskClickListener) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private var taskList: List<Task> = listOf()

    inner class TaskViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) {
            binding.textViewTitle.text = task.title
            binding.textViewDescription.text = task.description
            binding.textViewDueDate.text = DateUtils.formatDate(task.dueDate)

            binding.buttonEdit.setOnClickListener {
                listener.onEditClick(task)
            }

            binding.buttonDelete.setOnClickListener {
                listener.onDeleteClick(task)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(taskList[position])
    }

    override fun getItemCount(): Int = taskList.size

    fun submitList(list: List<Task>) {
        taskList = list
        notifyDataSetChanged()
    }

    interface OnTaskClickListener {
        fun onEditClick(task: Task)
        fun onDeleteClick(task: Task)
    }
}
