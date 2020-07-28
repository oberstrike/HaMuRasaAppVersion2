package de.hamurasa.main.fragments.dialogs

import android.os.Bundle
import android.util.Log
import android.view.View
import de.hamurasa.R
import de.hamurasa.main.MainViewModel
import de.hamurasa.data.profile.Profile
import de.hamurasa.util.BaseDialog
import de.hamurasa.util.widgets.bind
import kotlinx.android.synthetic.main.dialog_new_profile.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import java.lang.Exception

class NewProfileDialog(private val profile: Profile) : BaseDialog(), View.OnClickListener {

    private val myViewModel: MainViewModel by sharedViewModel()

    override fun getLayoutId() = R.layout.dialog_new_profile

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profile.name = "Profile"

        profile_name_editText.bind(profile::name)
        profile_self_controlled.bind(profile::selfControlled)
        profile_cancel_button.setOnClickListener(this)
        profile_ok_button.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        if (v == null)
            return

        when (v.id) {
            R.id.profile_cancel_button -> cancel()
            R.id.profile_ok_button -> computeOk()
        }
    }


    private fun cancel() {
        dismiss()
    }

    private fun computeOk() {
        myViewModel.launchJob {
            try {
                myViewModel.save(profile)
            } catch (exception: Exception) {
                Log.e("Error", exception.message ?: "Fehler bei der Erstellung des Profils")
            }
        }
    }


}