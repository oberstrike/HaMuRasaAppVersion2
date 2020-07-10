package de.hamurasa.model.vocable


import kotlinx.coroutines.flow.Flow
import org.joda.time.DateTime

interface VocableService {
    fun findById(id: Long): Vocable?

    fun save(vocable: Vocable)

    fun convertToDTO(vocable: Vocable): VocableDTO

    fun save(vocableDTO: VocableDTO): Vocable?

    fun update(vocable: Vocable): Long

    fun deleteAll()

    fun findAll(): Flow<List<Vocable>>

    fun findByName(name: String): List<Vocable>

    fun delete(vocable: Vocable)

}

class VocableServiceImpl(private val vocableRepository: VocableRepository) : VocableService {

    override fun delete(vocable: Vocable) {
        vocableRepository.delete(vocable)
    }

    override fun findAll(): Flow<List<Vocable>> {
        return vocableRepository.findAll()
    }

    override fun save(vocableDTO: VocableDTO): Vocable? {
        val vocable = vocableRepository.findById(vocableDTO.id)
        if (vocable != null)
            return vocable

        val newVocable = Vocable(
            id = 0,
            language = vocableDTO.language,
            lastChanged = DateTime.now(),
            translation = vocableDTO.translations,
            value = vocableDTO.value,
            type = vocableDTO.type
        )

        update(newVocable)
        return newVocable
    }

    override fun deleteAll() {
        vocableRepository.deleteAll()
    }


    override fun update(vocable: Vocable): Long {
        return vocableRepository.save(vocable)

    }


    override fun findById(id: Long): Vocable? {
        return vocableRepository.findById(id)
    }

    override fun save(vocable: Vocable) {
        vocableRepository.save(vocable)
    }

    override fun convertToDTO(vocable: Vocable): VocableDTO {
        return VocableDTO.create(
            vocable.id,
            vocable.value,
            vocable.type,
            vocable.translation,
            vocable.language
        )
    }

    override fun findByName(name: String): List<Vocable> {
        return vocableRepository.findByName(name)
    }


}