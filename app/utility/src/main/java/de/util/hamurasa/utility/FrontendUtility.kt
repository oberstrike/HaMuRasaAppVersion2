package de.util.hamurasa.utility

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

abstract class AbstractDialog<T>(obj: T) : AppCompatDialogFragment(), View.OnClickListener {

    abstract fun getLayoutId(): Int

    private fun afterCreation(builder: AlertDialog.Builder) = Unit

    abstract fun createView(view: View)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity!!.withDialog(
            dialogLayout = getLayoutId(),
            afterCreation = this::afterCreation,
            createView = this::createView
        ).create()
    }
}

abstract class AbstractFragment() : Fragment() {


    abstract fun init(view: View)

    abstract fun getLayoutId(): Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(getLayoutId(), container, false)
        if(view != null){
            init(view)
        }
        return view
    }

}

inline fun FragmentActivity.withDialog(
    dialogLayout: Int,
    crossinline afterCreation: (AlertDialog.Builder) -> Unit = {},
    crossinline createView: (view: View) -> Unit = {}
): AlertDialog.Builder {
    val builder = AlertDialog.Builder(this)

    val inflater = layoutInflater

    val view = inflater.inflate(dialogLayout, null)

    createView.invoke(view)

    builder.setView(view)

    afterCreation.invoke(builder)

    return builder
}


inline fun <reified E : Enum<E>> Activity.createSpinner(
    spinner: Spinner,
    layout: Int = android.R.layout.simple_spinner_dropdown_item
) {
    val values = enumValues<E>()
    val array = values.map { it.toString() }
    with(spinner) {
        adapter = ArrayAdapter(this.context, layout, array)
    }
}

fun Activity.createSpinner(
    spinner: Spinner,
    layout: Int = android.R.layout.simple_spinner_dropdown_item,
    array: Array<String>
) {
    with(spinner) {
        adapter = ArrayAdapter(this.context, layout, array)
    }
}


fun Menu.foreach(block: (MenuItem) -> Unit) {
    for (i in 0 until size()) {
        val item = this.getItem(i)
        block.invoke(item)
    }
}

fun Menu.findFirst(predicate: (MenuItem) -> Boolean): MenuItem? {
    for (i in 0 until size()) {
        val item = getItem(i)
        if (predicate.invoke(item)) {
            return item
        }
    }
    return null
}

fun Activity.toast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_LONG).show()
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
    })
}

fun Spinner.afterSelectedChanged(afterSelected: (String) -> Unit) {
    this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            val value = parent?.selectedItem?.toString()
            if (value != null) afterSelected.invoke(value)
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            TODO("Not yet implemented")
        }
    }


}


