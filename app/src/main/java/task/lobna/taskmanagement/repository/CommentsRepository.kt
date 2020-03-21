package task.lobna.taskmanagement.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

abstract class CommentsRepository {
    companion object {
        private val commentsCollection = FirebaseFirestore.getInstance().collection("comments")

        fun createComment(map: HashMap<String, Any>): Task<DocumentReference> {
            return commentsCollection.add(map)
        }

        fun getCommentsByTask(id: String): Query {
            return commentsCollection
                .whereEqualTo("taskid", id)
                .orderBy("timestamp")

        }
    }
}