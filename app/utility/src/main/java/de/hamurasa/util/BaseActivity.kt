package de.hamurasa.util

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.mitteloupe.solid.activity.SolidActivity

import de.hamurasa.main.AbstractViewModel

abstract class BaseActivity<T : AbstractViewModel>() : SolidActivity() {


    abstract val myViewModel: T

    abstract val layoutRes: Int

    abstract val toolbarToUse: Toolbar?

    open fun onFailure(exception: Exception) = Unit

    open fun onSuccess() = Unit


    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(layoutRes)
        if (toolbarToUse != null)
            setSupportActionBar(toolbarToUse)
        super.onCreate(savedInstanceState)
    }
}

fun Activity.toast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_LONG).show()
}




