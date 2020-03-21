package task.lobna.taskmanagement.repository

import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.shaiik.authentication.LoginSession

abstract class TaskRepository {
    companion object {
        private val tasksCollection = FirebaseFirestore.getInstance().collection("tasks")

        fun createTask(map: HashMap<String, Any>): Task<DocumentReference> {
            return tasksCollection.add(map)
        }

        fun deleteTask(id: String): Task<Void> {
            return tasksCollection.document(id).delete()
        }

        fun updateTaskDone(id: String): Task<Void> {
            return tasksCollection.document(id).update("done", true)
        }

        fun updateTaskDate(id: String, date: String): Task<Void> {
            return tasksCollection.document(id).update("date", date)
        }

        fun updateTaskPriority(id: String, priority: Long): Task<Void> {
            return tasksCollection.document(id).update("priority", priority)
        }

        fun getTasksByUser(context: Context): Query {
            return tasksCollection.whereEqualTo("userid", LoginSession.getUserData(context).id)
                .orderBy("created_on", Query.Direction.DESCENDING)
        }
    }
}