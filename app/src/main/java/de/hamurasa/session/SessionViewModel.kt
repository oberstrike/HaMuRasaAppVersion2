package de.hamurasa.session

import android.content.Context
import de.hamurasa.util.SchedulerProvider
import de.hamurasa.session.models.VocableWrapper
import de.hamurasa.settings.SettingsContext
import de.hamurasa.util.BaseViewModel
import de.util.hamurasa.utility.util.weight
import kotlinx.coroutines.flow.flowOf

class SessionViewModel(
    provider: SchedulerProvider,
    val context: Context
) : BaseViewModel(provider) {


    fun init() {
        with(SessionContext) {
            vocables = vocables.take(SettingsContext.SessionSettings.maxVocableCount)
            val next = vocables.random()
            activeVocable = next
            val sessionTypesTmp = mutableListOf<SessionType>()
            if (SettingsContext.SessionSettings.standardInputType) {
                sessionTypesTmp.add(SessionType.STANDARD)
            }
            if (SettingsContext.SessionSettings.alternativeInputType) {
                sessionTypesTmp.add(SessionType.ALTERNATIVE)

            }
            if (SettingsContext.SessionSettings.writingInputType) {
                sessionTypesTmp.add(SessionType.WRITING)
            }
            sessionTypes = sessionTypesTmp
            sessionType = sessionTypes.random()
        }
    }

    fun next(correct: Boolean): VocableWrapper? {
        val vocable = SessionContext.activeVocable
        vocable.level += if (correct) 1 else -1
        vocable.attempts += 1
        var nextVocableWrapper: VocableWrapper? = null

        if (vocable.level >= (SettingsContext.SessionSettings.maxRepetitions + 1)) {
            SessionContext.vocables =
                SessionContext.vocables.filterNot { it.value == vocable.value }
        }

        if (SessionContext.vocables.isNotEmpty()) {
            val weightedList = SessionContext.vocables.weight(keySelector = {
                it.level
            }, weightFunction = {
                SettingsContext.SessionSettings.maxRepetitions - it
            })

            val next = weightedList.random()
            nextVocableWrapper = next
        } else {
            SessionContext.running = flowOf(false)
        }
        SessionContext.sessionType = SessionContext.sessionTypes.random()
        if (nextVocableWrapper == null) {
            return null
        }
        SessionContext.activeVocable = nextVocableWrapper
        SessionContext.sessionType = SessionContext.sessionTypes.random()
        return nextVocableWrapper
    }
}


