package de.hamurasa.model.vocable

import de.hamurasa.data.ObjectBox
import io.objectbox.Box
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface VocableRepository {

    fun findById(id: Long): Vocable?

    fun findByName(value: String): List<Vocable>

    fun findAll(): List<Vocable>

    fun save(vocable: Vocable): Long

    fun delete(vocable: Vocable)

    fun size(): Int

    fun deleteAll()

}

class VocableRepositoryImpl :
    VocableRepository {
    private var vocableBox: Box<Vocable> = ObjectBox.boxStore.boxFor(
        Vocable::class.java
    )

    override fun findById(id: Long): Vocable? {
        return vocableBox.query().equal(Vocable_.id, id).build().findFirst()
    }

    override fun save(vocable: Vocable): Long {
        return vocableBox.put(vocable)
    }

    override fun delete(vocable: Vocable) {
        vocableBox.remove(vocable)
    }

    override fun findAll(): List<Vocable> = vocableBox.query().build().find()


    override fun size(): Int {
        return vocableBox.query().build().find().size
    }

    override fun deleteAll() {
        vocableBox.removeAll()
    }

    override fun findByName(value: String): List<Vocable> =
        vocableBox.query().equal(Vocable_.value, value).build().find()
}