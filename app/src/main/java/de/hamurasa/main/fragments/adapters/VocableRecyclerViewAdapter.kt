package de.hamurasa.main.fragments.adapters

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.hamurasa.R
import de.hamurasa.lesson.model.vocable.Vocable

class VocableRecyclerViewAdapter(
    val context: Context,
    private val onClickListener: OnClickListener
) :
    RecyclerView.Adapter<VocableRecyclerViewAdapter.ViewHolder>() {

    val items: ArrayList<Vocable> = ArrayList()

    class ViewHolder(
        val item: View,
        private val onClickListener: OnClickListener
    ) : RecyclerView.ViewHolder(item), View.OnClickListener {

        val wordValueTextView: TextView = itemView.findViewById(R.id.vocable_value)
        val wordTranslationTextView: TextView = itemView.findViewById(R.id.vocable_translation)

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

        val offline = word.isOffline
        if(offline){
            holder.item.setBackgroundColor(Color.parseColor("#D4D4D4"))
        }


        holder.wordValueTextView.text = word.value
        if (word.value.length < 6) {
            holder.wordValueTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 24f)
        } else {
            holder.wordValueTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20f)
        }

        val translation = word.translation.firstOrNull().orEmpty().toString()

        holder.wordTranslationTextView.text = translation.split(',').first()

        if (translation.length < 6) {
            holder.wordTranslationTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 22f)
        } else {
            holder.wordTranslationTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16f)
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.holder_vocable_fragment, parent, false)
        return ViewHolder(
            view,
            onClickListener
        )
    }

    fun setWords(words: List<Vocable>) {
        if(words != items){
            items.clear()
            items.addAll(words)
        }
    }

    interface OnClickListener {
        fun onItemClick(position: Int)
    }
}