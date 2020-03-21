package task.lobna.taskmanagement.viewmodel

import android.util.Log
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shaiik.utilities.Utilities
import task.lobna.taskmanagement.R
import task.lobna.taskmanagement.data.UserModel
import task.lobna.taskmanagement.repository.UserRepository


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
            UserRepository.getUser(username)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val isEmpty = task.result!!.isEmpty
                        if (isEmpty) {
                            val map = HashMap<String, Any>()
                            map.set("username", username)
                            UserRepository.createUser(map).addOnSuccessListener { p0 ->
                                applyLogin(p0!!.id, username)
                                Utilities.dismissLoading(loadingDialog)
                            }.addOnFailureListener { e ->
                                Utilities.dismissLoading(loadingDialog)
                                Log.e(TAG, "Error writing document", e)
                            }
                        } else {
                            if (task.result!!.documents[0].exists()) {
                                val doc = task.result!!.documents[0]
                                applyLogin(doc.id, doc.data!!["username"].toString())
                                Utilities.dismissLoading(loadingDialog)
                            }
                        }
                    } else {
                        Utilities.dismissLoading(loadingDialog)
                    }
                }
        }
    }

    private fun applyLogin(id: String, username: String) {
        userLogin.value = UserModel(id, username)
    }
}