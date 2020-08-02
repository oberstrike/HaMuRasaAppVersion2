package de.hamurasa.data.vocable

import org.jeasy.random.EasyRandom

fun getRandomVocable(): Vocable {
    val randomVocable = EasyRandom().nextObject(RandomVocable::class.java)
    return Vocable(
        value = randomVocable.value!!,
        type = randomVocable.type!!,
        translation = randomVocable.translation!!,
        language = randomVocable.language!!
    )
}

data class RandomVocable(
    var value: String? = null,
    var type: VocableType? = null,
    var translation: List<String>? = null,
    var language: Language? = null
)