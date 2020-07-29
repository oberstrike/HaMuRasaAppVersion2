package de.hamurasa.data.profile

import de.hamurasa.util.domain.IBaseRepository
import io.objectbox.Box

interface ProfileRepository : IBaseRepository<Profile> {
    suspend fun findByName(name: String): Profile?
}

class ProfileRepositoryImpl(private val vocableBox: Box<Profile>) : ProfileRepository {

    override suspend fun delete(t: Profile): Boolean {
        return vocableBox.remove(t)
    }

    override suspend fun save(t: Profile) {
        vocableBox.put(t)
    }

    override suspend fun findByName(name: String): Profile? {
        return vocableBox.query().equal(Profile_.name, name).build().findFirst()
    }

    override suspend fun findAll(): List<Profile> {
        return vocableBox.query().build().find()
    }

    override suspend fun findById(id: Long): Profile? {

        return vocableBox.query().equal(Profile_.id, id).build().findFirst()
    }


}