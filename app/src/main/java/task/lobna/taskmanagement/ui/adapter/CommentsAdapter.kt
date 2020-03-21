package comment.lobna.commentmanagement.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import comment.lobna.commentmanagement.viewmodel.CommentItemViewModel
import task.lobna.taskmanagement.R
import task.lobna.taskmanagement.data.CommentModel
import task.lobna.taskmanagement.databinding.ItemCommentBinding

class CommentsAdapter(var items: ArrayList<CommentModel>) :
    RecyclerView.Adapter<CommentsAdapter.CommentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        var itemCommentBinding = DataBindingUtil.inflate<ItemCommentBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_comment,
            parent,
            false
        )
        return CommentViewHolder(itemCommentBinding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class CommentViewHolder(var itemCommentBinding: ItemCommentBinding) :
        RecyclerView.ViewHolder(itemCommentBinding.root) {

        fun bind(commentModel: CommentModel) {
            val commentItemViewModel = CommentItemViewModel(commentModel)
            itemCommentBinding.civm = commentItemViewModel
        }
    }
}