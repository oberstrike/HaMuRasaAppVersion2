package de.hamurasa.login

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.hamurasa.R
import org.koin.android.viewmodel.ext.android.sharedViewModel

/**
 * A placeholder fragment containing a simple view.
 */
class LoginActivityFragment : Fragment() {


    private val myViewModel: LoginViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

}
