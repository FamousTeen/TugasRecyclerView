package praktikum.c14220059.tugasrecyclerview

import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class adapterRecView (private val taskList: ArrayList<task>) : RecyclerView
.Adapter<adapterRecView.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data: task, pos: Int)

        fun delData(pos: Int)

        fun likeTask(pos: Int)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ListViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        var _taskTitle = itemView.findViewById<TextView>(R.id.idTVTaskTitle)
        var _taskDate = itemView.findViewById<TextView>(R.id.idTVTaskDate)
        var _taskDesc = itemView.findViewById<TextView>(R.id.idTVTaskDescription)
        var _taskStatusImage = itemView.findViewById<ImageView>(R.id.idIVTaskImage)
        var _taskLikeButton = itemView.findViewById<ImageView>(R.id.likeBtn)
        var _deleteBtn = itemView.findViewById<Button>(R.id.deletebtn)
        var _editBtn = itemView.findViewById<Button>(R.id.editbtn)
        var _startOrStopBtn = itemView.findViewById<Button>(R.id.startorstopbtn)
        var _likeBtn = itemView.findViewById<ImageView>(R.id.likeBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        var task = taskList[position]

        holder._taskTitle.text = task.title
        holder._taskDate.text = task.date
        holder._taskDesc.text = task.description
        holder._taskStatusImage.setImageResource(task.image)
        holder._taskLikeButton.setImageResource(task.like)

        holder._editBtn.setOnClickListener {
            onItemClickCallback.onItemClicked(task, position)
        }

        holder._deleteBtn.setOnClickListener {
            onItemClickCallback.delData(position)
        }

        holder._likeBtn.setOnClickListener {
            onItemClickCallback.likeTask(position)
            holder._taskLikeButton.setImageResource(R.drawable.heart2)
        }

        holder._startOrStopBtn.setOnClickListener {
            if (holder._startOrStopBtn.text == "Start") {
                holder._startOrStopBtn.text = "Finish"
                holder._taskStatusImage.setImageResource(R.drawable.clock)
                holder._editBtn.isEnabled = false
                holder._startOrStopBtn.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#F4B400"))
                holder._startOrStopBtn.setTextColor(ColorStateList.valueOf(Color.parseColor("#000000")))
            } else {
                holder._startOrStopBtn.text = "Complete"
                holder._startOrStopBtn.isEnabled = false
                holder._taskStatusImage.setImageResource(R.drawable.check)
                holder._startOrStopBtn.setTextColor(ColorStateList.valueOf(Color.parseColor("#cccccc")))
                holder._startOrStopBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
            }
        }
    }
}