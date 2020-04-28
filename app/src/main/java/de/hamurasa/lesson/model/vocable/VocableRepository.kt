package de.hamurasa.lesson.model.vocable

import de.hamurasa.data.ObjectBox
import io.objectbox.Box
import io.objectbox.rx.RxQuery
import io.reactivex.Observable

interface VocableRepository {

    fun findById(id: Long): Vocable?

    fun findByServerId(id: Long): Vocable?

    fun findAll(): Observable<List<Vocable>>

    fun save(vocable: Vocable): Long

    fun delete(vocable: Vocable)

    fun size(): Int

    fun deleteAll()

}

class VocableRepositoryImpl :
    VocableRepository {
    private var vocableBox: Box<Vocable> = ObjectBox.boxStore.boxFor(
        Vocable::class.java
    )

    override fun findById(id: Long): Vocable? {
        return vocableBox.query().equal(Vocable_.id, id).build().findFirst()
    }

    override fun findByServerId(id: Long): Vocable? {
        return vocableBox.query().equal(Vocable_.serverId, id).build().findFirst()
    }

    override fun save(vocable: Vocable): Long {
        return vocableBox.put(vocable)
}

    override fun delete(vocable: Vocable) {
        vocableBox.remove(vocable)
    }

    override fun findAll(): Observable<List<Vocable>> {
        return RxQuery.observable(vocableBox.query().build())
    }

    override fun size(): Int {
        return vocableBox.query().build().find().size
    }

    override fun deleteAll() {
        vocableBox.removeAll()
    }


}