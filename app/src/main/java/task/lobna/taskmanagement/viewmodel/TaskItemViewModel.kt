package task.lobna.taskmanagement.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import task.lobna.taskmanagement.data.TaskModel

class TaskItemViewModel(var task: TaskModel) : ViewModel() {

    val taskObservable = ObservableField<TaskModel>()

    init {
        taskObservable.set(task)
    }

}