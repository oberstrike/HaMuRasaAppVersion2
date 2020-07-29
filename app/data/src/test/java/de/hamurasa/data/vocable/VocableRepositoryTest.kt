package de.hamurasa.data.vocable

import de.hamurasa.data.AbstractObjectBoxTest
import de.hamurasa.data.vocableStats.VocableStats
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class VocableRepositoryTest : AbstractObjectBoxTest<Vocable>() {

    override fun useClass() = Vocable::class.java

    private lateinit var vocableRepository: VocableRepository

    @Before
    override fun setUp() {
        super.setUp()
        withBox<VocableStats> {
            vocableRepository = VocableRepositoryImpl(box, it)
        }

    }

    @Test
    fun startUpTest() = runBlocking {
        val all = vocableRepository.findAll()
        println(all)
        Assert.assertEquals(0, all.size)
    }

    @Test
    fun x() = runBlocking {
        val vocable = getRandomVocable()

        //Check if object has the same id
        val id = vocableRepository.save(vocable)
        Assert.assertEquals(vocable.id, id)
    }

}