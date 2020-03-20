package task.lobna.taskmanagement.viewmodel

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import task.lobna.taskmanagement.R
import task.lobna.taskmanagement.data.TaskModel

class TaskItemViewModel(var task: TaskModel) : ViewModel() {

    val taskObservable = ObservableField<TaskModel>()

    init {
        taskObservable.set(task)
    }

    fun markAsDone(view:View){
        FirebaseFirestore.getInstance()
            .collection("tasks")
            .document(task.id)
            .update("done", true)
    }

    fun setPriority(view: View) {
        when (view.id) {
            R.id.priority_1 -> updatePriority(1)
            R.id.priority_2 -> updatePriority(2)
            R.id.priority_3 -> updatePriority(3)
        }
    }

    private fun updatePriority(priority: Int) {
        FirebaseFirestore.getInstance()
            .collection("tasks")
            .document(task.id)
            .update("priority", priority)
    }

}