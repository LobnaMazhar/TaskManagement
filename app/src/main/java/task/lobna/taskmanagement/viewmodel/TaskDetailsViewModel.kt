package task.lobna.taskmanagement.viewmodel

import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.shaiik.utilities.Utilities
import comment.lobna.commentmanagement.ui.adapter.CommentsAdapter
import task.lobna.taskmanagement.R
import task.lobna.taskmanagement.data.CommentModel
import task.lobna.taskmanagement.data.TaskModel
import task.lobna.taskmanagement.repository.CommentsRepository
import task.lobna.taskmanagement.repository.TaskRepository
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class TaskDetailsViewModel : ViewModel() {

    val TAG = TaskDetailsViewModel::class.java.simpleName

    val taskObservable = ObservableField<TaskModel>()
    val commentObservable = ObservableField<String>()

    val back = MutableLiveData<Boolean>()

    val comments = ArrayList<CommentModel>()
    var commentsAdapter: CommentsAdapter

    init {
        commentsAdapter = CommentsAdapter(comments)
    }

    fun back(view: View) {
        back.value = true
    }

    fun send(view: View) {
        val comment = commentObservable.get()
        if (!comment.isNullOrBlank()) {
//            val loadingDialog = Utilities.showLoading(view.context)
            val map = HashMap<String, Any>()
            map["taskid"] = taskObservable.get()!!.id
            map["comment"] = comment.toString()
            map["timestamp"] = System.currentTimeMillis()

            CommentsRepository.createComment(map).addOnSuccessListener { p0 ->
//                                Utilities.dismissLoading(loadingDialog)
                commentObservable.set(null)
                Utilities.hideKeyboard(view)
            }.addOnFailureListener { e ->
//                                Utilities.dismissLoading(loadingDialog)
                Log.e(TAG, "Error writing document", e)
            }
        }
    }

    fun delete(view: View) {
        TaskRepository.deleteTask(taskObservable.get()!!.id).addOnSuccessListener { back(view) }
    }

    fun markAsDone(view: View) {
        TaskRepository.updateTaskDone(taskObservable.get()!!.id)
            .addOnSuccessListener {
                val task = taskObservable.get()
                task!!.done = true
                taskObservable.set(task)
                taskObservable.notifyChange()
            }
    }

    fun changeDate(view: View) {
        var selectedDate = taskObservable.get()!!.date

        var simpleDateFormat = SimpleDateFormat("MMMM dd yyyy", Locale.US)
        var year: Int
        var month: Int
        var day: Int
        if (selectedDate.isNullOrBlank()) {
            val c = Calendar.getInstance()
            year = c.get(Calendar.YEAR)
            month = c.get(Calendar.MONTH)
            day = c.get(Calendar.DAY_OF_MONTH)
        } else {
            var date = simpleDateFormat.parse(selectedDate)

            simpleDateFormat = SimpleDateFormat("yyyy", Locale.US)
            year = simpleDateFormat.format(date).toInt()

            simpleDateFormat = SimpleDateFormat("MM", Locale.US)
            month = simpleDateFormat.format(date).toInt() - 1

            simpleDateFormat = SimpleDateFormat("dd", Locale.US)
            day = simpleDateFormat.format(date).toInt()
        }

        val datePickerDialog = DatePickerDialog(
            view.context,
            DatePickerDialog.OnDateSetListener { p0, year, monthOfYear, dayOfMonth ->
                simpleDateFormat = SimpleDateFormat("yyyy MM dd", Locale.US)
                val month = monthOfYear + 1
                val date = simpleDateFormat.parse("$year $month $dayOfMonth")
                simpleDateFormat = SimpleDateFormat("MMMM dd yyyy", Locale.US)
                selectedDate = simpleDateFormat.format(date)
                updateDate(selectedDate)
            },
            year,
            month,
            day
        )

        datePickerDialog.setOnCancelListener {
            selectedDate = ""
            updateDate(selectedDate)
        }
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()

        datePickerDialog.show()
    }

    private fun updateDate(date: String) {
        TaskRepository.updateTaskDate(taskObservable.get()!!.id, date)
            .addOnSuccessListener {
                val task = taskObservable.get()
                task!!.date = date
                taskObservable.set(task)
                taskObservable.notifyChange()
            }
    }

    fun setPriority(view: View) {
        when (view.id) {
            R.id.priority_1 -> updatePriority(1)
            R.id.priority_2 -> updatePriority(2)
            R.id.priority_3 -> updatePriority(3)
        }
    }

    private fun updatePriority(priority: Long) {
        TaskRepository.updateTaskPriority(taskObservable.get()!!.id, priority)
            .addOnSuccessListener {
                val task = taskObservable.get()
                task!!.priority = priority
                taskObservable.set(task)
                taskObservable.notifyChange()
            }
    }

    fun getComments(context: Context) {
        val loadingDialog = Utilities.showLoading(context)
        CommentsRepository.getCommentsByTask(taskObservable.get()!!.id)
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(
                    snapshots: QuerySnapshot?,
                    exception: FirebaseFirestoreException?
                ) {
                    Utilities.dismissLoading(loadingDialog)
                    comments.clear()
                    if (exception != null) {
                        Log.e(TAG, "error", exception)
                        return
                    }

                    if (snapshots != null) {
                        for (doc in snapshots.documents) {
                            val comment = CommentModel(
                                doc.id,
                                doc["taskid"] as String
                                , doc["comment"] as String
                                , doc["timestamp"] as Long
                            )
                            comments.add(comment)
                        }
                    }

                    commentsAdapter.notifyDataSetChanged()
                }
            })
    }
}