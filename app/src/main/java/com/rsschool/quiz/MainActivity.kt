package com.rsschool.quiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
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

            holder.binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
                var userAnswer: Int?

                userAnswer = checkedId
                userAnswers[position] = userAnswer
                //calculateResult(userAnswer, currentQuestion.rightIndex)

                holder.binding.nextButton.isEnabled = userAnswer != null

                holder.binding.nextButton.setOnClickListener {
                    if (position < QuestionSet.questions.size - 1) {
                        goNext()
                    } else {
                        val bundle = Bundle()
                        bundle.putIntArray("result", userAnswers)
                        holder.binding.appBarLayout.visibility = View.GONE
                        holder.binding.scroll.visibility = View.GONE
                        holder.binding.previousButton.visibility = View.GONE
                        holder.binding.nextButton.visibility = View.GONE
                        supportFragmentManager.beginTransaction()
                            .replace(android.R.id.content, ResultFragment.newInstance(bundle))
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

        private fun calculateResult(userAnswers: MutableList<Int>): {
            TODO("Not yet implemented")
        }

    }


    override fun share() {
        TODO("Not yet implemented")
    }

    override fun restart() {
        recreate()
    }

    override fun close() {
        finish()
    }

}

private fun Bundle.putIntArray(s: String, userAnswers: MutableList<Int>) {

}


