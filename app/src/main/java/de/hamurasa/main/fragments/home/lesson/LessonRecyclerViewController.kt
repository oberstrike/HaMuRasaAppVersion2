package de.hamurasa.main.fragments.home.lesson

import android.view.View
import com.airbnb.epoxy.TypedEpoxyController
import de.hamurasa.data.lesson.Lesson

class LessonRecyclerViewController(
    private val lessonClickListener: IOnLessonClickListener,
    private val lessonLongClickListener: IOnLessonLongClickListener,
    private val lessonOnCreateContextMenuListener: View.OnCreateContextMenuListener
) : TypedEpoxyController<List<Lesson>>() {

    override fun buildModels(data1: List<Lesson>) {
        data1.forEachIndexed { index, lesson ->
            LessonKotlinModel(
                lesson,
                lessonClickListener,
                lessonLongClickListener,
                lessonOnCreateContextMenuListener,
                index
            )
                .id("$index")
                .addTo(this)

        }
    }
}


