package de.hamurasa.main.fragments.adapters

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mitteloupe.solid.recyclerview.InflatedViewProvider
import com.mitteloupe.solid.recyclerview.SimpleViewBinder
import de.hamurasa.R
import de.hamurasa.data.vocable.Vocable
import kotlinx.android.extensions.LayoutContainer

interface VocableOnClickListener {
    fun onItemClick(vocable: de.hamurasa.data.vocable.Vocable)
}

class SolidVocableViewProvider(
    layoutInflater: LayoutInflater
) : InflatedViewProvider(layoutInflater, R.layout.holder_vocable_fragment)


class SolidVocableViewHolder(
    override val containerView: View
) : RecyclerView.ViewHolder(containerView), LayoutContainer {
    val wordValueTextView: TextView = itemView.findViewById(R.id.vocable_value)
    val wordTranslationTextView: TextView = itemView.findViewById(R.id.vocable_translation)
}


class SolidVocableViewBinder(
    private val vocableOnClickListener: VocableOnClickListener
) : SimpleViewBinder<SolidVocableViewHolder, de.hamurasa.data.vocable.Vocable>() {

    override fun bindView(viewHolder: SolidVocableViewHolder, data: de.hamurasa.data.vocable.Vocable) {
        viewHolder.itemView.setOnClickListener {
            vocableOnClickListener.onItemClick(data)
        }

        viewHolder.wordValueTextView.text = data.value
        if (data.value.length < 6) {
            viewHolder.wordValueTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 24f)
        } else {
            viewHolder.wordValueTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20f)
        }

        val translation = data.translation.first()
        viewHolder.wordTranslationTextView.text = translation

        if (translation.length < 6) {
            viewHolder.wordTranslationTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 22f)
        } else {
            viewHolder.wordTranslationTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16f)
        }
    }

}