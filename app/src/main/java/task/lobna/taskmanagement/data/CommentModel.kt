package task.lobna.taskmanagement.data

data class CommentModel(
    val id: String,
    val taskid: String,
    val comment: String,
    val timestamp: Long
)