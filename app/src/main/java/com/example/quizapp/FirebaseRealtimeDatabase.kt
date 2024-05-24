package com.example.quizapp

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FirebaseRealTimeDatabase {

    companion object {
        lateinit var db: DatabaseReference
        lateinit var currentUserId: String

        fun initializeDatabaseReference( callback: (data: String?) -> Unit) : DatabaseReference {
            currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
            db = FirebaseDatabase.getInstance().getReference("UserInfo").child(currentUserId)
            db.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val value = dataSnapshot.child("password").value // Replace with field name
                        if (value != null) {
                            // You have the retrieved value: value
                            callback(value.toString())
                        }
                        else{
                            callback(null)
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {

                }

            })
            return db
        }
    }
}