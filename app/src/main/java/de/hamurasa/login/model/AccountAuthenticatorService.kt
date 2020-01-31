package de.hamurasa.login.model

import android.R.attr
import android.accounts.*
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import de.hamurasa.login.LoginActivity


/*
 * Authenticator service that returns a subclass of AbstractAccountAuthenticator in onBind()
 */
class AccountAuthenticatorService : Service() {
    override fun onBind(intent: Intent): IBinder? {
        var ret: IBinder? = null
        if (intent.action == AccountManager.ACTION_AUTHENTICATOR_INTENT) {
            ret = authenticator!!.iBinder
        }
        return ret
    }


    private val authenticator: AccountAuthenticatorImpl?
        get() {
            if (sAccountAuthenticator == null) {
                sAccountAuthenticator =
                    AccountAuthenticatorImpl(this)
            }
            return sAccountAuthenticator
        }

    private class AccountAuthenticatorImpl(context: Context) :
        AbstractAccountAuthenticator(context) {
        private val mContext: Context = context
        /*
         * The user has requested to add a new account to the system. We return an intent that will launch
         * our login screen if the user has not logged in yet, otherwise our activity will just pass the
         * user's credentials on to the account manager
         */
        @Throws(NetworkErrorException::class)
        override fun addAccount(
            response: AccountAuthenticatorResponse,
            accountType: String,
            authTokenType: String,
            requiredFeatures: Array<String>,
            options: Bundle
        ): Bundle {


            val reply = Bundle()

            val intent = Intent(mContext, LoginActivity::class.java)
            intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response)
            intent.putExtra("TYPE", accountType)
            intent.putExtra("AUTH_TOKEN_TYPE", authTokenType)
            intent.putExtra("IS_ADDING_NEW_ACCOUNT", true)


            // return our AccountAuthenticatorActivity
            // return our AccountAuthenticatorActivity
            reply.putParcelable(AccountManager.KEY_INTENT, intent)

            return reply
        }

        @Throws(NetworkErrorException::class)
        override fun confirmCredentials(
            arg0: AccountAuthenticatorResponse,
            arg1: Account,
            arg2: Bundle
        ): Bundle? { // TODO Auto-generated method stub
            return null
        }

        override fun editProperties(
            arg0: AccountAuthenticatorResponse,
            arg1: String
        ): Bundle? { // TODO Auto-generated method stub
            return null
        }

        @Throws(NetworkErrorException::class)
        override fun getAuthToken(
            arg0: AccountAuthenticatorResponse,
            arg1: Account,
            arg2: String,
            arg3: Bundle
        ): Bundle? { // TODO Auto-generated method stub
            return null
        }

        override fun getAuthTokenLabel(arg0: String): String? { // TODO Auto-generated method stub
            return null
        }

        @Throws(NetworkErrorException::class)
        override fun hasFeatures(
            arg0: AccountAuthenticatorResponse,
            arg1: Account,
            arg2: Array<String>
        ): Bundle? { // TODO Auto-generated method stub
            return null
        }

        @Throws(NetworkErrorException::class)
        override fun updateCredentials(
            arg0: AccountAuthenticatorResponse,
            arg1: Account,
            arg2: String,
            arg3: Bundle
        ): Bundle? { // TODO Auto-generated method stub
            return null
        }

    }

    companion object {
        private val LOG_TAG = AccountAuthenticatorService::class.java.simpleName
        private var sAccountAuthenticator: AccountAuthenticatorImpl? =
            null
    }
}