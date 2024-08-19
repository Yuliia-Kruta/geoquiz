package com.yuliiakruta.geoquiz

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

const val ANSWER_IS_TRUE_KEY = "ANSWER_IS_TRUE_KEY"
const val ANSWER_IS_SHOWN_KEY = "ANSWER_IS_SHOWN_KEY"

class CheatViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {


    var answerIsTrue: Boolean
        get() = savedStateHandle.get(ANSWER_IS_TRUE_KEY) ?: false
        set(value) = savedStateHandle.set(ANSWER_IS_TRUE_KEY, value)

    var isAnswerShown: Boolean
        get() = savedStateHandle.get(ANSWER_IS_SHOWN_KEY) ?: false
        set(value) = savedStateHandle.set(ANSWER_IS_SHOWN_KEY, value)

    val answerText: String
        get() = if (answerIsTrue) "True" else "False"
}
