package com.example.quizapp

class ScoreClass {

    val userId: String
    val username: String
    val score: Long
    val time: String
    val imgSrc: String

    constructor(){
        this.userId = ""
        this.username = ""
        this.score = 0L
        this.time = ""
        this.imgSrc = ""
    }

    constructor(userId: String, username: String, score: Long, time: String, imgSrc: String){
        this.userId = userId
        this.username = username
        this.score = score
        this.time = time
        this.imgSrc = imgSrc
    }
}