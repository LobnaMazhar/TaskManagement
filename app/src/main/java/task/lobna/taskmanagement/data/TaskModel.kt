package task.lobna.taskmanagement.data

data class TaskModel(
    val id: String,
    val userid: String,
    val title: String,
    val date: String,
    val done: Boolean,
    val priority: Long
)