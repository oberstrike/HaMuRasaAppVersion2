package de.hamurasa.main

import de.hamurasa.data.lesson.Lesson
import de.hamurasa.data.profile.Profile
import de.hamurasa.data.vocable.Vocable
import de.hamurasa.util.FlowContainerHandler
import de.hamurasa.util.FlowHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi

object MainContext {

    const val name: String = "Spanisch"

    @ExperimentalCoroutinesApi
    object HomeContext : FlowContainerHandler<Profile?>(null)

    @ExperimentalCoroutinesApi
    object EditContext : FlowContainerHandler<Lesson?>(null)

    @ExperimentalCoroutinesApi
    object DictionaryContext : FlowHandler<List<Vocable>>(listOf())
}


