package de.hamurasa.lesson.model.vocable

interface VocableService {
    fun findById(id: Long): Vocable?

    fun save(vocable: Vocable)

    fun convertToDTO(vocable: Vocable): VocableDTO

    fun save(vocableDTO: VocableDTO, offline: Boolean): Vocable

    fun update(vocableDTO: VocableDTO): Boolean

}

class VocableServiceImpl(private val vocableRepository: VocableRepository) : VocableService {

    override fun update(vocableDTO: VocableDTO): Boolean {
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

            return true
        }
        return false
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