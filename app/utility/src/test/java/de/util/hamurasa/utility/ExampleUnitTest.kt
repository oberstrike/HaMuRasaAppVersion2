package de.util.hamurasa.utility

import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun test_observer_with_interface() {
        val subject = PublishSubject.create<String>()
        subject.onNext("alt 2")

        subject.subscribe {
            println("Neu: $it")
        }

        subject.onNext("alt")
        subject.onNext("alt 2")


    }


}
