package de.hamurasa.session

import android.content.Context
import de.hamurasa.session.models.SessionEvent
import de.hamurasa.session.models.SessionType
import de.hamurasa.session.models.VocableWrapper
import de.hamurasa.settings.model.Settings
import de.hamurasa.util.BaseViewModel
import de.hamurasa.util.SchedulerProvider
import de.hamurasa.util.weight
import kotlinx.coroutines.ExperimentalCoroutinesApi

class SessionViewModel(
    provider: SchedulerProvider,
    val context: Context,
    val settings: Settings,
    val session: SessionEvent
) : BaseViewModel(provider) {

    private lateinit var sessionTypes: List<SessionType>


    @ExperimentalCoroutinesApi
    fun init() {
        with(session) {
            vocables = vocables.take(settings.maxVocableCount)
            val next = vocables.random()
            activeVocable = next

            val tmp: MutableList<SessionType> = mutableListOf()
            if (settings.standardType) {
                tmp.add(SessionType.STANDARD)
            }
            if (settings.alternativeType) {
                tmp.add(SessionType.ALTERNATIVE)
            }
            if (settings.writingType) {
                tmp.add(SessionType.WRITING)
            }
            sessionTypes = tmp
            session.sessionType = sessionTypes.random()


        }
    }

    @ExperimentalCoroutinesApi
    fun next(correct: Boolean): Boolean {
        val vocable = session.activeVocable
        vocable.level += if (correct) 1 else -1
        vocable.attempts += 1


        val nextVocableWrapper: VocableWrapper = getNextVocableWrapper(vocable) ?: return false

        val nextSessionType: SessionType = sessionTypes.random()
        session.activeVocable = nextVocableWrapper
        session.sessionType = nextSessionType
        return true
    }

    @ExperimentalCoroutinesApi
    private fun getNextVocableWrapper(vocable: VocableWrapper): VocableWrapper? {
        return with(session) {
            if (vocable.level >= (settings.maxRepetitions + 1))
                vocables = vocables.filterNot { it.value == vocable.value }
            if (vocables.isNotEmpty()) {
                vocables.weight(
                    keySelector = {
                        it.level
                    }, weightFunction = {
                        settings.maxRepetitions - it
                    }).random()
            } else
                null
        }
    }
}


