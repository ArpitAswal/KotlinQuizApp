package com.example.quizapp

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


class QuestionPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    private var fragments: MutableList<MultipleChoiceQuestion> = mutableListOf()

    init {
        val question1 = "Can a user save all database updates in onStop ()?"
        val options1 = mutableListOf<String>(
            "A - Yes, a user can save all database updates in onStop()",
            "B - No, a user can save in onSavedInstance()",
            "C - No, a user can save in a Bundle()",
            "D - No, In some situations, a user can't reach onStop()"
        )
        val question2 = "What is android view group?"
        val options2 = mutableListOf<String>(
            "A - Collection of views and other child views",
            "B - Base class of building blocks",
            "C - Layouts",
            "D - None of the Above"
        )
        val question3 = "How to stop the services in android?"
        val options3 = mutableListOf<String>(
            "A - finish()",
            "B - system.exit()",
            "C - By manually",
            "D - stopSelf() and stopService()"
        )
        val question4 = "How many broadcast receivers are available in android?"
        val options4 = mutableListOf<String>(
            "A - sendIntent()",
            "B - onReceive()",
            "C - implicitBroadcast()",
            "D - sendBroadcast(), sendOrderBroadcast(), and sendStickyBroadcast()"
        )
        val question5 = "How to get current location in android?"
        val options5 = mutableListOf<String>(
            "A - Using with GPRS",
            "B - Using location provider",
            "C - A & B",
            "D - SQlite"
        )
        val question6 = "What is DDMS in android?"
        val options6 = mutableListOf<String>(
            "A - Dalvik memory server",
            "B - Device memory server",
            "C - Dalvik monitoring services",
            "D - Dalvik debug monitor services"
        )
        val question7 = "What is the package name of HTTP client in android?"
        val options7 = mutableListOf<String>(
            "A - com.json",
            "B - org.apache.http.client",
            "C - com.android.JSON",
            "D - org.json"
        )
        val question8 = "What is a GCM in android?"
        val options8 = mutableListOf<String>(
            "A - Goggle Could Messaging for chrome",
            "B - Goggle Count Messaging",
            "C - Goggle Message pack",
            "D - None of the above"
        )
        val question9 = "What are commands needed to create APK in android?"
        val options9 = mutableListOf<String>(
            "A - No need to write any commands",
            "B - Create apk_android in command line",
            "C - Javac,dxtool, aapt tool, jarsigner tool, and zipalign",
            "D - None of the above"
        )
        val question10 = "What is anchor view?"
        val options10 = mutableListOf<String>(
            "A - Same as list view",
            "B - provides the information on respective relative positions",
            "C - Same as relative layout",
            "D - None of the above"
        )
        fragments.add(MultipleChoiceQuestion(question1,options1,1))
        fragments.add(MultipleChoiceQuestion(question2,options2,2))
        fragments.add(MultipleChoiceQuestion(question3,options3,3))
        fragments.add(MultipleChoiceQuestion(question4,options4,4))
        fragments.add(MultipleChoiceQuestion(question5,options5,5))
        fragments.add(MultipleChoiceQuestion(question6,options6,6))
        fragments.add(MultipleChoiceQuestion(question7,options7,7))
        fragments.add(MultipleChoiceQuestion(question8,options8,8))
        fragments.add(MultipleChoiceQuestion(question9,options9,9))
        fragments.add(MultipleChoiceQuestion(question10,options10,10))
        // Add more fragments as needed

    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    companion object{
        val listOfAnswers = intArrayOf(3,0,3,3,2,3,1,0,3,1)
    }
}

