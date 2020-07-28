package de.hamurasa.data.vocable


interface VocableService {
    suspend fun findById(id: Long): Vocable?

    suspend fun save(vocable: Vocable)

    suspend fun deleteAll()

    suspend fun findAll(): List<Vocable>

    suspend fun findByName(name: String): List<Vocable>

    suspend fun delete(vocable: Vocable)

}

class VocableServiceImpl(private val vocableRepository: VocableRepository) : VocableService {

    override suspend fun findById(id: Long): Vocable? {
        return vocableRepository.findById(id)
    }

    override suspend fun save(vocable: Vocable) {
        vocableRepository.save(vocable)
    }

    override suspend fun deleteAll() {
        vocableRepository.deleteAll()
    }

    override suspend fun findAll(): List<Vocable> {
        return vocableRepository.findAll()
    }

    override suspend fun findByName(name: String): List<Vocable> {
        return vocableRepository.findByName(name)
    }

    override suspend fun delete(vocable: Vocable) {
        return vocableRepository.delete(vocable)
    }

}