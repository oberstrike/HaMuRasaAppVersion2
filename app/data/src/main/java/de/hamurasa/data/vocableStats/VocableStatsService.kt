package de.hamurasa.data.vocableStats

import de.hamurasa.util.domain.IBaseService

interface VocableStatsService :
    IBaseService<VocableStats> {
    suspend fun findByVocable(vocable: de.hamurasa.data.vocable.Vocable): VocableStats?

}

class VocableStatsServiceImpl(private val vocableStatsRepository: VocableStatsRepository) :
    VocableStatsService {

    override suspend fun save(t: VocableStats) {
        vocableStatsRepository.save(t)
    }

    override suspend fun findByVocable(vocable: de.hamurasa.data.vocable.Vocable): VocableStats? {
        return vocableStatsRepository.findByVocable(vocable)
    }

    override suspend fun findById(id: Long): VocableStats? {
        return vocableStatsRepository.findById(id)
    }

    override suspend fun findAll(): List<VocableStats> {
        return vocableStatsRepository.findAll()
    }

    override suspend fun delete(t: VocableStats) {
        vocableStatsRepository.delete(t)
    }

}