package task.lobna.taskmanagement.viewmodel

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.shaiik.authentication.LoginSession
import com.shaiik.utilities.Utilities
import task.lobna.taskmanagement.data.TaskModel
import task.lobna.taskmanagement.ui.TasksAdapter

class MainViewModel : ViewModel() {
    val TAG = MainViewModel::class.java.simpleName

    val usernameObservable = ObservableField<String>()

    val tasks = ArrayList<TaskModel>()
    val allTasks = ArrayList<TaskModel>()
    var tasksAdapter: TasksAdapter

    val newTask = MutableLiveData<AlertDialog.Builder>()

    var filterApplied = ObservableBoolean(false)

    init {
        tasksAdapter = TasksAdapter(tasks)
    }

    fun back(view: View) {
        LoginSession.clearData(view.context)
    }

    fun filter(view: View) {
        filterApplied.set(filterApplied.get().not())

        if (filterApplied.get()) {
            tasks.clear()
            for (task in allTasks)
                if (task.done)
                    tasks.add(task)
            tasksAdapter.notifyDataSetChanged()
        } else {
            tasks.clear()
            tasks.addAll(allTasks)
            tasksAdapter.notifyDataSetChanged()
        }
    }

    fun add(view: View) {
        newTask.value = AlertDialog.Builder(view.context)
    }

    fun getData(context: Context) {
        val loadingDialog = Utilities.showLoading(context)
        FirebaseFirestore.getInstance()
            .collection("tasks")
            .whereEqualTo("userid", LoginSession.getUserData(context).id)
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(
                    snapshots: QuerySnapshot?,
                    exception: FirebaseFirestoreException?
                ) {
                    Utilities.dismissLoading(loadingDialog)
                    tasks.clear()
                    allTasks.clear()
                    if (exception != null) {
                        Log.e(TAG, "error", exception)
                        return
                    }

                    if (snapshots != null) {
                        for (doc in snapshots.documents) {
                            val task = TaskModel(
                                doc.id,
                                doc["userid"] as String
                                , doc["title"] as String
                                , doc["date"] as String
                                , doc["done"] as Boolean
                                , doc["priority"] as Long
                            )
                            tasks.add(task)
                            allTasks.add(task)
                        }
                    }

                    tasksAdapter.notifyDataSetChanged()
                }
            })
    }
}