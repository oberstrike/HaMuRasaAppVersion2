package de.hamurasa.util

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


interface  SchedulerProvider {
    fun  io(): Scheduler
    fun  ui(): Scheduler
    fun computation(): Scheduler

}

class SchedulerProviderImpl : SchedulerProvider {

    override fun computation() = Schedulers.computation()

    override fun io() = Schedulers.io()

    override fun ui(): Scheduler = AndroidSchedulers.mainThread()
}