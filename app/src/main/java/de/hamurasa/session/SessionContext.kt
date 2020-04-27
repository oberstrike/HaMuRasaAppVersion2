package de.hamurasa.session

import de.hamurasa.session.fragments.VocableFragment
import de.hamurasa.lesson.model.vocable.Vocable
import de.hamurasa.session.models.VocableWrapper
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

object SessionContext {
    var running: PublishSubject<Boolean> = PublishSubject.create()
    lateinit var activeVocable: Observable<VocableWrapper>
    lateinit var vocables: List<VocableWrapper>
    lateinit var sessionType: SessionType
}


enum class SessionType {
    STANDARD, ALTERNATIVE
}