package de.hamurasa.model.lesson

import de.hamurasa.data.ObjectBox
import io.objectbox.Box
import io.objectbox.query.Query
import io.objectbox.reactive.DataSubscription
import io.objectbox.rx.RxQuery
import io.reactivex.Observable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.rx2.asFlow
import org.joda.time.DateTime

interface LessonRepository {
    fun findById(id: Long): Lesson?

    fun findAll(): List<Lesson>

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

    @ExperimentalCoroutinesApi
    override fun findAll(): List<Lesson> {
        return lessonBox.query().build().find()
    }


    override fun findById(id: Long): Lesson? {
        return (lessonBox.query().equal(Lesson_.id, id).build()).findFirst()

    }

    override fun findByServerId(serverId: Long): Lesson? =
        lessonBox.query().equal(Lesson_.id, serverId).build().findFirst()


    override fun save(lesson: Lesson) {
        lesson.lastChanged = DateTime.now()
        lessonBox.put(lesson)
    }

    override fun deleteAll() {
        lessonBox.removeAll()
    }


}
