package comment.lobna.commentmanagement.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import task.lobna.taskmanagement.data.CommentModel
import java.util.concurrent.TimeUnit

class CommentItemViewModel(var comment: CommentModel) : ViewModel() {

    val commentObservable = ObservableField<CommentModel>()
    val timeObservable = ObservableField<String>()

    init {
        commentObservable.set(comment)

        val millis = System.currentTimeMillis() - (comment.timestamp)
        val days = TimeUnit.MILLISECONDS.toDays(millis)
        if (days > 0)
            timeObservable.set("$days days ago")
        else {
            val hours = TimeUnit.MILLISECONDS.toHours(millis)
            timeObservable.set("$hours hours ago")
        }
    }

}