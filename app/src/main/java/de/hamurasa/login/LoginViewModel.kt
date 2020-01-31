package de.hamurasa.login

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Context
import de.hamurasa.network.RetrofitServices
import de.hamurasa.util.AbstractViewModel
import java.util.concurrent.CompletableFuture

class LoginViewModel(val context: Context) : AbstractViewModel() {


    fun login(username: String, password: String) : Boolean{
        RetrofitServices.init(username, password)

        val response = CompletableFuture.supplyAsync{
            RetrofitServices.userRetrofitService.login().execute()
        }.get()

        val mAccountManager = AccountManager.get(context)

        val account = Account(username, "de.hamurasa")

   //     mAccountManager.addAccountExplicitly(account, password, null)
    //    mAccountManager.setAuthToken(account, "authTokenType", "")


        if(mAccountManager.accounts.find { it.name == username } != null){
            mAccountManager.setPassword(account, password)
        }else{
            mAccountManager.addAccountExplicitly(account, password, null)
            mAccountManager.setAuthToken(account, "authTokenType", "")
        }

        if(response.isSuccessful){
            return true
        }
        return true

    }

}