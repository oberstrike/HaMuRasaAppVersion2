package de.hamurasa.util

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
//import com.fatboyindustrial.gsonjodatime.Converters
import com.google.gson.*
import de.hamurasa.R
import de.hamurasa.lesson.model.vocable.Language
import de.hamurasa.lesson.model.lesson.Lesson
import de.hamurasa.lesson.model.vocable.Vocable
import org.joda.time.DateTime
import java.util.*
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


@SuppressWarnings
fun convertDateTimeToHeadline(dateTime: DateTime, language: Language = Language.GER): String {
    val day = dateTime.dayOfMonth().get()
    val month = dateTime.monthOfYear().get()
    val dayMonth =
        "${if (day < 9) "0$day" else day.toString()}.${if (month < 9) "0$month" else month.toString()}."


    return when (language == Language.GER) {
        true -> "Training vom $dayMonth"
        false -> "Training from $dayMonth"
    }
}


fun verifyAvailableNetwork(activity: AppCompatActivity): Boolean {
    val connectivityManager =
        activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnected
}

object GsonObject {
    var gson: Gson
        private set

    init {
        gson = GsonBuilder() //Converters.registerDateTime(
            //      GsonBuilder()
            //   )
            .registerTypeAdapter(Lesson::class.java, LessonDeserializer())
            .registerTypeAdapter(Vocable::class.java, VocableDeserializer())
            .create()

    }
}

enum class Weekday(val id: Int) {
    MONTAG(1), DIENSTAG(2), MITTWOCH(3), DONNERSTAG(4), FREITAG(5), SAMSTAG(6), SONNTAG(7);

    companion object {
        fun byId(id: Int) = values().find { it.id == id }
    }

    override fun toString(): String {
        return super.toString().toLowerCase(Locale.ROOT)
    }


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

class StringPreference(
    private val sharedPreferences: SharedPreferences,
    private val key: String,
    private val defaultValue: Boolean = false
) : ReadWriteProperty<Any?, Boolean> {

    override fun getValue(thisRef: Any?, property: KProperty<*>): Boolean =
        sharedPreferences.getBoolean(key, defaultValue)

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: Boolean) =
        sharedPreferences.edit()
            .putBoolean(key, value)
            .apply()
}

fun SharedPreferences.boolean(
    key: String,
    defaultValue: Boolean = false
): ReadWriteProperty<Any?, Boolean> =
    StringPreference(this, key, defaultValue)

fun Menu.foreach(block: (MenuItem) -> Unit) {
    val size = this.size()
    for (i in 0 until size) {
        val item = this.getItem(i)
        block.invoke(item)
    }
}
