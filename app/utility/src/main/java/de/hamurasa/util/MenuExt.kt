package de.hamurasa.util

import android.view.Menu
import android.view.MenuItem

fun Menu.foreach(block: (MenuItem) -> Unit) {
    for (i in 0 until size()) {
        val item = this.getItem(i)
        block.invoke(item)
    }
}

fun Menu.findFirst(predicate: (MenuItem) -> Boolean): MenuItem? {
    for (i in 0 until size()) {
        val item = getItem(i)
        if (predicate.invoke(item)) {
            return item
        }
    }
    return null
}