package de.hamurasa.main.fragments

import android.content.Context
import android.provider.UserDictionary
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.hamurasa.R
import de.hamurasa.lesson.model.Vocable

class ResultRecyclerViewAdapter(
    val context: Context,
    val onClickListener: OnClickListener
) :
    RecyclerView.Adapter<ResultRecyclerViewAdapter.ViewHolder>() {

    val items: ArrayList<Vocable> = ArrayList()

    class ViewHolder(
        val item: View,
        private val onClickListener: OnClickListener
    ) : RecyclerView.ViewHolder(item), View.OnClickListener {

        val wordId: TextView = itemView.findViewById(R.id.lesson_id)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            onClickListener.onItemClick(adapterPosition)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val word = items[position]
        holder.wordId.text = word.value
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.lesson_fragment, parent, false)
        return ViewHolder(view, onClickListener)
    }

    fun setWords(words: List<Vocable>){
        items.clear()
        items.addAll(words)
    }

    interface OnClickListener {
        fun onItemClick(position: Int)
    }
}