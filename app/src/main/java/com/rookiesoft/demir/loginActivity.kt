package com.rookiesoft.demir

import android.os.Bundle
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.FirebaseDatabase

import kotlinx.android.synthetic.main.activity_login.*
import java.util.concurrent.TimeUnit

class loginActivity : Activity() {
    var preferences         : SharedPreferences?=null
    lateinit var tvInfo : TextView
    lateinit var mAuth : FirebaseAuth
    lateinit var activity : loginActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        tvInfo = findViewById(R.id.tvInfoLogin)
        activity = this
        preferences = getSharedPreferences("LogIn", Context.MODE_PRIVATE)
        val control = preferences!!.getBoolean("LogIn",false)
        if(control) goToMainPage()
        else{
            mAuth = FirebaseAuth.getInstance()
            mAuth.setLanguageCode("tr")
            btnVerifyRefresh.setOnClickListener {
                Thread{
                    runOnUiThread {
                        //                    btnVerifyRefresh.isClickable = false
                        btnVerifyRefresh.isEnabled = false
                    }
                    Thread.sleep(60000)
                    runOnUiThread {
                        btnVerifyRefresh.isEnabled = true
//                    btnVerifyRefresh.isClickable = true
                    }
                }.start()
                val phoneNumber : String=editTelLogin.text.toString()
                println("Send $phoneNumber")
                sendVerificationAgain("+90$phoneNumber")
            }
            btnSignUp.setOnClickListener {
                if(editTelLogin.text.length==10 && editAdSoyad.text.length>0 && editFirma.text.length>0 && editMailLogin.text.length>0){
                    runOnUiThread {
                        constraintSignUp.visibility = View.GONE
                        progressSignUp.visibility = View.VISIBLE
                    }
                    val phoneNumber : String=editTelLogin.text.toString()
                    sendVerification("+90$phoneNumber")
                }
                else{
                    Toast.makeText(this,"Girilen Bilgileri Kontrol Edin",Toast.LENGTH_SHORT).show()
                }


            }
            btnVerify.setOnClickListener {
                runOnUiThread {
                    progressSignUp.bringToFront()
                    progressSignUp.visibility = View.VISIBLE
                }
                if(editVerify.text.length>0)
                    signVerifyCode()
            }
        }

    }
    fun sendVerification(phoneNumber : String){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,      // Phone number to verify
                120,               // Timeout duration
                TimeUnit.SECONDS, // Unit of timeout
                this,             // Activity (for callback binding)
                callbacks) // OnVerificationStateChangedCallbacks
    }
    fun sendVerificationAgain(phoneNumber : String){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,      // Phone number to verify
                120,               // Timeout duration
                TimeUnit.SECONDS, // Unit of timeout
                this,             // Activity (for callback binding)
                callbacks,
                resendToken
        )
    }
    fun signVerifyCode(){
//        println("<><><>>>>${editVerify.text.toString()}")
        val credential = PhoneAuthProvider.getCredential(storedVerificationId!!, editVerify.text.toString())
        signInWithPhoneAuthCredential(credential)
    }
    var codeSent : String = ""
    var storedVerificationId : String? =""
    lateinit var resendToken : PhoneAuthProvider.ForceResendingToken
    var callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
//            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            runOnUiThread {
                constraintVerify.visibility = View.GONE
                progressSignUp.visibility = View.GONE
                constraintSignUp.visibility = View.VISIBLE
            }
            println("<><><>>>>${e.toString()}")
            if(e is FirebaseTooManyRequestsException){
               Toast.makeText(applicationContext,"Bu Cihaz Üzerinden Yapılan İşlem Sayısı Fazla! Lütfen Daha Sonra Tekrar Deneyiniz",Toast.LENGTH_LONG).show()
            }
            else Toast.makeText(applicationContext,"${e.toString()}Bir Hata İle Karşılaşıldı İnternet Bağlantınızı Kontrol Edip Tekrar Deneyin",Toast.LENGTH_LONG).show()

        }

        override fun onCodeSent(
                verificationId: String?,
                token: PhoneAuthProvider.ForceResendingToken
        ) {
            runOnUiThread {
                progressSignUp.visibility = View.GONE
                progressSignUp.visibility = View.GONE
                constraintVerify.visibility = View.VISIBLE

            }
            storedVerificationId = verificationId
            resendToken = token
        }
    }
    private fun goToMainPage(){
        runOnUiThread {
            val intent = Intent(applicationContext,MainActivity::class.java)
            startActivity(intent)
            activity.finish()
        }

//
    }
    private fun saveUser(){
        val database = FirebaseDatabase.getInstance();
        val time = System.currentTimeMillis() / 1000L
        val myRef = database.getReference("Users/$time");
        val user  = dataFireBase.Users()
        user.name_surname = editAdSoyad.text.toString()
        user.company = editFirma.text.toString()
        user.mail = editMailLogin.text.toString()
        user.phone = editTelLogin.text.toString()
        myRef.setValue(user);
    }
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        runOnUiThread {
                            progressSignUp.visibility = View.GONE
                        }
                        val editor = this.preferences!!.edit().apply {
                            putBoolean("LogIn",true)
                            apply()
                        }
                        saveUser()
                        goToMainPage()
                    } else {
                        runOnUiThread {
                            progressSignUp.visibility = View.GONE
                            tvInfo.text = "Doğrulama Kodunu Kontrol Ediniz."
                        }
//                        Toast.makeText(this,"Doğrulama Yapılamadı!!!.",Toast.LENGTH_SHORT).show()
                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        }
                    }
                }
    }
}
