package com.example.sample_app.ui.login.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sample_app.common.isValidPhoneNumber

/**
 *Created by Nivetha S on 27-11-2021.
 */
class LoginViewModel : ViewModel() {

    private val validationMutableLiveData = MutableLiveData<Boolean>()
    fun isUserValid(): LiveData<Boolean> = validationMutableLiveData


    private val validationOtpMutableLiveData = MutableLiveData<Boolean>()
    fun isUserValidOtp(): LiveData<Boolean> = validationOtpMutableLiveData

    var phone: String = ""
        set(value) {
            field = value
            registerValidations()
        }

    var otp: String = ""
        set(value) {
            field = value
            validateOtp()
        }


    private fun validateOtp() {
        Log.e("Nive ", "validateOtp: ${otp}")
        val isValid = otp.matches(Regex("[0-9].{5}"))
        validationOtpMutableLiveData.postValue(isValid)
    }

    private fun registerValidations() {
        val isCheckUser = phone.isValidPhoneNumber()
        if (isCheckUser) {
            validationMutableLiveData.postValue(true)
        } else {
            validationMutableLiveData.postValue(false)
        }

    }

}