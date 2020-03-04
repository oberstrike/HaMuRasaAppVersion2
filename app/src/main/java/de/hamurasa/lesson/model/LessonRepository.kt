package de.hamurasa.lesson.model

import de.hamurasa.data.ObjectBox
import io.objectbox.Box
import io.objectbox.rx.RxQuery
import io.reactivex.Observable

interface LessonRepository {
    fun findById(id: Long): Lesson

    fun findByServerId(id: Long): Lesson?

    fun findAll(): Observable<List<Lesson>>

    fun save(lesson: Lesson)

    fun delete(lesson: Lesson)

    fun size(): Int

    fun deleteAll()
}

class LessonRepositoryImpl : LessonRepository {
    private var activenessBox: Box<Lesson> = ObjectBox.boxStore.boxFor(
        Lesson::class.java
    )


    override fun size(): Int {
        return activenessBox.query().build().find().size
    }

    override fun delete(lesson: Lesson) {
        activenessBox.remove(lesson)
    }

    override fun findAll(): Observable<List<Lesson>> {
        return RxQuery.observable(activenessBox.query().build())
    }

    override fun findById(id: Long): Lesson {
        return RxQuery.observable(activenessBox.query().equal(Lesson_.id, id).build())
            .blockingFirst().first()
    }

    override fun findByServerId(id: Long): Lesson? {
        return activenessBox.query().equal(Lesson_.serverId, id).build().findFirst()
    }

    override fun save(lesson: Lesson) {
        activenessBox.put(lesson)
    }

    override fun deleteAll() {
        activenessBox.removeAll()
    }


}