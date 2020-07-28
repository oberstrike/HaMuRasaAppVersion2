package de.hamurasa.data

import de.hamurasa.data.lesson.Lesson
import de.hamurasa.data.vocable.Vocable
import de.hamurasa.data.vocable.easyRandom
import de.hamurasa.data.vocable.getRandomVocable
import io.objectbox.Box
import io.objectbox.BoxStore
import io.objectbox.DebugFlags
import org.junit.After
import org.junit.Before
import java.io.File


abstract class AbstractObjectBoxTest<T> {

    protected lateinit var store: BoxStore

    protected lateinit var box: Box<T>

    abstract fun useClass(): Class<T>


    @Before
    @Throws(Exception::class)
    open fun setUp() {
        // delete database files before each test to start with a clean database
        BoxStore.deleteAllFiles(TEST_DIRECTORY)
        store =
            MyObjectBox.builder() // add directory flag to change where ObjectBox puts its database files
                .directory(TEST_DIRECTORY) // optional: add debug flags for more detailed ObjectBox log output
                .debugFlags(DebugFlags.LOG_QUERIES or DebugFlags.LOG_QUERY_PARAMETERS)
                .build()
        box = store.boxFor(useClass())
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        box.removeAll()
        box.closeThreadResources()
        store.close()
        BoxStore.deleteAllFiles(TEST_DIRECTORY)
    }


    fun withRandomVocables(count: Int = 5, blocK: (List<Vocable>) -> Unit) {
        val box = store.boxFor(Vocable::class.java)
        val list = (0 until count).map { getRandomVocable() }

        for (vocable in list) {
            box.put(vocable)
        }

        blocK.invoke(list)

        for (vocable in list) {
            box.remove(vocable)
        }
    }

    fun withRandomLesson(blocK: (Lesson) -> Unit) {
        val lesson = Lesson()
        val box = store.boxFor(Lesson::class.java)
        box.put(lesson)

        blocK.invoke(lesson)

        box.remove(lesson)
    }


    companion object {
        protected val TEST_DIRECTORY: File = File("objectbox-example/test-db")
    }
}