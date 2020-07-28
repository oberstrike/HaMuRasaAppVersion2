package de.hamurasa.data.profile

import de.hamurasa.util.domain.IBaseService

interface ProfileService : IBaseService<Profile> {
    suspend fun findByName(name: String): Profile?
}


class ProfileServiceImpl(private val profileRepository: ProfileRepository) :
    ProfileService {

    override suspend fun save(t: Profile) {
        profileRepository.save(t)
    }

    override suspend fun delete(t: Profile) {
        profileRepository.delete(t)
    }

    override suspend fun findAll(): List<Profile> {
        return profileRepository.findAll()
    }

    override suspend fun findById(id: Long): Profile? {
        return profileRepository.findById(id)
    }

    override suspend fun findByName(name: String): Profile? {
        return profileRepository.findByName(name)
    }
}