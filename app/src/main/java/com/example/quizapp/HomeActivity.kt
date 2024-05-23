package com.example.quizapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeActivity : AppCompatActivity() {

    private lateinit var currentUser: FirebaseAuth
    private lateinit var start: Button
    private lateinit var menu: ImageButton
    private lateinit var nameTV: TextInputLayout

    override fun onStart() {
        super.onStart()
        val dbref = FirebaseRealTimeDatabase.initializeDatabaseReference()
        dbref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val name = snapshot.child("userName").value
                nameTV.editText?.setText(name.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        currentUser = FirebaseAuth.getInstance()
        start = findViewById(R.id.startBtn)
        menu = findViewById(R.id.menuButton)
        nameTV = findViewById(R.id.usernameTV)

        start.setOnClickListener {
            val username = nameTV.editText?.text.toString()
            if (TextUtils.isEmpty(username)) {
                Toast.makeText(this, "Before start enter your name", Toast.LENGTH_LONG).show()
            } else {
                val map = mutableMapOf<String,Any>()
                val rdb = FirebaseRealTimeDatabase.db
                map["userName"] = username
                rdb.updateChildren(map).addOnSuccessListener {
                    val intent = Intent(this, QuestionsActivity::class.java)
                    intent.putExtra("username", username)
                    startActivity(intent)
                }.addOnFailureListener {
                    Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
                }

            }
        }

        menu.setOnClickListener {
            // Initializing the popup menu and giving the reference as current context
            val popupMenu = PopupMenu(this@HomeActivity, menu)

            // Inflating popup menu from popup_menu.xml file
            popupMenu.menuInflater.inflate(R.menu.menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                // Toast message on menu item clicked
                when (menuItem.itemId) {
                    R.id.menu_profile -> {
                        startActivity(Intent(this, ProfileActivity::class.java))
                        true
                    }

                    R.id.menu_sign_out -> {
                        currentUser.signOut()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                        Toast.makeText(this, "SignOut Successful", Toast.LENGTH_LONG).show()
                        true
                    }

                    R.id.leaderboard -> {
                        startActivity(Intent(this, ResultActivity::class.java))
                        true
                    }

                    else -> false
                }
            }
            // Showing the popup menu
            popupMenu.show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_profile -> {
                Toast.makeText(applicationContext, "Profile", Toast.LENGTH_SHORT).show()
                return true
            }

            R.id.menu_sign_out -> {
                Toast.makeText(applicationContext, "Sign Out", Toast.LENGTH_SHORT).show()
                return true
            }

            R.id.leaderboard -> {
                Toast.makeText(applicationContext, "LeaderBoard Results", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}