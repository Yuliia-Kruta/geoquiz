package com.yuliiakruta.geoquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.yuliiakruta.geoquiz.databinding.ActivityCheatBinding

const val EXTRA_ANSWER_SHOWN = "com.yuliiakruta.geoquiz.answer_shown"
private const val EXTRA_ANSWER_IS_TRUE = "com.yuliiakruta.geoquiz.answer_is_true"

private const val TAG = "CheatActivity"

class CheatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheatBinding
    private val cheatViewModel: CheatViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        binding = ActivityCheatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "onCreate(Bundle?) called")

        cheatViewModel.answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)

        if(cheatViewModel.isAnswerShown){
            binding.answerTextView.setText(cheatViewModel.answerText)
            setAnswerShownResult()
        }
        binding.showAnswerButton.setOnClickListener{
            binding.answerTextView.setText(cheatViewModel.answerText)
            cheatViewModel.isAnswerShown = true
            setAnswerShownResult()
        }

    }

    private fun setAnswerShownResult(){
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, cheatViewModel.isAnswerShown)
        }
        setResult(Activity.RESULT_OK, data)
    }

    companion object {
        fun newIntent(packageContext: Context, answerIsTrue: Boolean) : Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            }
        }
    }
}