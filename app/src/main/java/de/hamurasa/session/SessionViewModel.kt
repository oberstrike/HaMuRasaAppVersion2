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
import org.joda.time.DateTime

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

            listOfVocableWrapper = vocables.sortedBy { it.lastChanged }
                .take(settings.maxVocableCount)
                .onEach { it.lastChanged = DateTime.now() }
                .map { VocableWrapper(it) }

            activeVocable = listOfVocableWrapper.random()

            val types: MutableList<SessionType> = mutableListOf()
            if (settings.standardType) {
                types.add(SessionType.STANDARD)
            }
            if (settings.alternativeType) {
                types.add(SessionType.ALTERNATIVE)
            }
            if (settings.writingType) {
                types.add(SessionType.WRITING)
            }
            sessionTypes = types
            session.sessionType = sessionTypes.random()
        }
    }

    @ExperimentalCoroutinesApi
    fun next(correct: Boolean): Boolean {
        val activeVocableWrapper = session.activeVocable
        activeVocableWrapper.level += if (correct) 1 else -1
        activeVocableWrapper.attempts += 1

        val nextVocableWrapper: VocableWrapper =
            getNextVocableWrapper(activeVocableWrapper) ?: return false
        val nextSessionType: SessionType = sessionTypes.random()
        session.activeVocable = nextVocableWrapper
        session.sessionType = nextSessionType
        return true
    }

    @ExperimentalCoroutinesApi
    private fun getNextVocableWrapper(vocable: VocableWrapper): VocableWrapper? {
        return with(session) {
            if (vocable.level >= (settings.maxRepetitions + 1))
                listOfVocableWrapper = listOfVocableWrapper.filterNot { it.value == vocable.value }
            if (listOfVocableWrapper.isNotEmpty()) {
                listOfVocableWrapper.weight(
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


