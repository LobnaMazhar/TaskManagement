package task.lobna.taskmanagement.data

import android.os.Parcel
import android.os.Parcelable

data class TaskModel(
    val id: String,
    val userid: String,
    val title: String,
    var date: String,
    var done: Boolean,
    var priority: Long
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readByte() != 0.toByte(),
        parcel.readLong()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(userid)
        parcel.writeString(title)
        parcel.writeString(date)
        parcel.writeByte(if (done) 1 else 0)
        parcel.writeLong(priority)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TaskModel> {
        override fun createFromParcel(parcel: Parcel): TaskModel {
            return TaskModel(parcel)
        }

        override fun newArray(size: Int): Array<TaskModel?> {
            return arrayOfNulls(size)
        }
    }
}