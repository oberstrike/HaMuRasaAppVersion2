package de.hamurasa.util

import android.app.AlertDialog
import android.view.View
import androidx.fragment.app.FragmentActivity
import de.hamurasa.R

inline fun withDialog(
    activity: FragmentActivity,
    dialogLayout: Int,
    crossinline createView: (view: View) -> Unit,
    crossinline afterCreation: (AlertDialog.Builder) -> Unit
): AlertDialog.Builder {
    val builder = AlertDialog.Builder(activity)

    val inflater = activity.layoutInflater

    val view = inflater.inflate(dialogLayout, null)

    createView.invoke(view)

    builder.setView(view)

    afterCreation.invoke(builder)

    return builder
}

inline fun withDialog(
    activity: FragmentActivity,
    dialogLayout: Int,
    crossinline createView: (view: View) -> Unit
): AlertDialog.Builder {
    return withDialog(
        activity,
        dialogLayout = dialogLayout,
        createView = createView,
        afterCreation = {
        })
}