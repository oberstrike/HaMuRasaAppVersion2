package de.hamurasa.main.fragments.adapters

import android.widget.TextView
import de.hamurasa.R
import de.hamurasa.util.epoxy.KotlinModel

data class LessonKotlinModel(
    val lessonId: String
) : KotlinModel(R.layout.holder_lesson_fragment) {

    val lessonIdTextView by bind<TextView>(R.id.lesson_id)

    override fun bind() {
        lessonIdTextView.text = lessonId
    }

}