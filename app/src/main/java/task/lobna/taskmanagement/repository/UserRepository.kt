package task.lobna.taskmanagement.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

abstract class UserRepository {
    companion object {
        private val usersCollection = FirebaseFirestore.getInstance().collection("users")

        fun getUser(username: String): Task<QuerySnapshot> {
            return usersCollection.whereEqualTo("username", username)
                .limit(1)
                .get()
        }

        fun createUser(map: HashMap<String, Any>): Task<DocumentReference> {
            return usersCollection.add(map)
        }
    }
}