package de.hamurasa.main

import android.accounts.AccountManager
import android.content.Context
import de.hamurasa.data.CommandLineRunner
import de.hamurasa.network.RetrofitServices
import de.hamurasa.util.AbstractViewModel
import de.hamurasa.util.SchedulerProvider
import de.hamurasa.vocable.model.LessonRepository
import de.hamurasa.vocable.model.VocableRepository
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.CompletableFuture
import kotlin.math.log

class MainViewModel(
    val context: Context,
    val provider: SchedulerProvider,
    val lessonRepository: LessonRepository,
    val commandLineRunner: CommandLineRunner
) : AbstractViewModel() {

    private val accountManager: AccountManager = AccountManager.get(context)

    val isLoggedIn: Observable<Boolean> = MainContext.isLoggedIn

    val lessons = lessonRepository.findAll()


    fun init() {
        commandLineRunner.init()
     //   logout()

        val isLoggedIn = accountManager.accounts.isNotEmpty()
        MainContext.isLoggedIn = Observable.just(isLoggedIn)
    }


    private fun logout() {
        if (accountManager.accounts.isNotEmpty())
            accountManager.removeAccountExplicitly(accountManager.accounts.first())
    }

    fun update() {
        val account = accountManager.accounts.first()
        val username = account.name
        val password = accountManager.getPassword(account)

        RetrofitServices.initVocableRetrofitService(username, password)
        RetrofitServices.initLessonRetrofitService(username, password)

        val response = CompletableFuture.supplyAsync {
            RetrofitServices.lessonRetrofitService.getLessons().execute()
        }

        val lessons = response.get().body()



        RetrofitServices.initLessonRetrofitService(username, accountManager.getPassword(account))

    }


}