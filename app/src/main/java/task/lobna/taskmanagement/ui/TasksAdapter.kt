package task.lobna.taskmanagement.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import task.lobna.taskmanagement.R
import task.lobna.taskmanagement.data.TaskModel
import task.lobna.taskmanagement.databinding.ItemTaskBinding
import task.lobna.taskmanagement.viewmodel.TaskItemViewModel

class TasksAdapter(var items: ArrayList<TaskModel>) :
    RecyclerView.Adapter<TasksAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        var itemTaskBinding = DataBindingUtil.inflate<ItemTaskBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_task,
            parent,
            false
        )
        return TaskViewHolder(itemTaskBinding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class TaskViewHolder(var itemTaskBinding: ItemTaskBinding) :
        RecyclerView.ViewHolder(itemTaskBinding.root) {

        fun bind(taskModel: TaskModel) {
            val taskItemViewModel = TaskItemViewModel(taskModel)
            itemTaskBinding.tivm = taskItemViewModel
        }
    }
}