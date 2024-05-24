package com.example.quizapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.UUID


class QuestionsActivity : AppCompatActivity() {
    private var viewPager: ViewPager? = null
    private var btnPrevious: Button? = null
    private var btnNext: Button? = null
    private var btnSubmit: Button? = null
    private var radioGroup: RadioGroup? = null
    private lateinit var countdownTimer: CountDownTimer
    private lateinit var timerTextView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var username: String
    private lateinit var dbref : DatabaseReference
    private lateinit var profileSrc : String

    override fun onStart() {
        super.onStart()

        timerTextView = findViewById(R.id.timer)
        progressBar = findViewById(R.id.progressBar)
        val durationMillis: Long = ( 1 * 60 * 1000 ) +1000
        val intervalMillis: Long = 1000 // Update interval every 1 second
        countdownTimer = object : CountDownTimer(durationMillis, intervalMillis) {
            override fun onTick(millisUntilFinished: Long) {
                // Update the UI to display the remaining time
                val minutes = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                timerTextView.text = String.format("%02d:%02d", minutes, seconds)
                // Update the progress of the circular progress bar
                val progress = (durationMillis - millisUntilFinished).toFloat() / durationMillis
                progressBar.progress = (progress * 100).toInt()
            }

            override fun onFinish() {
                // Perform any action when the countdown timer finishes
                timerTextView.text = "00:00"
                progressBar.progress = 100
                // Create the object of AlertDialog Builder class
                val builder = AlertDialog.Builder(this@QuestionsActivity)

                // Set the message show for the Alert time
                builder.setMessage("Submission Time is Complete")

                // Set Alert Title
                builder.setTitle("Complete")

                // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
                builder.setCancelable(false)

                // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
                builder.setPositiveButton("Result") {
                    // When the user click yes button then app will close
                        dialog, which ->
                    run {
                        saveScore()
                    }
                }

                // Create the Alert dialog
                val alertDialog = builder.create()
                // Show the Alert Dialog box
                alertDialog.show()
            }
        }

        countdownTimer.start()
    }

    @SuppressLint("SuspiciousIndentation")
    private fun saveScore() {
        val id = FirebaseRealTimeDatabase.currentUserId
        val doc = FirebaseFirestore.getInstance().collection("score_info").document(id)
        val updateMap = mutableMapOf<String,Any>()

        doc.get().addOnCompleteListener { task->
            if(task.isSuccessful){
                val value = task.result.toObject(ScoreClass::class.java)
                if(value!=null){
                    val previousScore = value.score
                    val previousTime = value.time
                    val time = previousTime.split(":")[1].toInt()
                    val currentTime = (timerTextView.text.toString().split(":")[1]).toInt()

                    if(score > previousScore){
                        updateMap["score"] = score
                        updateMap["time"] = timerTextView.text.toString()
                    }
                    if(score == previousScore && currentTime > time){
                        updateMap["time"] =  timerTextView.text.toString()
                    }
                    if(value.username != username){
                        updateMap["username"] = username
                    }
                    if(value.imgSrc == profileSrc){
                        updateMap["imgSrc"] = profileSrc
                    }
                    doc.update(updateMap).addOnSuccessListener {
                        startActivity(Intent(this, ResultActivity::class.java))
                        finish()
                    }.addOnFailureListener {
                        Toast.makeText(this,"score does not saved", Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    if(username.isEmpty()){
                        username = "UnKnown User"
                    }
                    val score = ScoreClass(
                        userId = id,
                        username = username,
                        score = score,
                        time = timerTextView.text.toString(),
                        imgSrc = profileSrc
                    )
                    doc.set(score).addOnSuccessListener {
                        startActivity(Intent(this, ResultActivity::class.java))
                        finish()
                    }.addOnFailureListener {
                        Toast.makeText(this,"score does not saved", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        countdownTimer.cancel() // Cancel the timer to avoid memory leaks
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questions)
        getProfileSrc()
        score = 0L
        viewPager = findViewById(R.id.viewPager)
        btnPrevious = findViewById(R.id.btnPrevious)
        btnNext = findViewById(R.id.btnNext)
        btnSubmit = findViewById(R.id.btnSubmit)
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.fragment_questions, null)
        radioGroup = view.findViewById(R.id.radioGroup)
        // Initialize ViewPager adapter
        val adapter = QuestionPagerAdapter(supportFragmentManager)
        viewPager!!.adapter = adapter
        // Set a listener to update button visibility when ViewPager page changes
        viewPager!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                updateButtonVisibility(position)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

        updateButtonVisibility(0)
    }

    private fun getProfileSrc() {
        // Set the duration of the countdown timer to 5 minutes (300,000 milliseconds)
        dbref = FirebaseRealTimeDatabase.initializeDatabaseReference(){}
        dbref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.getValue(UserInfo::class.java)
                profileSrc = data!!.profileImage
                username = data.userName
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    // Method to update button visibility based on current ViewPager position
    private fun updateButtonVisibility(position: Int) {
        val pageCount = viewPager!!.adapter!!.count
        when (position) {
            0 -> {
                // First question, only show Next button
                btnPrevious!!.visibility = View.GONE
                btnNext!!.visibility = View.VISIBLE
                btnSubmit!!.visibility = View.GONE
            }
            pageCount - 1 -> {
                // Last question, show Submit button instead of Next
                btnPrevious!!.visibility = View.VISIBLE
                btnNext!!.visibility = View.GONE
                btnSubmit!!.visibility = View.VISIBLE
            }
            else -> {
                // Middle questions, show both Previous and Next buttons
                btnPrevious!!.visibility = View.VISIBLE
                btnNext!!.visibility = View.VISIBLE
                btnSubmit!!.visibility = View.GONE
            }
        }
    }

    fun onNextButtonClick(view: View?) {
        val nextIndex = viewPager!!.currentItem + 1
        if (nextIndex < viewPager!!.adapter!!.count) {
            viewPager!!.currentItem = nextIndex
        }
    }

    fun onPreviousButtonClick(view: View?) {
        val previousIndex = viewPager!!.currentItem - 1
        if (previousIndex >= 0) {
            viewPager!!.currentItem = previousIndex
        }
    }

    // Click listener for Submit button
    fun onSubmitButtonClick(view: View?) {
        // Implement submission logic here
        saveScore()
        countdownTimer.cancel() // Cancel the timer to avoid memory leaks
    }

    companion object{
        var score = 0L
    }
}
