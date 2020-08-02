package de.hamurasa.main.fragments.adapters

import android.graphics.Color
import android.widget.TextView
import de.hamurasa.R
import de.hamurasa.data.lesson.Lesson
import de.hamurasa.util.epoxy.KotlinModel

data class LessonKotlinModel(
    val lesson: Lesson,
    val lessonRecyclerViewListener: ILessonRecyclerViewListener
) : KotlinModel(R.layout.holder_lesson_fragment) {

    private val lessonIdTextView by bind<TextView>(R.id.lesson_id)

    override fun bind() {
        lessonIdTextView.text = "Lesson ${lesson.id}"

        view?.apply {
            setBackgroundColor(Color.parseColor("#D4D4D4"))
            setOnClickListener {
                lessonRecyclerViewListener.onLessonClick(lesson)
            }
            setOnLongClickListener {
                lessonRecyclerViewListener.onLessonLongClick(id)
                false
            }
            setOnCreateContextMenuListener(lessonRecyclerViewListener)
        }

    }

}