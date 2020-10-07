package de.hamurasa.main

import de.hamurasa.main.fragments.dialogs.NewProfileDialog
import de.hamurasa.main.fragments.dictionary.DictionaryFragment
import de.hamurasa.main.fragments.edit.EditFragment
import de.hamurasa.main.fragments.edit.EditVocableDialog
import de.hamurasa.main.fragments.edit.NewVocableDialog
import de.hamurasa.main.fragments.home.HomeFragment
import de.hamurasa.session.TimerHandler
import org.koin.dsl.module

val mainModule = module {

    factory { NewVocableDialog(get(), get()) }

    factory { NewProfileDialog(get()) }

    factory { params -> EditVocableDialog(params[0]) }

    factory { _ -> HomeFragment() }

    factory { DictionaryFragment() }

    factory { EditFragment() }

    factory { TimerHandler(get()) }
}