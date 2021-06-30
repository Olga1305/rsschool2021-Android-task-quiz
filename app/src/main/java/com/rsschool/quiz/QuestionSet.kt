package com.rsschool.quiz

data class Question (
    val question: String,
    val answers: ArrayList<String>,
    val rightIndex: Int
)

object QuestionSet {
    val questions = listOf(
        Question(
            question = "From which language is the word ‘ketchup’ derived?",
            answers = arrayListOf(
                "Spanish",
                "Chinese",
                "Russian",
                "English",
                "French"
            ),
            1
        ),
        Question(
            question = "Which is the country with the biggest population in Europe?",
            answers = arrayListOf(
                "France",
                "Italy",
                "Germany",
                "Russia",
                "Greece"
            ),
            3
        ),
        Question(
            question = "What colour are the four stars on the flag of New Zealand?",
            answers = arrayListOf(
                "Red",
                "Blue",
                "Yellow",
                "Green",
                "Black"
            ),
            0
        ),
        Question(
            question = "H2O is the chemical formula for what?",
            answers = arrayListOf(
                "Oxygen",
                "Hydrogen",
                "Alcohol",
                "Water",
                "Carbon dioxide"
            ),
            3
        ),
        Question(
            question = "How many states make up the United States of America?",
            answers = arrayListOf(
                "48",
                "49",
                "50",
                "51",
                "52"
            ),
            2
        )
    )
}
