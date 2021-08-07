package com.example.otpcatcher

import android.Manifest
import android.R.attr.phoneNumber
import android.content.Intent
import android.content.pm.PackageManager

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.example.otpcatcher.databinding.ActivityMainBinding
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit
import javax.xml.datatype.DatatypeConstants.SECONDS


class MainActivity : AppCompatActivity() {

    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var editText: EditText
    private lateinit var binding: ActivityMainBinding
    private lateinit var Auth: FirebaseAuth
    private var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks? = null
    private  var mVerificationID: String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
       binding.btVerify1.isEnabled=false
       binding.etID1.visibility = View.GONE
       binding.btVerify1.visibility=View.GONE
        reqpermission()

        editText=findViewById(R.id.etID1)
        Log.d("edit","$editText")
        val i=OTPdetector()
        i.OTP(editText)

        Auth = FirebaseAuth.getInstance()

        binding.btVerify.setOnClickListener {
            binding.etID1.visibility = View.VISIBLE
            binding.btVerify1.visibility = View.VISIBLE
            binding.btVerify1.isEnabled=true
            val phoneNumber = binding.etID.text.toString()
            if (phoneNumber.length != 13) {
                Toast.makeText(
                    this@MainActivity,
                    "Please Enter Correct Phone Number with STD Code +91",
                    Toast.LENGTH_LONG
                ).show()
            }
            else
            {
            val options = callbacks?.let { it1 ->
                PhoneAuthOptions.newBuilder(Auth)
                    .setPhoneNumber(phoneNumber)       // Phone number to verify
                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                    .setActivity(this)                 // Activity (for callback binding)
                    .setCallbacks(it1)          // OnVerificationStateChangedCallbacks
                    .build()
            }
            if (options != null) {
                PhoneAuthProvider.verifyPhoneNumber(options)
            }

        }
        }


        binding.btVerify1.setOnClickListener {

            val code = binding.etID1.text.toString()
if (mVerificationID!=null)
{
    val p0 = mVerificationID?.let { it1 -> PhoneAuthProvider.getCredential(it1, code) }
    if (p0 != null) {
        signinWithPhoneAuthCredential(p0)
    }
}
         else
{
    Toast.makeText(this@MainActivity,"PLEASE ENTER THE PHONE NUMBER AND CLICK GET OTP",Toast.LENGTH_LONG).show()
}


        }

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                Log.d("abc", "onVerificationCompleted:$p0")
                signinWithPhoneAuthCredential(p0)
            }


            override fun onVerificationFailed(p0: FirebaseException) {
                Log.d("exception","$p0")
                Toast.makeText(this@MainActivity,"Exception Occured!!",Toast.LENGTH_SHORT).show()
            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                Toast.makeText(this@MainActivity, "OTP SENT TO YOUR DEVICE", Toast.LENGTH_LONG).show()
                mVerificationID = p0
            }
        }


    }

    private fun reqpermission() {
        if(ContextCompat.checkSelfPermission(this@MainActivity,
                Manifest.permission.RECEIVE_SMS)!=PackageManager.PERMISSION_GRANTED)
        {

            ActivityCompat.requestPermissions(this@MainActivity,
                arrayOf( Manifest.permission.RECEIVE_SMS)
                ,100)
        }
    }

    private fun signinWithPhoneAuthCredential(p0: PhoneAuthCredential) {
        Auth.signInWithCredential(p0)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("abc", "signInWithCredential:success")
                    Toast.makeText(this@MainActivity, "OTP SUCCESSFULLY VERIFIED", Toast.LENGTH_LONG)
                        .show()
startActivity(Intent(this@MainActivity,InnerActivity::class.java))
                    finish()
                    val user = task.result?.user
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w("abc", "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        Toast.makeText(this@MainActivity, "Exception Occured", Toast.LENGTH_LONG)
                            .show()
                    }
                    // Update UI
                }
            }

            }

    }


