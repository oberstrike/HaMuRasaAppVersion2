package de.hamurasa.lesson.model.vocable

import de.hamurasa.data.ObjectBox
import io.objectbox.Box
import io.objectbox.rx.RxQuery
import io.reactivex.Observable

interface VocableRepository {

    fun findById(id: Long): Vocable?

    fun findByServerId(id: Long): Vocable?

    fun findAll(): Observable<List<Vocable>>

    fun save(vocable: Vocable)

    fun delete(vocable: Vocable)

    fun size(): Int

}

class VocableRepositoryImpl :
    VocableRepository {
    private var activenessBox: Box<Vocable> = ObjectBox.boxStore.boxFor(
        Vocable::class.java
    )

    override fun findById(id: Long): Vocable? {
        return activenessBox.query().equal(Vocable_.id, id).build().findFirst()
    }

    override fun findByServerId(id: Long): Vocable? {
        return activenessBox.query().equal(Vocable_.serverId, id).build().findFirst()
    }

    override fun save(vocable: Vocable) {
        activenessBox.put(vocable)
    }

    override fun delete(vocable: Vocable) {
        activenessBox.remove(vocable)
    }

    override fun findAll(): Observable<List<Vocable>> {
        return RxQuery.observable(activenessBox.query().build())
    }

    override fun size(): Int {
        return activenessBox.query().build().find().size
    }


}