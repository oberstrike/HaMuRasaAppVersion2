package de.hamurasa.util

//import com.fatboyindustrial.gsonjodatime.Converters
import de.hamurasa.data.lesson.Lesson
import de.hamurasa.data.vocable.Vocable


fun Vocable.isValid(): Boolean{
    return value.isNotBlank()
}

fun Lesson.isValid(): Boolean{
    return language != validationLanguage
}

