package de.hamurasa.settings.model

import android.content.Context
import com.chibatching.kotpref.KotprefModel

class Settings(context: Context) : KotprefModel(context) {

    var writingType by booleanPref(true, WRITING_TYPE)

    var standardType by booleanPref(true, STANDARD_TYPE)

    var alternativeType by booleanPref(true, ALTERNATIVE_TYPE)

    var activeLessonId by intPref(0, ACTIVE_LESSON_ID)

    var maxRepetitions by intPref(5, MAX_REPETITIONS)

    var maxVocableCount by intPref(20, MAX_VOCABLE_COUNT)

    var maxVocablesPerPage by intPref(10, MAX_VOCABLES_PER_PAGE)

    var maxLessonsPerPage by intPref(10, MAX_LESSONS_PER_PAGE)


    private companion object Key {
        const val ACTIVE_LESSON_ID = "ACTIVE_LESSON"
        const val MAX_REPETITIONS = "MAX_REPETITIONS"
        const val MAX_VOCABLE_COUNT = "MAX_VOCABLE_COUNT"
        const val WRITING_TYPE = "WRITING_TYPE"
        const val STANDARD_TYPE = "STANDARD_TYPE"
        const val ALTERNATIVE_TYPE = "ALTERNATIVE_TYPE"
        const val MAX_VOCABLES_PER_PAGE = "MAX_VOCABLES_PER_PAGE"
        const val MAX_LESSONS_PER_PAGE = "MAX_LESSONS_PER_PAGE"
    }


}