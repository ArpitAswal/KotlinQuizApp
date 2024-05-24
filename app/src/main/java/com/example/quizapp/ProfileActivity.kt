package com.example.quizapp

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.util.TextUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import java.util.UUID

class ProfileActivity : AppCompatActivity() {
    private lateinit var image: CircleImageView
    private lateinit var upload: Button
    private lateinit var select: ImageButton
    private lateinit var remove: ImageButton
    private lateinit var currentUser: FirebaseUser
    private lateinit var loadingImage: ProgressBar
    private lateinit var dbf: DatabaseReference
    private lateinit var name: TextInputLayout
    private lateinit var password: TextInputLayout
    private lateinit var hideLayout: LinearLayout
    private lateinit var loading: ProgressBar
    private var userinfo: UserInfo? = null
    private var imageURI: Uri? = null
    private var userEmail: String? = null
    private var backScreen: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        supportActionBar?.hide()
        FirebaseRealTimeDatabase.initializeDatabaseReference(){}
        init()
        loadingImage.visibility = View.VISIBLE

        getValueFromDB()

        select.setOnClickListener {
            val cameraIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(cameraIntent, 101)
        }

        remove.setOnClickListener {
            image.setImageResource(R.drawable.avatar)
            imageURI = null
        }

        upload.setOnClickListener {
            if (imageURI != null && imageURI!!.path.isNullOrEmpty()) {
                Toast.makeText(
                    this, "First select a image", Toast.LENGTH_SHORT
                ).show()
            } else {
                val profile = ""
                imageStored(profile)
            }
        }
    }

    private fun getValueFromDB() {
        dbf.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userinfo = snapshot.getValue(UserInfo::class.java)
                if (userinfo != null) {
                    name.editText?.setText(userinfo!!.userName)
                    userEmail = userinfo!!.userEmail
                    if (userinfo!!.password.isNotEmpty()) {
                        password.visibility = View.VISIBLE
                        password.editText?.setText(userinfo!!.password)
                    }
                    loading.visibility = View.GONE
                    hideLayout.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@ProfileActivity, "Can not fetch User Info", Toast.LENGTH_SHORT
                ).show()
            }
        })

        dbf.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val profile: Any? = snapshot.child("profileImage").value
                if (profile.toString().isNullOrEmpty()) {
                    image.setImageResource(R.drawable.avatar)
                } else {
                    Glide.with(this@ProfileActivity).load(profile).into(image)
                }
                loadingImage.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun init() {
        currentUser = FirebaseAuth.getInstance().currentUser!!
        image = findViewById(R.id.image_view)
        select = findViewById(R.id.cameraBtn)
        remove = findViewById(R.id.removeBtn)
        upload = findViewById(R.id.upload_image)
        loadingImage = findViewById(R.id.loadingImage)
        name = findViewById(R.id.database_username)
        password = findViewById(R.id.database_userPassword)
        loading = findViewById(R.id.loadingScreen)
        hideLayout = findViewById(R.id.hideLayout)
        dbf = FirebaseRealTimeDatabase.db
        val intent =intent
        backScreen = intent.getStringExtra("fromSignUp")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101 && resultCode == RESULT_OK && data != null) {
            imageURI = data.data!!
            image.setImageURI(imageURI)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(backScreen!=null){
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }

    private fun imageStored(profile: String) {
        var value = profile
        loadingImage.visibility = View.VISIBLE

        if (imageURI != null) {
            val id = UUID.randomUUID().toString()
            val storageRef =
                FirebaseStorage.getInstance().reference.child("${currentUser.uid}/upload_images/$id")
            storageRef.putFile(imageURI!!).addOnSuccessListener {
                val result = storageRef.downloadUrl
                result.addOnSuccessListener {
                    value = it.toString()
                    updateProfileData(value)
                }
            }.addOnFailureListener {
                Toast.makeText(
                    this, "Image does not Uploaded", Toast.LENGTH_SHORT
                ).show()
                loadingImage.visibility = View.GONE
            }
        } else {
            updateProfileData(value)
        }
    }

    private fun updateProfileData(profile: String) {

        val map = mutableMapOf<String, Any>()

        if (!TextUtils.isEmpty(password.editText?.text)) {
            map["password"] = password.editText?.text.toString()
            currentUser.updatePassword(password.editText?.text.toString()).addOnFailureListener {
                if(it.message?.contains("This operation is sensitive and requires authentication") == true){
                    Toast.makeText(this, "Login in again before updating password", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }

        map["userName"] = name.editText?.text.toString()
        map["profileImage"] = profile

        dbf.updateChildren(map).addOnSuccessListener {
            loadingImage.visibility = View.GONE
            Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show()
            val id = FirebaseRealTimeDatabase.currentUserId
            val dbCollection: CollectionReference =
                FirebaseFirestore.getInstance().collection("score_info")
            dbCollection.document(id).addSnapshotListener { value, error ->
                run {
                    if (value?.get("score") != null) {
                        FirebaseFirestore.getInstance().runTransaction { transaction ->

                            transaction.update(
                                dbCollection.document(id), "imgSrc", profile
                            )
                            transaction.update(
                                dbCollection.document(id),
                                "username",
                                name.editText?.text.toString()
                            )
                            // Return the updated data
                            null
                        }
                    }
                }
            }
        }.addOnFailureListener {
            Toast.makeText(this, it.message.toString(), Toast.LENGTH_SHORT).show()
            loadingImage.visibility = View.GONE
        }
    }
}