package de.hamurasa.main

import androidx.fragment.app.Fragment
import io.reactivex.Observable

object MainContext {
    lateinit var isLoggedIn: Observable<Boolean>
    
    lateinit var activeFragment: Fragment

}
