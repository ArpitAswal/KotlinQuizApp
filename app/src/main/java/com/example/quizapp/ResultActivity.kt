package com.example.quizapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ResultActivity : AppCompatActivity() {

    private lateinit var username: String
    private lateinit var usertime: String
    private lateinit var userImage: String
    private var userscore: Long = 0L
    private lateinit var rcv: RecyclerView
    private lateinit var rankList : MutableList<RankViewModel>
    private lateinit var rank1name: TextView
    private lateinit var rank1Score: TextView
    private lateinit var rank1Image: CircleImageView
    private lateinit var loadingImage : ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        init()
    }

    override fun onStart() {
        super.onStart()
        rank1name = findViewById(R.id.rank1Name)
        rank1Score = findViewById(R.id.rank1Score)
        rank1Image = findViewById(R.id.rank1Image)
        loadingImage = findViewById(R.id.loading_rank1Image)
        getRanks()

    }

    private fun init() {
        GlobalScope.launch(Dispatchers.Main){
            delay(1000)
            rcv = findViewById(R.id.recyclerVIew)
            rcv.layoutManager = LinearLayoutManager(this@ResultActivity)
            val adapter = CustomAdapter(rankList)
            rcv.adapter = adapter
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getRanks() {
        val scoresRef = FirebaseFirestore.getInstance().collection("score_info")
          rankList = mutableListOf()
          var pos = 2
          var firstUser = true
          rankList.clear()
        // Query scores sorted by score in descending order and time in ascending order
        scoresRef.orderBy("score", Query.Direction.DESCENDING)
            .orderBy("time", Query.Direction.DESCENDING).limit(5).addSnapshotListener { value, error ->
                run {
                    if (value != null) {
                        for (document in value.documents) {
                            // Handle each score document
                            username = document.getString("username")!!
                            userscore = document.getLong("score")!!
                            usertime = document.getString("time")!!
                            userImage = document.getString("imgSrc")!!
                            if(firstUser){
                                 rank1name.text = username
                                 rank1Score.text = "${userscore} / 10"
                                if(userImage.isNullOrEmpty()){
                                        rank1Image.setImageResource(R.drawable.avatar)
                                      loadingImage.visibility = View.GONE
                                }
                                else {
                                    Glide.with(this@ResultActivity).load(userImage).into(rank1Image)
                                    loadingImage.visibility = View.GONE
                                }
                                firstUser = false
                            }
                            else{
                                rankList.add(RankViewModel(
                                    pos = pos++,
                                    imgSrc = userImage,
                                    name = username,
                                    score = userscore))
                            }
                        }
                    }
                }                // Handle any errors
            }
    }
}