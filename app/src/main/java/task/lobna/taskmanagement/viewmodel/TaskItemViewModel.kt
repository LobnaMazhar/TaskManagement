package task.lobna.taskmanagement.viewmodel

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import task.lobna.taskmanagement.R
import task.lobna.taskmanagement.data.TaskModel
import task.lobna.taskmanagement.repository.TaskRepository
import task.lobna.taskmanagement.ui.activity.TaskDetailsActivity

class TaskItemViewModel(var task: TaskModel) : ViewModel() {

    val taskObservable = ObservableField<TaskModel>()

    init {
        taskObservable.set(task)
    }

    fun markAsDone(view: View) {
        TaskRepository.updateTaskDone(task.id)
    }

    fun setPriority(view: View) {
        when (view.id) {
            R.id.priority_1 -> updatePriority(1)
            R.id.priority_2 -> updatePriority(2)
            R.id.priority_3 -> updatePriority(3)
        }
    }

    private fun updatePriority(priority: Long) {
        TaskRepository.updateTaskPriority(task.id, priority)
    }

    fun goToDetails(view: View) {
        val intent = Intent(view.context, TaskDetailsActivity::class.java)
        val bundle = Bundle()
        bundle.putParcelable("task", task)
        intent.putExtra("data", bundle)
        view.context.startActivity(intent)
    }

}