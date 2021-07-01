package com.rsschool.quiz

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import androidx.annotation.AttrRes
import com.rsschool.quiz.databinding.ActivityMainBinding
import com.rsschool.quiz.databinding.FragmentQuizBinding


class MainActivity : AppCompatActivity(), QuizInterface {

    private lateinit var binding: ActivityMainBinding

    private var userAnswers = mutableListOf(-1, -1, -1, -1, -1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        with(binding) {
            setContentView(root)

            viewPager.adapter = AppAdapter()
            viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            viewPager.isUserInputEnabled = false
            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            })
        }
    }

    inner class AppViewHolder(val binding: FragmentQuizBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class AppAdapter : RecyclerView.Adapter<AppViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = AppViewHolder(
            FragmentQuizBinding.inflate(layoutInflater, parent, false)
        )

        override fun getItemCount(): Int = QuestionSet.questions.size

        override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
            val currentQuestion = QuestionSet.questions[position]
            val currentNumber = (position + 1).toString()

            setTheme(
                when (position) {
                    0 -> R.style.Theme_Quiz_Second
                    1 -> R.style.Theme_Quiz_Third
                    2 -> R.style.Theme_Quiz_Fourth
                    3 -> R.style.Theme_Quiz_Fifth
                    4 -> R.style.Theme_Quiz_First
                    else -> R.style.Theme_Quiz_First
                }
            )

            window.statusBarColor = getThemeColor(android.R.attr.statusBarColor)

            holder.binding.toolbar.title = "Question $currentNumber"
            holder.binding.question.text = currentQuestion.question
            holder.binding.optionOne.text = currentQuestion.answers[0]
            holder.binding.optionTwo.text = currentQuestion.answers[1]
            holder.binding.optionThree.text = currentQuestion.answers[2]
            holder.binding.optionFour.text = currentQuestion.answers[3]
            holder.binding.optionFive.text = currentQuestion.answers[4]

            if (position == 0) {
                holder.binding.toolbar.navigationIcon = null
                holder.binding.previousButton.visibility = View.GONE
            }
            if (position == QuestionSet.questions.size - 1) {
                holder.binding.nextButton.text = getString(R.string.submit_button)
            }

            holder.binding.toolbar.setNavigationOnClickListener {
               goPrevious()
            }
            holder.binding.previousButton.setOnClickListener {
                goPrevious()
            }

            holder.binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
                var userAnswer: Int = -1

                when (checkedId) {
                    R.id.option_one -> userAnswer = 0
                    R.id.option_two -> userAnswer = 1
                    R.id.option_three -> userAnswer = 2
                    R.id.option_four -> userAnswer = 3
                    R.id.option_five -> userAnswer = 4
                }

                userAnswers[position] = userAnswer

                holder.binding.nextButton.isEnabled = userAnswer > -1

                holder.binding.nextButton.setOnClickListener {
                    if (position < QuestionSet.questions.size - 1) {
                        goNext()
                    } else {
                        val result = calculateResult(userAnswers)
                        val bundle = Bundle()
                        bundle.putInt("result", result)
                        holder.binding.appBarLayout.visibility = View.GONE
                        holder.binding.scroll.visibility = View.GONE
                        holder.binding.previousButton.visibility = View.GONE
                        holder.binding.nextButton.visibility = View.GONE
                        supportFragmentManager.beginTransaction()
                            .replace(android.R.id.content, ResultFragment.newInstance(result))
                            .commit()
                    }

                }

            }

        }

        private fun goNext() {
            ++binding.viewPager.currentItem
        }

        private fun goPrevious() {
            --binding.viewPager.currentItem
        }

        private fun calculateResult(userAnswers: MutableList<Int>): Int {
            var counter = 0
            userAnswers.forEachIndexed { index, item ->
                if (item == QuestionSet.questions[index].rightIndex) counter += 10
            }
            return counter
        }

    }

    private fun createMessage(result: Int?): String {
        val text = StringBuilder()
        text.append("Your result: $result from 50 \n\n")

        QuestionSet.questions.forEachIndexed { index, item ->
            val answer = item.answers[userAnswers[index]]
            val points = if (userAnswers[index] == item.rightIndex) "10" else "0"
            text.append(
                "${index + 1}. ${item.question}\n" +
                        "Your answer: $answer - $points points\n\n"
            )
        }
        return text.toString()
    }


    override fun share(result: Int?) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_SUBJECT, "Quiz results: $result from 50")
            putExtra(Intent.EXTRA_TEXT, createMessage(result))
            selector = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
            }
        }
        startActivity(Intent.createChooser(intent, "Share result"))
    }

    override fun restart() {
        val intent = intent
        finish()
        startActivity(intent)
    }

    override fun close() {
        finish()
    }

}

fun Context.getThemeColor(@AttrRes attrRes: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute (attrRes, typedValue, true)
    return typedValue.data
}


