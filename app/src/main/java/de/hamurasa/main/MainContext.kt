package de.hamurasa.main

import io.reactivex.Observable

object MainContext {
    var isLoggedIn: Observable<Boolean> = Observable.just(false)
}