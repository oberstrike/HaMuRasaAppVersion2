package de.hamurasa.main.fragments.home.lesson

import android.graphics.Color
import android.view.View
import android.widget.TextView
import de.hamurasa.R
import de.hamurasa.data.lesson.Lesson
import de.hamurasa.util.epoxy.KotlinModel

data class LessonKotlinModel(
    val lesson: Lesson,
    val lessonClickListener: IOnLessonClickListener,
    val lessonLongClickListener: IOnLessonLongClickListener,
    val lessonOnCreateContextMenuListener: View.OnCreateContextMenuListener,
    val lessonId: Int
) : KotlinModel(R.layout.holder_lesson_fragment) {

    private val lessonIdTextView by bind<TextView>(R.id.lesson_id)

    override fun bind() {
        lessonIdTextView.text = "Lesson ${lesson.id}"

        view?.apply {
            setBackgroundColor(Color.parseColor("#D4D4D4"))
            setOnClickListener {
                lessonClickListener.onLessonClick(lesson)
            }
            setOnLongClickListener {
                lessonLongClickListener.onLessonLongClick(lessonId)
                false
            }
            setOnCreateContextMenuListener(lessonOnCreateContextMenuListener)
        }

    }

}