package de.hamurasa.data.vocableStats

import de.hamurasa.util.domain.IBaseRepository
import io.objectbox.Box

interface VocableStatsRepository :
    IBaseRepository<VocableStats> {
    suspend fun findByVocable(vocable: de.hamurasa.data.vocable.Vocable): VocableStats?
}

class VocableStatsRepositoryImpl :
    VocableStatsRepository {

    private var vocableBox: Box<VocableStats> = de.hamurasa.data.util.ObjectBox.boxStore.boxFor(
        VocableStats::class.java
    )

    override suspend fun findByVocable(vocable: de.hamurasa.data.vocable.Vocable): VocableStats? {
        return vocableBox.query().equal(VocableStats_.vocableId, vocable.id).build().findFirst()
    }

    override suspend fun save(t: VocableStats) {
        vocableBox.put(t)
    }

    override suspend fun findById(id: Long): VocableStats? {
        return vocableBox.query().equal(VocableStats_.id, id).build().findFirst()
    }

    override suspend fun findAll(): List<VocableStats> {
        return vocableBox.query().build().find()
    }

    override suspend fun delete(t: VocableStats): Boolean {
        return vocableBox.remove(t)
    }

}