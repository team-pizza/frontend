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
            .requestIdToken(mainActivity.getString(R.string.server_google_id))
            .requestEmail()
            .requestId()
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
            mainActivity.postToBackend<SignInRequest, SignInResponse>("/auth/", SignInRequest(identity.googleIdentificationToken)) {
                val identity = this.identity
                if(it != null && it.success && identity != null) {
                    identity.sessionToken = it.sessionToken
                    this.identity = identity
                    callback?.invoke(identity)
                }
            }
        }
    }
    private fun accountToIdentity(account: GoogleSignInAccount?): UserIdentity {
        return UserIdentity(account?.id ?: "null", account?.idToken ?: "null", "jdlkfajfdlkjafdsasdlkfjlkjf", account?.email ?: "null")
    }
}

data class SignInRequest(val googleIdentificationToken: String)
data class SignInResponse(val success: Boolean, val sessionToken: String)
data class UserIdentity(val identificationNumber: String, val googleIdentificationToken: String, var sessionToken: String, val emailAddress: String)