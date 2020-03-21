package task.lobna.taskmanagement.viewmodel

import android.content.Context
import android.util.Log
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.shaiik.utilities.Utilities
import comment.lobna.commentmanagement.ui.adapter.CommentsAdapter
import task.lobna.taskmanagement.R
import task.lobna.taskmanagement.data.CommentModel
import task.lobna.taskmanagement.data.TaskModel

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

            FirebaseFirestore.getInstance().collection("comments")
                .add(map).addOnSuccessListener { p0 ->
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
        FirebaseFirestore.getInstance()
            .collection("tasks")
            .document(taskObservable.get()!!.id).delete()
        back(view)
    }

    fun markAsDone(view: View) {
        FirebaseFirestore.getInstance()
            .collection("tasks")
            .document(taskObservable.get()!!.id)
            .update("done", true)

        val task = taskObservable.get()
        task!!.done = true
        taskObservable.set(task)
        taskObservable.notifyChange()
    }

    fun setPriority(view: View) {
        when (view.id) {
            R.id.priority_1 -> updatePriority(1)
            R.id.priority_2 -> updatePriority(2)
            R.id.priority_3 -> updatePriority(3)
        }
    }

    private fun updatePriority(priority: Long) {
        FirebaseFirestore.getInstance()
            .collection("tasks")
            .document(taskObservable.get()!!.id)
            .update("priority", priority)

        val task = taskObservable.get()
        task!!.priority = priority
        taskObservable.set(task)
        taskObservable.notifyChange()
    }

    fun getComments(context: Context) {
        val loadingDialog = Utilities.showLoading(context)
        FirebaseFirestore.getInstance()
            .collection("comments")
            .whereEqualTo("taskid", taskObservable.get()!!.id)
            .orderBy("timestamp")
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