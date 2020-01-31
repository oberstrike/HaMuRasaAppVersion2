package de.hamurasa.main.fragments

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.hamurasa.R
import de.hamurasa.vocable.model.Lesson

class LessonRecylerViewAdapter(
    val context: Context
) : RecyclerView.Adapter<LessonRecylerViewAdapter.ViewHolder>() {

    private val items = ArrayList<Lesson>()

    class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView)
    {
        val lessonId: TextView = itemView.findViewById(R.id.lesson_id)

    }
    fun getLesson(position: Int): Lesson {
        return items[position]
    }

    fun setLessons(lessons: List<Lesson>){
        items.clear()
        items.addAll(lessons)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val id = item.id

        holder.lessonId.text = "Lektion: $id"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lesson_fragment, parent, false)
        return ViewHolder(view)
    }


}