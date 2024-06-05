package com.da_chelimo.whisper.auth.repo

import android.app.Activity

/**
 * Contains all methods used in authenticating {registering} the user
 */
interface AuthRepo {

    /**
     * Authenticates using the phone number provided
     */
    fun authenticateWithNumber(
        phoneNumber: String,
        activity: Activity,
        onVerificationDone: (Boolean) -> Unit
    )


    /**
     * In case the user can not be automatically authenticated, the user need to submit the SMS code they've received
     *
     * @param smsCode - SMS code entered by user
     * @param onSignInComplete - (Boolean) is true if the sign in was successful
     */
    suspend fun submitSMSCode(smsCode: String, onSignInComplete: (Boolean) -> Unit)
}