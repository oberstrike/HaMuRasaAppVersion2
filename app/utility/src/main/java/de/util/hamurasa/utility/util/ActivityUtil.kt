package de.util.hamurasa.utility.util

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import de.util.hamurasa.utility.main.AbstractViewModel
import java.lang.Exception

interface IAbstractActivity {

}


abstract class AbstractActivity<T : AbstractViewModel>
    : IAbstractActivity, AppCompatActivity() {

    abstract val myViewModel: T

    abstract val layoutRes: Int

    abstract val toolbarToUse: Toolbar?

    open fun onFailure(exception: Exception) = Unit

    open fun onSuccess() = Unit

    abstract fun init()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutRes)
        if (toolbarToUse != null)
            setSupportActionBar(toolbarToUse)
        init()
    }
}