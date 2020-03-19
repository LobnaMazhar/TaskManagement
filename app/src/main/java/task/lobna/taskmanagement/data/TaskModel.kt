package task.lobna.taskmanagement.data

data class TaskModel(
    val userid: String,
    val title: String,
    val date: String,
    val done: Boolean,
    val priority: Long
)