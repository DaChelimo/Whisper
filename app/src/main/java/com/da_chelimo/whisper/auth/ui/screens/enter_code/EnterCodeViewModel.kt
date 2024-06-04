package com.da_chelimo.whisper.auth.ui.screens.enter_code

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber
import java.util.concurrent.TimeUnit

class EnterCodeViewModel: ViewModel() {

    private val _code = MutableStateFlow("")
    val code: StateFlow<String> = _code

    private var storedVerificationId: String? = null
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null

    private val _shouldNavigate = MutableStateFlow(false)
    val shouldNavigate: StateFlow<Boolean> = _shouldNavigate

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    companion object {
        const val CODE_LENGTH = 6
    }

    fun updateCode(newCode: String) {
        _code.value = newCode
    }


    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(authCredential: PhoneAuthCredential) {
            Firebase.auth.signInWithCredential(authCredential).addOnCompleteListener { task ->
                Timber.d("task.isSuccessful is ${task.isSuccessful}")

                if (task.isSuccessful)
                    _shouldNavigate.value = true
            }

            _shouldNavigate.value = true
        }

        override fun onVerificationFailed(firebaseException: FirebaseException) {
            Timber.e(firebaseException)
        }

        override fun onCodeSent(verificationId: String, forceResendingToken: PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(verificationId, forceResendingToken)

            storedVerificationId = verificationId
            resendToken = forceResendingToken
        }
    }

    fun authenticateWithNumber(phoneNumber: String, activity: Activity?) {
        val phoneAuthOptions = PhoneAuthOptions.newBuilder()
            .setPhoneNumber(phoneNumber)
            .setTimeout(30L, TimeUnit.SECONDS)
            .setActivity(activity!!)
            .setCallbacks(callbacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions)
    }

    fun submitCode() {
        _isLoading.value = true

        storedVerificationId?.let {
            val authCredential = PhoneAuthProvider.getCredential(it, code.value)

            Firebase.auth.signInWithCredential(authCredential).addOnCompleteListener { task ->
                Timber.d("task.isSuccessful is ${task.isSuccessful}")
                _isLoading.value = false

                if (task.isSuccessful)
                    _shouldNavigate.value = true
            }
        }

        _isLoading.value = false
    }


    fun resetShouldNavigate() {
        _shouldNavigate.value = false
    }
}