package task.lobna.taskmanagement.viewmodel

import android.util.Log
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.shaiik.utilities.Utilities
import task.lobna.taskmanagement.R
import task.lobna.taskmanagement.data.UserModel


class LoginViewModel : ViewModel() {
    val TAG: String = LoginViewModel::class.simpleName!!

    val usernameTextObservable = ObservableField<String>()
    val usernameTextErrorObservable = ObservableField<String>()

    var userLogin = MutableLiveData<UserModel>()

    fun login(view: View) {
        usernameTextErrorObservable.set(null)

        val username = usernameTextObservable.get()
        if (username.isNullOrEmpty()) {
            usernameTextErrorObservable.set(view.context.getString(R.string.field_required))
        } else {
            Utilities.hideKeyboard(view)
            val loadingDialog = Utilities.showLoading(view.context)
            val db = FirebaseFirestore.getInstance().collection("users")
            db.whereEqualTo("username", username)
                .limit(1)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val isEmpty = task.result!!.isEmpty
                        if (isEmpty) {
                            val map = HashMap<String, String>()
                            map.set("username", username)
                            db.add(map).addOnSuccessListener { p0 ->
                                val userModel =
                                    UserModel(
                                        p0!!.id,
                                        username
                                    )
                                userLogin.value = userModel
                                Utilities.dismissLoading(loadingDialog)
                            }.addOnFailureListener { e ->
                                Utilities.dismissLoading(loadingDialog)
                                Log.e(TAG, "Error writing document", e)
                            }
                        } else {
                            if (task.result!!.documents[0].exists()) {
                                val doc = task.result!!.documents[0]
                                val userModel =
                                    UserModel(
                                        doc.id,
                                        doc.data!!["username"].toString()
                                    )
                                userLogin.value = userModel
                                Utilities.dismissLoading(loadingDialog)
                            }
                        }
                    } else {
                        Utilities.dismissLoading(loadingDialog)
                    }
                }
        }
    }
}