package de.hamurasa.session

import de.hamurasa.session.models.VocableWrapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf


object SessionContext {
    var running: Flow<Boolean> = flowOf(false)
    lateinit var vocables: List<VocableWrapper>
    lateinit var sessionType: SessionType
    lateinit var activeVocable: VocableWrapper
    lateinit var sessionTypes: List<SessionType>
}


enum class SessionType {
    STANDARD, ALTERNATIVE, WRITING
}
