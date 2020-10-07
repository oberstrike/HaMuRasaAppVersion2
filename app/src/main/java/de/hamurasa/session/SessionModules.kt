package de.hamurasa.session

import de.hamurasa.session.fragments.AlternativeFragmentAbstract
import de.hamurasa.session.fragments.StandardFragmentAbstract
import de.hamurasa.session.fragments.WritingFragment
import de.hamurasa.session.models.SessionEvent
import org.koin.dsl.module

val sessionModules = module {
    factory { params -> StandardFragmentAbstract(params[0]) }

    factory { params -> AlternativeFragmentAbstract(params[0]) }

    factory { params ->
        WritingFragment(
            params[0],
            params[1]
        )
    }

    single { SessionEvent() }
}