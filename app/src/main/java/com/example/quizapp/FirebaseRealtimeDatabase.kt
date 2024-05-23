package com.example.quizapp

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class FirebaseRealTimeDatabase {

    companion object {
        lateinit var db: DatabaseReference
        lateinit var currentUserId: String

        fun initializeDatabaseReference() : DatabaseReference {
            currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
            db = FirebaseDatabase.getInstance().getReference("UserInfo").child(currentUserId)
            return db
        }
    }
}