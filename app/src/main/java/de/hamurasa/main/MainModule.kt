package de.hamurasa.main

import de.hamurasa.main.fragments.dictionary.DictionaryFragment
import de.hamurasa.main.fragments.edit.EditFragment
import de.hamurasa.main.fragments.edit.EditVocableDialog
import de.hamurasa.main.fragments.edit.NewVocableDialog
import de.hamurasa.main.fragments.home.HomeFragment
import org.koin.dsl.module

val mainModule = module {

    factory { NewVocableDialog(get()) }

    factory { params -> EditVocableDialog(params[0]) }

    factory { params -> HomeFragment(params[0]) }

    factory { DictionaryFragment() }

    factory { EditFragment() }
}