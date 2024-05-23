package com.example.quizapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.auth

class SignupActivity : AppCompatActivity() {

    private lateinit var login: Button
    private lateinit var register: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var email: TextInputLayout
    private lateinit var pass: TextInputLayout
    private lateinit var cpass: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        supportActionBar?.hide()

        register = findViewById(R.id.registerbtn)
        login = findViewById(R.id.login_text_button)
        auth = Firebase.auth

        login.setOnClickListener {
            val intent = Intent(this@SignupActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        register.setOnClickListener {
            if (checkValidation()) {
                auth.createUserWithEmailAndPassword(
                    email.editText?.text.toString(),
                    pass.editText?.text.toString()
                )
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Registration successful, handle user
                            FirebaseRealTimeDatabase.initializeDatabaseReference()
                            Toast.makeText(
                                this@SignupActivity,
                                "Account Registered Successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            navigate()
                        } else {
                            // Handle registration errors (e.g., email already in use)
                            val exception = task.exception
                            if (exception != null && exception.message!!.contains("The email address is already in use by another account.")) {
                                // Email already in use, offer Google sign-in
                                showCustomAlertDialog(this)
                            } else {
                                // Handle other registration errors
                                Toast.makeText(
                                    this@SignupActivity,
                                    exception?.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
            }
        }
    }

    private fun showCustomAlertDialog(context: Context) {
        // Create an AlertDialog Builder
        val builder = AlertDialog.Builder(context)

        // Set the message and title of the dialog
        builder.setMessage("This email is already used with Google SignIn. Please register with a new email or sign in with this email")
            .setTitle("Email Alert")

        // Set the positive button and its click listener
        builder.setPositiveButton("OK") { dialog, id ->
            // User clicked "Yes" button
            dialog.dismiss()
        }

        // Create and show the AlertDialog
        val dialog = builder.create()
        dialog.show()
    }

    private fun navigate() {

        val rdb = FirebaseRealTimeDatabase.db
        val userInfo = UserInfo(
            userId = FirebaseRealTimeDatabase.currentUserId,
            userEmail = email.editText?.text.toString(),
            password = pass.editText?.text.toString(),
            profileImage = "",
            userName = ""
        )
        rdb.setValue(userInfo).addOnSuccessListener {
            startActivity(
                Intent(
                    this@SignupActivity,
                    HomeActivity::class.java
                ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "user info does not saved", Toast.LENGTH_SHORT).show()
        }
    }

    public override fun onStart() {
        super.onStart()
        email = findViewById(R.id.remailTV)
        pass = findViewById(R.id.rpassTV)
        cpass = findViewById(R.id.rcpassTV)

    }

    private fun checkValidation(): Boolean {
        if (TextUtils.isEmpty(email.editText?.text.toString())) {
            Toast.makeText(this, "Enter your email", Toast.LENGTH_LONG).show()
            return false
        } else if (TextUtils.isEmpty(pass.editText?.text.toString())) {
            Toast.makeText(this, "Enter your password", Toast.LENGTH_LONG).show()
            return false
        } else if (TextUtils.isEmpty(cpass.editText?.text.toString())) {
            Toast.makeText(this, "Enter your confirm password", Toast.LENGTH_LONG).show()
            return false
        } else if (!TextUtils.equals(
                pass.editText?.text.toString(),
                cpass.editText?.text.toString()
            )
        ) {
            Toast.makeText(this, "Password & Confirm  Password should be same", Toast.LENGTH_LONG)
                .show()
            return false
        } else if (pass.editText?.text?.length!! <= 6) {
            Toast.makeText(
                this,
                "Password length should be at least of 6 character",
                Toast.LENGTH_LONG
            )
                .show()
            return false
        }
        return true
    }
}