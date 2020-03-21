package task.lobna.taskmanagement.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import task.lobna.taskmanagement.R
import task.lobna.taskmanagement.data.TaskModel
import task.lobna.taskmanagement.databinding.ActivityTaskDetailsBinding
import task.lobna.taskmanagement.viewmodel.TaskDetailsViewModel

class TaskDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityTaskDetailsBinding = DataBindingUtil.setContentView<ActivityTaskDetailsBinding>(
            this,
            R.layout.activity_task_details
        )

        val taskDetailsViewModel = ViewModelProviders.of(this).get(TaskDetailsViewModel::class.java)
        activityTaskDetailsBinding.tdvm = taskDetailsViewModel

        val bundle = intent.getBundleExtra("data")
        if (bundle != null) {
            val task = bundle.getParcelable<TaskModel>("task")
            taskDetailsViewModel.taskObservable.set(task)
        }

        taskDetailsViewModel.back.observe(this, Observer { t -> if (t!!) onBackPressed() })

        taskDetailsViewModel.getComments(this)
    }
}
