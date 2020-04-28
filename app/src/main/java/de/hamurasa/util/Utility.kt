package de.hamurasa.util

//import com.fatboyindustrial.gsonjodatime.Converters
import de.hamurasa.lesson.model.lesson.Lesson
import de.hamurasa.lesson.model.vocable.Vocable


fun Vocable.isValid(): Boolean{
    return value.isNotBlank()
}

fun Lesson.isValid(): Boolean{
    return language != validationLanguage
}

