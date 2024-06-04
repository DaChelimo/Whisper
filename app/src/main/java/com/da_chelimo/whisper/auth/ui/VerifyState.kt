package com.da_chelimo.whisper.auth.ui

sealed class VerifyState(val messageRes: Int?) {
    
    class Success(messageRes: Int? = null): VerifyState(messageRes)
    class Error(errorMessageRes: Int): VerifyState(errorMessageRes)
    
}