package de.hamurasa.main.fragments.home.profile

import android.os.Bundle
import android.view.View
import com.mitteloupe.solid.fragment.handler.LifecycleHandler
import de.hamurasa.R
import de.hamurasa.main.MainContext
import de.hamurasa.main.fragments.home.HomeViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.angmarch.views.NiceSpinner

class ProfileHandler(
    private val homeViewModel: HomeViewModel
) : LifecycleHandler {

    private lateinit var profileSpinner: NiceSpinner

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val profiles = homeViewModel.getAllProfiles().map { it.name }
        profileSpinner = view.findViewById(R.id.profileSpinner)

        profileSpinner.apply {
            attachDataSource(profiles)

            setOnSpinnerItemSelectedListener { parent, _, position, _ ->
                val item = parent.getItemAtPosition(position) as String
                homeViewModel.launchJob {
                    val profile = homeViewModel.findProfileByName(item) ?: return@launchJob
                    MainContext.HomeContext.change(profile)
                }
            }
        }
    }
}