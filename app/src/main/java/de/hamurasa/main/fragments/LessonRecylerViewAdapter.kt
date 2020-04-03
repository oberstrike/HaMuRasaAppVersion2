package de.hamurasa.main.fragments

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.hamurasa.R
import de.hamurasa.lesson.model.Lesson

class LessonRecylerViewAdapter(
    val context: Context,
    val onClickListener: OnClickListener
) : RecyclerView.Adapter<LessonRecylerViewAdapter.ViewHolder>() {

    private val items = ArrayList<Lesson>()

    class ViewHolder(
        itemView: View,
        private val onClickListener: OnClickListener
    ) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val lessonId: TextView = itemView.findViewById(R.id.lesson_id)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            onClickListener.onItemClick(adapterPosition)
        }

    }

    fun getLesson(position: Int): Lesson {
        return items[position]
    }

    fun setLessons(lessons: List<Lesson>) {
        items.clear()
        items.addAll(lessons)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.lessonId.text = context.resources.getString(R.string.lesson, position + 1)
        if (item.words.size == 0) {
            holder.lessonId.setTextColor(Color.parseColor("#a1a1a1"))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.lesson_fragment, parent, false)

        return ViewHolder(view, onClickListener)
    }

    interface OnClickListener {
        fun onItemClick(position: Int)
    }
}

