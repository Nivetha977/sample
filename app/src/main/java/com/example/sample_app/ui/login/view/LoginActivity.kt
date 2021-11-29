package com.example.sample_app.ui.login.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.sample_app.R
import com.example.sample_app.databinding.ActivityLoginBinding
import com.example.sample_app.ui.login.viewmodel.LoginViewModel
import com.google.android.gms.tasks.TaskExecutors

import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit
import android.widget.Toast

import com.google.firebase.FirebaseException

import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.android.material.snackbar.Snackbar


import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException

import android.content.Intent
import android.content.SharedPreferences
import android.util.Log

import com.google.firebase.auth.AuthResult

import androidx.annotation.NonNull
import androidx.core.widget.doAfterTextChanged
import com.example.sample_app.app.App
import com.example.sample_app.ui.homemain.view.HomeActivity

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import java.util.zip.GZIPOutputStream


class LoginActivity : AppCompatActivity() {

    private lateinit var mVerificationId: String
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth = FirebaseAuth.getInstance()
        binding.editTextTextPhoneNumber.doAfterTextChanged {
            loginViewModel.phone = it.toString()
        }

        val pref = applicationContext.getSharedPreferences("MyPref", 0) // 0 - for private mode
        val editor: SharedPreferences.Editor = pref.edit()
        editor.putString(App.isLogin,"1")
        editor.commit()



        loginViewModel.isUserValid().observe(this, Observer {
            binding.buttonRegister.isEnabled = it
        })

        loginViewModel.isUserValidOtp().observe(this, Observer {
            binding.buttonVerify.isEnabled = it
        })


        binding.buttonRegister.setOnClickListener {
            binding.phoneLayout.visibility = View.GONE
            binding.otpLayout.visibility = View.VISIBLE
            sendVerificationCode(binding.editTextTextPhoneNumber.text.toString())

        }

        binding.buttonVerify.setOnClickListener {
            verifyVerificationCode(binding.pinView.text.toString())

        }


    }


    private fun sendVerificationCode(mobile: String) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            "+91$mobile",
            60,
            TimeUnit.SECONDS,
            this,
            mCallbacks
        )
    }


    private val mCallbacks: OnVerificationStateChangedCallbacks =
        object : OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {

                //Getting the code sent by SMS
                val code = phoneAuthCredential.smsCode

                //sometime the code is not detected automatically
                //in this case the code will be null
                //so user has to manually enter the code
                if (code != null) {
                    binding.pinView.setText(code)
                    binding.pinView.apply {
                        loginViewModel.otp = code
                    }
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {

                Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_LONG).show()
            }

            override fun onCodeSent(s: String, forceResendingToken: ForceResendingToken) {
                super.onCodeSent(s, forceResendingToken)

                //storing the verification id that is sent to the user
                mVerificationId = s
            }
        }


    private fun verifyVerificationCode(code: String) {
        //creating the credential
        val credential = PhoneAuthProvider.getCredential(mVerificationId, code)

        //signing the user
        signInWithPhoneAuthCredential(credential)
    }


    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth?.signInWithCredential(credential)
            ?.addOnCompleteListener(this,
                OnCompleteListener<AuthResult?> { task ->
                    if (task.isSuccessful) {
                        //verification successful we will start the profile activity
                        val intent = Intent(this, HomeActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }
                })
    }

}