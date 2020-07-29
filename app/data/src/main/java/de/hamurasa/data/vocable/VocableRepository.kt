package de.hamurasa.data.vocable

import de.hamurasa.data.util.ObjectBox
import de.hamurasa.data.vocableStats.VocableStats
import de.hamurasa.data.vocableStats.VocableStats_

import io.objectbox.Box

interface VocableRepository {

    suspend fun findById(id: Long): Vocable?

    suspend fun findByValue(value: String): List<Vocable>

    suspend fun findStatsByVocable(vocable: Vocable): VocableStats?

    suspend fun findAll(): List<Vocable>

    suspend fun save(vocable: Vocable): Long

    suspend fun delete(vocable: Vocable)

    suspend fun size(): Int

    suspend fun deleteAll()

}

class VocableRepositoryImpl(
    private val vocableBox: Box<Vocable>,
    private val vocableStatsBox: Box<VocableStats>
) :
    VocableRepository {

    override suspend fun findStatsByVocable(vocable: Vocable): VocableStats? {
        return vocableStatsBox.query().equal(VocableStats_.vocableId, vocable.id).build()
            .findFirst()
    }

    override suspend fun findById(id: Long): Vocable? {
        return vocableBox.query().equal(Vocable_.id, id).build().findFirst()
    }

    override suspend fun save(vocable: Vocable): Long {
        val stats = findStatsByVocable(vocable)
        if (stats == null) {
            val newOne = VocableStats()
            newOne.vocable.target = vocable
        }
        return vocableBox.put(vocable)
    }

    override suspend fun delete(vocable: Vocable) {
        vocableBox.remove(vocable)
    }

    override suspend fun findAll(): List<Vocable> =
        vocableBox.query().build().find()


    override suspend fun size(): Int {
        return vocableBox.query().build().find().size
    }

    override suspend fun deleteAll() {
        vocableBox.removeAll()
    }

    override suspend fun findByValue(value: String): List<Vocable> =
        vocableBox.query().equal(Vocable_.value, value).build().find()
}