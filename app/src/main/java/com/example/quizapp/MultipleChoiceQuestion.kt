package com.example.quizapp

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment


class MultipleChoiceQuestion(private val questionText: String, private val options: List<String>, private val questionNumber: Int) :
    Fragment() {
        private lateinit var question : TextView
        private lateinit var r1 : RadioButton
        private lateinit var r2 : RadioButton
        private lateinit var r3 : RadioButton
        private lateinit var r4 : RadioButton
        private lateinit var radio : RadioGroup
        private lateinit var radioIdOption: MutableMap<String, Int>

        override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_questions, container, false)

        // Initialize UI elements
        question = view.findViewById(R.id.textViewQuestion)
        radio = view.findViewById(R.id.radioGroup)
        r1 = view.findViewById(R.id.radioButtonOption1)
        r2 = view.findViewById(R.id.radioButtonOption2)
        r3 = view.findViewById(R.id.radioButtonOption3)
        r4 = view.findViewById(R.id.radioButtonOption4)
        // Set question text
            question.text = questionText;
            radioIdOption = mutableMapOf()
        // Add radio buttons for each option
        for(i in options.indices) {
          when(i){
              0-> {
                  r1.text = options[i]
                  radioIdOption["0"] = r1.id
              }
              1-> {
                  r2.text = options[i]
                  radioIdOption["1"] = r2.id
              }
              2-> {
                  r3.text = options[i]
                  radioIdOption["2"] = r3.id
              }
              else -> {
                  r4.text = options[i]
                  radioIdOption["3"] = r4.id
              }
          }
        }

            // Listen for changes in checked state of RadioGroup
            // Listen for changes in checked state of RadioGroup
            radio.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
                //val score: Int = calculateScore()
                // Do something with the score (e.g., update UI)
                checkAnswer(checkedId)
                disableRadioGroup()
            })
            return view
    }

    private fun disableRadioGroup() {
        for (i in 0 until radio.childCount) {
            radio.getChildAt(i).isEnabled = false
        }
    }

    private fun checkAnswer(checkedId: Int) {
        val answers = QuestionPagerAdapter.listOfAnswers
        if(checkedId == r1.id){
              if(answers[questionNumber-1] == 0){
                 r1.setBackgroundResource(R.drawable.green_background)
                  r1.setTextColor(Color.WHITE)
                  QuestionsActivity.score++
              }
              else{
                  r1.setBackgroundResource(R.drawable.red_background)
                  r1.setTextColor(Color.WHITE)
              }
          } else if(checkedId == r2.id){
            if(answers[questionNumber-1] == 1){
                r2.setBackgroundResource(R.drawable.green_background)
                r2.setTextColor(Color.WHITE)
                QuestionsActivity.score++
            }
            else{
                r2.setBackgroundResource(R.drawable.red_background)
                r2.setTextColor(Color.WHITE)
            }
          } else if(checkedId == r3.id){
            if(answers[questionNumber-1] == 2){
                r3.setBackgroundResource(R.drawable.green_background)
                r3.setTextColor(Color.WHITE)
                QuestionsActivity.score++
            }
            else{
                r3.setBackgroundResource(R.drawable.red_background)
                r3.setTextColor(Color.WHITE)
            }
          } else if(checkedId == r4.id){
            if(answers[questionNumber-1] == 3){
                r4.setBackgroundResource(R.drawable.green_background)
                r4.setTextColor(Color.WHITE)
                QuestionsActivity.score++
            }
            else{
                r4.setBackgroundResource(R.drawable.red_background)
                r4.setTextColor(Color.WHITE)
            }
          }
    }

}


