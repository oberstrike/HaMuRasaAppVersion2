package de.hamurasa.main.fragments.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.mitteloupe.solid.recyclerview.InflatedViewProvider
import com.mitteloupe.solid.recyclerview.SimpleViewBinder
import de.hamurasa.R
import kotlinx.android.extensions.LayoutContainer

class SolidViewProvider(
    layoutInflater: LayoutInflater
) : InflatedViewProvider(layoutInflater, R.layout.holder_lesson_fragment)

class SolidLessonViewHolder(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView), LayoutContainer {
    val lessonId: TextView = itemView.findViewById(R.id.lesson_id)
    val lessonView: ConstraintLayout = itemView.findViewById(R.id.lesson_view)


}

class SolidLessonViewBinder(
    private val lessonRecyclerViewListener: ILessonRecyclerViewListener
) : SimpleViewBinder<SolidLessonViewHolder, de.hamurasa.data.lesson.Lesson>() {

    private var position: Int = 0

    override fun bindView(viewHolder: SolidLessonViewHolder, data: de.hamurasa.data.lesson.Lesson) {

        viewHolder.lessonId.text = "Lesson: ${data.id}"
        viewHolder.lessonView.setBackgroundColor(Color.parseColor("#D4D4D4"))


        viewHolder.itemView.setOnClickListener {
            lessonRecyclerViewListener.onLessonClick(data)
        }


        viewHolder.itemView.setOnLongClickListener {
            lessonRecyclerViewListener.onLessonLongClick(viewHolder.adapterPosition)
            false
        }

        viewHolder.itemView.setOnCreateContextMenuListener(lessonRecyclerViewListener)
    }
}

interface OnLessonClickListener {
    fun onLessonClick(lesson: de.hamurasa.data.lesson.Lesson)
}

interface OnLessonLongClickListener {
    fun onLessonLongClick(position: Int)
}


interface ILessonRecyclerViewListener :
    OnLessonClickListener,
    View.OnCreateContextMenuListener,
    OnLessonLongClickListener