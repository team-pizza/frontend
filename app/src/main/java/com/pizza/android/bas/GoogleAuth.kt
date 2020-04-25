package com.pizza.android.bas

import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

class GoogleAuth(mainActivity: MainActivity) {

    private var callback: ((identity: UserIdentity)->Unit)? = null
    private var identity: UserIdentity? = null
    private val RESULT_CODE_SIGNIN = 9001
    private var googleSignInClient: GoogleSignInClient
    private val mainActivity: MainActivity

    init {
        this.mainActivity = mainActivity

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestId()
            .requestIdToken(mainActivity.getString(R.string.server_google_id))
            .build()

        googleSignInClient = GoogleSignIn.getClient(mainActivity, gso)

        val account = GoogleSignIn.getLastSignedInAccount(mainActivity)
        if(account!=null) {
            identity = accountToIdentity(account)
        }

    }

    fun requestSignIn(callback: (identity: UserIdentity)->Unit) {
       this.callback = callback
       val signInIntent = googleSignInClient.signInIntent
       mainActivity.startActivityForResult(signInIntent, RESULT_CODE_SIGNIN)
    }
    fun getIdentity(): UserIdentity? {
        return identity
    }
    fun handleActivityResult(requestCode: Int, data: Intent?) {
        if(requestCode == RESULT_CODE_SIGNIN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            val identity = accountToIdentity(account)
            callback?.invoke(identity)
            this.identity=identity
        }
    }
    private fun accountToIdentity(account: GoogleSignInAccount?): UserIdentity {
        return UserIdentity(account?.id ?: "null", account?.serverAuthCode ?: "null", "jdlkfajfdlkjafdsasdlkfjlkjf", account?.email ?: "null")
    }
}

data class UserIdentity(val identificationNumber: String, val googleIdentificationToken: String, val sessionToken: String, val emailAddress: String)