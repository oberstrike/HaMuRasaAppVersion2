package de.hamurasa.lesson.fragments

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import de.hamurasa.R
import de.hamurasa.lesson.LessonContext
import de.hamurasa.lesson.LessonViewModel
import kotlinx.android.synthetic.main.vocable_fragment.*
import kotlinx.android.synthetic.main.vocable_fragment.view.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class VocableFragment : Fragment(), View.OnClickListener {

    private val myViewModel: LessonViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.vocable_fragment, container, false)

        view.vocable_second_value.setOnClickListener(this)

        return view
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myViewModel.launch {
            myViewModel.activeVocable.subscribeOn(myViewModel.provider.computation())
                .observeOn(myViewModel.provider.ui())
                .subscribe {
                    if (vocable_first_value != null) {

                        if (LessonContext.vocableType == Type.VALUE_TRANSLATION) {
                            vocable_first_value.text = it.value
                        } else {
                            var translation = ""
                            for (trans in it.translation) {
                                translation += "$trans,"
                            }
                            if (translation.length > 11) {
                                vocable_first_value.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 24f)

                            }

                            vocable_first_value.text = translation.dropLast(1)
                        }
                    }
                }
        }
    }

    override fun onClick(v: View?) {

        myViewModel.launch {
            myViewModel.activeVocable.subscribeOn(myViewModel.provider.computation())
                .observeOn(myViewModel.provider.ui())
                .subscribe {
                    if (vocable_second_value != null) {
                        if(LessonContext.vocableType  == Type.VALUE_TRANSLATION){
                            var translation = ""
                            for (trans in it.translation) {
                                translation += "$trans,"
                            }

                            if (translation.length > 11) {
                                vocable_second_value.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25f)

                            }

                            vocable_second_value.text = translation.dropLast(1)
                        }else{
                            vocable_second_value.text = it.value
                        }


                    }
                }
        }
    }


    enum class Type {
        VALUE_TRANSLATION,
        TRANSLATION_VALUE

    }

}