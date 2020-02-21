package de.hamurasa

import io.reactivex.subjects.BehaviorSubject
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
    fun behaviour_Test() {
        var behaviorSubject = BehaviorSubject.create<Int>()

        behaviorSubject.onNext(9)
        behaviorSubject.onNext(10)

        behaviorSubject.take(1).subscribe {
            println("First $it")
        }

        behaviorSubject.subscribe{
            println(it)
        }

        behaviorSubject.onNext(11)





    }

}
