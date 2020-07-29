package de.hamurasa.data.vocableStats

import de.hamurasa.util.domain.IBaseRepository
import io.objectbox.Box

interface VocableStatsRepository :
    IBaseRepository<VocableStats> {
    suspend fun findByVocable(vocable: de.hamurasa.data.vocable.Vocable): VocableStats?
}

class VocableStatsRepositoryImpl(private var vocableStatsBox: Box<VocableStats>) :
    VocableStatsRepository {


    override suspend fun findByVocable(vocable: de.hamurasa.data.vocable.Vocable): VocableStats? {
        return vocableStatsBox.query().equal(VocableStats_.vocableId, vocable.id).build().findFirst()
    }

    override suspend fun save(t: VocableStats) {
        vocableStatsBox.put(t)
    }

    override suspend fun findById(id: Long): VocableStats? {
        return vocableStatsBox.query().equal(VocableStats_.id, id).build().findFirst()
    }

    override suspend fun findAll(): List<VocableStats> {
        return vocableStatsBox.query().build().find()
    }

    override suspend fun delete(t: VocableStats): Boolean {
        return vocableStatsBox.remove(t)
    }

}