package de.hamurasa.main.fragments.adapters

import android.content.Context
import android.graphics.Color
import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.hamurasa.R
import de.hamurasa.lesson.model.lesson.Lesson

class LessonRecyclerViewAdapter(
    val context: Context,
    private val onClickListener: OnClickListener
) : RecyclerView.Adapter<LessonRecyclerViewAdapter.ViewHolder>() {

    private val items = ArrayList<Lesson>()

    var position: Int = 0

    class ViewHolder(
        itemView: View,
        private val onClickListener: OnClickListener
    ) :
        RecyclerView.ViewHolder(itemView),
        View.OnClickListener,
        View.OnCreateContextMenuListener {
        val lessonId: TextView = itemView.findViewById(R.id.lesson_id)

        init {
            itemView.setOnClickListener(this)
            itemView.setOnCreateContextMenuListener(this)
        }

        override fun onClick(v: View?) {
            onClickListener.onItemClick(adapterPosition)
        }

        override fun onCreateContextMenu(
            menu: ContextMenu?,
            v: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            menu?.add(Menu.NONE, R.id.action_delete, Menu.NONE, R.string.action_delete)
            menu?.add(Menu.NONE, R.id.action_rename, Menu.NONE, R.string.action_rename)
            menu?.add(Menu.NONE, R.id.action_start, Menu.NONE, R.string.start)
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

        holder.itemView.setOnLongClickListener {
            this.position = holder.adapterPosition
            false
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.holder_lesson_fragment, parent, false)

        return ViewHolder(
            view,
            onClickListener
        )
    }

    interface OnClickListener {
        fun onItemClick(position: Int)

    }
}

