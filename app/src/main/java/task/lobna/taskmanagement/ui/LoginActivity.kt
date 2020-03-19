package task.lobna.taskmanagement.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.shaiik.authentication.LoginSession
import task.lobna.taskmanagement.R
import task.lobna.taskmanagement.data.UserModel
import task.lobna.taskmanagement.databinding.ActivityLoginBinding
import task.lobna.taskmanagement.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityLoginBinding =
            DataBindingUtil.setContentView<ActivityLoginBinding>(
                this,
                R.layout.activity_login
            )

        val loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        activityLoginBinding.lvm = loginViewModel

        loginViewModel.userLogin.observe(this, object : Observer<UserModel> {
            override fun onChanged(userModel: UserModel?) {
                LoginSession.setUserData(this@LoginActivity, userModel!!)

                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        })
    }
}
