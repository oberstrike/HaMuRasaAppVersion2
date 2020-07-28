package de.hamurasa.data.lesson

import de.hamurasa.data.AbstractObjectBoxTest
import org.junit.Assert
import org.junit.Test

class LessonBoxTest : AbstractObjectBoxTest<Lesson>() {


    override fun useClass(): Class<Lesson> = Lesson::class.java

    @Test
    fun checkPersistTest() {
        //Create a new lesson
        val lesson = Lesson()
        //Save lesson in the database
        box.put(lesson)
        //Get all lessons in box
        val size = box.query().build().count()
        Assert.assertEquals(1, size)

        //Remove lesson and check if it is removed
        box.remove(lesson)
        Assert.assertNotEquals(1, box.query().equal(Lesson_.id, lesson.id).build().count())
    }

    @Test
    fun checkVocableRelationTest() {
        withRandomVocables { vocables ->
            withRandomLesson { lesson ->
                lesson.words.addAll(vocables)
                Assert.assertNotEquals(0, lesson.words.size)
            }
        }
    }
}