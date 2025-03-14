package com.yuliiakruta.geoquiz

import android.app.Activity
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.yuliiakruta.geoquiz.databinding.ActivityMainBinding

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val quizViewModel: QuizViewModel by viewModels()

    private val cheatLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val isCheater = result.data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
            if (isCheater) {
                quizViewModel.markCurrentQuestionAsCheated()
                quizViewModel.markCurrentQuestionAsAnswered()
                updateDisabledButtons()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "Got a QuizViewModel: $quizViewModel")
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.trueButton.setOnClickListener {
            checkAnswer(true)
        }
        binding.falseButton.setOnClickListener {
            checkAnswer(false)
        }
        binding.nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }

        binding.finishButton.setOnClickListener {
            if (quizViewModel.areAllQuestionsAnswered()) {
                showScoreDialog()
            } else {
                Toast.makeText(this, "Answer all questions first!", Toast.LENGTH_SHORT).show()
            }
        }
        binding.prevButton.setOnClickListener {
            quizViewModel.moveToPrev()
            updateQuestion()
        }

        binding.cheatButton.setOnClickListener {
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            cheatLauncher.launch(intent)
        }

        updateQuestion()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            blurCheatBtn()
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        val questionNumber = quizViewModel.currentQuestionNumber
        val totalQuestions = quizViewModel.totalQuestions

        binding.questionNumberTextView.text = "Question $questionNumber of $totalQuestions"
        binding.questionTextView.setText(questionTextResId)
        binding.prevButton.isEnabled = questionNumber > 1
        if (questionNumber == totalQuestions) {
            binding.nextButton.visibility = View.GONE
            binding.finishButton.visibility = View.VISIBLE
        } else {
            binding.nextButton.visibility = View.VISIBLE
            binding.finishButton.visibility = View.GONE
        }
        updateDisabledButtons()
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer
        quizViewModel.markCurrentQuestionAsAnswered()
        updateDisabledButtons()

        val messageResId = when {
            quizViewModel.isCurrentQuestionCheated -> R.string.judgement_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }

    private fun updateDisabledButtons() {
        val isAnswered = quizViewModel.isCurrentQuestionAnswered
        binding.apply {
            trueButton.isEnabled = !isAnswered
            falseButton.isEnabled = !isAnswered
            cheatButton.isEnabled = !isAnswered
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun blurCheatBtn() {
        val effect = RenderEffect.createBlurEffect(
            10.0f, 10.0f, Shader.TileMode.CLAMP
        )
        binding.cheatButton.setRenderEffect(effect)
    }

    private fun showScoreDialog() {
        val score = quizViewModel.calculateScore()

        AlertDialog.Builder(this)
            .setTitle("Quiz Completed!")
            .setMessage("Your Score: $score/${quizViewModel.totalQuestions}")
            .setPositiveButton("Restart") { _, _ ->
                quizViewModel.restartGame()
                updateQuestion()
            }
            .setCancelable(false)
            .show()
    }


}