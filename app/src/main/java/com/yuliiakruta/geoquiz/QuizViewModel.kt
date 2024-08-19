package com.yuliiakruta.geoquiz

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

const val CURRENT_INDEX_KEY = "CURRENT_INDEX_KEY"
//const val IS_CHEATER_KEY = "IS_CHEATER_KEY"
const val CHEAT_STATUS_KEY = "CHEAT_STATUS_KEY"

class QuizViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    private var cheatStatus: MutableList<Boolean>
        get() = savedStateHandle.get(CHEAT_STATUS_KEY) ?: MutableList(questionBank.size) { false }
        set(value) = savedStateHandle.set(CHEAT_STATUS_KEY, value)

    /*var isCheater:Boolean
        get() = savedStateHandle.get(IS_CHEATER_KEY) ?: false
        set(value) = savedStateHandle.set(IS_CHEATER_KEY, value)*/

    private var currentIndex: Int
        get() = savedStateHandle.get(CURRENT_INDEX_KEY) ?: 0
        set(value) = savedStateHandle.set(CURRENT_INDEX_KEY, value)

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    val isCurrentQuestionCheated: Boolean
        get() = cheatStatus[currentIndex]

    fun markCurrentQuestionAsCheated() {
        cheatStatus = cheatStatus.also {
            it[currentIndex] = true
        }
    }

    fun moveToNext(){
        currentIndex = (currentIndex + 1) % questionBank.size
    }
    fun moveToPrev(){
        currentIndex = (currentIndex - 1 + questionBank.size) % questionBank.size
    }
}