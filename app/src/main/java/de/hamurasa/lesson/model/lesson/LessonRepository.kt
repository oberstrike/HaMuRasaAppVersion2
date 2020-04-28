package de.hamurasa.lesson.model.lesson

import de.hamurasa.data.ObjectBox
import io.objectbox.Box
import io.objectbox.rx.RxQuery
import io.reactivex.Observable

interface LessonRepository {
    fun findById(id: Long): Lesson?

    fun findAll(): Observable<List<Lesson>>

    fun save(lesson: Lesson)

    fun delete(lesson: Lesson)

    fun size(): Int

    fun deleteAll()

    fun findByServerId(serverId: Long): Lesson?
}

class LessonRepositoryImpl : LessonRepository {
    private var lessonBox: Box<Lesson> = ObjectBox.boxStore.boxFor(
        Lesson::class.java
    )


    override fun size(): Int {
        return lessonBox.query().build().find().size
    }

    override fun delete(lesson: Lesson) {
        lessonBox.remove(lesson)
    }

    override fun findAll(): Observable<List<Lesson>> {
        return RxQuery.observable(lessonBox.query().build())
    }

    override fun findById(id: Long): Lesson? {
        return RxQuery.observable(lessonBox.query().equal(Lesson_.id, id).build())
            .blockingFirst().firstOrNull()
    }

    override fun findByServerId(serverId: Long): Lesson? =
        lessonBox.query().equal(Lesson_.serverId, serverId).build().findFirst()


    override fun save(lesson: Lesson) {
        lessonBox.put(lesson)
    }

    override fun deleteAll() {
        lessonBox.removeAll()
    }


}