package de.hamurasa.session

import de.hamurasa.session.fragments.AlternativeFragment
import de.hamurasa.session.fragments.StandardFragment
import de.hamurasa.session.fragments.WritingFragment
import de.hamurasa.session.models.SessionEvent
import org.koin.dsl.module

val sessionModules = module {
    factory { params -> StandardFragment(params[0]) }

    factory { params -> AlternativeFragment(params[0]) }

    factory { params ->
        WritingFragment(
            params[0],
            params[1]
        )
    }

    single { SessionEvent() }
}