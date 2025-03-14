package com.yuliiakruta.geoquiz

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

const val CURRENT_INDEX_KEY = "CURRENT_INDEX_KEY"
//const val IS_CHEATER_KEY = "IS_CHEATER_KEY"
const val CHEAT_STATUS_KEY = "CHEAT_STATUS_KEY"
const val ANSWER_STATUS_KEY = "ANSWER_STATUS_KEY"

class QuizViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true),
        Question(R.string.question_europe, false),
        Question(R.string.question_pacific, false),
        Question(R.string.question_antarctica, true),
        Question(R.string.question_mountains, true),
        Question(R.string.question_rivers, true),
        Question(R.string.question_history, true),
        Question(R.string.question_capitals, true),
        Question(R.string.question_space, true),
        Question(R.string.question_animals, false),
        Question(R.string.question_energy, false)
    )

    private var cheatStatus: MutableList<Boolean>
        get() = savedStateHandle.get(CHEAT_STATUS_KEY) ?: MutableList(questionBank.size) { false }
        set(value) = savedStateHandle.set(CHEAT_STATUS_KEY, value)

    private var answerStatus: MutableList<Boolean>
        get() = savedStateHandle.get(ANSWER_STATUS_KEY) ?: MutableList(questionBank.size) { false }
        set(value) = savedStateHandle.set(ANSWER_STATUS_KEY, value)

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

    val currentQuestionNumber: Int
        get() = currentIndex + 1

    val totalQuestions: Int
        get() = questionBank.size

    val isCurrentQuestionCheated: Boolean
        get() = cheatStatus[currentIndex]

    val isCurrentQuestionAnswered: Boolean
        get() = answerStatus[currentIndex]

    fun areAllQuestionsAnswered(): Boolean {
        return answerStatus.all { it }
    }

    fun markCurrentQuestionAsCheated() {
        cheatStatus = cheatStatus.also {
            it[currentIndex] = true
        }
    }

    fun markCurrentQuestionAsAnswered() {
        answerStatus = answerStatus.also {
            it[currentIndex] = true
        }
    }

    fun calculateScore() : Int{
        return 0
    }

    fun restartGame() {
        answerStatus = MutableList(questionBank.size) { false }
        currentIndex = 0
    }

    fun moveToNext(){
        currentIndex = (currentIndex + 1) % questionBank.size
    }
    fun moveToPrev(){
        currentIndex = (currentIndex - 1 + questionBank.size) % questionBank.size
    }
}