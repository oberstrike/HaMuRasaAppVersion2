package de.hamurasa.main.fragments.home.lesson

interface IOnLessonLongClickListener {
    fun onLessonLongClick(position: Int)

    fun getPosition(): Int
}

class OnLessonLongClickListenerImpl : IOnLessonLongClickListener {
    private var position = 0

    override fun getPosition() = position

    override fun onLessonLongClick(position: Int) {
        this.position = position
    }
}