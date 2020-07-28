package de.hamurasa
/**
import de.hamurasa.model.MyObjectBox
import io.objectbox.BoxStore
import io.objectbox.DebugFlags
import org.junit.After
import org.junit.Before
import java.io.File


abstract class AbstractObjectBoxTest {
    protected var store: BoxStore? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        // delete database files before each test to start with a clean database
        BoxStore.deleteAllFiles(TEST_DIRECTORY)
        store =
            MyObjectBox.builder() // add directory flag to change where ObjectBox puts its database files
                .directory(TEST_DIRECTORY) // optional: add debug flags for more detailed ObjectBox log output
                .debugFlags(DebugFlags.LOG_QUERIES or DebugFlags.LOG_QUERY_PARAMETERS)
                .build()
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        if (store != null) {
            store!!.close()
            store = null
        }
        BoxStore.deleteAllFiles(TEST_DIRECTORY)
    }

    companion object {
        protected val TEST_DIRECTORY: File = File("objectbox-example/test-db")
    }
}**/