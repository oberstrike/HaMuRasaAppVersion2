package de.hamurasa.util.domain

interface IBaseRepository<T> {

    suspend fun findById(id: Long): T?

    suspend fun save(t: T)

    suspend fun delete(t: T): Boolean

    suspend fun findAll(): List<T>
}


interface IBaseService<T> {

    suspend fun findById(id: Long): T?

    suspend fun save(t: T)

    suspend fun delete(t: T)

    suspend fun findAll(): List<T>
}