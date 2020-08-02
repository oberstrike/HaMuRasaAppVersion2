package de.hamurasa

import de.hamurasa.data.appModules
import de.hamurasa.util.AbstractApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin


open class Application : AbstractApplication() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(applicationContext)
            modules(appModules)
        }
    }

}
