package task.lobna.taskmanagement.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shaiik.authentication.LoginSession
import task.lobna.taskmanagement.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onDestroy() {
        super.onDestroy()
        LoginSession.clearData(this)
    }
}
