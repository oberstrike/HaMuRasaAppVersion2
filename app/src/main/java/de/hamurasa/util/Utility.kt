package de.hamurasa.util

import android.content.Context
import android.net.ConnectivityManager
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.fatboyindustrial.gsonjodatime.Converters
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import de.hamurasa.lesson.model.Lesson
import de.hamurasa.lesson.model.Vocable
import org.joda.time.DateTime
import java.lang.reflect.Type


@SuppressWarnings
fun convertDateTimeToHeadline(dateTime: DateTime, language: Language = Language.DE): String {
    val day = dateTime.dayOfMonth().get()
    val month = dateTime.monthOfYear().get()
    val dayMonth = "${if (day < 9) "0$day" else day}.${if (month < 9) "0$month" else month}."

    return when (language == Language.DE) {
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
        gson = Converters.registerDateTime(
            GsonBuilder()
        )
            .registerTypeAdapter(Lesson::class.java, LessonDeserializer())
            .registerTypeAdapter(Vocable::class.java, VocableDeserializer())
            .create()

    }
}



enum class Language {
    DE, EN
}

enum class Weekday(val id: Int) {
    MONTAG(1), DIENSTAG(2), MITTWOCH(3), DONNERSTAG(4), FREITAG(5), SAMSTAG(6), SONNTAG(7);

    companion object {
        fun byId(id: Int) = values().find { it.id == id }
    }

    override fun toString(): String {
        return super.toString().toLowerCase()
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
