package de.hamurasa.lesson.model.vocable

import io.reactivex.Observable

interface VocableService {
    fun findById(id: Long): Vocable?

    fun save(vocable: Vocable)

    fun convertToDTO(vocable: Vocable): VocableDTO

    fun save(vocableDTO: VocableDTO, offline: Boolean): Vocable

    fun save(vocableDTO: VocableDTO): Long

    fun update(vocable: Vocable): Long

    fun deleteAll()

    fun findAll(): Observable<List<Vocable>>

}

class VocableServiceImpl(private val vocableRepository: VocableRepository) : VocableService {

    override fun findAll(): Observable<List<Vocable>> {
        return vocableRepository.findAll()
    }

    override fun save(vocableDTO: VocableDTO): Long {
        val vocable = vocableRepository.findById(vocableDTO.id)

        if (vocable != null) {
            with(vocable) {
                value = vocableDTO.value
                translation = vocableDTO.translations
                isOffline = true
                language = vocableDTO.language
                type = vocableDTO.type
                vocableRepository.save(this)
            }

            return vocable.id
        }
        return 0
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
            vocable.serverId,
            vocable.value,
            vocable.type,
            vocable.translation,
            vocable.language
        )
    }

    override fun save(
        vocableDTO: VocableDTO,
        offline: Boolean
    ): Vocable {
        if (!offline) {
            val oldVocable = vocableRepository.findByServerId(vocableDTO.id)
            if (oldVocable != null) {
                return oldVocable
            }
        }

        val newVocable = Vocable(
            0,
            vocableDTO.id,
            offline,
            vocableDTO.value,
            vocableDTO.type,
            vocableDTO.translations,
            vocableDTO.language
        )
        vocableRepository.save(newVocable)
        return newVocable
    }


}