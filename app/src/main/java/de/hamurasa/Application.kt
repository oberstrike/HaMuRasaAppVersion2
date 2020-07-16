package de.hamurasa

import android.app.Application
import de.hamurasa.data.ObjectBox
import de.hamurasa.data.appModules
import de.hamurasa.data.modelModules
import de.hamurasa.main.mainModule
import de.hamurasa.session.sessionModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class Application : Application() {


    override fun onCreate() {
        super.onCreate()

        ObjectBox.init(applicationContext)

        startKoin {
            // Koin Android logger
            androidLogger()
            //inject Android context
            androidContext(this@Application)
            // use modules
            modules(appModules.plus(modelModules).plus(sessionModules).plus(mainModule))
        }

    }
}
