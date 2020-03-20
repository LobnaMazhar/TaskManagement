package task.lobna.taskmanagement.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.shaiik.authentication.LoginSession
import com.shaiik.utilities.Utilities
import kotlinx.android.synthetic.main.activity_main.*
import task.lobna.taskmanagement.R
import task.lobna.taskmanagement.databinding.ActivityMainBinding
import task.lobna.taskmanagement.utils.SwipeToDeleteCallback
import task.lobna.taskmanagement.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {

    val TAG = MainActivity::class.java.simpleName

    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityMainBinding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        activityMainBinding.mvm = mainViewModel

        mainViewModel.usernameObservable.set(LoginSession.getUserData(this).username + "'s Tasks")

        initSwipeToDelete()

        mainViewModel.getData(this)

        mainViewModel.newTask.observe(this,
            Observer { builder ->
                val view =
                    LayoutInflater.from(this@MainActivity).inflate(R.layout.dialog_new_task, null)
                builder!!.setView(view)

                val alertDialog = builder.create()

                val titleEditText = view.findViewById<EditText>(R.id.title_edit_text)
                val createButton = view.findViewById<Button>(R.id.create_button)

                createButton.setOnClickListener {
                    val title = titleEditText.text
                    if (title.isNullOrEmpty()) {
                        titleEditText.error = getString(R.string.field_required)
                    } else {
                        Utilities.hideKeyboard(it)

//                        val loadingDialog = Utilities.showLoading(this)

                        val date = Date()
                        val simpleDateFormat = SimpleDateFormat("MMMM dd yyyy")
                        val currentDate = simpleDateFormat.format(date)

                        val map = HashMap<String, Any>()
                        map["userid"] = LoginSession.getUserData(this@MainActivity).id
                        map["title"] = title.toString()
                        map["priority"] = 0
                        map["done"] = false
                        map["date"] = currentDate
                        FirebaseFirestore.getInstance().collection("tasks")
                            .add(map).addOnSuccessListener { p0 ->
//                                Utilities.dismissLoading(loadingDialog)
                                Toast.makeText(
                                    this@MainActivity,
                                    "Task has been added successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                alertDialog.dismiss()
                            }.addOnFailureListener { e ->
//                                Utilities.dismissLoading(loadingDialog)
                                Log.e(TAG, "Error writing document", e)
                            }
                    }
                }

                alertDialog.show()
            })
    }

    private fun initSwipeToDelete() {
        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                mainViewModel.delete(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(tasks_recyclerview)
    }
}
