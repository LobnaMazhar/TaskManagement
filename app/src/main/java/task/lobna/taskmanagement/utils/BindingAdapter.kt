package task.lobna.taskmanagement.utils

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class BindingAdapter {

    @BindingAdapter("loadImage:resource")
    fun loadImage(imageView: ImageView, resource: Int) {
        imageView.setImageResource(resource)
    }

    @BindingAdapter("background:resource")
    fun backgroundResource(view: View, resource: Int) {
        view.setBackgroundResource(resource)
    }

    @BindingAdapter("recycler:adapter")
    fun recyclerAdapter(
        recyclerView: RecyclerView,
        adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>
    ) {
        recyclerView.adapter = adapter
    }
}