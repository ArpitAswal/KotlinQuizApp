package com.example.quizapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.android.gms.common.api.ApiException
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var forgotBtn: Button
    private lateinit var loginBtn: Button
    private lateinit var register: Button
    private lateinit var signIn: SignInButton
    private lateinit var email: TextInputLayout
    private lateinit var pass: TextInputLayout
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private lateinit var account: GoogleSignInAccount

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()

        // Configure Google Sign-In client
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("777379066436-4o9kbofchoqt5t8c4lvo8citatn3dbmm.apps.googleusercontent.com")
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Handle sign-in with email and password
        loginBtn.setOnClickListener {
            val email = email.editText?.text.toString()
            val password = pass.editText?.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                // Show error message for empty fields
                displayToast("Please fill all the fields")
            } else {
                auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                    Toast.makeText(this, "Login Successfully", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@MainActivity, HomeActivity::class.java))
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(this, it.message.toString(), Toast.LENGTH_LONG).show()
                }
            }
        }

        // Handle sign-in with Google
        signIn.setOnClickListener {
            googleSignInClient.signOut()
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, 101)
        }

        register.setOnClickListener {
            startActivity(Intent(this@MainActivity, SignupActivity::class.java))
            finish()
        }

        forgotBtn.setOnClickListener {
            val email = email.editText?.text.toString()

            if (email.isEmpty()) {
                // Show error message for empty fields
                displayToast("Enter your email")
            } else {
                auth.sendPasswordResetEmail(email).addOnSuccessListener {
                    displayToast("A reset password link sent to given email address")
                }.addOnFailureListener {
                    displayToast("Failed to reset password")
                }
            }
        }

    }

    private fun init() {
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        forgotBtn = findViewById(R.id.forgotPasswordButton)
        loginBtn = findViewById(R.id.loginbtn)
        signIn = findViewById(R.id.bt_sign_in)
        email = findViewById(R.id.emailTV)
        pass = findViewById(R.id.passTV)
        register = findViewById(R.id.text_button)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 101) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                account = task.getResult(ApiException::class.java)
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                auth.signInWithCredential(credential)
                    .addOnSuccessListener {
                        FirebaseRealTimeDatabase.initializeDatabaseReference{ data->
                            if(data != null){
                                val cred = EmailAuthProvider.getCredential(account.email.toString(), data)
                                auth.currentUser?.linkWithCredential(cred)?.addOnSuccessListener {
                                  navigate()
                                }?.addOnFailureListener {
                                    if(it.message.toString() == "User has already been linked to the given provider."){
                                        navigate()
                                    }
                                    else {
                                        displayToast(it.message.toString())
                                    }
                                }
                            } else{
                                navigate()
                            }
                        }
                    }.addOnFailureListener {
                        Toast.makeText(this@MainActivity, it.message.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }
            } catch (e: ApiException) {
                // Handle Google Sign In failure
                displayToast(e.message.toString())
            }
        }
    }

    private fun navigate() {
        val rdb = FirebaseRealTimeDatabase.db

        val map = mutableMapOf<String, Any>()

        rdb.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var user = snapshot.getValue(UserInfo::class.java)

                if (user != null) {
                    if (user.userName.isNotEmpty())
                        map["userName"] = user.userName
                    if (user.profileImage.isNotEmpty())
                        map["profileImage"] = user.profileImage
                    if (user.password.isNotEmpty())
                        map["password"] = user.password

                    map["userId"] = auth.currentUser?.uid.toString()
                    map["userEmail"] = auth.currentUser?.email.toString()

                    rdb.updateChildren(map).addOnSuccessListener {
                        Toast.makeText(this@MainActivity, "SignIn Successfully", Toast.LENGTH_SHORT)
                            .show()
                        startActivity(
                            Intent(this@MainActivity, HomeActivity::class.java).setFlags(
                                Intent.FLAG_ACTIVITY_NEW_TASK
                            )
                        )
                        finish()
                    }
                } else {
                    user = UserInfo(
                        userId = auth.currentUser?.uid!!,
                        userEmail = auth.currentUser?.email!!,
                        password = "",
                        profileImage = "",
                        userName = ""
                    )

                    rdb.setValue(user).addOnSuccessListener {
                        Toast.makeText(this@MainActivity, "Google SignIn Successfully", Toast.LENGTH_SHORT)
                            .show()
                        startActivity(
                            Intent(this@MainActivity, HomeActivity::class.java).setFlags(
                                Intent.FLAG_ACTIVITY_NEW_TASK
                            )
                        )
                        finish()
                    }.addOnFailureListener {
                        displayToast(it.message.toString())
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun displayToast(mes: String) {
        Toast.makeText(this, mes, Toast.LENGTH_SHORT).show()
    }
}
